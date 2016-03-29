/* Simple sketch to control servos and engines
 *
 * The following commands can be sent via UART:
 * --------------------------------------------
 * "sX Y" => Turn servo X to position Y
 *        => "s1 100" turns servo 1 to 100 degrees
 *
 * "fX Y" => Makes engine X go forward with speed Y (0<=Y<=255)
 *        => "f0 255" makes engine 0 go forward with max speed
 *
 * "bX Y" => Same as above but makes engine go backwards
 *
 * "hX"   => Stops engine X

 * NB: Servos and engines are defined further down in the code.
 *
 * To enable debug printouts, set ENABLE_DEBUG to 1.
 */ 
  
#include <Servo.h>
#include <Wire.h>
#include <TimerOne.h>
#include <Adafruit_MotorShield.h>

#define ENABLE_DEBUG 1
#define UART_SPEED 115200

/*****************************************************************************
 *
 * Types, definitions and forward declarations goes here.
 *
 *****************************************************************************/

Adafruit_MotorShield AFMS = Adafruit_MotorShield(); 

typedef struct {
  Adafruit_DCMotor* motor;
  volatile byte timeout; /* Used by watchdog to trigger automatic shutdown */
  volatile bool turn_off;
} connected_engine_t;

typedef struct {
  byte pin;
  Servo servo;
} connected_servo_t;

enum {
  DIRECTION_FORWARD,
  DIRECTION_BACKWARDS
};

struct ultrasonic_t {
  byte trigger_pin;
  byte echo_pin;
  volatile int trigger_state;
  volatile long echo_start;
  volatile long echo_end;
  volatile long echo_duration;
  volatile int trigger_time_count;
};

connected_engine_t* get_engine();
void handle_incoming_command();
void handle_timeouts();
void handle_servo();
void handle_engine_forward();
void handle_engine_backwards();
void handle_engine_halt();

#define ARR_SIZE(a) (sizeof(a)/sizeof(a[0]))

#if ENABLE_DEBUG
#define DEBUG(...) Serial.print(__VA_ARGS__)
#define DEBUGLN(...) Serial.println(__VA_ARGS__)
#else
#define DEBUG
#define DEBUGLN
#endif

/*****************************************************************************
 *
 * Supported input commands from serial interface
 *
 *****************************************************************************/

#define CMD_SERVO            's'
#define CMD_ENGINE_FORWARD   'f'
#define CMD_ENGINE_BACKWARDS 'b'
#define CMD_ENGINE_HALT      'h'

/*****************************************************************************
 *
 * Definitions of engines and servos
 *
 *****************************************************************************/

/* Servo 0 => pin 5, Servo 1 => ping 6 */
static connected_servo_t SERVOS[] = {
  {5, Servo()},
  {6, Servo()}
};

/* Ultrasonic 0 => pin 7, Ultrasonic 1 => pin 8 */
static ultrasonic_t ULTRASONIC[] = {
  {12, 2, 0, 0, 0, 0, 0},
  {11, 3, 0, 0, 0, 0, 0}
};

/* Engine 0 => M1 on shield, Engine 1 => M2 on shield */
static connected_engine_t ENGINES[] = {
  {AFMS.getMotor(1), 0, false},
  {AFMS.getMotor(2), 0, false}
};

/*****************************************************************************
 *
 * Variables for UART input.
 *
 *****************************************************************************/

String buffer = "";
boolean readComplete = false;

/*****************************************************************************
 *
 * Watchdog to turn off engines, etc.
 *
 *****************************************************************************/

/* FIXME: Generalize timeout management (time calculation) */
#define ENGINE_TIMEOUT 47 /* 47 * 32ms = 1504ms */

ISR(WDT_vect)
{
  for (unsigned i = 0; i < ARR_SIZE(ENGINES); ++i) {
    connected_engine_t* engine = &ENGINES[i];

    /* If timeout is 0 at this stage, the engine is not running */
    if (engine->timeout == 0)
      continue;

    /* If we reached 0 here, the engine was running and we should turn it off */
    engine->timeout--;
    if (engine->timeout == 0) {
      engine->turn_off = true;
    }
  }
}

/*****************************************************************************
 *
 * Ultrasonic sensor stuff
 *
 *****************************************************************************/

#define TIMER_US 50
#define TICK_COUNTS 4000 // 200 mS worth of timer ticks

void trigger_pulse_for_sensor(struct ultrasonic_t* sensor) {
  if (!(--sensor->trigger_time_count)) {
     sensor->trigger_time_count = TICK_COUNTS;
     sensor->trigger_state = 1;
  }

  switch(sensor->trigger_state) {
  case 0:
    break;
    
  case 1:
    digitalWrite(sensor->trigger_pin, HIGH);
    sensor->trigger_state = 2;
    break;
    
  case 2:
  default:      
    digitalWrite(sensor->trigger_pin, LOW);
    sensor->trigger_state = 0;
    break;
  }
}

void trigger_pulse() {
  for (unsigned i = 0; i < ARR_SIZE(ULTRASONIC); ++i) {
    trigger_pulse_for_sensor(&ULTRASONIC[i]);
  }
}

void handle_ultrasonic_sensor(struct ultrasonic_t* sensor) {
  switch (digitalRead(sensor->echo_pin)) {
  case HIGH:
    sensor->echo_end = 0;
    sensor->echo_start = micros();
    break;
      
  case LOW:
    sensor->echo_end = micros();
    sensor->echo_duration = sensor->echo_end - sensor->echo_start;
    break;
  }
}

