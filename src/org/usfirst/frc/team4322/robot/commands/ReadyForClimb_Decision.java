package org.usfirst.frc.team4322.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.usfirst.frc.team4322.robot.Robot;


public class ReadyForClimb_Decision extends InstantCommand
{
	@Override
	public synchronized void start()
	{
		if(Robot.isReadyForClimb)
		{
			new Group_ReadyForClimbStupid().start();
		}
		else
		{
			new Group_ReadyForClimbStupider().start();
			Robot.isReadyForClimb = true;
		}
	}
}
