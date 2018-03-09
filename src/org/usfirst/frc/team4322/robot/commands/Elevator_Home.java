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

		Robot.elevator.master.clearMotionProfileTrajectories();

		Robot.elevator.master.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 10);
		Robot.elevator.master.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);

		switch (Robot.elevator.position)
		{
			case SCALE:
				ticks = RobotMap.ELEVATOR_HOME_POSITION;
				Robot.elevator.master.configMotionCruiseVelocity(-RobotMap.ELEVATOR_MAX_SPEED / 2, 10);
				Robot.elevator.master.configMotionAcceleration(-RobotMap.ELEVATOR_MAX_ACCEL, 10);
				Robot.elevator.master.set(ControlMode.MotionMagic, ticks);
				break;
			case HOME:
				ticks = 0;
				Robot.elevator.master.configMotionCruiseVelocity(RobotMap.ELEVATOR_MAX_SPEED, 10);
				Robot.elevator.master.configMotionAcceleration(RobotMap.ELEVATOR_MAX_ACCEL, 10);
				break;
			case SWITCH:
				ticks = RobotMap.ELEVATOR_HOME_POSITION;
				Robot.elevator.master.configMotionCruiseVelocity(-RobotMap.ELEVATOR_MAX_SPEED / 2, 10);
				Robot.elevator.master.configMotionAcceleration(-RobotMap.ELEVATOR_MAX_ACCEL, 10);
				Robot.elevator.master.set(ControlMode.MotionMagic, ticks);
				break;
		}
	}
	@Override
	protected void execute()
	{
		System.out.print("RUNNING MOTION MAGIC HOME: ");
		System.out.print(Robot.elevator.master.getSelectedSensorPosition(0) - RobotMap.ELEVATOR_HOME_POSITION);
		System.out.println(" (" + Robot.elevator.master.getActiveTrajectoryVelocity() + ")");
	}
	@Override
	protected void end()
	{
		System.out.println("ELEVATOR HOMED!");
		Robot.elevator.position = ElevatorPosition.HOME;
		Robot.elevator.master.clearMotionProfileTrajectories();
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		currentError = Math.abs(Robot.elevator.master.getSelectedSensorPosition(0) - RobotMap.ELEVATOR_HOME_POSITION);
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
