package org.usfirst.frc.team4322.robot.motion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.DoubleSupplier;

import org.usfirst.frc.team4322.robot.RobotMap;

import static java.lang.Math.toRadians;

public class MotionProfileCurve
{
	private String name;

	private double[][] position;
	private double[][] positionLeft;
	private double[][] positionRight;

	private double[] velocity;
	private double[] velocityLeft;
	private double[] velocityRight;

	private double[] rampedVelocityLeft;
	private double[] rampedVelocityRight;

	public double[][] generatedProfileLeft;
	public double[][] generatedProfileRight;

	private double targetVelocity = 3; //cruising speed in feet per s
	private double rampRate = 1;
	private double jConstant = 10.3;
	private double timeConstant = 0; //accumulates over time
	private double maxTime = 5 / 3; //duration of profile
	private static final double duration = .10; //duration of each point in seconds
	private static final double wheelBaseWidth = RobotMap.DRIVEBASE_WHEELBASE_WIDTH; //feet
	private int numOfPoints = (int) (maxTime / duration);

	private double theta1;
	private double theta2;
	private double distance;

	//numbers used for quintic hermite spline interpolation
	private double quintic;
	private double quartic;
	private double cubic;
	private double linear;

	public File csvFileLeft;
	public File csvFileRight;
	FileWriter writer;
	public boolean isAppended = false;

	public MotionProfileCurve()
	{
		position = new double[numOfPoints][2];
		positionLeft = new double[numOfPoints][2];
		positionRight = new double[numOfPoints][2];
		velocity = new double[numOfPoints];
		velocityLeft = new double[numOfPoints];
		velocityRight = new double[numOfPoints];
	}

	public MotionProfileCurve(double theta1, double theta2, double distance, double maxTime)
	{
		this.theta1 = Math.toRadians(theta1);
		this.theta2 = Math.toRadians(theta2);
		this.distance = distance;
		this.maxTime = maxTime;
		targetVelocity = distance / maxTime;
		numOfPoints = (int) (maxTime / duration);


	}
	public MotionProfileCurve(double theta1, double theta2, double distance, double velocity, double acceleration)
	{
		this.theta1 = Math.toRadians(theta1);
		this.theta2 = Math.toRadians(theta2);
		this.distance = distance;
		targetVelocity = velocity;
		rampRate = acceleration;
		maxTime = (distance / velocity) + (velocity / acceleration);
		numOfPoints = (int) (maxTime / duration);


	}

