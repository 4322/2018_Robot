package org.usfirst.frc.team4322.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4322.robot.Robot;

public class CollectorRollers_EjectFast extends Command {

	public CollectorRollers_EjectFast()
	{
		requires(Robot.collectorRollers);
	}
	@Override
	public void execute()
	{
		Robot.collectorRollers.collectorLeft.set(ControlMode.PercentOutput, 1);
		Robot.collectorRollers.collectorRight.set(ControlMode.PercentOutput, 1);
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

}
