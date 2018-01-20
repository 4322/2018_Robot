package org.usfirst.frc.team4322.robot.commands;


import org.usfirst.frc.team4322.robot.MotionProfileController;
import org.usfirst.frc.team4322.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Auto_MotionProfileDrive extends Command
{
	
//	MotionProfileController mpControllerLeft;
	MotionProfileController mpControllerRight;
    public Auto_MotionProfileDrive()
    {
    	
        requires(Robot.driveBase);
//        mpControllerLeft = new MotionProfileController(Robot.driveBase.leftMaster);
        mpControllerRight = new MotionProfileController(Robot.driveBase.rightMaster);
        System.out.println("Motion Profile Controller Assigned");
    }
    @Override
    protected void initialize()
    {
    	Robot.driveBase.resetEncoder();
    	System.out.println("Encoder Reset");
//    	mpControllerLeft.configure();
    	System.out.println("Motion Profile Controller Configured");
    	mpControllerRight.configure();
//    	mpControllerLeft.startMotionProfile();
//    	System.out.println("Set left to follow right for now");
//    	Robot.driveBase.leftMaster.follow(Robot.driveBase.rightMaster);
    	
    }
    @Override
    protected void execute()
    {
    	mpControllerRight.control();
    	System.out.println("Motion Profile Started!");
    	mpControllerRight.startMotionProfile();
//        mpControllerLeft.control();	
    	Robot.driveBase.rightMaster.set(ControlMode.MotionProfile, mpControllerRight.getSetValue().value);
    }
    @Override
    protected void end()
    {
//    	mpControllerLeft.reset();
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
