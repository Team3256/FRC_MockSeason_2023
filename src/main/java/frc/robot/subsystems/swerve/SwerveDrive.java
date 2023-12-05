// Copyright (c) 2023 FRC 3256
// https://github.com/Team3256
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.subsystems.swerve;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class SwerveDrive {
  SwerveModule[] mSwerveMods =
      new SwerveModule[] {
        new SwerveModule(0, swerveConstants.FrontLeft.constants),
        new SwerveModule(1, swerveConstants.FrontRight.constants),
        new SwerveModule(2, swerveConstants.BackLeft.constants),
        new SwerveModule(3, swerveConstants.BackRight.constants)
      };

  Translation2d kfrontLeftModuleLocation = new Translation2d(1, 1);

  Translation2d kfrontRightModuleLocation = new Translation2d(1, -1);
  Translation2d kbackLeftModuleLocation = new Translation2d(-1, 1);
  Translation2d kbackRightModuleLocation = new Translation2d(-1, -1);

  public SwerveModuleState[] drive(
      double vx, double vy, double angularVelocity, boolean isOpenLoop) {
    ChassisSpeeds speeds = new ChassisSpeeds(vx, vy, angularVelocity);
    SwerveDriveKinematics kinematics =
        new SwerveDriveKinematics(
            kfrontLeftModuleLocation,
            kfrontRightModuleLocation,
            kbackLeftModuleLocation,
            kbackRightModuleLocation);
    SwerveModuleState[] moduleStates = kinematics.toSwerveModuleStates(speeds);
    //
    //

    for (SwerveModule mod : mSwerveMods) {
      mod.setDesiredState(moduleStates[mod.moduleNumber], isOpenLoop);
    }

    return moduleStates;
  }
}