	private void initializeCurve()
	{
		System.out.println("File does not exist, calculate points.");
		fillHermite();
		fillPosition();
		fillPosition();
		velocityLeft = fillVelocity(positionLeft);
		velocityRight = fillVelocity(positionRight);

	}
	public void setName(String name)
	{
		this.name = name;
	}
	public static MotionProfileCurve appendProfiles(MotionProfileCurve curve1, MotionProfileCurve curve2)
	{
		int numOfPoints = curve1.numOfPoints + curve2.numOfPoints;
		if (!curve1.isAppended)
		{
			curve1.initializeCurve();
		}
		if (!curve2.isAppended)
		{
			curve2.initializeCurve();
		}

		MotionProfileCurve appendedCurve = new MotionProfileCurve();
		appendedCurve.maxTime = curve1.maxTime + curve2.maxTime;
		appendedCurve.numOfPoints = numOfPoints;
		appendedCurve.isAppended = true;
		appendedCurve.position = new double[numOfPoints][2];
		appendedCurve.positionLeft = new double[numOfPoints][2];
		appendedCurve.positionRight = new double[numOfPoints][2];
		appendedCurve.velocity = new double[numOfPoints];
		appendedCurve.velocityLeft = new double[numOfPoints];
		appendedCurve.velocityRight = new double[numOfPoints];
		for (int i = 0; i < curve1.numOfPoints; i++)
		{
			appendedCurve.velocityLeft[i] = curve1.velocityLeft[i];
			appendedCurve.velocityRight[i] = curve1.velocityRight[i];
		}
		for (int i = curve1.numOfPoints; i < numOfPoints; i++)
		{
			appendedCurve.velocityLeft[i] = curve2.velocityLeft[i - curve1.numOfPoints];
			appendedCurve.velocityRight[i] = curve2.velocityRight[i - curve1.numOfPoints];
		}
		appendedCurve.velocityLeft[curve1.numOfPoints] = (appendedCurve.velocityLeft[curve1.numOfPoints - 1] + appendedCurve.velocityLeft[curve1.numOfPoints + 1]) / 2;
		appendedCurve.velocityRight[curve1.numOfPoints] = (appendedCurve.velocityRight[curve1.numOfPoints - 1] + appendedCurve.velocityRight[curve1.numOfPoints + 1]) / 2;
		double[] velocityLeftOrig = appendedCurve.velocityLeft;
		double[] velocityRightOrig = appendedCurve.velocityRight;

		appendedCurve.rampedVelocityLeft = appendedCurve.applyRamping(velocityLeftOrig);
		appendedCurve.rampedVelocityLeft = appendedCurve.optimizeVelocity(velocityLeftOrig, appendedCurve.rampedVelocityLeft);

		appendedCurve.rampedVelocityRight = appendedCurve.applyRamping(velocityRightOrig);
		appendedCurve.rampedVelocityRight = appendedCurve.optimizeVelocity(velocityRightOrig, appendedCurve.rampedVelocityRight);

//		appendedCurve.generatedProfileLeft = appendedCurve.compileProfile(appendedCurve.arcLength(appendedCurve.rampedVelocityLeft), appendedCurve.rampedVelocityLeft, appendedCurve.name + "Left");
//		appendedCurve.generatedProfileRight = appendedCurve.compileProfile(appendedCurve.arcLength(appendedCurve.rampedVelocityRight), appendedCurve.rampedVelocityRight, appendedCurve.name + "Right");

		appendedCurve.compileAppendedProfile();

		return appendedCurve;
	}
	public void compileAppendedProfile()
	{
//		generatedProfileLeft = compileProfile(arcLength(rampedVelocityLeft), rampedVelocityLeft, name + "Left");
//		generatedProfileRight = compileProfile(arcLength(rampedVelocityRight), rampedVelocityRight, name + "Right");
		generatedProfileLeft = compileTestProfile(arcLength(rampedVelocityLeft), rampedVelocityLeft);
		generatedProfileRight = compileTestProfile(arcLength(rampedVelocityRight), rampedVelocityRight);
	}
	double[] trapezoidalProfile(double distance, double velocity, double acceleration)
	{
		double[] result = new double[numOfPoints];
		double time = 0.000001;

		for(int i = 0; i < numOfPoints; i++)
		{
			if (time < 0)
			{
				result[i] = 0;
			}
			else if (time > 0 && time < (velocity/acceleration))
			{
				result[i] = acceleration * time;
			}
			else if (time >= (velocity/acceleration) && time < (distance / velocity))
			{
				result[i] = velocity;
			}
			else if (time >= (distance / velocity) && time <= (maxTime))
			{
				result[i] = -acceleration * (time - maxTime);
			}
			else if (time >= maxTime)
			{
				result[i] = 0;
			}
			System.out.print("Profile Value: ");
			System.out.println(result[i]);
			time += duration;
		}
		return result;
	}
	double[] integrate(double[] input)
	{
		//calculates position setpoint at each node
		double[] tmp = new double[input.length + 1];
		double[] result = new double[input.length];
		tmp[0] = 0;
		for (int i = 1; i < numOfPoints + 1; i++)
		{
			if (i == numOfPoints)
			{
				tmp[i] = tmp[i-1];
			}
			else
			{
				tmp[i] = (input[i] * duration) + tmp[i - 1]; //numerical integration
			}
			result[i - 1] = tmp[i];
			System.out.print("Integration value: ");
			System.out.println(tmp[i]);
		}
		return result;
	}
	public void fillHermite()
	{
		//calculates coefficients for quintic polynomial
				quintic = (-3 * (Math.tan(theta2) + Math.tan(theta1))) / (Math.pow(distance, 4));
				quartic = (Math.tan(theta1) - ((2.333333) * Math.pow(distance, 4) * quintic)) / Math.pow(distance, 3);
				cubic = ((10 * Math.pow(distance, 2) * quintic) + (6 * distance * quartic)) / -3;
				linear = Math.tan(theta1);

				System.out.println("Quintic Term: " + quintic);
				System.out.println("Quartic Term: " + quartic);
				System.out.println("Cubic Term: " + cubic);
				System.out.println("Linear Term: " + linear);

				position = new double[numOfPoints][2];
				positionLeft = new double[numOfPoints][2];
				positionRight = new double[numOfPoints][2];
				velocity = new double[numOfPoints];
				velocityLeft = new double[numOfPoints];
				velocityRight = new double[numOfPoints];
	}

