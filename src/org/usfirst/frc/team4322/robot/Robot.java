
package org.usfirst.frc.team4322.robot;

import edu.wpi.first.wpilibj.Compressor;
import org.usfirst.frc.team4322.robot.commands.AutoGroup_DriveSquare;
import org.usfirst.frc.team4322.robot.commands.Auto_MotionProfileDrive;
import org.usfirst.frc.team4322.robot.commands.DriveBase_DriveDistance;
import org.usfirst.frc.team4322.robot.commands.DriveBase_Rotate;
import org.usfirst.frc.team4322.robot.motion.AppendedMotionProfile;
import org.usfirst.frc.team4322.robot.motion.MotionProfileCurve;
import org.usfirst.frc.team4322.robot.subsystems.Collector;
import org.usfirst.frc.team4322.robot.subsystems.DriveBase;
import org.usfirst.frc.team4322.robot.subsystems.Elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.File;
import java.io.IOException;

import static java.lang.Math.toRadians;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot
{
	// Controls Class
	private static OI oi;
	// Auto Chooser Class
	private static SmartAuto smartAuto;
	// Drivebase Subsystem Class
	public static DriveBase driveBase;
	// Collector Subsystem Class
	public static Collector collector;
	// Elevator Subsystem Class
	public static Elevator elevator;
	//MotionProfiles
	public static MotionProfileCurve spline;
	public static MotionProfileCurve straight;
	public static MotionProfileCurve autoSwitchRight;
	public static MotionProfileCurve autoSwitchLeft;
	public static MotionProfileCurve autoScaleLeftLeft;
	public static MotionProfileCurve autoScaleLeftRight;
	public static MotionProfileCurve autoScaleRightRight;
	public static MotionProfileCurve autoScaleRightLeft;

	//Autonomous
	private Command autoCommand;

//	//Pneumatics
//	public Compressor compressor;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit()
	{
		//Autos
		smartAuto = new SmartAuto();
		//Start Subsystems (Mind Dependencies!)
		driveBase = new DriveBase();
		collector = new Collector();
		elevator = new Elevator();
		//Start OI
		oi = new OI();
		//Start compressor
		new Compressor().setClosedLoopControl(true);
		//Motion Profiles
		autoSwitchLeft = new MotionProfileCurve(toRadians(-24.396), toRadians(-24.396), 9.33333, 4);
        autoSwitchRight = new MotionProfileCurve(toRadians(24.396), toRadians(24.396), 9.33333, 4);
		autoScaleLeftLeft = new AppendedMotionProfile(
				new MotionProfileCurve[]{
					new MotionProfileCurve(-.13505, -.13505, 185.702 / 12, 4),
					new MotionProfileCurve(toRadians(33), toRadians(180 - 3.4), 84.513 / 12, 3)
				}
		);
				
        autoScaleLeftRight = new AppendedMotionProfile(
				new MotionProfileCurve[]{
					new MotionProfileCurve(0, 0, 159.563 / 12, 3),
					new MotionProfileCurve(toRadians(63.7), toRadians(180 - 26.3), 94.81 / 12, 3),
					new MotionProfileCurve(toRadians(3.1), toRadians(3.1), 75.107 / 12, 1),
					new MotionProfileCurve(-toRadians(66.5), toRadians(23.5), 63.809 / 12, 2)
				}
		);
		autoScaleRightRight = new AppendedMotionProfile(
				new MotionProfileCurve[]{
						new MotionProfileCurve(.13505, .13505, 185.702 / 12, 4),
						new MotionProfileCurve(-toRadians(33), -toRadians(180 - 3.4), 84.513 / 12, 3)
				}
		);
        autoScaleRightLeft = new AppendedMotionProfile(
				new MotionProfileCurve[]{
						new MotionProfileCurve(0, 0, 159.563 / 12, 3),
						new MotionProfileCurve(-toRadians(63.7), -toRadians(180 - 26.3), 94.81 / 12, 3),
						new MotionProfileCurve(-toRadians(3.1), -toRadians(3.1), 75.107 / 12, 1),
						new MotionProfileCurve(toRadians(66.5), -toRadians(23.5), 63.809 / 12, 2)
				}
		);
        
		autoSwitchLeft.setFileName("autoSwitchLeft");
		autoSwitchLeft.readProfileFromCSV();
		autoSwitchRight.setFileName("autoSwitchRight");
		autoSwitchRight.readProfileFromCSV();
		autoScaleLeftLeft.setFileName("autoScaleLeftLeft");
		autoScaleLeftLeft.readProfileFromCSV();
		autoScaleRightRight.setFileName("autoScaleRightRight");
		autoScaleRightRight.readProfileFromCSV();
		autoScaleRightLeft.setFileName("autoScaleRightLeft");
		autoScaleRightLeft.readProfileFromCSV();
		autoScaleLeftRight.setFileName("autoScaleLeftRight");
		autoScaleLeftRight.readProfileFromCSV();
		SmartDashboard.putString("Build DateTime", "2/18/17 5:24PM");
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	public void disabledInit()
	{
		
	}

	public void disabledPeriodic()
	{
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("DriveBase Encoder Value: ", Robot.driveBase.getDist());
		SmartDashboard.putNumber("Left Enc: ", Robot.driveBase.leftMaster.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right Enc: ", Robot.driveBase.rightMaster.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Elevator Enc: ", Robot.elevator.master.getSelectedSensorPosition(0));
//		SmartDashboard.putNumber("NavX Yaw: ", Robot.driveBase.getAngle());
//		SmartDashboard.putNumber("Pitch: ", Robot.driveBase.getPitch());
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 * <p>
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	public void autonomousInit()
	{
		smartAuto.choose();
		autoCommand = smartAuto.getAuto();
		autoCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic()
	{
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("DriveBase Encoder Value: ", Robot.driveBase.getDist());
	}

	public void teleopInit()
	{
//		driveBase.leftMaster.clearMotionProfileTrajectories();
//		driveBase.rightMaster.clearMotionProfileTrajectories();
		elevator.position = Elevator.ElevatorPosition.HOME;
		driveBase.leftMaster.set(ControlMode.PercentOutput, 0);
		driveBase.rightMaster.set(ControlMode.PercentOutput, 0);
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic()
	{
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("Elevator Enc: ", Robot.elevator.master.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Elevator Vel: ", Robot.elevator.master.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("Elevator Voltage: ", Robot.elevator.master.getMotorOutputPercent());
		SmartDashboard.putNumber("Left Velocity", Robot.driveBase.leftMaster.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("Right Velocity: ", Robot.driveBase.rightMaster.getSelectedSensorVelocity(0));

	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic()
	{
		LiveWindow.setEnabled(true);
	}
}
