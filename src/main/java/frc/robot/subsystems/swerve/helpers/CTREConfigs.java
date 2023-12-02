package frc.robot.subsystems.swerve.helpers;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import frc.robot.Constants;
import frc.robot.subsystems.swerve.swerveConstants;

import static frc.robot.subsystems.swerve.swerveConstants.*;

public final class CTREConfigs {
  public TalonFXConfiguration swerveAngleFXConfig = new TalonFXConfiguration();
  public TalonFXConfiguration swerveDriveFXConfig = new TalonFXConfiguration();
  public CANcoderConfiguration swerveCANcoderConfig = new CANcoderConfiguration();

  public CTREConfigs(){
    /** Swerve Angle Motor Configurations */
    /* Motor Inverts and Neutral Mode */
    MotorOutputConfigs angleMotorOutput = swerveAngleFXConfig.MotorOutput;
    angleMotorOutput.Inverted = kAngleMotorInvert;
    angleMotorOutput.NeutralMode = kAngleNeutralMode;

    /* Current Limiting */
    CurrentLimitsConfigs angleCurrentLimits = swerveAngleFXConfig.CurrentLimits;
    angleCurrentLimits.SupplyCurrentLimitEnable = kAngleEnableCurrentLimit;
    angleCurrentLimits.SupplyCurrentLimit = kAngleContinuousCurrentLimit;
    angleCurrentLimits.SupplyCurrentThreshold = kAnglePeakCurrentLimit;
    angleCurrentLimits.SupplyTimeThreshold = kAnglePeakCurrentDuration;

    /* PID Config */
    Slot0Configs angleSlot0 = swerveAngleFXConfig.Slot0;
    angleSlot0.kP = kAngleKP;
    angleSlot0.kI = kAngleKI;
    angleSlot0.kD = kAngleKD;

    /** Swerve Drive Motor Configuration */
    /* Motor Inverts and Neutral Mode */
    var driveMotorOutput = swerveDriveFXConfig.MotorOutput;
    driveMotorOutput.Inverted = kDriveMotorInvert;
    driveMotorOutput.NeutralMode = kDriveNeutralMode;

    /* Current Limiting */
    var driveCurrentLimits = swerveDriveFXConfig.CurrentLimits;
    driveCurrentLimits.SupplyCurrentLimitEnable = kDriveEnableCurrentLimit;
    driveCurrentLimits.SupplyCurrentLimit = kDriveContinuousCurrentLimit;
    driveCurrentLimits.SupplyCurrentThreshold = kDrivePeakCurrentLimit;
    driveCurrentLimits.SupplyTimeThreshold = kDrivePeakCurrentDuration;

    /* PID Config */
    var driveSlot0 = swerveDriveFXConfig.Slot0;
    driveSlot0.kP = 0;
    driveSlot0.kI = 0;
    driveSlot0.kD = 0;

    /* Open and Closed Loop Ramping */
    swerveDriveFXConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = kOpenLoopRamp;
    swerveDriveFXConfig.OpenLoopRamps.VoltageOpenLoopRampPeriod = kOpenLoopRamp;

    swerveDriveFXConfig.ClosedLoopRamps.DutyCycleClosedLoopRampPeriod = kClosedLoopRamp;
    swerveDriveFXConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod = kClosedLoopRamp;

    /** Swerve CANCoder Configuration */
    swerveCANcoderConfig.MagnetSensor.SensorDirection = kCanCoderInvert;
  }
}