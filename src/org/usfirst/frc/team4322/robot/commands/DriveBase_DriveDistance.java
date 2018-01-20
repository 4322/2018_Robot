package org.usfirst.frc.team4322.robot.commands;


import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DriveBase_DriveDistance extends Command
{
	double rotations;
	double ticks;

	int cruiseVel = 1467;
	int accel = 684;
	
    public DriveBase_DriveDistance(double inches)
    {
        requires(Robot.driveBase);
        rotations = inches / (4 * Math.PI);
        ticks = rotations * RobotMap.DRIVEBASE_ENCODER_TICKS_PER_ROTATION;
    }
    @Override
    protected void initialize()
    {
    	//Right-side controllers
    	Robot.driveBase.rightMaster.configMotionCruiseVelocity(cruiseVel, 10);
    	Robot.driveBase.rightMaster.configMotionAcceleration(accel, 10);
    	
    	Robot.driveBase.rightMaster.config_kF(0, 0.76, 10);
    	Robot.driveBase.rightMaster.config_kP(0, 1.0, 10);
    	Robot.driveBase.rightMaster.config_kI(0, 0, 10);
    	Robot.driveBase.rightMaster.config_kD(0, 20.0, 10);
    	
    	Robot.driveBase.rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 10);
		Robot.driveBase.rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);
		
		Robot.driveBase.rightMaster.configNominalOutputForward(0, 10);
		Robot.driveBase.rightMaster.configNominalOutputReverse(0, 10);
		Robot.driveBase.rightMaster.configPeakOutputForward(1, 10);
		Robot.driveBase.rightMaster.configPeakOutputReverse(-1, 10);
		
		//Left-side controller
		Robot.driveBase.leftMaster.configMotionCruiseVelocity(cruiseVel, 10);
    	Robot.driveBase.leftMaster.configMotionAcceleration(accel, 10);
    	
    	Robot.driveBase.leftMaster.config_kF(0, .76, 10);
    	Robot.driveBase.leftMaster.config_kP(0, 1.0, 10);
    	Robot.driveBase.leftMaster.config_kI(0, 0, 10);
    	Robot.driveBase.leftMaster.config_kD(0, 20.0, 10);
    	
    	Robot.driveBase.leftMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 10);
		Robot.driveBase.leftMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);
		
		Robot.driveBase.leftMaster.configNominalOutputForward(0, 10);
		Robot.driveBase.leftMaster.configNominalOutputReverse(0, 10);
		Robot.driveBase.leftMaster.configPeakOutputForward(1, 10);
		Robot.driveBase.leftMaster.configPeakOutputReverse(-1, 10);
		
    	Robot.driveBase.resetEncoder();

    	Robot.driveBase.rightMaster.set(ControlMode.MotionMagic, ticks);
    	Robot.driveBase.leftMaster.set(ControlMode.MotionMagic, ticks);
    }
    @Override
    protected void execute()
    {
        // TODO Auto-generated method stub
    	Robot.driveBase.rightMaster.set(ControlMode.MotionMagic, ticks);
    	Robot.driveBase.leftMaster.set(ControlMode.MotionMagic, ticks);
    }
    @Override
    protected boolean isFinished()
    {
        // TODO Auto-generated method stub
        return Robot.driveBase.leftMaster.getActiveTrajectoryVelocity() == 0 ;
    }
    
}
