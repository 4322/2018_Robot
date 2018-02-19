package org.usfirst.frc.team4322.robot.commands;


import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.motion.MotionProfileController;
import org.usfirst.frc.team4322.robot.motion.MotionProfileCurve;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;

import edu.wpi.first.wpilibj.command.Command;

public class Auto_MotionProfileDrive extends Command
{
	MotionProfileController mpControllerLeft;
	MotionProfileController mpControllerRight;
    public Auto_MotionProfileDrive(MotionProfileCurve curve)
    {
        requires(Robot.driveBase);
        mpControllerLeft = new MotionProfileController(Robot.driveBase.leftMaster, curve.generatedProfileLeft);
        mpControllerRight = new MotionProfileController(Robot.driveBase.rightMaster, curve.generatedProfileRight);
        System.out.println("Motion Profile Controller Assigned");
    }
    @Override
    protected void initialize()
    {
    	Robot.driveBase.resetEncoder();
    	//Right-side controllers  	
		Robot.driveBase.rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);
		
		Robot.driveBase.rightMaster.configNominalOutputForward(0, 10);
		Robot.driveBase.rightMaster.configNominalOutputReverse(0, 10);
		Robot.driveBase.rightMaster.configPeakOutputForward(1, 10);
		Robot.driveBase.rightMaster.configPeakOutputReverse(-1, 10);
		
		//Left-side controllers
    	
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
		Robot.driveBase.leftMaster.clearMotionProfileTrajectories();
		Robot.driveBase.rightMaster.clearMotionProfileTrajectories();
    }
    @Override
    protected boolean isFinished()
    {
        // TODO Auto-generated method stub
        return ((mpControllerLeft.getSetValue() == SetValueMotionProfile.Hold) && 
        		(mpControllerRight.getSetValue() == SetValueMotionProfile.Hold));
    }
    
}
