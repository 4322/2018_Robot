package org.usfirst.frc.team4322.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;


public class CollectorDeployer_UnDeploy extends Command
{
	public CollectorDeployer_UnDeploy()
	{
		requires(Robot.collectorDeployer);
	}
	@Override
	protected void initialize()
	{
		Robot.collectorDeployer.resetEncoder();
	}
	/**
	 * The execute method is called repeatedly when this Command is
	 * scheduled to run until this Command either finishes or is canceled.
	 */
	@Override
	protected void execute()
	{
		if (Robot.collectorDeployer.getEncoder() < RobotMap.COLLECTOR_DEPLOYER_SETPOINT)
		{
			Robot.collectorDeployer.set(-1);
		}
		else if ((Robot.collectorDeployer.getEncoder() >= RobotMap.COLLECTOR_DEPLOYER_SETPOINT) ||
			Robot.collectorDeployer.isUp())
		{
			Robot.collectorDeployer.set(0);
		}
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
		// TODO: Make this return true when this Command no longer needs to run execute()
		return Robot.collectorDeployer.isDone() || Robot.collectorDeployer.isUp();
	}
	@Override
	protected void end()
	{
		Robot.collectorDeployer.resetEncoder();
		Robot.collectorDeployer.setUp();
	}
}
