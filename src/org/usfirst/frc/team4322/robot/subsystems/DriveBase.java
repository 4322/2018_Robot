package org.usfirst.frc.team4322.robot.subsystems;

import org.usfirst.frc.team4322.robot.Robot;
import org.usfirst.frc.team4322.robot.RobotMap;
import org.usfirst.frc.team4322.robot.commands.DriveBase_DriveManual;
import org.usfirst.frc.team4322.robot.commands.Drivebase_DriveManual_Voltage;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.*;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveBase extends Subsystem {

	public WPI_TalonSRX leftMaster, leftSlave, rightMaster, rightSlave;
	private AHRS navx;
	private static final double ticksToDist = 6 * Math.PI / 1024;
	double offset = 0.0;
	double offsetNavX = 0;
	public DifferentialDrive drive;
	public double basePitch;
	
	public DriveBase() {
		try {
			
			System.out.println("[d] ********* Constructing DriveBase() *********");
			System.out.println("[d] DriveBase() creating leftMaster...");
			leftMaster = new WPI_TalonSRX(RobotMap.DRIVEBASE_MOTORCONTROLLER_LEFT_MASTER_ADDR);
			leftMaster.configClosedloopRamp(RobotMap.DRIVEBASE_TALON_RAMP_RATE, 1000);
			leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
			leftMaster.setSensorPhase(true);
		
			leftMaster.config_kF(0, RobotMap.DRIVEBASE_KF, 10);
	    	leftMaster.config_kP(0, RobotMap.DRIVEBASE_KP, 10);
	    	leftMaster.config_kI(0, RobotMap.DRIVEBASE_KI, 10);
	    	leftMaster.config_kD(0, RobotMap.DRIVEBASE_KD, 10);

			System.out.println("[d] DriveBase() creating rightMaster...");
			rightMaster = new WPI_TalonSRX(RobotMap.DRIVEBASE_MOTORCONTROLLER_RIGHT_MASTER_ADDR);
			rightMaster.configClosedloopRamp(RobotMap.DRIVEBASE_TALON_RAMP_RATE, 1000);
			rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
			rightMaster.setSensorPhase(true);
			rightMaster.setInverted(true);

			rightMaster.config_kF(0, RobotMap.DRIVEBASE_KF, 10);
	    	rightMaster.config_kP(0, RobotMap.DRIVEBASE_KP, 10);
	    	rightMaster.config_kI(0, RobotMap.DRIVEBASE_KI, 10);
	    	rightMaster.config_kD(0, RobotMap.DRIVEBASE_KD, 10);
			
			System.out.println("[d] DriveBase() creating leftSlave...");
			leftSlave = new WPI_TalonSRX(RobotMap.DRIVEBASE_MOTORCONTROLLER_LEFT_SLAVE_ADDR);
			System.out.println("[d] DriveBase() setting leftSlave to follow leftMaster...");
			leftSlave.follow(leftMaster);

			
			System.out.println("[d] DriveBase() creating rightSlave...");
			rightSlave = new WPI_TalonSRX(RobotMap.DRIVEBASE_MOTORCONTROLLER_RIGHT_SLAVE_ADDR);
			System.out.println("[d] DriveBase() setting rightSlave to follow rightMaster...");
			rightSlave.follow(rightMaster);
			rightSlave.setInverted(true);

			System.out.println("[d] DriveBase() creating Navx...");
//			navx = new AHRS(Port.kMXP);
//			navx.reset();
//			basePitch = navx.getPitch();
			
//			drive = new DifferentialDrive(leftMaster, rightMaster);

		} catch (Exception ex) {
		}
	}

	@Override
	protected void initDefaultCommand()
	{
		setDefaultCommand(new DriveBase_DriveManual());
	}

	public double getDist()
	{
		return ((rightMaster.getSelectedSensorPosition(0) + leftMaster.getSelectedSensorPosition(0)) / 2);
	}
	public double getDistInches()
	{
		return ((rightMaster.getSelectedSensorPosition(0) + leftMaster.getSelectedSensorPosition(0)) / 2) * ticksToDist;
	}
	public double getDistRight()
	{
		return rightMaster.getSelectedSensorPosition(0);
	}
	public double getDistLeft()
	{
		return leftMaster.getSelectedSensorPosition(0);
	}
	public double getVelocity()
	{
		return (leftMaster.getSelectedSensorVelocity(0) + rightMaster.getSelectedSensorVelocity(0)) / 2;
	}
	public double getAngularVelocity()
	{
		return (leftMaster.getSelectedSensorPosition(0) + rightMaster.getSelectedSensorPosition(0)) / 2;
	}
//	public double getRoll()
//	{
//		return navx.getRoll();
//	}
//	public double getPitch()
//	{
//		return navx.getPitch();
//	}
//	public double getAngle()
//	{
//		return (navx.getYaw() - offsetNavX);
//	}
//
//	public void resetNavX()
//	{
//		offsetNavX = navx.getYaw();
////	}
	public double getVoltageLeft()
	{
		return leftMaster.getMotorOutputVoltage();
	}
	public double getVoltageRight()
	{
		return rightMaster.getMotorOutputVoltage();
	}

//	public void autoDrive(double pow, double rot)
//	{
//		drive.arcadeDrive(pow, rot);
//	}
//
	public void drive(double pow, double rot) {
		System.out.println("[d] Drivebase() calling drive.arcadeDrive(" + pow + ", " + rot + ", true);");
		drive.arcadeDrive(pow, rot, true); 
	}

	public void resetEncoder() {
		leftMaster.setSelectedSensorPosition(0, 0, 10);
		leftMaster.getSensorCollection().setQuadraturePosition(0, 10);
		rightMaster.setSelectedSensorPosition(0, 0, 10);
		rightMaster.getSensorCollection().setQuadraturePosition(0, 10);
		offset = (leftMaster.getSensorCollection().getQuadraturePosition() + rightMaster.getSensorCollection().getQuadraturePosition()) / 2;
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
		}
	}
}
