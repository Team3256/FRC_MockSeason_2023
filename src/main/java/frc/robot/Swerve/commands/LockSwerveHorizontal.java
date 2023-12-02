// Copyright (c) 2023 FRC 3256
// https://github.com/Team3256
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.Swerve.commands;

import static frc.robot.Swerve.SwerveConstants.*;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.helpers.DebugCommandBase;
import frc.robot.Swerve.SwerveDrive;

public class LockSwerveHorizontal extends DebugCommandBase {
  private final SwerveDrive swerveSubsystem;

  public LockSwerveHorizontal(SwerveDrive swerveSubsystem) {
    this.swerveSubsystem = swerveSubsystem;
    addRequirements(swerveSubsystem);
  }

  @Override
  public void initialize() {
    super.initialize();
    double inwardAngle = Math.atan(kTrackWidth / kWheelBase);
    SwerveModuleState[] states = new SwerveModuleState[4];

    for (int mod = 0; mod < 4; mod++) {
      states[mod] =
          new SwerveModuleState(1, new Rotation2d(inwardAngle + klockAngleOffsetsHorizontal[mod]));
    }

    swerveSubsystem.setDesiredAngleState(states);
    swerveSubsystem.setDriveMotorsNeutralMode(NeutralMode.Brake);
    swerveSubsystem.setAngleMotorsNeutralMode(NeutralMode.Brake);
  }

  @Override
  public boolean isFinished() {
    return true;
  }
}
