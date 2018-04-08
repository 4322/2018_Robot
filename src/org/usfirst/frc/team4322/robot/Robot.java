
package org.usfirst.frc.team4322.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Compressor;
//import org.usfirst.frc.team4322.logging.RobotLogger;
//import org.usfirst.frc.team4322.robot.commands.auto.AutoGroup_DriveSquare;
//import org.usfirst.frc.team4322.robot.commands.auto.Auto_MotionProfileDrive;
//import org.usfirst.frc.team4322.robot.commands.DriveBase_DriveDistance;
//import org.usfirst.frc.team4322.robot.commands.DriveBase_Rotate;
import org.usfirst.frc.team4322.robot.commands.auto.AutoGroup_ReachBaseline;
import org.usfirst.frc.team4322.robot.motion.MotionProfileCurve;
import org.usfirst.frc.team4322.robot.subsystems.*;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
//	public static RobotLogger logger;

	// Controls Class
	private static OI oi;
	// Auto Chooser Class
	private static SmartAuto smartAuto;
	// Drivebase Subsystem Class
	public static DriveBase driveBase;
	// CollectorRollers Subsystem Class
	public static CollectorRollers collectorRollers;
	public static CollectorDeployer collectorDeployer;
	public static CollectorActuator collectorActuator;
	//Stupid
	public static boolean isReadyForClimb = false;
	// Elevator Subsystem Class
	public static Elevator elevator;
	//MotionProfiles
	public static MotionProfileCurve testSpline;
	public static MotionProfileCurve straight;
	public static MotionProfileCurve autoSwitchRight;
	public static MotionProfileCurve autoSwitchLeft;
	public static MotionProfileCurve autoScaleLeftLeft1;
	public static MotionProfileCurve autoScaleLeftLeft2;
	public static MotionProfileCurve autoScaleLeftRight;
	public static MotionProfileCurve autoScaleRightRight1;
	public static MotionProfileCurve autoScaleRightRight2;
	public static MotionProfileCurve autoScaleRightLeft;

	//Autonomous
	private Command autoCommand;

	//Limelight
	private static NetworkTable limelight;

