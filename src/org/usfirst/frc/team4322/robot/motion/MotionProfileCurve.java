package org.usfirst.frc.team4322.robot.motion;

import java.io.*;

import org.usfirst.frc.team4322.robot.RobotMap;

public class MotionProfileCurve
{
	private static final double duration = .10;
	private static final double jConstant = 10.3;
	private static final double rampRate = 1;

	private double theta1;
	private double theta2;
	private double distance;
	private double maxTime;
	private double targetVelocity;
	int numOfPoints;

	double[][] positionLeft;
	double[][] positionRight;
	double[] rotationsLeft;
	double[] rotationsRight;
	double[] velocityLeft;
	double[] velocityRight;
	double[] rampedVelocityLeft;
	double[] rampedVelocityRight;

	public double[][] generatedProfileLeft;
	public double[][] generatedProfileRight;

	private FileWriter writer;
	private String fileName;

	MotionProfileCurve()
	{

	}
	public MotionProfileCurve(double theta1, double theta2, double distance, double maxTime)
	{
		this.theta1 = theta1;
		this.theta2 = theta2;
		this.distance = distance;
		this.maxTime = maxTime;
		targetVelocity = distance / maxTime;
		numOfPoints = (int) (maxTime / duration);

		positionLeft = new double[numOfPoints][2];
		positionRight = new double[numOfPoints][2];
	}
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	void position(double[][] left, double[][] right)
	{
		System.out.println("--- Position Values ---");
		double grad;
		double timeConstant = 0;

		double quintic = (-3 * (Math.tan(theta2) + Math.tan(theta1))) / (Math.pow(distance, 4));
		double quartic = (Math.tan(theta1) - ((2.333333) * Math.pow(distance, 4) * quintic)) / Math.pow(distance, 3);
		double cubic = ((10 * Math.pow(distance, 2) * quintic) + (6 * distance * quartic)) / -3;
		double linear = Math.tan(theta1);

		double[][] center = new double[numOfPoints][2];

		for (int i = 0; i < numOfPoints; i++)
		{
			center[i][0] = targetVelocity * timeConstant;
			center[i][1] = (quintic * Math.pow(center[i][0], 5)) +
					(quartic * Math.pow(center[i][0], 4)) +
					(cubic * Math.pow(center[i][0], 3)) +
					(linear * center[i][0]);
			System.out.println("Center X: " + center[i][0]);
			System.out.println("Center Y: " + center[i][1]);
			if (i == numOfPoints - 1)
			{
				grad = Math.atan2(center[i][1] - center[i - 1][1], center[i][0] - center[i - 1][0]);
			} else
			{
				grad = Math.atan2(center[i + 1][1] - center[i][1], center[i + 1][0] - center[i][0]);
			}
			left[i][0] = RobotMap.DRIVEBASE_WHEELBASE_WIDTH / 2 * Math.cos(grad + (Math.PI / 2)) + center[i][0];
			left[i][1] = RobotMap.DRIVEBASE_WHEELBASE_WIDTH / 2 * Math.sin(grad + (Math.PI / 2)) + center[i][1];

			System.out.println("Left X: " + left[i][0]);
			System.out.println("Left Y: " + left[i][0]);

			right[i][0] = RobotMap.DRIVEBASE_WHEELBASE_WIDTH / 2 * Math.cos(grad - (Math.PI / 2)) + center[i][0];
			right[i][1] = RobotMap.DRIVEBASE_WHEELBASE_WIDTH / 2 * Math.sin(grad - (Math.PI / 2)) + center[i][1];

			System.out.println("Right X: " + right[i][0]);
			System.out.println("Right Y: " + right[i][0]);

			timeConstant += duration;
		}
	}

	double[] velocity(double[][] position)
	{
		double dxdt;
		double dydt;
		double[] result = new double[position.length];
		for (int i = 1; i < result.length; i++)
		{
			dxdt = (position[i][0] - position[i - 1][0]) / duration;
			dydt = (position[i][1] - position[i - 1][1]) / duration;

			result[i] = Math.sqrt(Math.pow(dxdt, 2) + Math.pow(dydt, 2)) * 60 * 12 / (RobotMap.DRIVEBASE_WHEEL_DIAMETER * Math.PI);
		}
		return result;
	}

