package org.usfirst.frc.team4322.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;
import org.usfirst.frc.team4322.robot.subsystems.Elevator.ElevatorPosition;


public class Elevator_ReadyToClimb extends Command
{
	private int ticks;
	private double currentError = 0;
	private double lastError;

	public Elevator_ReadyToClimb()
	{
		requires(Robot.elevator);
	}


	/**
	 * The initialize method is called just before the first time
	 * this Command is run after being started.
	 */
	@Override
	protected void initialize()
	{
		lastError = Double.MAX_VALUE;

		Robot.elevator.clearProfiles();

		Robot.elevator.useMotionMagicMode();

		switch (Robot.elevator.position)
		{
			case HOME:
				ticks = RobotMap.ELEVATOR_READY_TO_CLIMB_POSITION;
				Robot.elevator.setMotionMagic(RobotMap.ELEVATOR_MAX_SPEED, RobotMap.ELEVATOR_MAX_ACCEL, ticks);
				break;
			case SWITCH:
				ticks = RobotMap.ELEVATOR_READY_TO_CLIMB_POSITION;
				Robot.elevator.setMotionMagic(RobotMap.ELEVATOR_MAX_SPEED, RobotMap.ELEVATOR_MAX_ACCEL, ticks);
				break;
			case SCALE:
				ticks = RobotMap.ELEVATOR_READY_TO_CLIMB_POSITION;
				Robot.elevator.setMotionMagic(-RobotMap.ELEVATOR_MAX_SPEED / 2, -RobotMap.ELEVATOR_MAX_ACCEL, ticks);
				break;
			case READY_TO_CLIMB:
				break;
		}
	}


	/**
	 * The execute method is called repeatedly when this Command is
	 * scheduled to run until this Command either finishes or is canceled.
	 */
	@Override
	protected void execute()
	{

	}


	/**
	 * <p>
	 * Returns whether this command is finished. If it is, then the command will be removed and
	 * {@link #end()} will be called.
	 * </p><p>
	 * It may be useful for a team to reference the {@link #isTimedOut()}
	 * method for time-sensitive commands.
	 * </p><p>
	 * Returning false will result in the command never ending automatically. It may still be
	 * cancelled manually or interrupted by another command. Returning true will result in the
	 * command executing once and finishing immediately. It is recommended to use
	 * {@link edu.wpi.first.wpilibj.command.InstantCommand} (added in 2017) for this.
	 * </p>
	 *
	 * @return whether this command is finished.
	 * @see Command#isTimedOut() isTimedOut()
	 */
	@Override
	protected boolean isFinished()
	{
		currentError = Math.abs(Robot.elevator.getPosition() - RobotMap.ELEVATOR_READY_TO_CLIMB_POSITION);
		if (Robot.elevator.position == ElevatorPosition.READY_TO_CLIMB)
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


	/**
	 * Called once when the command ended peacefully; that is it is called once
	 * after {@link #isFinished()} returns true. This is where you may want to
	 * wrap up loose ends, like shutting off a motor that was being used in the
	 * command.
	 */
	@Override
	protected void end()
	{
		Robot.elevator.clearProfiles();
		Robot.elevator.position = ElevatorPosition.READY_TO_CLIMB;
	}
}
