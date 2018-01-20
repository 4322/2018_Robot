package org.usfirst.frc.team4322.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoGroup_DriveSquare extends CommandGroup 
{
	public AutoGroup_DriveSquare()
	{
		addSequential(new DriveBase_DriveDistance(36));
		addSequential(new DriveBase_Rotate(90));
		addSequential(new DriveBase_DriveDistance(36));
		addSequential(new DriveBase_Rotate(90));
		addSequential(new DriveBase_DriveDistance(36));
		addSequential(new DriveBase_Rotate(90));
		addSequential(new DriveBase_DriveDistance(36));
		addSequential(new DriveBase_Rotate(90));
	}

}
