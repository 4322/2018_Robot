package org.usfirst.frc.team4322.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;
import org.usfirst.frc.team4322.robot.subsystems.Elevator.ElevatorPosition;

import edu.wpi.first.wpilibj.command.Command;

public class Elevator_Switch extends Command
{

	private int ticks;
	private double currentError = 0;
	private double lastError;

	public Elevator_Switch()
	{
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
			case HOME:
				ticks = RobotMap.ELEVATOR_SWITCH_POSITION;
				Robot.elevator.setMotionMagic(RobotMap.ELEVATOR_MAX_SPEED, RobotMap.ELEVATOR_MAX_ACCEL, ticks);
				break;
			case SWITCH:
				ticks = 0;
				break;
			case SCALE:
				ticks = RobotMap.ELEVATOR_SWITCH_POSITION;
				Robot.elevator.setMotionMagic(-RobotMap.ELEVATOR_MAX_SPEED, -RobotMap.ELEVATOR_MAX_ACCEL, ticks);
				break;
			case READY_TO_CLIMB:
				ticks = RobotMap.ELEVATOR_SWITCH_POSITION;
				Robot.elevator.setMotionMagic(-RobotMap.ELEVATOR_MAX_SPEED, -RobotMap.ELEVATOR_MAX_ACCEL, ticks);
				break;
		}
		
	}
	@Override
	protected void execute()
	{
//		Robot.elevator.master.set(ControlMode.MotionMagic, ticks);
//		currentError = Robot.elevator.master.getSelectedSensorPosition(0) - RobotMap.ELEVATOR_SWITCH_POSITION;
		System.out.print("RUNNING MOTION MAGIC SWITCH: ");
		System.out.print(currentError);
		System.out.println(" (" + Robot.elevator.getTrajectoryVelocity() + ")");
	}
	@Override
	protected void end()
	{
		System.out.print("MOTION MAGIC SWITCH COMPLETED AT ");
		System.out.println(Robot.elevator.getPosition() - RobotMap.ELEVATOR_SWITCH_POSITION);
		Robot.elevator.position = ElevatorPosition.SWITCH;
		Robot.elevator.clearProfiles();

		Robot.elevator.useMotionMagicMode();
	}
	@Override
	protected boolean isFinished()
	{
		// TODO Auto-generated method stub
		currentError = Math.abs(Robot.elevator.getPosition() - RobotMap.ELEVATOR_SWITCH_POSITION);
		if (Robot.elevator.position == ElevatorPosition.SWITCH)
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
