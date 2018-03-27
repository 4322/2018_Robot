package org.usfirst.frc.team4322.robot.commands.auto;

import org.usfirst.frc.team4322.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team4322.robot.commands.*;

public class AutoGroup_SwitchCenter_Right extends CommandGroup {

	public AutoGroup_SwitchCenter_Right()
	{
		addParallel(new Elevator_Switch());
		addParallel(new CollectorDeployer_Deploy());
//		addSequential(new Auto_MotionProfileDrive(Robot.autoSwitchRight));
		addSequential(new DriveBase_DriveArc(2, 20.6 * 2,  .9 * Math.abs((110/12) * Math.sin(Math.toRadians(24.06)))));
		addSequential(new DriveBase_DriveArc(2, -20.6 * 2, Math.abs((110/12) * Math.sin(Math.toRadians(24.06)))));
		addSequential(new CollectorActuator_Open());
	}
}
