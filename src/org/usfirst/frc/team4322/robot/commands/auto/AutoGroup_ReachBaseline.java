package org.usfirst.frc.team4322.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team4322.robot.commands.DriveBase_DriveDistance;


public class AutoGroup_ReachBaseline extends CommandGroup
{

	public AutoGroup_ReachBaseline()
	{
		addSequential(new DriveBase_DriveDistance(20, 900, 900));
	}
}
