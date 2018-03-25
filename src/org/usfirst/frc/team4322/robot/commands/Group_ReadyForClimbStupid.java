package org.usfirst.frc.team4322.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team4322.robot.Robot;


public class Group_ReadyForClimbStupid extends CommandGroup
{
	public Group_ReadyForClimbStupid()
	{
		addSequential(new Elevator_ReadyToClimb());
	}
}
