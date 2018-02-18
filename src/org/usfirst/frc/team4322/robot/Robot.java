
package org.usfirst.frc.team4322.robot;

import org.usfirst.frc.team4322.robot.commands.AutoGroup_DriveSquare;
import org.usfirst.frc.team4322.robot.commands.Auto_MotionProfileDrive;
import org.usfirst.frc.team4322.robot.commands.DriveBase_DriveDistance;
import org.usfirst.frc.team4322.robot.commands.DriveBase_Rotate;
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
	public static OI oi;
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
	public static double[][] autoSwitchRight_Left;
	public static double[][] autoSwitchRight_Right;
	public static MotionProfileCurve autoSwitchLeft;
	public static double[][] autoSwitchLeft_Left;
	public static double[][] autoSwitchLeft_Right;
	public static MotionProfileCurve turn90;

	public static MotionProfileCurve motionProfileAppendTest;

	//Autonomous
	private Command autoCommand;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit()
	{
		//Start Subsystems (Mind Dependencies!)
		driveBase = new DriveBase();
		collector = new Collector();
		elevator = new Elevator();
		//Start OI
		oi = new OI();

//        autoSwitchRight = new MotionProfileCurve(Math.toRadians(24.396), Math.toRadians(24.396), 9.33333, 3.5);
//        autoSwitchLeft = new MotionProfileCurve(Math.toRadians(-24.396), Math.toRadians(-24.396), 9.33333, 3.5);
		spline = new MotionProfileCurve(Math.toRadians(30), Math.toRadians(30), 6, 4);
		autoSwitchLeft = new MotionProfileCurve(-Math.PI / 6, -Math.PI / 6, 5, 3.5);
		autoSwitchRight = new MotionProfileCurve(Math.PI / 6, Math.PI / 6, 5, 5);
		turn90 = new MotionProfileCurve(Math.PI / 4, 3 * Math.PI / 4, 4.2, 1.5);
		straight = new MotionProfileCurve(0, 0, 10, 2);
		
		motionProfileAppendTest = new MotionProfileCurve();
		
		spline.setName("line");
		spline.readProfileFromCSV();
//		autoSwitchLeft.setName("autoSwitchLeft");
//		autoSwitchLeft.readProfileFromCSV();

		motionProfileAppendTest.setName("chain");
		motionProfileAppendTest = 
				MotionProfileCurve.appendProfiles(
						MotionProfileCurve.appendProfiles(straight, turn90), 
						MotionProfileCurve.appendProfiles(turn90, straight)) 
				;
		motionProfileAppendTest.readProfileFromCSV();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	public void disabledInit()
	{
//    	autoSwitchRight_Left = autoSwitchRight.generateProfileLeft();
//    	autoSwitchRight_Right = autoSwitchRight.generateProfileRight();
//    	
//    	autoSwitchLeft_Left = autoSwitchLeft.generateProfileLeft();
//    	autoSwitchLeft_Right = autoSwitchLeft.generateProfileRight();

//		File test = new File("/home/lvuser/stupid.csv");
//		try
//		{
//			test.createNewFile();
//			System.out.println("file created!");
//		}
//		catch (IOException e)
//		{
//			
//		}
		
//		line.initializeCurve();
//		turn90.initializeCurve();
//		autoSwitchLeft.initializeCurve();
//		autoSwitchRight.initializeCurve();
//		motionProfileAppendTest.setName();
////    	motionProfileAppendTest = MotionProfileCurve.appendProfiles(autoSwitchLeft, autoSwitchRight);
//		try
//		{
//			motionProfileAppendTest =
//					MotionProfileCurve.appendProfiles(
//							MotionProfileCurve.appendProfiles(
//									MotionProfileCurve.appendProfiles(
//											autoSwitchLeft,
//											autoSwitchRight)
//									, turn90)
//
//							, turn90);
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		;
	}

	public void disabledPeriodic()
	{
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("DriveBase Encoder Value: ", Robot.driveBase.getDist());
		SmartDashboard.putNumber("Left Enc: ", Robot.driveBase.leftMaster.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right Enc: ", Robot.driveBase.rightMaster.getSelectedSensorPosition(0));
		Robot.driveBase.rightMaster.set(ControlMode.PercentOutput, 0);
		SmartDashboard.putNumber("NavX Yaw:", Robot.driveBase.getAngle());
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
		autoCommand = new Auto_MotionProfileDrive(motionProfileAppendTest.generatedProfileLeft, motionProfileAppendTest.generatedProfileRight);
//		autoCommand = new AutoGroup_DriveSquare();
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
		driveBase.leftMaster.set(ControlMode.PercentOutput, 0);
		driveBase.rightMaster.set(ControlMode.PercentOutput, 0);
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic()
	{
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("DriveBase Encoder Value: ", Robot.driveBase.getDist());
		SmartDashboard.putNumber("Left Enc: ", Robot.driveBase.leftMaster.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right Enc: ", Robot.driveBase.rightMaster.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right motor voltage: ", Robot.driveBase.getVoltageRight());
		SmartDashboard.putNumber("Left motor voltage: ", Robot.driveBase.getVoltageLeft());
		SmartDashboard.putNumber("V Left: ", Robot.driveBase.leftMaster.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("V Right: ", Robot.driveBase.rightMaster.getSelectedSensorVelocity(0));
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic()
	{
		LiveWindow.setEnabled(true);
	}
}
