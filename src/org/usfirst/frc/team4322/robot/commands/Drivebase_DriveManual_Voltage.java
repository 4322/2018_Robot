package org.usfirst.frc.team4322.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4322.robot.OI;
import org.usfirst.frc.team4322.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4322.robot.RobotMap;
import org.usfirst.frc.team4322.robot.subsystems.Elevator;

public class Drivebase_DriveManual_Voltage extends Command {


	public Drivebase_DriveManual_Voltage()
	{
		requires(Robot.driveBase);
	}
	double oldTurn = 0;
	double negInertiaAccum = 0;
	@Override
	protected void initialize()
	{
		Robot.driveBase.rightMaster.set(ControlMode.PercentOutput, 0);
		Robot.driveBase.leftMaster.set(ControlMode.PercentOutput, 0);
	}
	@Override
	protected void execute()
	{
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
			vAngular = RobotMap.DRIVEBASE_TURN_SENSITIVITY * 1.5 *
					RobotMap.cubicRamping(turn);

		}
		else
		{
			//Normal Turning
			vAngular = RobotMap.DRIVEBASE_TURN_SENSITIVITY * Math.abs(power) *
					RobotMap.cubicRamping(turn);
		}
		SmartDashboard.putNumber("vAngular: ", vAngular);
		if (power != 0)
		{
			vLeft = RobotMap.spookyRamping(power)
					* (Robot.elevator.position == Elevator.ElevatorPosition.SCALE ? .5 : 1)
			;
			vRight = RobotMap.spookyRamping(power)
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

		Robot.driveBase.rightMaster.set(ControlMode.PercentOutput, vRight);
		Robot.driveBase.leftMaster.set(ControlMode.PercentOutput, vLeft);

	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
