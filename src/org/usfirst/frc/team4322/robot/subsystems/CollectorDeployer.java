package org.usfirst.frc.team4322.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team4322.robot.RobotMap;
import org.usfirst.frc.team4322.robot.commands.CollectorDeployer_Stop;

import java.util.stream.Collector;

import static org.usfirst.frc.team4322.robot.subsystems.CollectorDeployer.CollectorPosition.DOWN;
import static org.usfirst.frc.team4322.robot.subsystems.CollectorDeployer.CollectorPosition.UP;

//import org.usfirst.frc.team4322.logging.RobotLogger;

public class CollectorDeployer extends Subsystem
{

	private Counter counter;
	private AnalogTrigger trigger;
	private WPI_TalonSRX collectorDeployer;
	private DigitalInput limit;

	public enum CollectorPosition
	{
		UP,
		DOWN
	}

	private CollectorPosition position = UP;

	public CollectorDeployer()
	{
		collectorDeployer = new WPI_TalonSRX(RobotMap.COLLECTOR_DEPLOYER_MOTORCONTROLLER_ADDR);
		trigger = new AnalogTrigger(RobotMap.COLLECTOR_DEPLOYER_ENC_ANALOG_PORT);
		counter = new Counter(trigger);
		limit = new DigitalInput(4);
	}

	@Override
	protected void initDefaultCommand()
	{
		// TODO Auto-generated method stub
		setDefaultCommand(new CollectorDeployer_Stop());
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

	public boolean isLimit()
	{
		return !limit.get();
	}

	public void setUp()
	{
		position = UP;
	}

	public void setDown()
	{
		position = DOWN;
	}
	public boolean isUp()
	{
		return position == UP;
	}
	public boolean isDown()
	{
		return position == DOWN;
	}
}
