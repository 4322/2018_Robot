package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoGroup_SwitchCenter_Left extends CommandGroup {

	public AutoGroup_SwitchCenter_Left()
	{
		addParallel(new Elevator_Switch());
		addSequential(new Auto_MotionProfileDrive(Robot.autoSwitchLeft));
		addSequential(new Collector_Eject());
	}
}
