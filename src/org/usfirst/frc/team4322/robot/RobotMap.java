package org.usfirst.frc.team4322.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap
{
	// ****************************
    // ** DIGITAL INPUT MAPPINGS **
    // ****************************
	public static final int AUTO_DIO_PORT_0 = 0;
	public static final int AUTO_DIO_PORT_1 = 1;
	public static final int AUTO_DIO_PORT_2 = 2;
	public static final int AUTO_DIO_PORT_3 = 3;
	public static final int ELEVATOR_LIMIT_HOME = 4;

	public static final int COLLECTOR_DEPLOYER_ENC_ANALOG_PORT  = 1;

    // *******************************
    // ** MOTOR CONTROLLER MAPPINGS **
    // *******************************
    public static final int DRIVEBASE_MOTORCONTROLLER_LEFT_MASTER_ADDR = 12;
    public static final int DRIVEBASE_MOTORCONTROLLER_LEFT_SLAVE_ADDR = 13;
    public static final int DRIVEBASE_MOTORCONTROLLER_RIGHT_MASTER_ADDR = 10;
    public static final int DRIVEBASE_MOTORCONTROLLER_RIGHT_SLAVE_ADDR = 11;
    public static final int COLLECTOR_MOTORCONTROLLER_LEFT_ADDR = 20;
    public static final int COLLECTOR_MOTORCONTROLLER_RIGHT_ADDR = 21;
    public static final int ELEVATOR_MOTORCONTROLLER_MASTER_ADDR = 30;
    public static final int ELEVATOR_MOTORCONTROLLER_SLAVE_ADDR = 31;
    public static final int COLLECTOR_DEPLOYER_MOTORCONTROLLER_ADDR = 40;
    
    // *******************************
    // ** DRIVEBASE PARAMETERS **
    // *******************************
    public static final double DRIVEBASE_TALON_RAMP_RATE = .25;
    public static final double DRIVEBASE_ENCODER_TICKS_PER_ROTATION = 1024;
    public static final double DRIVEBASE_MAX_SPEED = 1300;
    public static final double DRIVEBASE_TURN_SENSITIVITY = .3;
    public static final double DRIVEBASE_WHEELBASE_WIDTH = 25.25/12; //2.099 feet
    public static final double DRIVEBASE_WHEEL_DIAMETER = 6; //INCHES
    public static final double DRIVEBASE_KF = .7869; //good
    public static final double DRIVEBASE_KP = 4; //temporary
    public static final double DRIVEBASE_KI = 0.0000065; //temporary
    public static final double DRIVEBASE_KD = 350; //temporary
    public static final double DRIVEBASE_ANTI_TIP_CONSTANT = 150;

    // *************************
    // ** ELEVATOR PARAMETERS **
    // *************************
    public static final int ELEVATOR_SCALE_POSITION = 28500;
    public static final int ELEVATOR_READY_TO_CLIMB_POSITION = 22000;
    public static final int ELEVATOR_SWITCH_POSITION = 10500;
    public static final int ELEVATOR_HOME_POSITION = 0;
    public static final int ELEVATOR_MAX_SPEED = 1000;
    public static final int ELEVATOR_MAX_ACCEL = 2000;
    public static final double ELEVATOR_TRIM_SENSITIVITY = .3;
    public static final double ELEVATOR_HOLDING_VPERCENT = 0.07;
    public static final double ELEVATOR_KF = .5115;
    public static final double ELEVATOR_KP = 1;
    public static final double ELEVATOR_KI = 0;
    public static final double ELEVATOR_KD = 0;
    public static final int ELEVATOR_TOLERANCE = 30;

    // **************************
    // ** COLLECTOR PARAMETERS **
    // **************************
    public static final int COLLECTOR_DEPLOYER_SETPOINT = 90;
    public static final int COLLECTOR_DEPLOYER_SCALE_SETPOINT = 45;
    public static final double COLLECTOR_DEPLOYER_KP = 1;
    public static final double COLLECTOR_DEPLOYER_KI = 0;
    public static final double COLLECTOR_DEPLOYER_KD = 0;

    // ********************************
    // ** JOYSTICK RAMPING EQUATIONS **
    // ********************************
    public static double spookyRamping(double input)
    {
        return 2 / Math.PI * Math.sin(Math.tan(input)) * Math.cosh(Math.pow(input, 5));
    }
    public static double cubicRamping(double input)
    {
        return Math.pow(input, 3);
    }

    // *************************
    // ** PNEUMATIC ID VALUES **
    // *************************
    public static final int PNEUMATIC_COLLECTOR_ACTUATOR_SOLENOID_ID_0 = 0;
    public static final int PNEUMATIC_COLLECTOR_ACTUATOR_SOLENOID_ID_1 = 1;
    public static final int PNEUMATIC_COLLECTOR_DEPLOYER_SOLENOID_ID_2 = 2;
    public static final int PNEUMATIC_COLLECTOR_DEPLOYER_SOLENOID_ID_3 = 3;
    
}
