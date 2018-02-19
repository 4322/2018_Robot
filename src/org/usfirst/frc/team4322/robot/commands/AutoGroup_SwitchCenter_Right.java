package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoGroup_SwitchCenter_Right extends CommandGroup {

	public AutoGroup_SwitchCenter_Right()
	{
		addParallel(new Elevator_Switch());
		addSequential(new Auto_MotionProfileDrive(Robot.autoSwitchRight));
		addSequential(new Collector_Eject());
	}
}
