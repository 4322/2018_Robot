package org.usfirst.frc.team4322.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class Elevator_Home extends Command {

	private int ticks;
	
	public Elevator_Home()
	{
		ticks = Robot.elevator.home;
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
			case SCALE:
				ticks = -RobotMap.ELEVATOR_SCALE_POSITION;
				Robot.elevator.master.configMotionCruiseVelocity(-RobotMap.ELEVATOR_MAX_SPEED, 10);
				Robot.elevator.master.configMotionAcceleration(-RobotMap.ELEVATOR_MAX_ACCEL, 10);
				Robot.elevator.master.set(ControlMode.MotionMagic, ticks);
				break;
			case HOME:
				ticks = 0;
				Robot.elevator.master.configMotionCruiseVelocity(RobotMap.ELEVATOR_MAX_SPEED, 10);
				Robot.elevator.master.configMotionAcceleration(RobotMap.ELEVATOR_MAX_ACCEL, 10);
				break;
			case SWITCH:
				ticks = -RobotMap.ELEVATOR_SWITCH_POSITION;
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
		Robot.elevator.master.set(ControlMode.PercentOutput, 0);
		Robot.elevator.master.clearMotionProfileTrajectories();
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return ((Math.abs(Robot.elevator.master.getActiveTrajectoryPosition() - RobotMap.ELEVATOR_HOME_POSITION) > 1) 
				&& 
				(Robot.elevator.master.getActiveTrajectoryVelocity() == 0)) || Robot.elevator.limitHome.get();
	}

}
