package org.myrobotlab.codec.serial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.myrobotlab.logging.Level;
import org.myrobotlab.logging.LoggerFactory;
import org.myrobotlab.logging.Logging;
import org.myrobotlab.logging.LoggingFactory;
import org.myrobotlab.service.Arduino;
import org.myrobotlab.service.Runtime;
import org.myrobotlab.service.Serial;
import org.myrobotlab.service.interfaces.LoggingSink;
import org.python.netty.handler.codec.CodecException;
import org.slf4j.Logger;

// FIXME - use InputStream OutputStream 
// Stream encoders are more complicated than Document 
// with InputStream decoding - you need to deal with blocking / timeouts etc
// if the thing before it deals with it then you have a byte array - but it may not be complete

/**
 * This file is generated by running ArduinoBindingsGenerator
 * the template which is used is in /src/resource/generate/ArduinoMsgCodec.txt
 * If there are modifications directly to this file - it is likely they will be overwritten.
 *
 * Codec to interface with the Arduino service and MRLComm.ino part of this file
 * is dynamically generated from the method signatures of the Arduino service
 * 
 * MAGIC_NUMBER|NUM_BYTES|FUNCTION|DATA0|DATA1|....|DATA(N) NUM_BYTES - is the
 * number of bytes after NUM_BYTES to the end
 * 
 * @author GroG
 *
 */
public class ArduinoMsgCodec extends Codec implements Serializable {

  public ArduinoMsgCodec() {
    super(null);
  }

  public ArduinoMsgCodec(LoggingSink sink) {
    super(sink);
  }

  private static final long serialVersionUID = 1L;

  public final static Logger log = LoggerFactory.getLogger(ArduinoMsgCodec.class);

  transient static final HashMap<Integer, String> byteToMethod = new HashMap<Integer, String>();
  transient static final HashMap<String, Integer> methodToByte = new HashMap<String, Integer>();
  int byteCount = 0;
  int decodeMsgSize = 0;
  StringBuilder rest = new StringBuilder();

  public static final int MAX_MSG_SIZE = 64;
  public static final int MAGIC_NUMBER = 170; // 10101010
  public static final int MRLCOMM_VERSION = 34;
  
  // ----------- event types -------------------
  public static final int STEPPER_EVENT_STOP = 1;
  public static final int STEPPER_EVENT_STEP = 2;
  
  // -------- byteToMethod begin --------------------
  /*
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.MAX_MSG_SIZE;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.MAGIC_NUMBER;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.MRLCOMM_VERSION;

  	///// java static import definition - DO NOT MODIFY - Begin //////
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.PUBLISH_MRLCOMM_ERROR;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.GET_VERSION;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.PUBLISH_VERSION;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.ADD_SENSOR_DATA_LISTENER;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.ANALOG_READ_POLLING_START;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.ANALOG_READ_POLLING_STOP;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.ANALOG_WRITE;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.ATTACH_DEVICE;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.CREATE_DEVICE;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.DIGITAL_READ_POLLING_START;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.DIGITAL_READ_POLLING_STOP;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.DIGITAL_WRITE;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.FIX_PIN_OFFSET;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.I2C_READ;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.I2C_WRITE;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.I2C_WRITE_READ;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.INTS_TO_STRING;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.MOTOR_ATTACH;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.MOTOR_DETACH;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.MOTOR_MOVE;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.MOTOR_MOVE_TO;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.MOTOR_RESET;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.MOTOR_STOP;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.PIN_MODE;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.PUBLISH_ATTACHED_DEVICE;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.PUBLISH_DEBUG;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.PUBLISH_MESSAGE_ACK;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.PUBLISH_PIN;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.PUBLISH_PULSE;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.PUBLISH_PULSE_STOP;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.PUBLISH_SENSOR_DATA;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.PUBLISH_SERVO_EVENT;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.PUBLISH_STATUS;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.PUBLISH_TRIGGER;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.PULSE;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.PULSE_STOP;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.RELEASE_DEVICE;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.SERVO_ATTACH;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.SERVO_DETACH;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.SERVO_EVENTS_ENABLED;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.SERVO_SWEEP_START;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.SERVO_SWEEP_STOP;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.SERVO_WRITE;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.SERVO_WRITE_MICROSECONDS;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.SET_DEBOUNCE;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.SET_DEBUG;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.SET_DIGITAL_TRIGGER_ONLY;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.SET_LOAD_TIMING_ENABLED;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.SET_PWMFREQUENCY;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.SET_SAMPLE_RATE;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.SET_SERIAL_RATE;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.SET_SERVO_SPEED;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.SET_TRIGGER;
	import static org.myrobotlab.codec.serial.ArduinoMsgCodec.SOFT_RESET;

  */
  
