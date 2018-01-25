package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.OI;
import org.usfirst.frc.team4322.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class Collector_Collect extends Command {

	public Collector_Collect()
	{
		requires(Robot.collector);
	}
	@Override
	protected void execute()
	{
		Double pow = OI.operator.leftStick.getY();
		Robot.collector.collectorLeft.set(ControlMode.PercentOutput, pow);
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
