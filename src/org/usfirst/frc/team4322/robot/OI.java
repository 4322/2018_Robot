package org.usfirst.frc.team4322.robot;

import org.usfirst.frc.team4322.robot.commands.*;

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
        operator.rt.whileHeld(new Elevator_Manual());

        operator.y.whenPressed(new Elevator_Scale());
        operator.a.whenPressed(new Elevator_Switch());
        operator.b.whenPressed(new Elevator_Home());
        operator.x.whenPressed(new Group_ReadyForClimb());

        operator.lt.whileHeld(new CollectorActuator_Open());
        operator.lb.whileHeld(new CollectorRollers_Collect());
        operator.rb.whileHeld(new CollectorRollers_Eject());

        operator.dPad.down.whileHeld(new CollectorDeployer_Deploy());
        operator.dPad.up.whileHeld(new CollectorDeployer_UnDeploy());
        
      
    }
}

