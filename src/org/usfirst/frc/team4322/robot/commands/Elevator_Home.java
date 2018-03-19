package org.usfirst.frc.team4322.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;
import org.usfirst.frc.team4322.robot.subsystems.Elevator.ElevatorPosition;

import edu.wpi.first.wpilibj.command.Command;

public class Elevator_Home extends Command {

	private int ticks;
	private double currentError = 0;
	private double lastError;

	public Elevator_Home()
	{
		ticks = Robot.elevator.home;
		requires(Robot.elevator);
	}
	@Override
	protected void initialize()
	{
		lastError = Double.MAX_VALUE;

		Robot.elevator.clearProfiles();

		Robot.elevator.useMotionMagicMode();

		switch (Robot.elevator.position)
		{
			case SCALE:
				ticks = RobotMap.ELEVATOR_HOME_POSITION;
				Robot.elevator.setMotionMagic(-RobotMap.ELEVATOR_MAX_SPEED / 2, -RobotMap.ELEVATOR_MAX_ACCEL, ticks);
				break;
			case HOME:
				break;
			case SWITCH:
				ticks = RobotMap.ELEVATOR_HOME_POSITION;
				Robot.elevator.setMotionMagic(-RobotMap.ELEVATOR_MAX_SPEED / 2, -RobotMap.ELEVATOR_MAX_ACCEL, ticks);
				break;
			case READY_TO_CLIMB:
				ticks = RobotMap.ELEVATOR_HOME_POSITION;
				Robot.elevator.setMotionMagic(-RobotMap.ELEVATOR_MAX_SPEED / 2, -RobotMap.ELEVATOR_MAX_ACCEL, ticks);
				break;
		}
	}
	@Override
	protected void execute()
	{
		System.out.print("RUNNING MOTION MAGIC HOME: ");
		System.out.print(Robot.elevator.getPosition() - RobotMap.ELEVATOR_HOME_POSITION);
		System.out.println(" (" + Robot.elevator.getTrajectoryVelocity() + ")");
	}
	@Override
	protected void end()
	{
		System.out.println("ELEVATOR HOMED!");
		Robot.elevator.position = ElevatorPosition.HOME;
		Robot.elevator.clearProfiles();
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		currentError = Math.abs(Robot.elevator.getPosition() - RobotMap.ELEVATOR_HOME_POSITION);
		if (Robot.elevator.position == ElevatorPosition.HOME)
		{
			return true;
		}
		else
		{
			if (currentError > (lastError + RobotMap.ELEVATOR_TOLERANCE))
			{
				return true;
			}
			else
			{
				lastError = currentError;

			}
			return (currentError <= RobotMap.ELEVATOR_TOLERANCE);
		}
	}

}
