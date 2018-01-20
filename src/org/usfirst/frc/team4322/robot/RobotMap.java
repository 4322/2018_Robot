package org.usfirst.frc.team4322.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap
{

    // *******************************
    // ** MOTOR CONTROLLER MAPPINGS **
    // *******************************
    public static final int DRIVEBASE_MOTORCONTROLLER_LEFT_MASTER_ADDR = 10;
    public static final int DRIVEBASE_MOTORCONTROLLER_LEFT_SLAVE_ADDR = 11;
    public static final int DRIVEBASE_MOTORCONTROLLER_RIGHT_MASTER_ADDR = 12;
    public static final int DRIVEBASE_MOTORCONTROLLER_RIGHT_SLAVE_ADDR = 13;
    
    public static final double DRIVEBASE_TALON_RAMP_RATE = .25;
    
    public static final double DRIVEBASE_ENCODER_TICKS_PER_ROTATION = 1024;
    
    public static final double DRIVEBASE_MAX_SPEED = 977.85;

}
