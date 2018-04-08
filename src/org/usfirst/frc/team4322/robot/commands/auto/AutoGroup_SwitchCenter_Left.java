package org.usfirst.frc.team4322.robot.commands.auto;

import org.usfirst.frc.team4322.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team4322.robot.commands.*;

public class AutoGroup_SwitchCenter_Left extends CommandGroup {

	public AutoGroup_SwitchCenter_Left()
	{
		addParallel(new Elevator_Switch());
		addParallel(new CollectorDeployer_Deploy());
		addSequential(new Auto_MotionProfileDrive(Robot.autoSwitchLeft));
		addSequential(new CollectorActuator_Open());
//		addSequential(new Command_Delay(1500));
//		addSequential(new DriveBase_DriveDistance(-18, -500, -500));
//		addSequential(new Elevator_Home());
//		addSequential(new DriveBase_Rotate(90));
//		addParallel(new CollectorActuator_Open());
//		addParallel(new CollectorRollers_Collect());
//		addSequential(new DriveBase_DriveDistance(30, 500, 500));
//		addParallel(new CollectorRollers_Stop());
//		addSequential(new CollectorActuator_Close());
//		addSequential(new DriveBase_DriveDistance(-30, -500, -500));
//		addParallel(new Elevator_Switch());
//		addSequential(new DriveBase_Rotate(-90));
//		addSequential(new DriveBase_DriveDistance(20, 500, 500));
//		addSequential(new CollectorActuator_Open());



//	//	addSequential(new DriveBase_DriveArc(2, -28.06 * 2, Math.abs((118/12) * Math.sin(Math.toRadians(32.06)))));
//	//	addSequential(new DriveBase_DriveArc(2, 28.06 * 2, Math.abs((118/12) * Math.sin(Math.toRadians(32.06)))));

//		addSequential(new DriveBase_DriveArc(2, -20.6 * 2,  .9 * Math.abs((110/12) * Math.sin(Math.toRadians(24.06)))));
//		addSequential(new DriveBase_DriveArc(2, 20.6 * 2, Math.abs((110/12) * Math.sin(Math.toRadians(24.06)))));

	}
}
