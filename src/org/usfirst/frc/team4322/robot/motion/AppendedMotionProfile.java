package org.usfirst.frc.team4322.robot.motion;

public class AppendedMotionProfile extends MotionProfileCurve
{
	MotionProfileCurve[] curves;
	public AppendedMotionProfile(MotionProfileCurve[] curves)
	{
		this.curves = curves;
		numOfPoints = 0;
		for (MotionProfileCurve curve:
			 curves)
		{
			numOfPoints+=curve.numOfPoints;
		}
	}
	@Override
	protected void calculateStuff()
	{
		velocityLeft = new double[numOfPoints];
		velocityRight = new double[numOfPoints];

		for(int i = 0; i < curves.length; i++)
		{
			MotionProfileCurve curve = curves[i];

			curve.position(curve.positionLeft, curve.positionRight);

			curve.velocityLeft = curve.velocity(curve.positionLeft);
			curve.velocityRight = curve.velocity(curve.positionRight);

			if (i == 0)
			{
				for (int j = 0; j < curve.numOfPoints; j++)
				{
					velocityLeft[j] = curve.velocityLeft[j];
					velocityRight[j] = curve.velocityRight[j];
				}
			}
			else
			{
				for (int j = curves[i-1].numOfPoints, n = 0; j < (curves[i-1].numOfPoints + curve.numOfPoints); j++, n++)
				{
					velocityLeft[j] = curve.velocityLeft[n];
					velocityRight[j] = curve.velocityRight[n];
				}
			}
		}

		rampedVelocityLeft = applyRamping(velocityLeft);
		rampedVelocityRight = applyRamping(velocityRight);

		rampedVelocityLeft = optimizeVelocity(velocityLeft, rampedVelocityLeft);
		rampedVelocityRight = optimizeVelocity(velocityRight, rampedVelocityRight);

		rotationsLeft = arcLength(rampedVelocityLeft);
		rotationsRight = arcLength(rampedVelocityRight);
	}
}
