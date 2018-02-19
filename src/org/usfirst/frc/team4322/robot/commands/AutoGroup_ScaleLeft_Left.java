package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoGroup_ScaleLeft_Left extends CommandGroup 
{
	public AutoGroup_ScaleLeft_Left()
	{
		addParallel(new Elevator_Scale());
		addSequential(new Auto_MotionProfileDrive(Robot.autoScaleLeftLeft));
		addSequential(new Collector_Eject());
	}
}