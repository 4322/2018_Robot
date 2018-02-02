package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;

import edu.wpi.first.wpilibj.command.Command;

public class DriveBase_Rotate extends Command {

	private static final double ticksPerDegree = 22.2;
	public double ticks;
	public int anglarVel = 978;
	public int angularAccel = 288;
	double degrees;
	
	public DriveBase_Rotate(double degrees)
	{
		requires(Robot.driveBase);
		this.degrees = degrees;
		ticks = -degrees * ticksPerDegree;
	}
	@Override 
	protected void initialize()
	{
		Robot.driveBase.resetNavX();
		
    	Robot.driveBase.leftMaster.clearMotionProfileTrajectories();
    	Robot.driveBase.rightMaster.clearMotionProfileTrajectories();
		
		//Right-side controllers
    	Robot.driveBase.rightMaster.configMotionCruiseVelocity(anglarVel, 10);
    	Robot.driveBase.rightMaster.configMotionAcceleration(angularAccel, 10);
    	
    	Robot.driveBase.rightMaster.config_kF(0, .76, 10);
    	Robot.driveBase.rightMaster.config_kP(0, 2.0, 10);
    	Robot.driveBase.rightMaster.config_kI(0, 0.0001, 10);
    	Robot.driveBase.rightMaster.config_kD(0, 20.0, 10);
    	
    	Robot.driveBase.rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 10);
		Robot.driveBase.rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);
		
		Robot.driveBase.rightMaster.configNominalOutputForward(0, 10);
		Robot.driveBase.rightMaster.configNominalOutputReverse(0, 10);
		Robot.driveBase.rightMaster.configPeakOutputForward(1, 10);
		Robot.driveBase.rightMaster.configPeakOutputReverse(-1, 10);
		
		//Left-side controller
		Robot.driveBase.leftMaster.configMotionCruiseVelocity(anglarVel, 10);
    	Robot.driveBase.leftMaster.configMotionAcceleration(angularAccel, 10);
    	
    	Robot.driveBase.leftMaster.config_kF(0, .76, 10);
    	Robot.driveBase.leftMaster.config_kP(0, 2.0, 10);
    	Robot.driveBase.leftMaster.config_kI(0, 0.0001, 10);
    	Robot.driveBase.leftMaster.config_kD(0, 20.0, 10);
    	
    	Robot.driveBase.leftMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 10);
		Robot.driveBase.leftMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);
		
		Robot.driveBase.leftMaster.configNominalOutputForward(0, 10);
		Robot.driveBase.leftMaster.configNominalOutputReverse(0, 10);
		Robot.driveBase.leftMaster.configPeakOutputForward(1, 10);
		Robot.driveBase.leftMaster.configPeakOutputReverse(-1, 10);
		
    	Robot.driveBase.resetEncoder();

    
    	Robot.driveBase.rightMaster.set(ControlMode.MotionMagic, ticks);
    	Robot.driveBase.leftMaster.set(ControlMode.MotionMagic, -ticks);
	}
	@Override
	protected void execute()
	{
		Robot.driveBase.rightMaster.set(ControlMode.MotionMagic, ticks);
    	Robot.driveBase.leftMaster.set(ControlMode.MotionMagic, -ticks);
	}
	@Override 
    protected void end()
    {
    	Robot.driveBase.leftMaster.set(ControlMode.PercentOutput, 0);
    	Robot.driveBase.rightMaster.set(ControlMode.PercentOutput, 0);
    	Robot.driveBase.leftMaster.clearMotionProfileTrajectories();
    	Robot.driveBase.rightMaster.clearMotionProfileTrajectories();
    }
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return (Robot.driveBase.leftMaster.getActiveTrajectoryVelocity() == 0) && (Robot.driveBase.rightMaster.getActiveTrajectoryVelocity() == 0);
	}

}
