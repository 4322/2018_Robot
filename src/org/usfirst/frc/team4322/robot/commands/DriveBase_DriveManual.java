package org.usfirst.frc.team4322.robot.commands;

import org.usfirst.frc.team4322.robot.OI;
import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class DriveBase_DriveManual extends Command
{

    public DriveBase_DriveManual()
    {
        requires(Robot.driveBase);
    }
    @Override
    protected void initialize()
    {	
    	Robot.driveBase.leftMaster.config_kF(0, 0.76, 10);
    	Robot.driveBase.leftMaster.config_kP(0, 1.0, 10);
    	Robot.driveBase.leftMaster.config_kI(0, 0, 10);
    	Robot.driveBase.leftMaster.config_kD(0, 20.0, 10);
    	
    	Robot.driveBase.rightMaster.config_kF(0, 0.76, 10);
    	Robot.driveBase.rightMaster.config_kP(0, 1.0, 10);
    	Robot.driveBase.rightMaster.config_kI(0, 0, 10);
    	Robot.driveBase.rightMaster.config_kD(0, 20.0, 10);
    	
    	Robot.driveBase.rightMaster.set(ControlMode.Velocity, 0);
    	Robot.driveBase.leftMaster.set(ControlMode.Velocity, 0);
    }
    @Override
    protected void execute()
    {
        // TODO Auto-generated method stub
        Double power= OI.pilot.leftStick.getY();
        Double turn = OI.pilot.rightStick.getX();
        
        
        
        double vLeft = 0;
        double vRight = 0;
        
        double vAngular = (power == 0) ? 
        		RobotMap.DRIVEBASE_TURN_SENSITIVITY * RobotMap.DRIVEBASE_MAX_SPEED * 2 / Math.PI * Math.sin(Math.tan(turn)) * Math.cosh(Math.pow(turn, 5))
        		:
        		RobotMap.DRIVEBASE_TURN_SENSITIVITY * Math.abs(power) * RobotMap.DRIVEBASE_MAX_SPEED * 2 / Math.PI * Math.sin(Math.tan(turn)) * Math.cosh(Math.pow(turn, 5)); //spooky ramping
        
        if (power != 0)
        {
        	vLeft = RobotMap.DRIVEBASE_MAX_SPEED * 2 / Math.PI * Math.sin(Math.tan(power)) * Math.cosh(Math.pow(power, 5)); //spooky ramping
        	vRight = RobotMap.DRIVEBASE_MAX_SPEED * 2 / Math.PI * Math.sin(Math.tan(power)) * Math.cosh(Math.pow(power, 5)); 
        }
        vLeft += vAngular;
        vRight -= vAngular;
        
        Robot.driveBase.rightMaster.set(ControlMode.Velocity, vRight);
        Robot.driveBase.leftMaster.set(ControlMode.Velocity, vLeft);
        
    }

    @Override
    protected boolean isFinished()
    {
        // TODO Auto-generated method stub
        return false;
    }
    
}
