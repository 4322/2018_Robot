package org.usfirst.frc.team4322.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public class Command_Delay extends Command {

	int delay;
	long target;
	public Command_Delay(int ms)
	{
		delay = ms;
	}
	@Override
	public synchronized void start()
	{
		super.start();
		target = System.currentTimeMillis() + delay;
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return System.currentTimeMillis() >= target;
	}

}
