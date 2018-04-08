package org.usfirst.frc.team4322.robot.commands;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

	int cruiseVel;
	int accel;

	private double currentError = 0;
	private double lastError = Double.MAX_VALUE;
	
    public DriveBase_DriveDistance(double inches, int velocity, int acceleration)
    {
        requires(Robot.driveBase);
        rotations = inches / (RobotMap.DRIVEBASE_WHEEL_DIAMETER * Math.PI);
        System.out.println("Rotations: " + rotations);
        ticks = rotations * RobotMap.DRIVEBASE_ENCODER_TICKS_PER_ROTATION;
        System.out.println("Ticks: " + ticks);
        SmartDashboard.putNumber("Ticks: ", ticks);
        cruiseVel = (int) Math.copySign(velocity, inches);
        accel = (int) Math.copySign(acceleration, velocity);
    }
    @Override
    protected void initialize()
    {
    	Robot.driveBase.resetEncoder();
    	
    	Robot.driveBase.leftMaster.clearMotionProfileTrajectories();
    	Robot.driveBase.rightMaster.clearMotionProfileTrajectories();
    	//Right-side controllers
		Robot.driveBase.rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 10);
		Robot.driveBase.rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);

    	Robot.driveBase.rightMaster.configMotionCruiseVelocity(cruiseVel, 10);
    	Robot.driveBase.rightMaster.configMotionAcceleration(accel, 10);
		
		Robot.driveBase.rightMaster.configNominalOutputForward(0, 10);
		Robot.driveBase.rightMaster.configNominalOutputReverse(0, 10);
		Robot.driveBase.rightMaster.configPeakOutputForward(1, 10);
		Robot.driveBase.rightMaster.configPeakOutputReverse(-1, 10);
		
		//Left-side controller
		Robot.driveBase.leftMaster.configMotionCruiseVelocity(cruiseVel, 10);
    	Robot.driveBase.leftMaster.configMotionAcceleration(accel, 10);

    	Robot.driveBase.leftMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 10);
		Robot.driveBase.leftMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);
		
		Robot.driveBase.leftMaster.configNominalOutputForward(0, 10);
		Robot.driveBase.leftMaster.configNominalOutputReverse(0, 10);
		Robot.driveBase.leftMaster.configPeakOutputForward(1, 10);
		Robot.driveBase.leftMaster.configPeakOutputReverse(-1, 10);
		

    	Robot.driveBase.rightMaster.set(ControlMode.MotionMagic, ticks);
    	Robot.driveBase.leftMaster.set(ControlMode.MotionMagic, ticks);
    }
    @Override
    protected void execute()
	{

    }
    @Override 
    protected void end()
    {
    	System.out.println("MOTION MAGIC COMPLETE!");
    	Robot.driveBase.leftMaster.set(ControlMode.PercentOutput, 0);
    	Robot.driveBase.rightMaster.set(ControlMode.PercentOutput, 0);
    	Robot.driveBase.leftMaster.clearMotionProfileTrajectories();
    	Robot.driveBase.rightMaster.clearMotionProfileTrajectories();
    }
    @Override
    protected boolean isFinished()
    {
		currentError = Math.abs(Robot.driveBase.getDist() - ticks);

		if (currentError > (lastError + RobotMap.ELEVATOR_TOLERANCE))
		{
			return true;
		}
		else
		{
			lastError = currentError;

		}
		return (currentError <= RobotMap.ELEVATOR_TOLERANCE);
    }
    
}
