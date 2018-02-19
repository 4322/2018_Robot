package org.usfirst.frc.team4322.robot;

import org.usfirst.frc.team4322.robot.commands.AutoGroup_ScaleLeft_Left;
import org.usfirst.frc.team4322.robot.commands.AutoGroup_ScaleLeft_Right;
import org.usfirst.frc.team4322.robot.commands.AutoGroup_ScaleRight_Left;
import org.usfirst.frc.team4322.robot.commands.AutoGroup_ScaleRight_Right;
import org.usfirst.frc.team4322.robot.commands.AutoGroup_SwitchCenter_Left;
import org.usfirst.frc.team4322.robot.commands.AutoGroup_SwitchCenter_Right;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

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
				auto = new AutoGroup_ScaleLeft_Right();
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
				auto = new AutoGroup_ScaleRight_Left();
			}
			else
			{
				auto = new AutoGroup_ScaleRight_Right();
			}
		}
	}
	public Command getAuto()
	{
		return auto;
	}
}
