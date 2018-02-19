package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.OI;
import org.usfirst.frc.team4322.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class Elevator_Manual extends Command {

	public Elevator_Manual()
	{
		requires(Robot.elevator);
	}
	@Override
	protected void execute()
	{
		Robot.elevator.master.set(OI.operator.getY());
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
