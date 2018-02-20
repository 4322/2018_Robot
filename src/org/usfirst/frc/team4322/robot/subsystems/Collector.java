package org.usfirst.frc.team4322.robot.subsystems;

import org.usfirst.frc.team4322.robot.RobotMap;
import org.usfirst.frc.team4322.robot.commands.Collector_Collect;
import org.usfirst.frc.team4322.robot.commands.Collector_Stop;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Collector extends Subsystem {

	public WPI_TalonSRX collectorLeft;
	public WPI_TalonSRX collectorRight;
	public Collector()
	{
		collectorLeft = new WPI_TalonSRX(RobotMap.COLLECTOR_MOTORCONTROLLER_LEFT_ADDR);
		collectorRight = new WPI_TalonSRX(RobotMap.COLLECTOR_MOTORCONTROLLER_RIGHT_ADDR);
//		collectorRight.follow(collectorLeft);
		collectorRight.setInverted(true);
		
		collectorLeft.set(ControlMode.PercentOutput, 0);
		collectorRight.set(ControlMode.PercentOutput, 0);
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		setDefaultCommand(new Collector_Stop());
	}

}
