package org.usfirst.frc.team4322.robot;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    public static XboxController pilot;
    public static XboxController operator;
    
    public OI()
    {
        //DRIVER CONTROLS
        pilot = new XboxController(0);

        operator = new XboxController(1);

        
      
    }
}

