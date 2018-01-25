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
	  public double maxTime = 5/3; //duration of profile
	  public static final double duration = .10; //duration of each point in seconds
	  public static final double wheelBaseWidth = 2.57; //feet
	  public int numOfPoints = (int) (maxTime / duration);
	    
	  public double theta1;
	  public double theta2;
	  public double distance;
	  
	  //numbers used for quintic hermite spline interpolation
	  private double quintic;
	  private double quartic;
	  private double cubic;
	  private double linear;
	  
	  public MotionProfileCurve(double theta1, double theta2, double distance, double maxTime)
	  {
	    this.theta1 = theta1;
	    this.theta2 = theta2;
	    this.distance = distance;
	    this.maxTime = maxTime;
	    targetVelocity = distance / maxTime;
	    numOfPoints = (int) (maxTime / duration);
	    
	    position = new double[numOfPoints][2];
	    positionLeft = new double[numOfPoints][2];
	    positionRight = new double[numOfPoints][2];
	    velocity = new double[numOfPoints];
	    velocityLeft = new double[numOfPoints];
	    velocityRight = new double[numOfPoints];
	    generatedProfile = new double[numOfPoints][3];
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
	  }
	  public void fillPosition()
	  {
	    timeConstant = 0;
	    System.out.println("--- Generating Position Values! ---");
	    for(int i = 0; i < numOfPoints; i++)
	    {
	      double grad;
	      
	      position[i][0] = targetVelocity * timeConstant;//x
	      position[i][1] = (quintic * Math.pow(targetVelocity * timeConstant, 5)) + 
	        (quartic * Math.pow(targetVelocity * timeConstant, 4)) +
	        (cubic * Math.pow(targetVelocity * timeConstant, 3)) + 
	        (linear * targetVelocity * timeConstant);//y
	      
	      System.out.println(timeConstant + " (" + position[i][0] + ", " + position[i][1] + ")");
	      if (i == numOfPoints-1)
	      {
	        grad = Math.atan2(position[i][1] - position[i-1][1], position[i][0] - position[i-1][0]);
	      }
	      else {
	        grad = Math.atan2(position[i+1][1] - position[i][1], position[i+1][0] - position[i][0]);
	      }
	      
	      
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
	  public void fillVelocity(double[][] positionLeft, double[][] positionRight, double[] velocityLeft, double[] velocityRight)
	  {
	    //velocity = derivative of position
	    double dxdtL;
	    double dydtL;
	    double dxdtR;
	    double dydtR;
	    System.out.println("--- Generating Velocity Values ---");
	    velocityLeft[0] = 0;
	    velocityRight[0] = 0;
	    for (int i = 1; i < numOfPoints; i++)
	    {
	      
	      dxdtL = (positionLeft[i][0] - positionLeft[i-1][0]) / duration; //run an approximation of dxdt and dydt
	      dydtL = (positionLeft[i][1] - positionLeft[i-1][1]) / duration;
	        
	      dxdtR = (positionRight[i][0] - positionRight[i-1][0]) / duration; //run an approximation of dxdt and dydt
	      dydtR = (positionRight[i][1] - positionRight[i-1][1]) / duration;
	      
	      velocityLeft[i] = Math.sqrt(Math.pow(dxdtL, 2) + Math.pow(dydtL, 2)) * 180 / Math.PI; //calculate magnitude of velocity, convert to rpm
	      velocityRight[i] = Math.sqrt(Math.pow(dxdtR, 2) + Math.pow(dydtR, 2)) * 180 / Math.PI;
	      System.out.println("Left: " + velocityLeft[i]);
	      System.out.println("Right: " + velocityRight[i]);
	    }
	    System.out.println("--- End of Velocity Values ---");
	  }
	  public double[] applyRamping(double[] velocity) 
	  {
	    //this method applies a smoothed trapezoidal profile to the velocity so it ramps up and down
	    double[] result = new double[numOfPoints];
	    double t = 0;
	    for(int i = 0; i < numOfPoints; i++)
	    {
	      result[i] = velocity[i] * ( (1 / (1 + Math.exp(-14.8 * 1 * (t + (.05 * 14.8) - 1))) - (1 / (1 + Math.exp(-14.8 * ( 1 * (t + 14.8 *.05 - 1) - (maxTime-.5))))))); 
	      //apply s-curve profile
	      
	      t+=duration;
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
	      for(int i = 1; i < numOfPoints; i++)
	      {
	        result[i] = result[i] - (correctionFactor * error / 50);
	      }
	      result = applyRamping(result);
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
	    for(int i = 1; i < numOfPoints; i++)
	    {
	        result[i] = ((velocity[i] * duration / 60) + result[i-1]); //numerical integration of velocity
	    }
	    return result;
	  }
	  public double[][] compileProfile(double[] distanceInput, double[] velocityInput)
	  {
	    System.out.println("--- START OF PROFILE ---");
	    double [][] generatedProfile = new double[numOfPoints][3];
	    for(int i = 0; i < numOfPoints; i++)
	    {
	      generatedProfile[i][0] = distanceInput[i];
	      generatedProfile[i][1] = velocityInput[i];
	      generatedProfile[i][2] = duration * 1000;
	      System.out.println("{" + generatedProfile[i][0] + ", " + generatedProfile[i][1] +  ", " + generatedProfile[i][2] + "}");
	    }
	    System.out.println("--- END OF PROFILE ---");
	    return generatedProfile;
	  }
	  public double[][] generateProfileLeft()
	  {
	    System.out.println("--- LEFT PROFILE ---");
	    double [][] outputLeft;
	    double[] rampedVelocityLeft;
	    double[] rotLeft;
	    fillHermite();
	    fillPosition();
	    fillPosition(); //stupid
	    fillVelocity(positionLeft, positionRight, velocityLeft, velocityRight);
	    rampedVelocityLeft = optimizeVelocity(velocityLeft, applyRamping(velocityLeft));
//	     rampedVelocityLeft = applyRamping(velocityLeft);
	    rotLeft = arcLength(rampedVelocityLeft);
	    outputLeft = compileProfile(rotLeft, rampedVelocityLeft);
	    System.out.println("--- LEFT PROFILE END ---");
	    return outputLeft;
	  }
	  public double[][] generateProfileRight()
	  {
	    System.out.println("--- RIGHT PROFILE ---");
	    double [][] outputRight;
	    double[] rampedVelocityRight;
	    double[] rotRight;
	    fillHermite();
	    fillPosition();
	    fillVelocity(positionLeft, positionRight, velocityLeft, velocityRight);
	    rampedVelocityRight = optimizeVelocity(velocityRight, applyRamping(velocityRight));
//	     rampedVelocityRight = applyRamping(velocityRight);
	    rotRight = arcLength(rampedVelocityRight);
	    outputRight = compileProfile(rotRight, rampedVelocityRight);
	    System.out.println("--- RIGHT PROFILE END ---");
	    return outputRight;
	  }
}