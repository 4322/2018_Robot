package org.usfirst.frc.team4322.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team4322.robot.RobotMap;
import org.usfirst.frc.team4322.robot.commands.CollectorActuator_Close;

//import org.usfirst.frc.team4322.logging.RobotLogger;

public class CollectorActuator extends Subsystem {

	public DoubleSolenoid collectorActuator;

	public CollectorActuator()
	{
		collectorActuator = new DoubleSolenoid(RobotMap.PNEUMATIC_COLLECTOR_ACTUATOR_SOLENOID_ID_0, RobotMap.PNEUMATIC_COLLECTOR_ACTUATOR_SOLENOID_ID_1);
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		setDefaultCommand(new CollectorActuator_Close());
	}
	public void open()
	{
		collectorActuator.set(DoubleSolenoid.Value.kForward);
	}
	public void close()
	{
		collectorActuator.set(DoubleSolenoid.Value.kReverse);
	}

}
