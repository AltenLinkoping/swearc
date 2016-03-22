#ifndef __SERIAL_HH__
#define __SERIAL_HH__

#include <pthread.h>
#include <string>

class ArduinoSerial {
 public:
  ArduinoSerial(const std::string& serial_port);
  virtual ~ArduinoSerial();
    
  static const int LEFT_MOTOR = 0;
  static const int RIGHT_MOTOR = 1;
  int engine_forward(unsigned engine, unsigned speed);
  int engine_backwards(int engine, unsigned speed);
  int engine_halt(int engine);
  int servo_set_position(int servo, unsigned position);

  int write_to_serial(const char* str);
  int send_command(const char* format, ...);

  void open_port();
  void init_reader();

 private:

  struct MpuData {
    int values[7]; // AcX, AcY, AcZ, temp, GcX, GcY, GcZ
    int* fd;
    pthread_mutex_t mutex;
    pthread_cond_t cond;
  };

  static void* reader_thread(void* arg);

  int fd;
  pthread_t reader;
  MpuData mpuData;
  const std::string serial_port;
};

#endif
