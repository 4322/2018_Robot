package org.usfirst.frc.team4322.robot.commands.auto;

import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.commands.*;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoGroup_ScaleLeft_Left extends CommandGroup 
{
	public AutoGroup_ScaleLeft_Left()
	{
		addSequential(new Auto_MotionProfileDrive(Robot.autoScaleLeftLeft));
//		addSequential(new DriveBase_DriveDistance());
		addSequential(new Elevator_Scale());
//		addSequential(new DriveBase_DriveArc( .5, 33, 2));
		addSequential(new CollectorRollers_Eject());
		addSequential(new DriveBase_DriveDistance(-12, 900, 900));
		addSequential(new Elevator_Home());

	}
}
