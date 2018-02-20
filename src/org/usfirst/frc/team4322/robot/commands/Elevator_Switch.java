package org.usfirst.frc.team4322.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;
import org.usfirst.frc.team4322.robot.subsystems.Elevator.ElevatorPosition;

import edu.wpi.first.wpilibj.command.Command;

public class Elevator_Switch extends Command {

	private int ticks;
	
	public Elevator_Switch()
	{
		requires(Robot.elevator);
	}
	@Override
	protected void initialize()
	{
		Robot.elevator.master.clearMotionProfileTrajectories();
		

		Robot.elevator.master.configNominalOutputForward(0, 10);
		Robot.elevator.master.configNominalOutputReverse(0,10);
		Robot.elevator.master.configPeakOutputForward(1, 10);
		Robot.elevator.master.configPeakOutputReverse(-1, 10);

		Robot.elevator.master.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 10);
		Robot.elevator.master.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);

		switch (Robot.elevator.position)
		{
			case HOME:
				ticks = RobotMap.ELEVATOR_SWITCH_POSITION;
				Robot.elevator.master.configMotionCruiseVelocity(RobotMap.ELEVATOR_MAX_SPEED, 10);
				Robot.elevator.master.configMotionAcceleration(RobotMap.ELEVATOR_MAX_ACCEL, 10);
				Robot.elevator.master.set(ControlMode.MotionMagic, ticks);
				break;
			case SWITCH:
				ticks = 0;
				Robot.elevator.master.configMotionCruiseVelocity(RobotMap.ELEVATOR_MAX_SPEED, 10);
				Robot.elevator.master.configMotionAcceleration(RobotMap.ELEVATOR_MAX_ACCEL, 10);
				break;
			case SCALE:
				ticks = -Robot.elevator.master.getSelectedSensorPosition(0) + RobotMap.ELEVATOR_SWITCH_POSITION;
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
		System.out.println("MOTION MAGIC COMPLETED!");
		Robot.elevator.master.set(ControlMode.PercentOutput, 0);
		Robot.elevator.master.clearMotionProfileTrajectories();
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return (Math.abs(Robot.elevator.master.getActiveTrajectoryPosition() - RobotMap.ELEVATOR_SWITCH_POSITION) < 1) && (Robot.elevator.master.getActiveTrajectoryVelocity() == 0);
	}

}
