package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoGroup_SwitchCenter_Right extends CommandGroup {

	public AutoGroup_SwitchCenter_Right()
	{
		addParallel(new Elevator_Switch());
		addParallel(new CollectorDeployer_Deploy());
//		addSequential(new Auto_MotionProfileDrive(Robot.autoSwitchRight));
		addSequential(new DriveBase_DriveArc(2, 30, 5));
		addSequential(new DriveBase_DriveArc(2, -30, 5));
		addSequential(new CollectorActuator_Open());
	}
}
