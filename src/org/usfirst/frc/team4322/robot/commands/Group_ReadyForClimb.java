package org.usfirst.frc.team4322.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;


public class Group_ReadyForClimb extends CommandGroup
{
	public Group_ReadyForClimb()
	{
		addParallel(new CollectorActuator_Open());
		addParallel(new CollectorDeployer_UnDeploy());
		addSequential(new Elevator_ReadyToClimb());
	}
}