	public void fillPosition()
	{
		timeConstant = 0;
		System.out.println("--- Generating Position Values! ---");
		double[] basePositionProfile = integrate(trapezoidalProfile(distance, targetVelocity, rampRate));
		for (int i = 0; i < numOfPoints; i++)
		{
			double grad;

			position[i][0] = basePositionProfile[i];//x
			position[i][1] = (quintic * Math.pow(position[i][0], 5)) +
					(quartic * Math.pow(position[i][0], 4)) +
					(cubic * Math.pow(position[i][0], 3)) +
					(linear * position[i][0]);//y

			System.out.println(timeConstant + " (" + position[i][0] + ", " + position[i][1] + ")");

			if (i == numOfPoints - 1)
			{
				grad = Math.atan2(position[i][1] - position[i - 1][1], position[i][0] - position[i - 1][0]);
			}
			else
			{
				grad = Math.atan2(position[i + 1][1] - position[i][1], position[i + 1][0] - position[i][0]);
			}
			System.out.println("Grad: " + grad);

			positionLeft[i][0] = wheelBaseWidth / 2 * Math.cos(grad + (Math.PI / 2)) + position[i][0];
			positionLeft[i][1] = wheelBaseWidth / 2 * Math.sin(grad + (Math.PI / 2)) + position[i][1];
			System.out.println("Left x: " + positionLeft[i][0]);
			System.out.println("Left y: " + positionLeft[i][1]);

			positionRight[i][0] = wheelBaseWidth / 2 * Math.cos(grad - (Math.PI / 2)) + position[i][0];
			positionRight[i][1] = wheelBaseWidth / 2 * Math.sin(grad - (Math.PI / 2)) + position[i][1];
			System.out.println("Right x: " + positionRight[i][0]);
			System.out.println("Right y: " + positionRight[i][1]);

			timeConstant += duration;
		}
		System.out.println("--- End of Position Values! ---");
	}

	public double[] fillVelocity(double[][] position)
	{
		double[] velocity = new double[numOfPoints];
		//velocity = derivative of position
		double dxdt;
		double dydt;
		System.out.println("--- Generating Velocity Values ---");
		velocity[0] = 0;
		for (int i = 1; i < numOfPoints - 1; i++)
		{

			dxdt = (position[i][0] - position[i - 1][0]) / duration; //run an approximation of dxdt and dydt
			dydt = (position[i][1] - position[i - 1][1]) / duration;

			velocity[i] = Math.sqrt(Math.pow(dxdt, 2) + Math.pow(dydt, 2)) * 60 * 12 / (RobotMap.DRIVEBASE_WHEEL_DIAMETER * Math.PI); //calculate magnitude of velocity, convert to rpm
			System.out.println(velocity[i]);
		}
		velocity[numOfPoints - 2] = 0;
		System.out.println("--- End of Velocity Values ---");
		return velocity;
	}

