package org.usfirst.frc.team4322.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team4322.robot.Robot;


public class Group_ReadyForClimbStupider extends CommandGroup
{

	public Group_ReadyForClimbStupider()
	{
		addParallel(new CollectorActuator_Open());
		addSequential(new CollectorDeployer_UnDeploy());
	}

	@Override
	protected void end()
	{
		Robot.isReadyForClimb = true;
	}
}
