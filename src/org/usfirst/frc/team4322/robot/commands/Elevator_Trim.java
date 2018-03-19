package org.usfirst.frc.team4322.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4322.robot.OI;
import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;
import org.usfirst.frc.team4322.robot.subsystems.Elevator;

public class Elevator_Trim extends Command
{
	public Elevator_Trim()
	{
		requires(Robot.elevator);
	}
	@Override
	protected void execute()
	{
		double out = OI.operator.rightStick.getY();
		Robot.elevator.set((RobotMap.cubicRamping(out) * RobotMap.ELEVATOR_TRIM_SENSITIVITY) + RobotMap.ELEVATOR_HOLDING_VPERCENT);
	}
	@Override
	protected boolean isFinished()
	{
		return false;
	}
}
