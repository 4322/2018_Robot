package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class CollectorRollers_Eject extends Command {

	public CollectorRollers_Eject()
	{
		requires(Robot.collectorRollers);
	}
	@Override
	public void execute()
	{
		Robot.collectorRollers.collectorLeft.set(ControlMode.PercentOutput, .5);
		Robot.collectorRollers.collectorRight.set(ControlMode.PercentOutput, .5);
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

}
