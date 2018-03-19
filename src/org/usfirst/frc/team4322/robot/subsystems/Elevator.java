package org.usfirst.frc.team4322.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;

import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;
import org.usfirst.frc.team4322.robot.commands.Elevator_Hold;
import org.usfirst.frc.team4322.robot.commands.Elevator_Manual;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem {
	
	private WPI_TalonSRX master;
	private WPI_TalonSRX slave;
	public DigitalInput limitHome;
	public int home;
	
	public enum ElevatorPosition
	{
		HOME,
		SWITCH,
		SCALE,
		READY_TO_CLIMB,
		LOW_SCALE
	}
	public ElevatorPosition position;
	
	public Elevator()
	{
		master = new WPI_TalonSRX(RobotMap.ELEVATOR_MOTORCONTROLLER_MASTER_ADDR);
		master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);

		master.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 10);
		master.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 10);

		master.setSensorPhase(false);
		master.setSelectedSensorPosition(0, 0, 10);
//		master.setInverted(true);
		
		master.configNominalOutputForward(RobotMap.ELEVATOR_HOLDING_VPERCENT, 10);
		master.configNominalOutputReverse(0, 10);
		master.configPeakOutputForward(1, 10);
		master.configPeakOutputReverse(-1, 10);
		
		slave = new WPI_TalonSRX(RobotMap.ELEVATOR_MOTORCONTROLLER_SLAVE_ADDR);
		slave.follow(master);
//		slave.setInverted(true);

		
		master.config_kF(0, RobotMap.ELEVATOR_KF, 10);
		master.config_kP(0, RobotMap.ELEVATOR_KP, 10);
		master.config_kI(0, RobotMap.ELEVATOR_KI, 10);
		master.config_kD(0, RobotMap.ELEVATOR_KD, 10);
		
//		limitHome = new DigitalInput(RobotMap.ELEVATOR_LIMIT_HOME);
		position = ElevatorPosition.HOME;
	}
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		setDefaultCommand(new Elevator_Hold());
	}
	public double getTrajectoryPosition()
	{
		return master.getActiveTrajectoryPosition();
	}
	public double getTrajectoryVelocity()
	{
		return master.getActiveTrajectoryVelocity();
	}
	public double getPosition()
	{
		return master.getSelectedSensorPosition(0);
	}
	public double getVelocity()
	{
		return master.getSelectedSensorVelocity(0);
	}
	public void setMotionMagic(int velocity, int acceration, int ticks)
	{
		master.configMotionCruiseVelocity(velocity, 10);
		master.configMotionAcceleration(acceration, 10);
		master.set(ControlMode.MotionMagic, ticks);

	}
	public void set(double val)
	{
		master.set(ControlMode.PercentOutput, val);
	}
	public void useMotionMagicMode()
	{
		master.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 10);
		master.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);
	}
	public void clearProfiles()
	{
		master.clearMotionProfileTrajectories();
	}

}
