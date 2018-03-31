package org.usfirst.frc.team4322.robot.commands.auto;

import org.usfirst.frc.team4322.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team4322.robot.commands.CollectorActuator_Open;
import org.usfirst.frc.team4322.robot.commands.CollectorDeployer_Deploy;
import org.usfirst.frc.team4322.robot.commands.DriveBase_DriveArc;
import org.usfirst.frc.team4322.robot.commands.Elevator_Switch;

public class AutoGroup_SwitchCenter_Left extends CommandGroup {

	public AutoGroup_SwitchCenter_Left()
	{
		addParallel(new Elevator_Switch());
		addParallel(new CollectorDeployer_Deploy());
		addSequential(new Auto_MotionProfileDrive(Robot.autoSwitchLeft));

//	//	addSequential(new DriveBase_DriveArc(2, -28.06 * 2, Math.abs((118/12) * Math.sin(Math.toRadians(32.06)))));
//	//	addSequential(new DriveBase_DriveArc(2, 28.06 * 2, Math.abs((118/12) * Math.sin(Math.toRadians(32.06)))));

//		addSequential(new DriveBase_DriveArc(2, -20.6 * 2,  .9 * Math.abs((110/12) * Math.sin(Math.toRadians(24.06)))));
//		addSequential(new DriveBase_DriveArc(2, 20.6 * 2, Math.abs((110/12) * Math.sin(Math.toRadians(24.06)))));
		addSequential(new CollectorActuator_Open());
	}
}
