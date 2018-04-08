package org.usfirst.frc.team4322.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;


public class Group_Scale extends CommandGroup
{

	public Group_Scale()
	{
		addParallel(new CollectorDeployer_DeployStupid());
		addSequential(new Elevator_Scale());
	}
}