  	///// java ByteToMethod generated definition - DO NOT MODIFY - Begin //////
	// {publishMRLCommError Integer} 
	public final static int PUBLISH_MRLCOMM_ERROR =		1;

	// {getVersion} 
	public final static int GET_VERSION =		2;

	// {publishVersion Integer} 
	public final static int PUBLISH_VERSION =		3;

	// {addSensorDataListener SensorDataListener} 
	public final static int ADD_SENSOR_DATA_LISTENER =		4;

	// {analogReadPollingStart Integer Integer} 
	public final static int ANALOG_READ_POLLING_START =		5;

	// {analogReadPollingStop int} 
	public final static int ANALOG_READ_POLLING_STOP =		6;

	// {analogWrite int int} 
	public final static int ANALOG_WRITE =		7;

	// {attachDevice Device} 
	public final static int ATTACH_DEVICE =		8;

	// {createDevice int int String} 
	public final static int CREATE_DEVICE =		9;

	// {digitalReadPollingStart Integer Integer} 
	public final static int DIGITAL_READ_POLLING_START =		10;

	// {digitalReadPollingStop int} 
	public final static int DIGITAL_READ_POLLING_STOP =		11;

	// {digitalWrite int int} 
	public final static int DIGITAL_WRITE =		12;

	// {fixPinOffset Integer} 
	public final static int FIX_PIN_OFFSET =		13;

	// {i2cRead int int byte[] int} 
	public final static int I2C_READ =		14;

	// {i2cWrite int int byte[] int} 
	public final static int I2C_WRITE =		15;

	// {i2cWriteRead int int byte[] int byte[] int} 
	public final static int I2C_WRITE_READ =		16;

	// {intsToString int[] int int} 
	public final static int INTS_TO_STRING =		17;

	// {motorAttach MotorControl int} 
	public final static int MOTOR_ATTACH =		18;

	// {motorDetach MotorControl} 
	public final static int MOTOR_DETACH =		19;

	// {motorMove MotorControl} 
	public final static int MOTOR_MOVE =		20;

	// {motorMoveTo MotorControl} 
	public final static int MOTOR_MOVE_TO =		21;

	// {motorReset MotorControl} 
	public final static int MOTOR_RESET =		22;

	// {motorStop MotorControl} 
	public final static int MOTOR_STOP =		23;

	// {pinMode int String} 
	public final static int PIN_MODE =		24;

	// {publishAttachedDevice Device} 
	public final static int PUBLISH_ATTACHED_DEVICE =		25;

	// {publishDebug String} 
	public final static int PUBLISH_DEBUG =		26;

	// {publishMessageAck} 
	public final static int PUBLISH_MESSAGE_ACK =		27;

	// {publishPin Pin} 
	public final static int PUBLISH_PIN =		28;

	// {publishPulse Long} 
	public final static int PUBLISH_PULSE =		29;

	// {publishPulseStop Integer} 
	public final static int PUBLISH_PULSE_STOP =		30;

	// {publishSensorData SensorData} 
	public final static int PUBLISH_SENSOR_DATA =		31;

	// {publishServoEvent Integer} 
	public final static int PUBLISH_SERVO_EVENT =		32;

	// {publishStatus Long Integer} 
	public final static int PUBLISH_STATUS =		33;

	// {publishTrigger Pin} 
	public final static int PUBLISH_TRIGGER =		34;

	// {pulse int int int int} 
	public final static int PULSE =		35;

	// {pulseStop} 
	public final static int PULSE_STOP =		36;

