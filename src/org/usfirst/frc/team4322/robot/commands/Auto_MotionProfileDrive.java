package org.usfirst.frc.team4322.robot.commands;


import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.motion.MotionProfile;
import org.usfirst.frc.team4322.robot.motion.MotionProfileController;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Auto_MotionProfileDrive extends Command
{
	
	MotionProfileController mpControllerLeft;
	MotionProfileController mpControllerRight;
    public Auto_MotionProfileDrive(double[][] leftProfile, double[][] rightProfile)
    {
    	
        requires(Robot.driveBase);
        mpControllerLeft = new MotionProfileController(Robot.driveBase.leftMaster, leftProfile);
        mpControllerRight = new MotionProfileController(Robot.driveBase.rightMaster, rightProfile);
        System.out.println("Motion Profile Controller Assigned");
    }
    @Override
    protected void initialize()
    {
    	Robot.driveBase.resetEncoder();
    	//Right-side controllers
    	Robot.driveBase.rightMaster.config_kF(0, 0.76, 10);
    	Robot.driveBase.rightMaster.config_kP(0, 1.0, 10);
    	Robot.driveBase.rightMaster.config_kI(0, 0, 10);
    	Robot.driveBase.rightMaster.config_kD(0, 20.0, 10);
    	
		Robot.driveBase.rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);
		
		Robot.driveBase.rightMaster.configNominalOutputForward(0, 10);
		Robot.driveBase.rightMaster.configNominalOutputReverse(0, 10);
		Robot.driveBase.rightMaster.configPeakOutputForward(1, 10);
		Robot.driveBase.rightMaster.configPeakOutputReverse(-1, 10);
		
		//Left-side controller
    	Robot.driveBase.leftMaster.config_kF(0, .76, 10);
    	Robot.driveBase.leftMaster.config_kP(0, 1.0, 10);
    	Robot.driveBase.leftMaster.config_kI(0, 0, 10);
    	Robot.driveBase.leftMaster.config_kD(0, 20.0, 10);
    	
		Robot.driveBase.leftMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);
		
		Robot.driveBase.leftMaster.configNominalOutputForward(0, 10);
		Robot.driveBase.leftMaster.configNominalOutputReverse(0, 10);
		Robot.driveBase.leftMaster.configPeakOutputForward(1, 10);
		Robot.driveBase.leftMaster.configPeakOutputReverse(-1, 10);
    	
		Robot.driveBase.rightMaster.set(ControlMode.MotionProfile, mpControllerRight.getSetValue().value);
    	Robot.driveBase.leftMaster.set(ControlMode.MotionProfile, mpControllerLeft.getSetValue().value);
    	
    	System.out.println("Motion Profile Started!");
    	mpControllerRight.startMotionProfile();
    	mpControllerLeft.startMotionProfile();
    }
    @Override
    protected void execute()
    {
    	mpControllerRight.control();
    	mpControllerLeft.control();
    	
    	Robot.driveBase.rightMaster.set(ControlMode.MotionProfile, mpControllerRight.getSetValue().value);
    	Robot.driveBase.leftMaster.set(ControlMode.MotionProfile, mpControllerLeft.getSetValue().value);
    }
    @Override
    protected void end()
    {
    	mpControllerLeft.reset();
    	mpControllerRight.reset();
    	Robot.driveBase.leftMaster.set(ControlMode.PercentOutput, 0);
    	Robot.driveBase.rightMaster.set(ControlMode.PercentOutput, 0);
    }
    @Override
    protected boolean isFinished()
    {
        // TODO Auto-generated method stub
        return false;
    }
    
}
