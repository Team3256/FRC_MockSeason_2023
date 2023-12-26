// Copyright (c) 2023 FRC 3256
// https://github.com/Team3256
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.swerve.helpers;

import static frc.robot.swerve.SwerveConstants.*;

import com.ctre.phoenix6.configs.*;
import frc.robot.swerve.SwerveConstants;

public final class CTREConfigs {
  public TalonFXConfiguration swerveAngleFXConfig;
  public TalonFXConfiguration swerveDriveFXConfig;
  public CANcoderConfiguration swerveCanCoderConfig;

  public CTREConfigs() {
    swerveAngleFXConfig = new TalonFXConfiguration();
    swerveDriveFXConfig = new TalonFXConfiguration();
    swerveCanCoderConfig = new CANcoderConfiguration();

    /* Swerve Angle Motor Configurations */
    MotorOutputConfigs angleMotorOutput =
            swerveAngleFXConfig.MotorOutput;
    //angleMotorOutput.Inverted = SwerveConstants...
    //TODO: add constant that inverts motion of the angle motor
    //angleMotorOutput.Neutral = SwerveConstants...

    /* Current Limiting */
    CurrentLimitsConfigs angleCurrentLimitConfig =
            swerveAngleFXConfig.CurrentLimits;
    angleCurrentLimitConfig.SupplyCurrentLimitEnable =
            kAngleEnableCurrentLimit;
    angleCurrentLimitConfig.SupplyCurrentLimit =
            kAngleContinuousCurrentLimit;
    angleCurrentLimitConfig.SupplyCurrentThreshold =
            kAnglePeakCurrentLimit;
    angleCurrentLimitConfig.SupplyTimeThreshold =
            kAnglePeakCurrentDuration;

    /* PID Configs */
    Slot0Configs angleSlot0 = swerveAngleFXConfig.Slot0;
    angleSlot0.kP = kAngleKP;
    angleSlot0.kI = kAngleKI;
    angleSlot0.kD = kAngleKD;
    //TODO: implement feed forward
    // angleSlot0.kF = kAngleKF;

    //swerveAngleFXConfig.supplyCurrLimit = angleSupplyLimit;

    /* Swerve Drive Motor Configuration */
    var driveMotorOutput = swerveDriveFXConfig.MotorOutput;
    //TODO: implement invert and neutral,
    // refer to angleMotorOutput above

    /* Current Limiting */
    var driveCurrentLimits = swerveDriveFXConfig.CurrentLimits;
    driveCurrentLimits.SupplyCurrentLimitEnable =
            kDriveEnableCurrentLimit;
    driveCurrentLimits.SupplyCurrentLimit =
            kDriveContinuousCurrentLimit;
    driveCurrentLimits.SupplyCurrentThreshold =
            kDrivePeakCurrentLimit;
    driveCurrentLimits.SupplyTimeThreshold =
            kDrivePeakCurrentDuration;

    /* PID Config */
    Slot0Configs driveSlot0 = swerveDriveFXConfig.Slot0;
    driveSlot0.kP = kDriveKP;
    driveSlot0.kI = kDriveKI;
    driveSlot0.kD = kDriveKD;

    /*TODO: implement feed forward constant
    * driveSlot0.kF = kDriveKF;
    * */

    //swerveDriveFXConfig.supplyCurrLimit = driveSupplyLimit;

    swerveDriveFXConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod =
            kOpenLoopRamp;
    swerveDriveFXConfig.ClosedLoopRamps.DutyCycleClosedLoopRampPeriod =
            kClosedLoopRamp;

    /*TODO: implement voltage open/closed ramp period
    * swerveDriveFXConfig.VoltageOpenLoopRampPeriod = ?? constant
    * */

//
//    /* Swerve CANCoder Configuration */
//    swerveCanCoderConfig.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
//    swerveCanCoderConfig.sensorDirection = kCanCoderInvert;
//    swerveCanCoderConfig.initializationStrategy =
//            SensorInitializationStrategy.BootToAbsolutePosition;
//    swerveCanCoderConfig.sensorTimeBase = SensorTimeBase.PerSecond;

    //TODO: fix encoder inverse direction code
    swerveCanCoderConfig.MagnetSensor.SensorDirection = SwerveConstants.kCanCoderInvert;
  }
}