	public double[] applyRamping(double[] velocity)
	{
		//this method applies a smoothed trapezoidal profile to the velocity so it ramps up and down
		//rampRate is acceleration in ft/s^2
		//jConstant controls jerk in profile; lower jConstant means more s-curving in profile
		double[] result = new double[numOfPoints];
		double t = 0;
		for (int i = 0; i < numOfPoints; i++)
		{
			result[i] = velocity[i] * (Math.log(

					(
							(Math.exp(-jConstant * rampRate * t) + 1)
							/
							(Math.exp((-jConstant * rampRate * t) + jConstant) + 1)
							)
					/
					(Math.exp(-jConstant * rampRate * (t - (maxTime * (-Math.pow(jConstant, -2) + 1) + Math.pow(rampRate, -1)) ) + 1)
					)
					* (Math.exp(-jConstant * rampRate * (t - (maxTime * (-Math.pow(jConstant, -2) + 1) + Math.pow(rampRate, -1) ) ) + jConstant ) + 1)


					)
					/ jConstant);
			//apply s-curve profile

			t += duration;
		}
		return result;
	}
	double[] smoothVelocity(double[] velocity, double weight_data, double weight_smooth, double tolerance)
	{
		double[] result = velocity;
		double change = tolerance;
		while (change >= tolerance)
		{
			change = 0;
			for (int i = 1; i < velocity.length - 1; i++)
			{
					double aux = result[i];
					result[i] += weight_data * (velocity[i] - result[i]) + weight_smooth * (result[i-1] + result[i+1] - (2 * result[i]));
					change += Math.abs(aux - result[i]);
			}
		}
		return result;
	}
	double[] optimizeVelocity(double[] velocity, double[] rampedVelocity)
	{
		/*since ramping ends up lowering the arclength of the profile, we need to
		 * optimize the velocity so that the ramping still applies but we also
		 * get to our setpoint
		 */
		System.out.println("--- Begin Optimizing Velocity! ---");
		double[] result = rampedVelocity;
		double[] positionOrig = arcLength(velocity);
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

	public double[] arcLength(double[] velocity) //calculate number of rotations for encoder
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

	public double[][] compileProfile(double[] distanceInput, double[] velocityInput, String filename)
	{

		System.out.println("--- START OF PROFILE ---");
		double[][] generatedProfile = new double[numOfPoints][3];
		try
		{
			writer = new FileWriter("/home/lvuser/" + filename + ".csv");
		}
		catch(IOException e)
		{

		}
		for (int i = 0; i < numOfPoints; i++)
		{
			generatedProfile[i][0] = distanceInput[i];
			generatedProfile[i][1] = velocityInput[i];
			generatedProfile[i][2] = duration * 1000;
			System.out.println("{" + generatedProfile[i][0] + ", " + generatedProfile[i][1] + ", " + generatedProfile[i][2] + "}");

			try
			{
				writer.append("" + generatedProfile[i][0]);
				writer.append(',');
				writer.append("" + generatedProfile[i][1]);
				writer.append(',');
				writer.append("" + generatedProfile[i][2]);
				writer.append('\n');
			}
			catch (IOException e)
			{

			}
		}
		try
		{
			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{

		}
		System.out.println("--- END OF PROFILE ---");
		return generatedProfile;
	}
	public double[][] compileTestProfile(double[] distanceInput, double[] velocityInput)
	{

		System.out.println("--- START OF PROFILE ---");
		double[][] generatedProfile = new double[numOfPoints][3];
		for (int i = 0; i < numOfPoints; i++)
		{
			generatedProfile[i][0] = distanceInput[i];
			generatedProfile[i][1] = velocityInput[i];
			generatedProfile[i][2] = duration * 1000;
			System.out.println("{" + generatedProfile[i][0] + ", " + generatedProfile[i][1] + ", " + generatedProfile[i][2] + "}");
		}
		System.out.println("--- END OF PROFILE ---");
		return generatedProfile;
	}
	public double[][] generateProfileLeft()
	{
		System.out.println("--- LEFT PROFILE ---");
		double[][] outputLeft;
		double[] rampedVelocityLeft;
		double[] rotLeft;
		fillHermite();
		fillPosition();
		fillPosition(); //stupid
		velocityLeft = fillVelocity(positionLeft);
		rampedVelocityLeft = velocityLeft;
//		rampedVelocityLeft = optimizeVelocity(velocityLeft, applyRamping(velocityLeft));
//	     rampedVelocityLeft = applyRamping(velocityLeft);
		rotLeft = arcLength(rampedVelocityLeft);
		outputLeft = compileTestProfile(rotLeft, rampedVelocityLeft);
		System.out.println("--- LEFT PROFILE END ---");
		return outputLeft;
	}

	public double[][] generateProfileRight()
	{
		System.out.println("--- RIGHT PROFILE ---");
		double[][] outputRight;
		double[] rampedVelocityRight;
		double[] rotRight;
		fillHermite();
		fillPosition();
		fillPosition();
		velocityRight = fillVelocity(positionRight);
		rampedVelocityRight = velocityRight;
//		rampedVelocityRight = optimizeVelocity(velocityRight, applyRamping(velocityRight));
//	     rampedVelocityRight = applyRamping(velocityRight);
		rotRight = arcLength(rampedVelocityRight);
		outputRight = compileTestProfile(rotRight, rampedVelocityRight);
		System.out.println("--- RIGHT PROFILE END ---");
		return outputRight;
	}

	public void readProfileFromCSV()
	{
		String pathLeft = "/home/lvuser/" + name + "Left.csv";
		String pathRight = "/home/lvuser/" + name + "Right.csv";
		csvFileLeft = new File(pathLeft);
		csvFileRight = new File(pathRight);
		String line = "";
		if (csvFileLeft.exists() && csvFileRight.exists())
		{
			//read the file
			System.out.println("Files found, read file.");
			generatedProfileLeft = new double[numOfPoints][3];
			generatedProfileRight = new double[numOfPoints][3];
			try(BufferedReader br = new BufferedReader(new FileReader(csvFileLeft)))
			{
				int i = 0;
				while((line = br.readLine()) != null)
				{
					String[] values = line.split(",");
					generatedProfileLeft[i][0] = Double.parseDouble(values[0]);
					generatedProfileLeft[i][1] = Double.parseDouble(values[1]);
					generatedProfileLeft[i][2] = Double.parseDouble(values[2]);
					i++;
				}
			}
			catch (IOException e)
			{

			}
			try(BufferedReader br = new BufferedReader(new FileReader(csvFileRight)))
			{
				int i = 0;
				while((line = br.readLine()) != null)
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

			}
			csvFileLeft.delete();
			csvFileRight.delete();
		}
		else //if a file doesn't exist
		{
			try
			{
				csvFileLeft.delete(); //delete the other file
				csvFileRight.delete();
				csvFileLeft.createNewFile(); //create new files
				csvFileRight.createNewFile();
			}
			catch (IOException e)
			{

			}
			if (isAppended)
			{
				System.out.println("Spline is appended!");
				compileAppendedProfile();
			}
			else
			{
				generatedProfileLeft = generateProfileLeft();
				generatedProfileRight = generateProfileRight();
			}
		}
	}
	public static void main(String [] args)
	{
		MotionProfileCurve curve = new MotionProfileCurve(30, 30, 5, 1, 1);
//		MotionProfileCurve curve = MotionProfileCurve.appendProfiles(
//				MotionProfileCurve.appendProfiles(
//				new MotionProfileCurve(30, 30, 5, 5),
//				new MotionProfileCurve(30, 30, 5 ,5)
//		),
//				MotionProfileCurve.appendProfiles(
//						new MotionProfileCurve(30, 30, 5, 5),
//						new MotionProfileCurve(30, 30, 5 ,5)
//				))
				;
		curve.generateProfileLeft();
		curve.generateProfileRight();
	}
}