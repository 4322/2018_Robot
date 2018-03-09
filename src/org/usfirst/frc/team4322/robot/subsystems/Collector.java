package org.usfirst.frc.team4322.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
//import org.usfirst.frc.team4322.logging.RobotLogger;
import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;
import org.usfirst.frc.team4322.robot.commands.Collector_Collect;
import org.usfirst.frc.team4322.robot.commands.Collector_Stop;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Collector extends Subsystem {

	public WPI_TalonSRX collectorLeft;
	public WPI_TalonSRX collectorRight;

	public DoubleSolenoid collectorDeployer;

	public DoubleSolenoid collectorActuator;

	public Collector()
	{
		collectorLeft = new WPI_TalonSRX(RobotMap.COLLECTOR_MOTORCONTROLLER_LEFT_ADDR);
		collectorRight = new WPI_TalonSRX(RobotMap.COLLECTOR_MOTORCONTROLLER_RIGHT_ADDR);
//		collectorRight.follow(collectorLeft);
		collectorRight.setInverted(true);

//		collectorActuator = new DoubleSolenoid(RobotMap.PNEUMATIC_COLLECTOR_ACTUATOR_SOLENOID_ID_0, RobotMap.PNEUMATIC_COLLECTOR_ACTUATOR_SOLENOID_ID_1);
//		collectorDeployer = new DoubleSolenoid(RobotMap.PNEUMATIC_COLLECTOR_DEPLOYER_SOLENOID_ID_0, RobotMap.PNEUMATIC_COLLECTOR_DEPLOYER_SOLENOID_ID_1);
		
		collectorLeft.set(ControlMode.PercentOutput, 0);
		collectorRight.set(ControlMode.PercentOutput, 0);
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		setDefaultCommand(new Collector_Stop());
	}
	public void deploy()
	{
		collectorDeployer.set(DoubleSolenoid.Value.kForward);
	}
	public void unDeploy()
	{
		collectorDeployer.set(DoubleSolenoid.Value.kReverse);
	}
	public void open()
	{
		collectorActuator.set(DoubleSolenoid.Value.kReverse);
	}
	public void close()
	{
		collectorActuator.set(DoubleSolenoid.Value.kForward);
	}

}
