package org.usfirst.frc.team4322.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;


public class Group_HomeAndCollect extends CommandGroup
{

	public Group_HomeAndCollect()
	{
		addParallel(new Elevator_Home());
		addSequential(new CollectorDeployer_Deploy());
	}
}
