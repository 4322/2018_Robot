package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class CollectorRollers_Stop extends Command {

	public CollectorRollers_Stop()
	{
		requires(Robot.collectorRollers);
	}
	@Override
	protected void execute()
	{
		Robot.collectorRollers.collectorRight.set(0);
		Robot.collectorRollers.collectorLeft.set(0);
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

}
