package org.usfirst.frc.team4322.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4322.robot.commands.DriveBase_Rotate;
import org.usfirst.frc.team4322.robot.commands.auto.*;

public class SmartAuto 
{
	String gameData;
	private DigitalInput d0, d1, d2, d3;
	int position = 1; //0 = left, 1 = center, 2 = right
	private Command auto;
	public SmartAuto()
	{
		d0 = new DigitalInput(RobotMap.AUTO_DIO_PORT_0);
		d1 = new DigitalInput(RobotMap.AUTO_DIO_PORT_1);
		d2 = new DigitalInput(RobotMap.AUTO_DIO_PORT_2);
		d3 = new DigitalInput(RobotMap.AUTO_DIO_PORT_3);
		position = (d0.get() ? 1 : 0) + (d1.get() ? 2: 0) + (d2.get() ? 4 : 0) + (d3.get() ? 8 : 0);
		
	}
	public void choose()
	{
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		if (position == 0) // if left
		{
			if (gameData.charAt(1) == 'L')
			{
				auto = new AutoGroup_ScaleLeft_Left();
			}
			else
			{
				auto = new AutoGroup_ReachBaseline();
			}
		}
		else if (position == 1) // if center
		{
			if (gameData.charAt(0) == 'L')
			{
				auto = new AutoGroup_SwitchCenter_Left();
			}
			else
			{
				auto = new AutoGroup_SwitchCenter_Right();
			}
		}
		else if (position == 2) //if right
		{
			if (gameData.charAt(1) == 'L')
			{
				auto = new AutoGroup_ReachBaseline();
			}
			else
			{
				auto = new AutoGroup_ScaleRight_Right();
			}
		}
		else if (position == 3)
		{
			auto = new AutoGroup_ReachBaseline();
		}
		else if (position == 4)
		{
			auto = new AutoGroup_DoNothing();
		}
		else if (position == 5)
		{
			auto = new DriveBase_Rotate(90);
		}
	}
	public Command getAuto()
	{
		return auto;
	}
	public int getPosition()
	{
		position = (d0.get() ? 1 : 0) + (d1.get() ? 2: 0) + (d2.get() ? 4 : 0) + (d3.get() ? 8 : 0);

		return position;
	}
}