	// {releaseDevice int int} 
	public final static int RELEASE_DEVICE =		37;

	// {servoAttach Servo Integer} 
	public final static int SERVO_ATTACH =		38;

	// {servoDetach Servo} 
	public final static int SERVO_DETACH =		39;

	// {servoEventsEnabled Servo} 
	public final static int SERVO_EVENTS_ENABLED =		40;

	// {servoSweepStart Servo} 
	public final static int SERVO_SWEEP_START =		41;

	// {servoSweepStop Servo} 
	public final static int SERVO_SWEEP_STOP =		42;

	// {servoWrite Servo} 
	public final static int SERVO_WRITE =		43;

	// {servoWriteMicroseconds Servo} 
	public final static int SERVO_WRITE_MICROSECONDS =		44;

	// {setDebounce int} 
	public final static int SET_DEBOUNCE =		45;

	// {setDebug boolean} 
	public final static int SET_DEBUG =		46;

	// {setDigitalTriggerOnly Boolean} 
	public final static int SET_DIGITAL_TRIGGER_ONLY =		47;

	// {setLoadTimingEnabled boolean} 
	public final static int SET_LOAD_TIMING_ENABLED =		48;

	// {setPWMFrequency Integer Integer} 
	public final static int SET_PWMFREQUENCY =		49;

	// {setSampleRate int} 
	public final static int SET_SAMPLE_RATE =		50;

	// {setSerialRate int} 
	public final static int SET_SERIAL_RATE =		51;

	// {setServoSpeed Servo} 
	public final static int SET_SERVO_SPEED =		52;

	// {setTrigger int int int} 
	public final static int SET_TRIGGER =		53;

	// {softReset} 
	public final static int SOFT_RESET =		54;


