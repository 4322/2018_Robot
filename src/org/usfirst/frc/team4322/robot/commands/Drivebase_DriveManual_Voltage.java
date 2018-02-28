package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.OI;
import org.usfirst.frc.team4322.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class Drivebase_DriveManual_Voltage extends Command {

	public Drivebase_DriveManual_Voltage()
	{
		requires(Robot.driveBase);
	}
	@Override
	protected void execute()
	{
		Robot.driveBase.leftMaster.set(ControlMode.PercentOutput, OI.pilot.leftStick.getY());
		Robot.driveBase.rightMaster.set(ControlMode.PercentOutput, OI.pilot.leftStick.getY());
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