	double[] applyRamping(double[] velocity)
	{
		double[] result = new double[velocity.length];
		double timeConstant = 0;

		for (int i = 0; i < numOfPoints; i++)
		{
			result[i] = velocity[i] * (Math.log(

					(
							(Math.exp(-jConstant * rampRate * timeConstant) + 1) / (Math.exp((-jConstant * rampRate * timeConstant) + jConstant) + 1)
					) /
							(Math.exp(-jConstant * rampRate * (timeConstant - (maxTime * (-Math.pow(jConstant, -2) + 1) + Math.pow(rampRate, -1))) + 1))
							* (Math.exp(-jConstant * rampRate * (timeConstant - (maxTime * (-Math.pow(jConstant, -2) + 1) + Math.pow(rampRate, -1))) + jConstant) + 1)
					)
					/ jConstant);
			timeConstant += duration;
		}
		return result;
	}
	double[] optimizeVelocity(double[] velocityOrig, double[] rampedVelocity)
	{
		System.out.println("--- Begin Optimizing Velocity! ---");
		double[] result = rampedVelocity;
		double[] positionOrig = arcLength(velocityOrig);
		double positionExpected = positionOrig[numOfPoints - 1];
		double[] positionRamped = arcLength(result);
		double positionActual = positionRamped[numOfPoints - 1];
		double error = positionActual - positionExpected;
		System.out.println("Original Error: " + error);
		double correctionFactor = 1;
		while (Math.abs(error) > .01)
		{
//	       System.out.println("Correction Factor: " + correctionFactor);
			for (int i = 1; i < numOfPoints; i++)
			{
				result[i] = result[i] - (correctionFactor * error / 50);
			}
			positionRamped = arcLength(result);
			positionActual = positionRamped[numOfPoints - 1];
			error = positionActual - positionExpected;
//	       System.out.println("New Error: " + error);
			correctionFactor += .1;
		}
		System.out.println("Final Error: " + error);
		System.out.println("--- Ended Optimizing Velocity ---");
		return result;
	}
	double[] arcLength(double[] velocity) //calculate number of rotations for encoder
	{
		//calculates position setpoint at each node
		double[] result = new double[numOfPoints];
		result[0] = 0;
		for (int i = 1; i < numOfPoints; i++)
		{
			result[i] = ((velocity[i] * duration / 60) + result[i - 1]); //numerical integration of velocity
		}
		return result;
	}
	private double[][] writeMotionProfile(double[] rotations, double[] velocity, String filePath)
	{
		System.out.println("--- Begin Writing Profile " + fileName + " ---");
		double[][] result = new double[numOfPoints][3];
		try
		{
			writer = new FileWriter(filePath);
			for (int i = 0; i < numOfPoints; i++)
			{
				writer.append(Double.toString(rotations[i]));
				writer.append(',');
				writer.append(Double.toString(velocity[i]));
				writer.append(',');
				writer.append(Double.toString(duration * 1000));
				writer.append('\n');

				result[i][0] = rotations[i];
				result[i][1] = velocity[i];
				result[i][2] = duration * 1000;

				System.out.println("{" + result[i][0] + ", " + result[i][1] + ", " + result[i][2] + "}");
			}
			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{
			System.out.println("FILE WRITE FAILED: " + e.toString());
		}
		System.out.println("--- End Writing Profile ---");
		return result;
	}
	protected void calculateStuff()
	{
		position(positionLeft, positionRight);
		position(positionLeft, positionRight);

		velocityLeft = velocity(positionLeft);
		velocityRight = velocity(positionRight);

		rampedVelocityLeft = applyRamping(velocityLeft);
		rampedVelocityRight = applyRamping(velocityRight);

		rampedVelocityLeft = optimizeVelocity(velocityLeft, rampedVelocityLeft);
		rampedVelocityRight = optimizeVelocity(velocityLeft, rampedVelocityRight);

		rotationsLeft = arcLength(rampedVelocityLeft);
		rotationsRight = arcLength(rampedVelocityRight);
	}
	public void readProfileFromCSV()
	{
		String pathLeft = "/home/lvuser/" + fileName + "Left.csv";
		String pathRight = "/home/lvuser/" + fileName + "Right.csv";
		File left = new File(pathLeft);
		File right = new File(pathRight);
		String line = "";
		if (left.exists() && right.exists())
		{
			generatedProfileLeft = new double[numOfPoints][3];
			generatedProfileRight = new double[numOfPoints][3];
			try (BufferedReader brLeft = new BufferedReader(new FileReader(left));
				 BufferedReader brRight = new BufferedReader(new FileReader(right)))
			{
				int i = 0;
				while ((line = brLeft.readLine()) != null)
				{
					String[] values = line.split(",");
					generatedProfileLeft[i][0] = Double.parseDouble(values[0]);
					generatedProfileLeft[i][1] = Double.parseDouble(values[1]);
					generatedProfileLeft[i][2] = Double.parseDouble(values[2]);
					i++;
				}
				i = 0;
				while ((line = brRight.readLine()) != null)
				{
					String[] values = line.split(",");
					generatedProfileRight[i][0] = Double.parseDouble(values[0]);
					generatedProfileRight[i][1] = Double.parseDouble(values[1]);
					generatedProfileRight[i][2] = Double.parseDouble(values[2]);
					i++;
				}
			}
			catch (IOException e)
			{
				System.out.println("FILE READ FAILED: " + e.toString());
			}
		}
		else
		{
			try
			{
				left.delete();
				right.delete();
				left.createNewFile();
				right.createNewFile();
			}
			catch(IOException e)
			{
				System.out.println("FAILED: " + e.toString());
			}
			calculateStuff();
			generatedProfileLeft = writeMotionProfile(rotationsLeft, rampedVelocityLeft, pathLeft);
			generatedProfileRight = writeMotionProfile(rotationsRight, rampedVelocityRight, pathRight);
		}
	}


}