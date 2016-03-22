#include <dirent.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <termios.h>
#include <stdarg.h>
#include <pthread.h>
#include <sys/time.h>
#include <fstream>
#include <iostream>
#include "serial.h"

#define END_FILE_CHARACTER 0x04

// For test: socat -d -d pty,raw,echo=0 pty,raw,echo=0 (+minicom)

ArduinoSerial::ArduinoSerial(const std::string& serial_port) :
  fd(0), 
  serial_port(serial_port) {
  open_port();
  //  init_reader();
}

void ArduinoSerial::open_port() {
  struct termios serial;

  printf("[Serial] Opening %s\n", serial_port.c_str());

  fd = open(serial_port.c_str(), O_RDWR | O_NOCTTY);
  if (fd == -1) {
    perror(serial_port.c_str());
    throw "Failed to open port";
  }

  if (tcgetattr(fd, &serial) < 0) {
    close(fd);
    perror("Getting configuration");
    throw "Failed to get configuration";
  }

  serial.c_cflag = B115200 | CS8 | CREAD;
  serial.c_iflag = IGNPAR | ICRNL;
  serial.c_oflag = 0;
  serial.c_lflag = ICANON;
  tcsetattr(fd, TCSANOW, &serial);
  printf("[Serial] Device opened\n");
}

void ArduinoSerial::init_reader() {
  mpuData.fd = &fd;
  pthread_mutex_init(&mpuData.mutex, NULL);
  pthread_cond_init(&mpuData.cond, NULL);
  if (pthread_create(&reader, NULL, reader_thread, (void*)&mpuData) != 0) {
    throw "Failed to create reader thread";
  }
}

void* ArduinoSerial::reader_thread(void* arg) {
  char buffer[1024];
  char* p = buffer;
  MpuData* data = (MpuData *)arg;
  for (;;) {
    /* HACK: This is crap... Best part is that it's not buffer overflow-safe */
    int n = read(*data->fd, p, 100);

    if (n > 0) {
      if (*p == '\n' || *p == '\r' || *(p + n - 1)) {
        *(p + n - 1) = '\0';
	printf("Incoming: '%s'\n", buffer);
        p = buffer;
        buffer[0] = '\0';
      } else {
        p += n;
      }
    } else if (n != 0) {
      printf("Failed to read from UART: %d\n", n);
    }
  }
}

ArduinoSerial::~ArduinoSerial() {
  pthread_cancel(reader);
  close(fd);
}

int ArduinoSerial::write_to_serial(const char* str) {
  int wcount = write(fd, str, strlen(str));
  if (wcount < 0) {
    close(fd);
    perror("Write");
    return -1;
  }
  return wcount;
}

int ArduinoSerial::send_command(const char* format, ...) {
  char buf[30];

  va_list args;
  va_start(args, format);
  vsprintf(buf, format, args);
  va_end(args);

  printf("[Serial] Send: %s", buf);
  return write_to_serial(buf);
}

int ArduinoSerial::engine_forward(unsigned engine, unsigned speed) {
  return send_command("f%u %u\n", engine, speed);
}

int ArduinoSerial::engine_backwards(int engine, unsigned speed) {
  return send_command("b%u %u\n", engine, speed);
}

int ArduinoSerial::engine_halt(int engine) {
  return send_command("f%u\n", engine);
}

int ArduinoSerial::servo_set_position(int servo, unsigned position) {
  return send_command("s%u %u\n", servo, position);
}

char linux_getch(void) {
  struct termios oldstuff;
  struct termios newstuff;
  int inch;

  tcgetattr(STDIN_FILENO, &oldstuff);
  newstuff = oldstuff; /* save old attributes */
  newstuff.c_lflag &= ~(ICANON | ECHO); /* reset "canonical" and "echo" flags*/
  tcsetattr(STDIN_FILENO, TCSANOW, &newstuff); /* set new attributes */
  inch = getchar();
  tcsetattr(STDIN_FILENO, TCSANOW, &oldstuff); /* restore old attributes */
  if (inch == END_FILE_CHARACTER) {
    inch = EOF;
  }
  return (char)inch;
}

// FIXME: Ugly as f...
const std::string find_device() {
  DIR* dpdf;
  struct dirent *epdf;
  dpdf = opendir("/dev");
  if (dpdf != NULL){
    while ((epdf = readdir(dpdf))) {
      static const char* PREFIX = "tty.postlund";
      if (strncmp(PREFIX, epdf->d_name, strlen(PREFIX)) == 0) {
	closedir(dpdf);
	return "/dev/" + std::string(epdf->d_name);
      }
    }
  }
  closedir(dpdf);
  return "";
}

int main(int argc, char* argv[]) {
  const std::string dev = find_device();

  if (dev == "") {
    std::cerr << "Could not determine device name" << std::endl;
    return -1;
  }

  std::cout << "Device: " << dev << std::endl;
  ArduinoSerial serial(dev);
  serial.open_port();
  serial.init_reader();

  while(int c =linux_getch()) {
    if (c == 0x77) { /* w = Forward */
      serial.engine_forward(0, 250);
      serial.engine_forward(1, 250);
    } else if (c == 0x73) { /* s = Backwards */
      serial.engine_backwards(0, 250);
      serial.engine_backwards(1, 250);
    } else if (c == 0x64) { /* d = Right */
      serial.engine_backwards(0, 250);
      serial.engine_forward(1, 250);
    } else if (c == 0x61) { /* a = Left */
      serial.engine_forward(0, 250);
      serial.engine_backwards(1, 250);
    } else if (c == 'h') {
      serial.engine_halt(0);
      serial.engine_halt(1);
    }
  }

  return 0;
}