  static {
  		byteToMethod.put(PUBLISH_MRLCOMM_ERROR,"publishMRLCommError");
		methodToByte.put("publishMRLCommError",PUBLISH_MRLCOMM_ERROR);

		byteToMethod.put(GET_VERSION,"getVersion");
		methodToByte.put("getVersion",GET_VERSION);

		byteToMethod.put(PUBLISH_VERSION,"publishVersion");
		methodToByte.put("publishVersion",PUBLISH_VERSION);

		byteToMethod.put(ADD_SENSOR_DATA_LISTENER,"addSensorDataListener");
		methodToByte.put("addSensorDataListener",ADD_SENSOR_DATA_LISTENER);

		byteToMethod.put(ANALOG_READ_POLLING_START,"analogReadPollingStart");
		methodToByte.put("analogReadPollingStart",ANALOG_READ_POLLING_START);

		byteToMethod.put(ANALOG_READ_POLLING_STOP,"analogReadPollingStop");
		methodToByte.put("analogReadPollingStop",ANALOG_READ_POLLING_STOP);

		byteToMethod.put(ANALOG_WRITE,"analogWrite");
		methodToByte.put("analogWrite",ANALOG_WRITE);

		byteToMethod.put(ATTACH_DEVICE,"attachDevice");
		methodToByte.put("attachDevice",ATTACH_DEVICE);

		byteToMethod.put(CREATE_DEVICE,"createDevice");
		methodToByte.put("createDevice",CREATE_DEVICE);

		byteToMethod.put(DIGITAL_READ_POLLING_START,"digitalReadPollingStart");
		methodToByte.put("digitalReadPollingStart",DIGITAL_READ_POLLING_START);

		byteToMethod.put(DIGITAL_READ_POLLING_STOP,"digitalReadPollingStop");
		methodToByte.put("digitalReadPollingStop",DIGITAL_READ_POLLING_STOP);

		byteToMethod.put(DIGITAL_WRITE,"digitalWrite");
		methodToByte.put("digitalWrite",DIGITAL_WRITE);

		byteToMethod.put(FIX_PIN_OFFSET,"fixPinOffset");
		methodToByte.put("fixPinOffset",FIX_PIN_OFFSET);

		byteToMethod.put(I2C_READ,"i2cRead");
		methodToByte.put("i2cRead",I2C_READ);

		byteToMethod.put(I2C_WRITE,"i2cWrite");
		methodToByte.put("i2cWrite",I2C_WRITE);

		byteToMethod.put(I2C_WRITE_READ,"i2cWriteRead");
		methodToByte.put("i2cWriteRead",I2C_WRITE_READ);

		byteToMethod.put(INTS_TO_STRING,"intsToString");
		methodToByte.put("intsToString",INTS_TO_STRING);

		byteToMethod.put(MOTOR_ATTACH,"motorAttach");
		methodToByte.put("motorAttach",MOTOR_ATTACH);

		byteToMethod.put(MOTOR_DETACH,"motorDetach");
		methodToByte.put("motorDetach",MOTOR_DETACH);

		byteToMethod.put(MOTOR_MOVE,"motorMove");
		methodToByte.put("motorMove",MOTOR_MOVE);

		byteToMethod.put(MOTOR_MOVE_TO,"motorMoveTo");
		methodToByte.put("motorMoveTo",MOTOR_MOVE_TO);

		byteToMethod.put(MOTOR_RESET,"motorReset");
		methodToByte.put("motorReset",MOTOR_RESET);

		byteToMethod.put(MOTOR_STOP,"motorStop");
		methodToByte.put("motorStop",MOTOR_STOP);

		byteToMethod.put(PIN_MODE,"pinMode");
		methodToByte.put("pinMode",PIN_MODE);

		byteToMethod.put(PUBLISH_ATTACHED_DEVICE,"publishAttachedDevice");
		methodToByte.put("publishAttachedDevice",PUBLISH_ATTACHED_DEVICE);

		byteToMethod.put(PUBLISH_DEBUG,"publishDebug");
		methodToByte.put("publishDebug",PUBLISH_DEBUG);

		byteToMethod.put(PUBLISH_MESSAGE_ACK,"publishMessageAck");
		methodToByte.put("publishMessageAck",PUBLISH_MESSAGE_ACK);

		byteToMethod.put(PUBLISH_PIN,"publishPin");
		methodToByte.put("publishPin",PUBLISH_PIN);

		byteToMethod.put(PUBLISH_PULSE,"publishPulse");
		methodToByte.put("publishPulse",PUBLISH_PULSE);

		byteToMethod.put(PUBLISH_PULSE_STOP,"publishPulseStop");
		methodToByte.put("publishPulseStop",PUBLISH_PULSE_STOP);

		byteToMethod.put(PUBLISH_SENSOR_DATA,"publishSensorData");
		methodToByte.put("publishSensorData",PUBLISH_SENSOR_DATA);

		byteToMethod.put(PUBLISH_SERVO_EVENT,"publishServoEvent");
		methodToByte.put("publishServoEvent",PUBLISH_SERVO_EVENT);

		byteToMethod.put(PUBLISH_STATUS,"publishStatus");
		methodToByte.put("publishStatus",PUBLISH_STATUS);

		byteToMethod.put(PUBLISH_TRIGGER,"publishTrigger");
		methodToByte.put("publishTrigger",PUBLISH_TRIGGER);

		byteToMethod.put(PULSE,"pulse");
		methodToByte.put("pulse",PULSE);

		byteToMethod.put(PULSE_STOP,"pulseStop");
		methodToByte.put("pulseStop",PULSE_STOP);

		byteToMethod.put(RELEASE_DEVICE,"releaseDevice");
		methodToByte.put("releaseDevice",RELEASE_DEVICE);

		byteToMethod.put(SERVO_ATTACH,"servoAttach");
		methodToByte.put("servoAttach",SERVO_ATTACH);

		byteToMethod.put(SERVO_DETACH,"servoDetach");
		methodToByte.put("servoDetach",SERVO_DETACH);

		byteToMethod.put(SERVO_EVENTS_ENABLED,"servoEventsEnabled");
		methodToByte.put("servoEventsEnabled",SERVO_EVENTS_ENABLED);

		byteToMethod.put(SERVO_SWEEP_START,"servoSweepStart");
		methodToByte.put("servoSweepStart",SERVO_SWEEP_START);

		byteToMethod.put(SERVO_SWEEP_STOP,"servoSweepStop");
		methodToByte.put("servoSweepStop",SERVO_SWEEP_STOP);

		byteToMethod.put(SERVO_WRITE,"servoWrite");
		methodToByte.put("servoWrite",SERVO_WRITE);

		byteToMethod.put(SERVO_WRITE_MICROSECONDS,"servoWriteMicroseconds");
		methodToByte.put("servoWriteMicroseconds",SERVO_WRITE_MICROSECONDS);

		byteToMethod.put(SET_DEBOUNCE,"setDebounce");
		methodToByte.put("setDebounce",SET_DEBOUNCE);

		byteToMethod.put(SET_DEBUG,"setDebug");
		methodToByte.put("setDebug",SET_DEBUG);

		byteToMethod.put(SET_DIGITAL_TRIGGER_ONLY,"setDigitalTriggerOnly");
		methodToByte.put("setDigitalTriggerOnly",SET_DIGITAL_TRIGGER_ONLY);

		byteToMethod.put(SET_LOAD_TIMING_ENABLED,"setLoadTimingEnabled");
		methodToByte.put("setLoadTimingEnabled",SET_LOAD_TIMING_ENABLED);

		byteToMethod.put(SET_PWMFREQUENCY,"setPWMFrequency");
		methodToByte.put("setPWMFrequency",SET_PWMFREQUENCY);

		byteToMethod.put(SET_SAMPLE_RATE,"setSampleRate");
		methodToByte.put("setSampleRate",SET_SAMPLE_RATE);

		byteToMethod.put(SET_SERIAL_RATE,"setSerialRate");
		methodToByte.put("setSerialRate",SET_SERIAL_RATE);

		byteToMethod.put(SET_SERVO_SPEED,"setServoSpeed");
		methodToByte.put("setServoSpeed",SET_SERVO_SPEED);

		byteToMethod.put(SET_TRIGGER,"setTrigger");
		methodToByte.put("setTrigger",SET_TRIGGER);

		byteToMethod.put(SOFT_RESET,"softReset");
		methodToByte.put("softReset",SOFT_RESET);


  }
  // -------- byteToMethod begin --------------------

