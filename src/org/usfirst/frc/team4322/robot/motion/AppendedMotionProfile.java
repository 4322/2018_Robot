package org.usfirst.frc.team4322.robot.motion;

import java.util.ArrayList;
import java.util.List;

public class AppendedMotionProfile extends MotionProfileCurve
{
//	MotionProfileCurve[] curves;
//	public AppendedMotionProfile(MotionProfileCurve[] curves)
//	{
//		this.curves = curves;
//		numOfPoints = 0;
//		maxTime = 0;
//		for (MotionProfileCurve curve:
//			 curves)
//		{
//			numOfPoints += curve.numOfPoints;
//			maxTime += curve.maxTime;
//		}
//		System.out.println(numOfPoints);
//	}
//	@Override
//	public void calculateStuff()
//	{
//		List<Double> vLeft = new ArrayList<>();
//		List<Double> vRight = new ArrayList<>();
//		velocityLeft = new double[numOfPoints];
//		velocityRight = new double[numOfPoints];
//
//
//		for (MotionProfileCurve curve : curves)
//		{
//			curve.position(curve.positionLeft, curve.positionRight);
//
//			curve.velocityLeft = curve.velocity(curve.positionLeft);
//			curve.velocityRight = curve.velocity(curve.positionRight);
//
//			for (int i = 0; i < curve.numOfPoints; i++)
//			{
//				vLeft.add(curve.velocityLeft[i]);
//				vRight.add(curve.velocityRight[i]);
//			}
//		}
//		for(int i = 0; i < vLeft.size(); i++)
//		{
//			velocityLeft[i] = vLeft.get(i);
//		}
//		for(int i = 0; i < vRight.size(); i++)
//		{
//			velocityRight[i] = vRight.get(i);
//		}
////		rampedVelocityLeft = velocityLeft;
////		rampedVelocityRight = velocityRight;
//		rampedVelocityLeft = applyRamping(velocityLeft);
//		rampedVelocityRight = applyRamping(velocityRight);
//
//		rampedVelocityLeft = optimizeVelocity(velocityLeft, rampedVelocityLeft);
//		rampedVelocityRight = optimizeVelocity(velocityRight, rampedVelocityRight);
//
//		rotationsLeft = arcLength(rampedVelocityLeft);
//		rotationsRight = arcLength(rampedVelocityRight);
//	}
}
