package org.usfirst.frc.team4322.robot;

import org.usfirst.frc.team4322.robot.commands.Collector_Collect;
import org.usfirst.frc.team4322.robot.commands.Collector_Eject;
import org.usfirst.frc.team4322.robot.commands.Elevator_Home;
import org.usfirst.frc.team4322.robot.commands.Elevator_Scale;
import org.usfirst.frc.team4322.robot.commands.Elevator_Switch;

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
        operator.y.whenPressed(new Elevator_Scale());
        operator.a.whenPressed(new Elevator_Switch());
        operator.b.whenPressed(new Elevator_Home());
        operator.lb.whileHeld(new Collector_Collect());
        operator.rb.whileHeld(new Collector_Eject());
        
      
    }
}

