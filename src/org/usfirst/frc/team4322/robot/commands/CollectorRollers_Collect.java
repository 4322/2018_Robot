package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class CollectorRollers_Collect extends Command {

	public CollectorRollers_Collect()
	{
		requires(Robot.collectorRollers);
	}
	@Override
	protected void execute()
	{
//		Double powLeft = OI.operator.leftStick.getY();
//		Double powRight = OI.operator.rightStick.getY();
//		Robot.collectorRollers.collectorLeft.set(ControlMode.PercentOutput, powLeft);
//		Robot.collectorRollers.collectorRight.set(ControlMode.PercentOutput, powRight);
		Robot.collectorRollers.collectorLeft.set(ControlMode.PercentOutput, -1);
		Robot.collectorRollers.collectorRight.set(ControlMode.PercentOutput, -1);
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

}