  static public String byteToMethod(int m) {
    if (byteToMethod.containsKey(m)) {
      return byteToMethod.get(m);
    }
    return null;
  }

  /**
   * MAGIC_NUMBER|NUM_BYTES|FUNCTION|DATA0|DATA1|....|DATA(N)
   * 
   * @throws CodecException
   */
  @Override
  public String decodeImpl(int newByte) {

    // log.info(String.format("byteCount %d", byteCount));
    ++byteCount;
    if (byteCount == 1 && newByte != MAGIC_NUMBER) {
      // reset - try again
      rest.setLength(0);
      byteCount = 0;
      decodeMsgSize = 0;

      error("bad magic number %d", newByte);
    }

    if (byteCount == 2) {
      // get the size of message
      // todo check msg < 64 (MAX_MSG_SIZE)
      decodeMsgSize = newByte;
    }

    // set method
    if (byteCount == 3) {
      rest.append(byteToMethod.get(newByte));
    }

    if (byteCount > 3) {
      // FIXME - for
      rest.append(String.format("/%d", newByte));
    }

    // if received header + msg
    if (byteCount == 2 + decodeMsgSize) {
      // msg done
      byteCount = 0;
      rest.append("\n");
      String ret = rest.toString();
      rest.setLength(0);
      byteCount = 0;
      decodeMsgSize = 0;
      return ret;
    }

    // not ready yet
    // no msg :P should be null ???
    return null;
  }

  @Override
  public String decode(int[] msgs) {
    if (msgs == null) {
      return new String("");
    }

    log.info(String.format("decoding input of %d bytes", msgs.length));

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < msgs.length; ++i) {
      sb.append(decode(msgs[i]));
    }

