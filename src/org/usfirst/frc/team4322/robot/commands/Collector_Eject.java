package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class Collector_Eject extends Command {

	public Collector_Eject()
	{
		requires(Robot.collector);
	}
	@Override
	public void execute()
	{
		Robot.collector.collectorLeft.set(ControlMode.PercentOutput, -.5);
		Robot.collector.collectorRight.set(ControlMode.PercentOutput, -.5);
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
