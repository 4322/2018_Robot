package org.usfirst.frc.team4322.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import org.usfirst.frc.team4322.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem {

	public WPI_TalonSRX master;
	public WPI_TalonSRX slave;
	
	public Elevator()
	{
		master = new WPI_TalonSRX(RobotMap.ELEVATOR_MOTORCONTROLLER_MASTER_ADDR);
		master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		slave = new WPI_TalonSRX(RobotMap.ELEVATOR_MOTORCONTROLLER_SLAVE_ADDR);
		slave.follow(master);
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

}
