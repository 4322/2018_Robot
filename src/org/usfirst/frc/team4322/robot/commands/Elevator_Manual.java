package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.OI;
import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class Elevator_Manual extends Command {

	public Elevator_Manual()
	{
		requires(Robot.elevator);
	}
	@Override
	protected void execute()
	{
		Robot.elevator.master.set(ControlMode.PercentOutput, OI.operator.getY()+RobotMap.ELEVATOR_HOLDING_VPERCENT);
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