//	//Pneumatics
//	public Compressor compressor;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit()
	{
//		logger = new RobotLogger();
		//Autos
		smartAuto = new SmartAuto();
		//Start Subsystems (Mind Dependencies!)
		driveBase = new DriveBase();
		collectorRollers = new CollectorRollers();
		collectorDeployer = new CollectorDeployer();
		collectorActuator = new CollectorActuator();
		elevator = new Elevator();
		//Start OI
		oi = new OI();
		//Start compressor
		new Compressor().setClosedLoopControl(true);
		//Limelight settings
		limelight = NetworkTableInstance.getDefault().getTable("limelight");
		limelight.getEntry("ledMode").setValue(1);
		limelight.getEntry("camMode").setValue(1);

		//Motion Profiles
		autoSwitchLeft = new MotionProfileCurve(-30.6, -30.6, 9.726, 4, 4);
		autoSwitchRight = new MotionProfileCurve(24.396, 24.396, 9.333333, 4, 4);

//		autoScaleLeftLeft = MotionProfileCurve.appendProfiles(
//				new MotionProfileCurve(Math.toDegrees(-.13505), Math.toDegrees(-.13505), 185.702 / 12, 2),
//				new MotionProfileCurve(33, 180 - 3.4, 84.513 / 12, 1));
//		testSpline = new MotionProfileCurve(.5, .5, 5, 2, 2);

		autoScaleLeftLeft1 = new MotionProfileCurve(-6.6, -6.6, 185.702 / 12, 6, 8);
		autoScaleLeftLeft2 = new MotionProfileCurve(33,-3.4, 84.513 / 12, 2, 2);

		autoScaleRightRight1 = new MotionProfileCurve(6.6, 6.6, 185.702 / 12, 6, 8);
		autoScaleRightRight2 = new MotionProfileCurve(-33, 3.4, 84.513 / 12, 2, 2);
// autoSwitchRight = new MotionProfileCurve(toRadians(24.396), toRadians(24.396), 9.33333, 10,10);
//
// 		autoScaleLeftLeft = new AppendedMotionProfile(
//				new MotionProfileCurve[]{
//					new MotionProfileCurve(-.13505, -.13505, 185.702 / 12, 30, 30),
//					new MotionProfileCurve(toRadians(33), toRadians(180 - 3.4), 84.513 / 12, 10,10)
//				}
//		);
//
//        autoScaleLeftRight = new AppendedMotionProfile(
//				new MotionProfileCurve[]{
//					new MotionProfileCurve(0, 0, 159.563 / 12, 30, 30),
//					new MotionProfileCurve(toRadians(63.7), toRadians(180 - 26.3), 94.81 / 12, 10, 10),
//					new MotionProfileCurve(toRadians(3.1), toRadians(3.1), 75.107 / 12, 20, 20),
//					new MotionProfileCurve(-toRadians(66.5), toRadians(23.5), 63.809 / 12, 20, 20)
//				}
//		);
//		autoScaleRightRight = new AppendedMotionProfile(
//				new MotionProfileCurve[]{
//						new MotionProfileCurve(.13505, .13505, 185.702 / 12, 30, 30),
//						new MotionProfileCurve(-toRadians(33), -toRadians(180 - 3.4), 84.513 / 12, 10, 10)
//				}
//		);
//        autoScaleRightLeft = new AppendedMotionProfile(
//				new MotionProfileCurve[]{
//						new MotionProfileCurve(0, 0, 159.563 / 12, 30, 30),
//						new MotionProfileCurve(-toRadians(63.7), -toRadians(180 - 26.3), 94.81 / 12, 10, 10),
//						new MotionProfileCurve(-toRadians(3.1), -toRadians(3.1), 75.107 / 12, 20, 20),
//						new MotionProfileCurve(toRadians(66.5), -toRadians(23.5), 63.809 / 12, 20, 20)
//				}
//		);
//
		autoSwitchLeft.setName("autoSwitchLeft");
		autoSwitchLeft.readProfileFromCSV();

		autoSwitchRight.setName("autoSwitchRight");
		autoSwitchRight.readProfileFromCSV();

		autoScaleLeftLeft1.setName("autoScaleLeftLeft1");
		autoScaleLeftLeft1.readProfileFromCSV();

		autoScaleLeftLeft2.setName("autoScaleLeftLeft2");
		autoScaleLeftLeft2.readProfileFromCSV();

		autoScaleRightRight1.setName("autoScaleRightRight1");
		autoScaleRightRight1.readProfileFromCSV();

		autoScaleRightRight2.setName("autoScaleRightRight2");
		autoScaleRightRight2.readProfileFromCSV();
//
//		autoScaleRightRight.setFileName("autoScaleRightRight");
//		autoScaleRightRight.readProfileFromCSV();
//
//		autoScaleRightLeft.setFileName("autoScaleRightLeft");
//		autoScaleRightLeft.readProfileFromCSV();
//
//		autoScaleLeftRight.setFileName("autoScaleLeftRight");
//		autoScaleLeftRight.readProfileFromCSV();
//
//		testSpline.setFileName("testSpline");
//		testSpline.readProfileFromCSV();
		SmartDashboard.putString("Build DateTime", "2/18/17 5:24PM");
		elevator.reset();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	public void disabledInit()
	{
		limelight.getEntry("ledMode").setValue(1);
		limelight.getEntry("camMode").setValue(1);

	}

	public void disabledPeriodic()
	{
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("DriveBase Encoder Value: ", Robot.driveBase.getDist());
		SmartDashboard.putNumber("Left Enc: ", Robot.driveBase.leftMaster.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right Enc: ", Robot.driveBase.rightMaster.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Elevator Enc: ", Robot.elevator.getPosition());
		SmartDashboard.putNumber("NavX Yaw: ", Robot.driveBase.getAngle());
		SmartDashboard.putNumber("Auto Switch: ", Robot.smartAuto.getPosition());

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
		Robot.elevator.reset();
		smartAuto.choose();
		autoCommand = smartAuto.getAuto();
//		autoCommand = new AutoGroup_ReachBaseline();
//		autoCommand = new AutoGroup_SwitchCenter_Right();
		autoCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic()
	{
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("DriveBase Encoder Value: ", Robot.driveBase.getDist());
		SmartDashboard.putNumber("NavX Yaw: ", Robot.driveBase.getAngle());
	}

	public void teleopInit()
	{
//		driveBase.leftMaster.clearMotionProfileTrajectories();
//		driveBase.rightMaster.clearMotionProfileTrajectories();
//		elevator.position = Elevator.ElevatorPosition.SWITCH;
		driveBase.leftMaster.set(ControlMode.PercentOutput, 0);
		driveBase.rightMaster.set(ControlMode.PercentOutput, 0);
		limelight.getEntry("ledMode").setValue(1);
		limelight.getEntry("camMode").setValue(1);
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic()
	{
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("DriveBase Encoder Value: ", Robot.driveBase.getDist());
		SmartDashboard.putNumber("Elevator Enc: ", Robot.elevator.getPosition());
		SmartDashboard.putNumber("Elevator Vel: ", Robot.elevator.getVelocity());
		SmartDashboard.putNumber("Left Velocity", Robot.driveBase.leftMaster.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("Right Velocity: ", Robot.driveBase.rightMaster.getSelectedSensorVelocity(0));
		SmartDashboard.putBoolean("Pivot Limit: ", Robot.collectorDeployer.isLimit());
		SmartDashboard.putNumber("NavX Yaw: ", Robot.driveBase.getAngleRaw());
		SmartDashboard.putNumber("Deployer Encoder: ", Robot.collectorDeployer.getEncoder());
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic()
	{
		LiveWindow.setEnabled(true);
	}
}
