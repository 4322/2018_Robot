package org.usfirst.frc.team4322.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class Elevator_Scale_Up extends Command {

	private int ticks;
	
	public Elevator_Scale_Up()
	{
		ticks = RobotMap.ELEVATOR_SCALE_DISTANCE;
		requires(Robot.elevator);
	}
	@Override
	protected void initialize()
	{
		Robot.elevator.master.clearMotionProfileTrajectories();
		Robot.elevator.master.configMotionCruiseVelocity(RobotMap.ELEVATOR_MAX_SPEED, 10);
		Robot.elevator.master.configMotionAcceleration(RobotMap.ELEVATOR_MAX_ACCEL, 10);

		Robot.elevator.master.configNominalOutputForward(0, 10);
		Robot.elevator.master.configNominalOutputReverse(0,10);
		Robot.elevator.master.configPeakOutputForward(1, 10);
		Robot.elevator.master.configPeakOutputReverse(-1, 10);

		Robot.elevator.master.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 10);
		Robot.elevator.master.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);

		Robot.elevator.master.set(ControlMode.MotionMagic, ticks);
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
		return (Math.abs(Robot.elevator.master.getActiveTrajectoryPosition() - RobotMap.ELEVATOR_SCALE_DISTANCE) > 1) && (Robot.elevator.master.getActiveTrajectoryVelocity() == 0);
	}

}
