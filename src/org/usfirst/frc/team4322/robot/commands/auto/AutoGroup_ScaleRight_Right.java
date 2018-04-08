package org.usfirst.frc.team4322.robot.commands.auto;

import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.commands.*;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoGroup_ScaleRight_Right extends CommandGroup 
{
	public AutoGroup_ScaleRight_Right()
	{
		addSequential(new Auto_MotionProfileDrive(Robot.autoScaleRightRight1));
//		addSequential(new DriveBase_DriveDistance());
		addParallel(new CollectorDeployer_Deploy());
		addSequential(new Elevator_Scale());
//		addSequential(new DriveBase_DriveArc( .5, 33, 2));
		addSequential(new Auto_MotionProfileDrive(Robot.autoScaleRightRight2));
		addSequential(new CollectorRollers_Eject());
		addSequential(new DriveBase_DriveDistance(20, 100, 100));
		addSequential(new Elevator_Home());
	}
}
