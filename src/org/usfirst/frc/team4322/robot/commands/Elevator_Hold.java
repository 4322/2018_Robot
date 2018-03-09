package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class Elevator_Hold extends Command {

	private double setpoint;
	public Elevator_Hold()
	{
		requires(Robot.elevator);
	}
	@Override
	protected void initialize()
	{
		setpoint = Robot.elevator.getPosition();
	}
	@Override
	protected void execute()
	{
		System.out.println("HOLDING ELEVATOR WITH " + RobotMap.ELEVATOR_HOLDING_VPERCENT + " OUTPUT!");
		Robot.elevator.master.set(ControlMode.PercentOutput, RobotMap.ELEVATOR_HOLDING_VPERCENT);
//		double out;
//		double error = setpoint - Robot.elevator.getPosition();
//
//		out = error * RobotMap.ELEVATOR_HOLDING_VPERCENT + RobotMap.ELEVATOR_HOLDING_VPERCENT;
	}
	@Override
	protected boolean isFinished() {
		return true;
	}

}
