package org.usfirst.frc.team4322.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4322.robot.OI;
import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4322.robot.subsystems.Elevator;

public class DriveBase_DriveManual extends Command
{

    public DriveBase_DriveManual()
    {
        requires(Robot.driveBase);
    }
    double oldTurn = 0;
    double negInertiaAccum = 0;
    @Override
    protected void initialize()
    {	
    	Robot.driveBase.rightMaster.set(ControlMode.Velocity, 0);
    	Robot.driveBase.leftMaster.set(ControlMode.Velocity, 0);
    }
    @Override
    protected void execute()
    {

//        Robot.driveBase.leftMaster.config_kF(0, RobotMap.DRIVEBASE_KF, 10);
//        Robot.driveBase.leftMaster.config_kP(0, SmartDashboard.getNumber("P: ", 1), 10);
//        Robot.driveBase.leftMaster.config_kI(0, SmartDashboard.getNumber("I: ", 1), 10);
//        Robot.driveBase.leftMaster.config_kD(0, SmartDashboard.getNumber("D: ", 1), 10);
//
//        Robot.driveBase.rightMaster.config_kF(0, RobotMap.DRIVEBASE_KF, 10);
//        Robot.driveBase.rightMaster.config_kP(0, SmartDashboard.getNumber("P: ", 1), 10);
//        Robot.driveBase.rightMaster.config_kI(0, SmartDashboard.getNumber("I: ", 1), 10);
//        Robot.driveBase.rightMaster.config_kD(0, SmartDashboard.getNumber("D: ", 1), 10);
        double power= OI.pilot.leftStick.getY();
        double turn = OI.pilot.rightStick.getX();
        double negInertia = turn - oldTurn;
        oldTurn = turn;

//        Robot.driveBase.drive(power, turn);

        double negInertiaScalar = 5;

        double negInertiaPower = negInertia * negInertiaScalar;
        negInertiaAccum += negInertiaPower;

        turn += negInertiaAccum;

        if (negInertiaAccum > 1)
        {
            negInertiaAccum -= 1;
        }
        else if (negInertiaAccum < -1)
        {
            negInertiaAccum += 1;
        }
        else
        {
            negInertiaAccum = 0;
        }

        SmartDashboard.putNumber("Turn Stick: ", turn);
        
        double vLeft = 0;
        double vRight = 0;
        
        double vAngular;
        if (power == 0 || OI.pilot.lb.get())
        {
        	//Quick Turning
            vAngular = RobotMap.DRIVEBASE_TURN_SENSITIVITY * RobotMap.DRIVEBASE_MAX_SPEED *
            		RobotMap.cubicRamping(turn);

        }
        else
        {
        	//Normal Turning
        	vAngular = RobotMap.DRIVEBASE_TURN_SENSITIVITY * Math.abs(power) * RobotMap.DRIVEBASE_MAX_SPEED * 
        			RobotMap.cubicRamping(turn);
        }
        SmartDashboard.putNumber("vAngular: ", vAngular);
        if (power != 0)
        {
        	vLeft = RobotMap.DRIVEBASE_MAX_SPEED * RobotMap.spookyRamping(power)
                    * (Robot.elevator.position == Elevator.ElevatorPosition.SCALE ? .5 : 1)
            ;
        	vRight = RobotMap.DRIVEBASE_MAX_SPEED * RobotMap.spookyRamping(power)
                     * (Robot.elevator.position == Elevator.ElevatorPosition.SCALE ? .5 : 1)
            ;

        }
        vLeft += vAngular;
        vRight -= vAngular;

        SmartDashboard.putNumber("vLeft: ", vLeft);
        SmartDashboard.putNumber("vRight: ", vRight);

        //Anti tip code
//        double roll = Robot.driveBase.getPitch();
//        vLeft -= RobotMap.DRIVEBASE_ANTI_TIP_CONSTANT * (roll - Robot.driveBase.basePitch);
//        vRight -= RobotMap.DRIVEBASE_ANTI_TIP_CONSTANT * (roll - Robot.driveBase.basePitch);
        
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
