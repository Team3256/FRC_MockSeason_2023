// Copyright (c) 2023 FRC 3256
// https://github.com/Team3256
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.swerve.helpers;

import static frc.robot.swerve.SwerveConstants.*;

import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
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
    angleMotorOutput.Inverted = kAngleMotorInvert;
    angleMotorOutput.NeutralMode = kAngleNeutralMode;

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
    //angleSlot0.kF = kAngleKF;

    //swerveAngleFXConfig.supplyCurrLimit = angleSupplyLimit;

    /* Swerve Drive Motor Configuration */
    var driveMotorOutput = swerveDriveFXConfig.MotorOutput;
    driveMotorOutput.Inverted = kDriveMotorInvert;
    driveMotorOutput.NeutralMode = kDriveNeutralMode;

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
    //driveSlot0.kF = kDriveKF;

    //swerveDriveFXConfig.supplyCurrentLimit = driveSupplyLimit;

    swerveDriveFXConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod =
            kOpenLoopRamp;
    swerveDriveFXConfig.ClosedLoopRamps.DutyCycleClosedLoopRampPeriod =
            kClosedLoopRamp;

    swerveDriveFXConfig.OpenLoopRamps.VoltageOpenLoopRampPeriod =
            kOpenLoopRamp;
    swerveDriveFXConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod =
            kClosedLoopRamp;

    /* Swerve CANCoder Configuration */

//    swerveCanCoderConfig.initializationStrategy =
//            SensorInitializationStrategy.BootToAbsolutePosition;
//    swerveCanCoderConfig.sensorTimeBase = SensorTimeBase.PerSecond;

    swerveCanCoderConfig.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
    swerveCanCoderConfig.MagnetSensor.SensorDirection = kCanCoderInvert;
  }
}