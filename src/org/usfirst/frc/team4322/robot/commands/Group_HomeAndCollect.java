package org.usfirst.frc.team4322.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team4322.robot.Robot;


public class Group_HomeAndCollect extends CommandGroup
{

	public Group_HomeAndCollect()
	{
		addParallel(new Elevator_Home());
		addParallel(new CollectorActuator_Open());
		addSequential(new CollectorDeployer_Deploy());
		addSequential(new CollectorActuator_Close());
	}

	@Override
	protected void end()
	{
		Robot.isReadyForClimb = false;
	}
}
