package org.usfirst.frc.team4322.robot.motion;

import java.util.function.DoubleSupplier;

public class MotionProfileCurve {
	public double[][] position;
	public double[][] positionLeft;
	public double[][] positionRight;
	
	public double[] velocity;
	public double[] velocityLeft;
	public double[] velocityRight;
	
	public double[][] generatedProfile;
	
	public double targetVelocity = 3; //cruising speed in feet per s
	public double timeConstant = 0; //accumulates over time
	public double maxTime = 10; //duration of profile
	public static final double duration = .10; //duration of each point
	public static final double wheelBaseWidth = 2.57; //feet
	public int numOfPoints = (int) (maxTime / duration);
			
	public MotionProfileCurve()
	{
		position = new double[numOfPoints][2];
		positionLeft = new double[numOfPoints][2];
		positionRight = new double[numOfPoints][2];
		velocity = new double[numOfPoints];
		velocityLeft = new double[numOfPoints];
		velocityRight = new double[numOfPoints];
		generatedProfile = new double[numOfPoints][3];
	}
	public MotionProfileCurve(DoubleSupplier functionX, DoubleSupplier functionY) 
	{
		// TODO Auto-generated constructor stub
	}
	public void fillPosition()
	{
		for(int i = 0; i < numOfPoints; i++)
		{
			double grad;
			
			position[i][0] = timeConstant;//x
			position[i][1] = .5 * Math.sin( timeConstant);  //y
			
			if (i == 0)
			{
				grad = Math.atan2(position[i][1], position[i][0]);
			}
			else
			{
				grad = Math.atan2(position[i][1] - position[i-1][1], position[i][1] - position[i-1][1]);
			}
			positionLeft[i][0] = wheelBaseWidth / 2 * Math.cos(grad + (Math.PI / 2)) + position[i][0];
			positionLeft[i][1] = wheelBaseWidth / 2 * Math.sin(grad + (Math.PI / 2)) + position[i][1];
			positionRight[i][0] = wheelBaseWidth / 2 * Math.cos(grad - (Math.PI / 2)) + position[i][0];
			positionRight[i][1] = wheelBaseWidth / 2 * Math.sin(grad - (Math.PI / 2)) + position[i][1];
			
			timeConstant += duration;
		}
	}
	public void fillVelocity(double[][] positionLeft, double[][] positionRight, double[] velocityLeft, double[] velocityRight)
	{
		//velocity = derivative of position
		double dxdtL;
		double dydtL;
		double dxdtR;
		double dydtR;
		for (int i = 0; i < numOfPoints; i++)
		{
			if (i == 0)
			{
				dxdtL = positionLeft[i][0] / duration; //run an approximation of dxdt and dydt
				dydtL = positionLeft[i][1] / duration;
				dxdtR = positionRight[i][0] / duration; //run an approximation of dxdt and dydt
				dydtR = positionRight[i][1] / duration;
			}
			else
			{
				dxdtL = (positionLeft[i][0] - positionLeft[i-1][0]) / duration; //run an approximation of dxdt and dydt
				dydtL = (positionLeft[i][1] - positionLeft[i-1][1]) / duration;
				dxdtR = (positionRight[i][0] - positionRight[i-1][0]) / duration; //run an approximation of dxdt and dydt
				dydtR = (positionRight[i][1] - positionRight[i-1][1]) / duration;
			}
			velocityLeft[i] = Math.sqrt(Math.pow(dxdtL, 2) + Math.pow(dydtL, 2)) * 90 / Math.PI; //calculate magnitude of velocity, convert to rpm
			velocityRight[i] = Math.sqrt(Math.pow(dxdtR, 2) + Math.pow(dydtR, 2)) * 90 / Math.PI;
		}
	}
	public double[] applyRamping(double[] velocity) 
	{
		//this method applies a smoothed trapezoidal profile to the velocity so it ramps up and down
		double[] result = new double[numOfPoints];
		double t = 0;
		for(int i = 0; i < numOfPoints; i++)
		{
			result[i] = velocity[i] * ( (targetVelocity / (1 + Math.exp(-14.8 * targetVelocity * (t + (.05 * 14.8) - 1))) - (targetVelocity / (1 + Math.exp(-14.8 * ( targetVelocity * (t + 14.8 *.05 - 1) - (maxTime-.5))))))); 
			//apply s-curve profile
			
			t+=duration;
		}
		return result;
	}
	public double[] arcLength(double[] velocity) //calculate number of rotations for encoder
	{
		//calculates position setpoint at each node
		double[] result = new double[numOfPoints];
		for(int i = 0; i < numOfPoints; i++)
		{
			if (i == 0)
			{
				result[i] = velocity[i];
			}
			else
			{
				result[i] = (velocity[i] * duration / 60 + result[i - 1]); //numerical integration of velocity
			}
		}
		return result;
	}
	public double[][] compileProfile(double[] distanceInput, double[] velocityInput)
	{
		double [][] generatedProfile = new double[numOfPoints][3];
		for(int i = 0; i < numOfPoints; i++)
		{
			generatedProfile[i][0] = Math.floor(distanceInput[i] * 1000000) / 1000000;
			generatedProfile[i][1] = Math.floor(velocityInput[i] * 1000000) / 1000000;
			generatedProfile[i][2] = duration * 1000;
			System.out.println("{" + generatedProfile[i][0] + ", " + generatedProfile[i][1] +  ", " + generatedProfile[i][2] + "}");
		}
		return generatedProfile;
	}
	public double[][] generateProfileLeft()
	{
		double [][] outputLeft;
		double[] rampedVelocityLeft;
		double[] rotLeft;
		fillPosition();
		fillVelocity(positionLeft, positionRight, velocityLeft, velocityRight);
		rampedVelocityLeft = applyRamping(velocityLeft);
		rotLeft = arcLength(rampedVelocityLeft);
		outputLeft = compileProfile(rotLeft, rampedVelocityLeft);
		return outputLeft;
	}
	public double[][] generateProfileRight()
	{
		double [][] outputRight;
		double[] rampedVelocityRight;
		double[] rotRight;
		fillPosition();
		fillVelocity(positionLeft, positionRight, velocityLeft, velocityRight);
		rampedVelocityRight = applyRamping(velocityRight);
		rotRight = arcLength(rampedVelocityRight);
		outputRight = compileProfile(rotRight, rampedVelocityRight);
		return outputRight;
	}
}
