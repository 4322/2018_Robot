package org.usfirst.frc.team4322.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class Elevator_Scale extends Command {

	private int ticks;
	
	public Elevator_Scale()
	{
		ticks = RobotMap.ELEVATOR_SCALE_POSITION - Robot.elevator.master.getSelectedSensorPosition(0);
		requires(Robot.elevator);
	}
	@Override
	protected void initialize()
	{
		Robot.elevator.master.clearMotionProfileTrajectories();
		Robot.elevator.master.configMotionCruiseVelocity(RobotMap.ELEVATOR_MAX_SPEED, 10);
		Robot.elevator.master.configMotionAcceleration(RobotMap.ELEVATOR_MAX_ACCEL, 10);

		Robot.elevator.master.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 10);
		Robot.elevator.master.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);

		Robot.elevator.master.configNominalOutputForward(0.03, 10);
		Robot.elevator.master.configNominalOutputReverse(0,10);
		Robot.elevator.master.configPeakOutputForward(.5, 10);
		Robot.elevator.master.configPeakOutputReverse(-.5, 10);

		switch (Robot.elevator.position)
		{
			case HOME:
				ticks = RobotMap.ELEVATOR_SCALE_POSITION;
				Robot.elevator.master.configMotionCruiseVelocity(RobotMap.ELEVATOR_MAX_SPEED, 10);
				Robot.elevator.master.configMotionAcceleration(RobotMap.ELEVATOR_MAX_ACCEL, 10);
				Robot.elevator.master.set(ControlMode.MotionMagic, ticks);
				break;
			case SCALE:
				ticks = 0;
				Robot.elevator.master.configMotionCruiseVelocity(RobotMap.ELEVATOR_MAX_SPEED, 10);
				Robot.elevator.master.configMotionAcceleration(RobotMap.ELEVATOR_MAX_ACCEL, 10);
				break;
			case SWITCH:
				ticks = RobotMap.ELEVATOR_SCALE_POSITION - Robot.elevator.master.getSelectedSensorPosition(0);
				Robot.elevator.master.configMotionCruiseVelocity(-RobotMap.ELEVATOR_MAX_SPEED, 10);
				Robot.elevator.master.configMotionAcceleration(-RobotMap.ELEVATOR_MAX_ACCEL, 10);
				Robot.elevator.master.set(ControlMode.MotionMagic, ticks);
				break;
		}
	}
	@Override
	protected void execute()
	{
		Robot.elevator.master.set(ControlMode.MotionMagic, ticks);
	}
	@Override
	protected void end()
	{
		System.out.println("MOTION MAGIC COMPLETE!");
		Robot.elevator.master.set(ControlMode.PercentOutput, 0);
		Robot.elevator.master.clearMotionProfileTrajectories();
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return ((Math.abs(Robot.elevator.master.getActiveTrajectoryPosition() - Robot.elevator.master.getSelectedSensorPosition(0)) <= 30) && 
				(Robot.elevator.master.getActiveTrajectoryVelocity() == 0));
	}

}
