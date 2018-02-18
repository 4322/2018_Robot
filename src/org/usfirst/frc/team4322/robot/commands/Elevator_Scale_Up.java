package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class Elevator_Scale_Up extends Command {

	private double ticks;
	
	public Elevator_Scale_Up()
	{
		ticks = RobotMap.ELEVATOR_SCALE_DISTANCE;
		requires(Robot)
	}
	@Override
	protected void initialize()
	{
	
	}
	@Override
	protected void execute()
	{
		
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