typedef void(*echo_interrupt_func)();
void echo_interrupt_0() { handle_ultrasonic_sensor(&ULTRASONIC[0]); }
void echo_interrupt_1() { handle_ultrasonic_sensor(&ULTRASONIC[1]); }
static echo_interrupt_func ECHO_INTERRUPTS[] = {echo_interrupt_0, echo_interrupt_1};

void init_ultrasonic() {
  Serial.println("Initialize ultrasonic");
  for (unsigned i = 0; i < ARR_SIZE(ULTRASONIC); ++i) {
    struct ultrasonic_t* sensor = &ULTRASONIC[i];
    pinMode(sensor->echo_pin, INPUT);
    pinMode(sensor->trigger_pin, OUTPUT);
    // TODO: Remove this for now (will be replaced by library soon)
    //attachInterrupt(digitalPinToInterrupt(sensor->echo_pin), ECHO_INTERRUPTS[i], CHANGE);
  }
  Timer1.initialize(TIMER_US);
  Timer1.attachInterrupt(trigger_pulse);
}

/*****************************************************************************
 *
 * Implementation goes below...
 *
 *****************************************************************************/

void setup() {
  Serial.begin(UART_SPEED);
  Serial.println("Booting system");
  buffer.reserve(30);

  /* Attach all servos */
  for (unsigned i = 0; i < ARR_SIZE(SERVOS); ++i) {
    Serial.print("Attach servo ");
    Serial.print(i);
    Serial.print(" to pin ");
    Serial.println(SERVOS[i].pin);

    SERVOS[i].servo.attach(SERVOS[i].pin);
  }

  /* Configure motors */
  Serial.println("Configure motor shield");
  AFMS.begin();

  /* Configure watchdog for timeout handling */
  Serial.println("Enable internal interrupts");
  MCUSR &= ~(1<<WDRF); /* Clear reset flag */
  WDTCSR |= (1<<WDCE) | (1<<WDE);
  WDTCSR = 1<<WDP0; /* 32 ms, http://4.bp.blogspot.com/-FHV4xIWlwng/TtFF3sMMP-I/AAAAAAAAInE/ap5dKUWD4-g/s1600/Screen+shot+2011-11-26+at+20.00.51.png */
  WDTCSR |= _BV(WDIE); /* Enable interrupt */

  init_ultrasonic();

  /* We are ready to serve! */
  Serial.println("READY!");
}

void loop() {
  //Serial.print(ULTRASONIC[1].echo_duration / 58);
  //Serial.print("  ");
  //Serial.println(ULTRASONIC[0].echo_duration / 58);
  //delay(50);
  handle_incoming_command();
  handle_timeouts();
}

void handle_incoming_command() {
  if (!readComplete) {
    return;
  }

  DEBUG("Incoming command: ");
  DEBUGLN(buffer);

  switch (buffer[0]) {
  case CMD_SERVO:            handle_servo(); break;
  case CMD_ENGINE_FORWARD:   handle_engine_forward(); break;
  case CMD_ENGINE_BACKWARDS: handle_engine_backwards(); break;
  case CMD_ENGINE_HALT:      handle_engine_halt(); break;
  default:                   break;
    DEBUGLN("Unknown command!");
  }
  
  buffer = "";
  readComplete = false;
}

void serialEvent() {
  while (Serial.available()) {
    /* NB: This code is not buffer overflow-safe */
    char inChar = (char)Serial.read(); 

    if (inChar == '\n' || inChar == '\r') {
      readComplete = true;
      break;
    }  else {
      buffer += inChar;
    }
  } 
}

void handle_servo() {
  DEBUGLN("Handle servo");
  
  /* Assume format: "s1 100" => servo 1, 100 degrees */
  byte servo = atoi(&buffer[1]);
  unsigned int pos = atoi(&buffer[3]);
  
  /* Sanity checks */
  if (servo > (ARR_SIZE(SERVOS) - 1)) {
    Serial.print("Invalid servo: ");
    Serial.println(servo);
    return;
  } else if (pos > 180) {
    Serial.print("Invalid servo position: ");
    Serial.println(pos);
    return;  
  }

  /* Adjust servo to requested position */
  SERVOS[servo].servo.write(pos);
}

connected_engine_t* get_engine() {
  /* Assume format: "f|b0 255" => engine 0 forward, full speed */
  byte index = atoi(&buffer[1]);

  /* Sanity check for engine */
  if (index > (ARR_SIZE(ENGINES) - 1)) {
    Serial.print("Invalid engine: ");
    Serial.println(index);
    return NULL;
  }

  return &ENGINES[index];
}

void start_engine(int dir) {
  connected_engine_t* engine = get_engine();

  if (engine) {
    byte speed = (byte)atoi(&buffer[3]);
    DEBUG("Set engine speed to ");
    DEBUGLN(speed);

    engine->timeout = ENGINE_TIMEOUT;
    engine->motor->run(dir == DIRECTION_FORWARD ? FORWARD : BACKWARD);
    engine->motor->setSpeed(speed);
  }
}

void handle_engine_forward() {
  DEBUGLN("Engine forward");
  start_engine(DIRECTION_FORWARD);
}

void handle_engine_backwards() {
  DEBUGLN("Engine backwards");
  start_engine(DIRECTION_BACKWARDS);
}

void handle_engine_halt() {
  DEBUGLN("Engine halt");
  connected_engine_t* engine = get_engine();

  if (engine) {
    engine->timeout = 0;
    engine->motor->run(RELEASE);
    engine->motor->setSpeed(0);
  }
}

void handle_timeouts() {
  for (unsigned i = 0; i < ARR_SIZE(ENGINES); ++i) {
    connected_engine_t* engine = &ENGINES[i];
    if (engine->turn_off == true) {
      engine->motor->setSpeed(0);
      engine->turn_off = false;
    }
  }
}

