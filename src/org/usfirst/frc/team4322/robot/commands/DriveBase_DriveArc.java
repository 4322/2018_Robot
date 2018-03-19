package org.usfirst.frc.team4322.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;

public class DriveBase_DriveArc extends Command
{
	private double velocity;
	private double endAngle;
	private double radius;
	private double arcLength;

	private boolean isFinished = false;

	private static final double angleTolerance = Math.toRadians(4);

	public DriveBase_DriveArc(double velocity, double endAngle, double straightDistance)
	{
		this.velocity = velocity;
		this.endAngle = endAngle;
		double endAngleRadians = Math.toRadians(endAngle);
		radius = (straightDistance * Math.sin((Math.PI - endAngleRadians)) / 2) / Math.sin(endAngleRadians); //radius of the circle
		arcLength = Math.abs(endAngleRadians) * Math.abs(radius);
	}
	@Override
	public void execute()
	{
		double vLeft;
		double vRight;

		double position = Robot.driveBase.getDistInches() / 12;
		double angle = Robot.driveBase.getAngle();

		double positionErr = arcLength - position;
		double angleErr = Math.abs(endAngle - angle);
		if (angleErr < angleTolerance)
		{
			isFinished = true;
		}
		if (endAngle > 0)
		{
			vLeft = turnValue();
			vRight = velocity;
		}
		else if (endAngle < 0)
		{
			vRight = turnValue();
			vLeft = velocity;
		}
		else
		{
			vLeft = velocity;
			vRight = velocity;
		}
		Robot.driveBase.setVelocity(vLeft, vRight);
	}
	private double turnValue()
	{
		return velocity * ((Math.abs(radius) - (RobotMap.DRIVEBASE_WHEELBASE_WIDTH / 2)) / (Math.abs(radius) + (RobotMap.DRIVEBASE_WHEELBASE_WIDTH / 2)));
	}
	@Override
	protected boolean isFinished()
	{
		return isFinished;
	}
}