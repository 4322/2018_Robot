package org.usfirst.frc.team4322.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team4322.robot.RobotMap;
import org.usfirst.frc.team4322.robot.commands.CollectorDeployer_Deploy;

//import org.usfirst.frc.team4322.logging.RobotLogger;

public class CollectorDeployer extends Subsystem {

	private Counter counter;
	private AnalogTrigger trigger;
	private TalonSRX collectorDeployer;


	public CollectorDeployer()
	{
		collectorDeployer = new TalonSRX(RobotMap.COLLECTOR_DEPLOYER_MOTORCONTROLLER_ADDR);
		trigger = new AnalogTrigger(RobotMap.COLLECTOR_DEPLOYER_ENC_ANALOG_PORT);
		counter = new Counter(trigger);
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		setDefaultCommand(new CollectorDeployer_Deploy());
	}

	public void set(double val)
	{
		collectorDeployer.set(ControlMode.PercentOutput, val);
	}
	public int getEncoder()
	{
		return counter.get();
	}
	public void resetEncoder()
	{
		counter.reset();
	}
	public boolean isDone()
	{
		return (counter.get() == RobotMap.COLLECTOR_DEPLOYER_SETPOINT);
	}
}
