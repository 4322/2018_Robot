package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class Elevator_Scale_PID extends Command {
	public Elevator_Scale_PID()
	{
		requires(Robot.elevator);
	}
	@Override
	protected void initialize()
	{
		
	}
	@Override
	protected void execute()
	{
//		Robot.elevator.set()
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