    return sb.toString();
  }

  // must maintain state - partial string
  @Override
  public int[] encode(String msgs) {

    // moved all member vars as local
    // otherwise state information would explode
    // cheap way of making threadsafe
    // this variables
    int pos = 0;
    int newLinePos = 0;
    int slashPos = 0;

    ArrayList<Integer> temp = new ArrayList<Integer>();
    ArrayList<Integer> data = new ArrayList<Integer>();

    // --

    if (msgs == null) {
      return new int[0];
    }

    // log.info(String.format("encoding input of %d characters",
    // msgs.length()));

    newLinePos = msgs.indexOf("\n", pos);
    slashPos = msgs.indexOf("/", pos);

    // while not done - string not completed...
    // make sure you leave in a good state if not a full String

    // FIXME test cases - newLinePos == -1 pos == -1 00 01 10 11

    // while either / or new line or eof (string) [eof leave vars in
    // unfinished state]
    while (slashPos != -1 || newLinePos != -1) {

      // ++currentLine;

      if (slashPos > 0 && newLinePos > 0 && slashPos < newLinePos) {
        // digitalWrite/9/1
        // pos^ slashpos ^ ^newLinePos
        if (temp.size() == 0) {
          String method = msgs.substring(pos, slashPos);
          pos = slashPos + 1;
          // found method
          if (methodToByte.containsKey(method)) {
            temp.add(methodToByte.get(method));
          } else {
            error("method [%s] at position %d is not defined for codec", method, pos);
            pos = 0;
            data.clear();
          }
        } else {
          // in data region
          String param = msgs.substring(pos, slashPos);
          temp.add(Integer.parseInt(param));
          pos = slashPos + 1;
        }
      } else if ((slashPos > 0 && newLinePos > 0 && newLinePos < slashPos) || (slashPos == -1 && newLinePos > 0)) {
        // end of message slash is beyond newline || newline exists and
        // slash does not
        String param = msgs.substring(pos, newLinePos);
        temp.add(Integer.parseInt(param));
        pos = newLinePos + 1;
        slashPos = pos;

        // unload temp buffer - start next message - if there is one
        data.add(170);// MAGIC NUMBER
        data.add(temp.size());// SIZE
        for (int i = 0; i < temp.size(); ++i) {
          // should be end of record
          data.add(temp.get(i));
        }
        // clear buffer - ready for next message
        temp.clear();
      }

      newLinePos = msgs.indexOf("\n", pos);
      slashPos = msgs.indexOf("/", pos);

    }

    int[] ret = new int[data.size()];
    // for (int i : data) {
    for (int i = 0; i < data.size(); ++i) {
      ret[i] = data.get(i);
    }

    // FIXME - more cases when pos is reset - or all vars reset?
    pos = 0;
    data.clear();
    return ret;
  }

  @Override
  public String getCodecExt() {
    return getKey().substring(0, 3);
  }

  @Override
  public String getKey() {
    return "arduino";
  }
  
  public static String functionToString(int function) {
 	switch(function){
	case ArduinoMsgCodec.PUBLISH_MRLCOMM_ERROR:{
		return "PUBLISH_MRLCOMM_ERROR";

	}
	case ArduinoMsgCodec.GET_VERSION:{
		return "GET_VERSION";

	}
	case ArduinoMsgCodec.PUBLISH_VERSION:{
		return "PUBLISH_VERSION";

	}
	case ArduinoMsgCodec.ADD_SENSOR_DATA_LISTENER:{
		return "ADD_SENSOR_DATA_LISTENER";

	}
	case ArduinoMsgCodec.ANALOG_READ_POLLING_START:{
		return "ANALOG_READ_POLLING_START";

	}
	case ArduinoMsgCodec.ANALOG_READ_POLLING_STOP:{
		return "ANALOG_READ_POLLING_STOP";

	}
	case ArduinoMsgCodec.ANALOG_WRITE:{
		return "ANALOG_WRITE";

	}
	case ArduinoMsgCodec.ATTACH_DEVICE:{
		return "ATTACH_DEVICE";

	}
	case ArduinoMsgCodec.CREATE_DEVICE:{
		return "CREATE_DEVICE";

	}
	case ArduinoMsgCodec.DIGITAL_READ_POLLING_START:{
		return "DIGITAL_READ_POLLING_START";

	}
	case ArduinoMsgCodec.DIGITAL_READ_POLLING_STOP:{
		return "DIGITAL_READ_POLLING_STOP";

	}
	case ArduinoMsgCodec.DIGITAL_WRITE:{
		return "DIGITAL_WRITE";

	}
	case ArduinoMsgCodec.FIX_PIN_OFFSET:{
		return "FIX_PIN_OFFSET";

	}
	case ArduinoMsgCodec.I2C_READ:{
		return "I2C_READ";

	}
	case ArduinoMsgCodec.I2C_WRITE:{
		return "I2C_WRITE";

	}
	case ArduinoMsgCodec.I2C_WRITE_READ:{
		return "I2C_WRITE_READ";

	}
	case ArduinoMsgCodec.INTS_TO_STRING:{
		return "INTS_TO_STRING";

	}
	case ArduinoMsgCodec.MOTOR_ATTACH:{
		return "MOTOR_ATTACH";

	}
	case ArduinoMsgCodec.MOTOR_DETACH:{
		return "MOTOR_DETACH";

	}
	case ArduinoMsgCodec.MOTOR_MOVE:{
		return "MOTOR_MOVE";

	}
	case ArduinoMsgCodec.MOTOR_MOVE_TO:{
		return "MOTOR_MOVE_TO";

	}
	case ArduinoMsgCodec.MOTOR_RESET:{
		return "MOTOR_RESET";

	}
	case ArduinoMsgCodec.MOTOR_STOP:{
		return "MOTOR_STOP";

	}
	case ArduinoMsgCodec.PIN_MODE:{
		return "PIN_MODE";

	}
	case ArduinoMsgCodec.PUBLISH_ATTACHED_DEVICE:{
		return "PUBLISH_ATTACHED_DEVICE";

	}
	case ArduinoMsgCodec.PUBLISH_DEBUG:{
		return "PUBLISH_DEBUG";

	}
	case ArduinoMsgCodec.PUBLISH_MESSAGE_ACK:{
		return "PUBLISH_MESSAGE_ACK";

	}
	case ArduinoMsgCodec.PUBLISH_PIN:{
		return "PUBLISH_PIN";

	}
	case ArduinoMsgCodec.PUBLISH_PULSE:{
		return "PUBLISH_PULSE";

	}
	case ArduinoMsgCodec.PUBLISH_PULSE_STOP:{
		return "PUBLISH_PULSE_STOP";

	}
	case ArduinoMsgCodec.PUBLISH_SENSOR_DATA:{
		return "PUBLISH_SENSOR_DATA";

	}
	case ArduinoMsgCodec.PUBLISH_SERVO_EVENT:{
		return "PUBLISH_SERVO_EVENT";

	}
	case ArduinoMsgCodec.PUBLISH_STATUS:{
		return "PUBLISH_STATUS";

	}
	case ArduinoMsgCodec.PUBLISH_TRIGGER:{
		return "PUBLISH_TRIGGER";

	}
	case ArduinoMsgCodec.PULSE:{
		return "PULSE";

	}
	case ArduinoMsgCodec.PULSE_STOP:{
		return "PULSE_STOP";

	}
	case ArduinoMsgCodec.RELEASE_DEVICE:{
		return "RELEASE_DEVICE";

	}
	case ArduinoMsgCodec.SERVO_ATTACH:{
		return "SERVO_ATTACH";

	}
	case ArduinoMsgCodec.SERVO_DETACH:{
		return "SERVO_DETACH";

	}
	case ArduinoMsgCodec.SERVO_EVENTS_ENABLED:{
		return "SERVO_EVENTS_ENABLED";

	}
	case ArduinoMsgCodec.SERVO_SWEEP_START:{
		return "SERVO_SWEEP_START";

	}
	case ArduinoMsgCodec.SERVO_SWEEP_STOP:{
		return "SERVO_SWEEP_STOP";

	}
	case ArduinoMsgCodec.SERVO_WRITE:{
		return "SERVO_WRITE";

	}
	case ArduinoMsgCodec.SERVO_WRITE_MICROSECONDS:{
		return "SERVO_WRITE_MICROSECONDS";

	}
	case ArduinoMsgCodec.SET_DEBOUNCE:{
		return "SET_DEBOUNCE";

	}
	case ArduinoMsgCodec.SET_DEBUG:{
		return "SET_DEBUG";

	}
	case ArduinoMsgCodec.SET_DIGITAL_TRIGGER_ONLY:{
		return "SET_DIGITAL_TRIGGER_ONLY";

	}
	case ArduinoMsgCodec.SET_LOAD_TIMING_ENABLED:{
		return "SET_LOAD_TIMING_ENABLED";

	}
	case ArduinoMsgCodec.SET_PWMFREQUENCY:{
		return "SET_PWMFREQUENCY";

	}
	case ArduinoMsgCodec.SET_SAMPLE_RATE:{
		return "SET_SAMPLE_RATE";

	}
	case ArduinoMsgCodec.SET_SERIAL_RATE:{
		return "SET_SERIAL_RATE";

	}
	case ArduinoMsgCodec.SET_SERVO_SPEED:{
		return "SET_SERVO_SPEED";

	}
	case ArduinoMsgCodec.SET_TRIGGER:{
		return "SET_TRIGGER";

	}
	case ArduinoMsgCodec.SOFT_RESET:{
		return "SOFT_RESET";

	}

  
		default: {
			return "OTHER(" + Integer.toString(function) + ")";

		} // default
		} // switch
	}

    
    
      public static void main(String[] args) {
    
        try {
          LoggingFactory.getInstance().configure();
          LoggingFactory.getInstance().setLevel(Level.INFO);
          LoggingFactory.getInstance().addAppender("FILE");
    
          // begin ----
    
          log.info("===setUpBeforeClass===");
          // LoggingFactory.getInstance().setLevel(Level.INFO);
          Runtime.start("gui", "GUIService");
          Arduino arduino = (Arduino) Runtime.start("arduino", "Arduino");
          Serial serial = arduino.getSerial();
          serial.record("out");
    
          // rxtxLib
          arduino.connect("COM15");
    
          // arduino.connectTCP("localhost", 9191);
    
          arduino.pinMode(10, 1);
          arduino.digitalWrite(10, 1);
          arduino.analogReadPollingStart(0);
          // uart = serial.createVirtualUART();
          arduino.analogReadPollingStop(0);
          arduino.analogReadPollingStart(0);
          arduino.analogReadPollingStop(0);
          arduino.analogReadPollingStart(0);
          arduino.analogReadPollingStop(0);
    
          serial.stopRecording();
          // Test test = (org.myrobotlab.service.Test) Runtime.start("test",
          // "Test");
    
          // / end ---
    
          /*
           * ArduinoMsgCodec codec = new ArduinoMsgCodec();
           * 
           * FileOutputStream test = new FileOutputStream(new File("out.bin"));
           * 
           * for (int j = 0; j < 4; ++j) { for (int i = 0; i < 100; ++i) { int[]
           * data = codec.encode(String.format("publishPin/15/%d/%d\n", j, i)); for
           * (int z = 0; z < data.length; ++z){ test.write(data[z]); } } }
           * 
           * test.close();
           */
    
          /*
           * 
           * // digitalWrite/9/1 StringBuilder sb = new StringBuilder();
           * sb.append(codec.decode(170)); sb.append(codec.decode(3));
           * sb.append(codec.decode(7)); sb.append(codec.decode(9));
           * sb.append(codec.decode(1));
           * 
           * sb.append(codec.decode(170)); sb.append(codec.decode(3));
           * sb.append(codec.decode(7)); sb.append(codec.decode(11));
           * sb.append(codec.decode(0));
           * 
           * log.info(String.format("[%s]", sb.toString()));
           * 
           * codec.encode(sb.toString());
           * 
           * Arduino arduino = (Arduino) Runtime.start("arduino", "Arduino"); Serial
           * serial = arduino.getSerial(); serial.record();
           * serial.processRxByte(170); serial.processRxByte(3);
           * serial.processRxByte(7); serial.processRxByte(9);
           * serial.processRxByte(1);
           */
    
        } catch (Exception e) {
          Logging.logError(e);
        }
    
      }

  
}