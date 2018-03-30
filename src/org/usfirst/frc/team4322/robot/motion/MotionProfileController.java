package org.usfirst.frc.team4322.robot.motion;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motion.TrajectoryPoint.TrajectoryDuration;

import edu.wpi.first.wpilibj.Notifier;

public class MotionProfileController {
	private MotionProfileStatus motionProfileStatus;
	double position = 0;
	double velocity = 0;
	double heading = 0;
	private WPI_TalonSRX talon;
	private int state = 0;
	private int timeout = 0;
	private boolean start = false;
	private SetValueMotionProfile setValue = SetValueMotionProfile.Disable;
	
	private double[][] points;
	
	private static int minBufferSize = 5;
	private static int timeoutLoops = 10;
	
	class PeriodicRunnable implements java.lang.Runnable 
	{
	    public void run() 
	    {
	    	talon.processMotionProfileBuffer();    
	    	}
	}
	Notifier notifier = new Notifier(new PeriodicRunnable());
	public MotionProfileController(WPI_TalonSRX talon, double[][] points)
	{
		this.talon = talon;
		this.points = points;
		motionProfileStatus = new MotionProfileStatus();
		talon.changeMotionControlFramePeriod(5);
		notifier.startPeriodic(0.05);
	
	}
	public void reset()
	{
		talon.clearMotionProfileTrajectories();
		setValue = SetValueMotionProfile.Disable;
		state = 0;
		timeout = -1;
		start = false;
	}
	public void control()
	{
		talon.getMotionProfileStatus(motionProfileStatus);
		switch(state) 
		{
			case 0:
				if (start)
				{
					start = false;
					setValue = SetValueMotionProfile.Disable;
					startFilling();
					state = 1;
				}
				break;
			case 1:
				if (motionProfileStatus.btmBufferCnt > minBufferSize)
				{
					setValue = SetValueMotionProfile.Enable;
					state = 2;
				}
				break;
			case 2:
				if (motionProfileStatus.activePointValid && motionProfileStatus.isLast) 
				{
					setValue = SetValueMotionProfile.Hold;
				}
				break;
		}
		talon.getMotionProfileStatus(motionProfileStatus);
		heading = talon.getActiveTrajectoryHeading();
		position = talon.getActiveTrajectoryPosition();
		velocity = talon.getActiveTrajectoryVelocity();
	}
	private TrajectoryDuration GetTrajectoryDuration(int durationMs)
	{	 
		/* create return value */
		TrajectoryDuration retval = TrajectoryDuration.Trajectory_Duration_0ms;
		/* convert duration to supported type */
		retval = retval.valueOf(durationMs);
		/* check that it is valid */
		if (retval.value != durationMs) {
			System.out.println("Trajectory Duration not supported - use configMotionProfileTrajectoryPeriod instead");		
		}
		/* pass to caller */
		return retval;
	}
	private void startFilling()
	{
		startFilling(points, points.length);
	}
	private void startFilling(double [][] profile, int total)
	{
		TrajectoryPoint point = new TrajectoryPoint();
		if (motionProfileStatus.hasUnderrun)
		{
			talon.clearMotionProfileHasUnderrun(0);
		}
		talon.clearMotionProfileTrajectories();
		talon.configMotionProfileTrajectoryPeriod(0, 10);
		for (int i = 0; i < total; i++)
		{
			double positionRot = profile[i][0];
			double velocityRPM = profile[i][1];
			
			point.position = positionRot * 1024;
			point.velocity = velocityRPM * 1024 / 600;
			point.headingDeg = 0;
			point.profileSlotSelect0 = 0;
			point.profileSlotSelect1 = 0;
			point.timeDur = GetTrajectoryDuration((int)profile[i][2]);
			point.zeroPos = false;
			if (i == 0)
				point.zeroPos = true; /* set this to true on the first point */

			point.isLastPoint = false;
			if ((i + 1) == total)
				point.isLastPoint = true; /* set this to true on the last point  */

			talon.pushMotionProfileTrajectory(point);
		}
	}
	public void startMotionProfile()
	{
		start = true;
	}
	public SetValueMotionProfile getSetValue()
	{
		return setValue;
	}
	public void configure()
	{
		talon.config_kF(0, .076, 10);
		talon.config_kP(0, 2, 10);
		talon.config_kI(0, 0, 10);
		talon.config_kD(0, 20, 10);
		talon.configMotionAcceleration(10, 10);
		talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);
		talon.set(ControlMode.MotionProfile, getSetValue().value);
	}
}
