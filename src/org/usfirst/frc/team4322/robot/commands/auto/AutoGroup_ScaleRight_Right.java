package org.usfirst.frc.team4322.robot.commands.auto;

import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.commands.*;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoGroup_ScaleRight_Right extends CommandGroup 
{
	public AutoGroup_ScaleRight_Right()
	{
//		addParallel(new Elevator_Scale());
//		addSequential(new Auto_MotionProfileDrive(Robot.autoScaleRightRight));
		addSequential(new CollectorRollers_Eject());
	}
}
