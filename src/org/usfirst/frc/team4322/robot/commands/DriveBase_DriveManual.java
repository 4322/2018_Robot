package org.usfirst.frc.team4322.robot.commands;

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
    @Override
    protected void initialize()
    {	
    	Robot.driveBase.rightMaster.set(ControlMode.Velocity, 0);
    	Robot.driveBase.leftMaster.set(ControlMode.Velocity, 0);
    }
    @Override
    protected void execute()
    {
        // TODO Auto-generated method stub
        double power= OI.pilot.leftStick.getY();
        double turn = OI.pilot.rightStick.getX();
        double negInertia = turn - oldTurn;
        oldTurn = turn;

//        Robot.driveBase.drive(power, turn);

        double negInertiaAccum = 0;
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
        
        double vLeft = 0;
        double vRight = 0;
        
        double vAngular;
        if (power == 0 || OI.pilot.rb.get())
        {
        	//Quick Turning
            vAngular = RobotMap.DRIVEBASE_TURN_SENSITIVITY * 1.5 * RobotMap.DRIVEBASE_MAX_SPEED *
            		RobotMap.cubicRamping(turn);

        }
        else
        {
        	//Normal Turning
        	vAngular = RobotMap.DRIVEBASE_TURN_SENSITIVITY * Math.abs(power) * RobotMap.DRIVEBASE_MAX_SPEED * 
        			RobotMap.cubicRamping(turn);
        }
        
        if (power != 0)
        {
        	vLeft = RobotMap.DRIVEBASE_MAX_SPEED * RobotMap.cubicRamping(power)
                    * (Robot.elevator.position == Elevator.ElevatorPosition.SCALE ? .5 : 1)
            ;
        	vRight = RobotMap.DRIVEBASE_MAX_SPEED * RobotMap.cubicRamping(power)
                     * (Robot.elevator.position == Elevator.ElevatorPosition.SCALE ? .5 : 1)
            ;

        }
        vLeft += vAngular;
        vRight -= vAngular;
        
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
