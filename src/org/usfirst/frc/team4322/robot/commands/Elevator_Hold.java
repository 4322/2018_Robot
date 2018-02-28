package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class Elevator_Hold extends Command {

	public Elevator_Hold()
	{
		requires(Robot.elevator);
	}
	@Override
	protected void execute()
	{
		Robot.elevator.master.set(ControlMode.PercentOutput, .06);
	}
	@Override
	protected void end()
	{
		Robot.elevator.master.set(ControlMode.PercentOutput, 0);
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

}
