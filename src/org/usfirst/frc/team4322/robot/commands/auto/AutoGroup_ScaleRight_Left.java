package org.usfirst.frc.team4322.robot.commands.auto;

import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.commands.*;


import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoGroup_ScaleRight_Left extends CommandGroup 
{
	public AutoGroup_ScaleRight_Left()
	{
		addParallel(new Elevator_Scale());
//		addSequential(new Auto_MotionProfileDrive(Robot.autoScaleRightLeft));
		addSequential(new CollectorRollers_Eject());
	}
}
