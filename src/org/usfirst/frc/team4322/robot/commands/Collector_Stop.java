package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class Collector_Stop extends Command {

	public Collector_Stop()
	{
		requires(Robot.collector);
	}
	@Override
	protected void execute()
	{
		Robot.collector.collectorRight.set(0);
		Robot.collector.collectorLeft.set(0);
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

}
