// Copyright (c) 2023 FRC 3256
// https://github.com/Team3256
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.swerve.helpers;

import static frc.robot.Constants.ShuffleboardConstants.kElectricalTabName;
import static frc.robot.swerve.helpers.SwerveModuleConstants.*;
import static frc.robot.swerve.SwerveConstants.*;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.CANcoder;

/*
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.WPI_CANCoder;
*/

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.drivers.CANDeviceTester;
import frc.robot.logging.Loggable;

public class SwerveModule implements Loggable {
  public int moduleNumber;
  private Rotation2d angleOffset;
  private Rotation2d lastAngle;
  private CANcoder angleEncoder;
  private TalonFX mAngleMotor;
  private TalonFX mDriveMotor;

  private CTREConfigs ctreConfigs = new CTREConfigs();

  SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(kDriveKS, kDriveKV, kDriveKA);

  public SwerveModule(int moduleNumber, SwerveModuleConstants moduleConstants) {
    this.moduleNumber = moduleNumber;
    this.angleOffset = moduleConstants.angleOffset;

    /* Angle Encoder Config */
    angleEncoder = new CANcoder(moduleConstants.cancoderID);
    configAngleEncoder();

    /* Angle Motor Config */
    mAngleMotor = new TalonFX(moduleConstants.angleMotorID);
    configAngleMotor();

    /* Drive Motor Config */
    mDriveMotor = new TalonFX(moduleConstants.driveMotorID);
    configDriveMotor();

    lastAngle = getState().angle;
  }

  public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop) {
    /*
     * This is a custom optimize function, since default WPILib optimize assumes
     * continuous controller which CTRE and Rev onboard is not
     */
    desiredState = CTREModuleState.optimize(desiredState, getState().angle);
    setAngle(desiredState);
    setSpeed(desiredState, isOpenLoop);
  }

  private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop) {
    if (isOpenLoop) {
      double percentOutput = desiredState.speedMetersPerSecond / kMaxSpeed;
      mDriveMotor.set(percentOutput);
    } else {
      double velocity =
              Conversions.MPSToFalcon(
                      desiredState.speedMetersPerSecond, kWheelCircumference, kDriveGearRatio);
      mDriveMotor.set(velocity);
    }
  }

  private void setAngle(SwerveModuleState desiredState) {
    Rotation2d angle =
            (Math.abs(desiredState.speedMetersPerSecond) <= (kMaxSpeed * 0.01))
                    ? lastAngle
                    : desiredState
                    .angle; // Prevent rotating module if speed is less than 1%. Prevents Jittering.
    mAngleMotor.set(Conversions.degreesToFalcon(angle.getDegrees(), kAngleGearRatio));
    lastAngle = angle;
  }

  private Rotation2d getAngle() {
    return Rotation2d.fromDegrees(
            Conversions.falconToDegrees(mAngleMotor.getPosition().getValue(), kAngleGearRatio));
  }

  public Rotation2d getCanCoder() {
    return Rotation2d.fromDegrees(
            angleEncoder.getAbsolutePosition().getValue());
  }

  public void resetToAbsolute() {
    double absolutePosition =
            Conversions.degreesToFalcon(
                    getCanCoder().getDegrees() - angleOffset.getDegrees(), kAngleGearRatio);
    mAngleMotor.setRotorPosition(absolutePosition);
  }

  private void configAngleEncoder() {
    angleEncoder.getConfigurator().apply(
            ctreConfigs.swerveCanCoderConfig);
  }

  private void configAngleMotor() {
    mAngleMotor.configFactoryDefault();
    mAngleMotor.configAllSettings(ctreConfigs.swerveAngleFXConfig);
    mAngleMotor.setInverted(kAngleMotorInvert);
    mAngleMotor.setNeutralMode(kAngleNeutralMode);
    resetToAbsolute();

    mAngleMotor.getConfigurator().apply(
            ctreConfigs.swerveAngleFXConfig);
  }

  private void configDriveMotor() {
    mDriveMotor.configFactoryDefault();
    mDriveMotor.configAllSettings(ctreConfigs.swerveDriveFXConfig);
    mDriveMotor.setInverted(kDriveMotorInvert);
    mDriveMotor.setNeutralMode(kDriveNeutralMode);
    mDriveMotor.setSelectedSensorPosition(0);
  }

  public SwerveModuleState getState() {
    return new SwerveModuleState(
            Conversions.falconToMPS(
                    mDriveMotor.getSelectedSensorVelocity(), kWheelCircumference, kDriveGearRatio),
            getAngle());
  }

  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(
            Conversions.falconToMeters(
                    mDriveMotor.getSelectedSensorPosition(), kWheelCircumference, kDriveGearRatio),
            getAngle());
  }

  public void setDesiredAngleState(SwerveModuleState desiredState) {
    desiredState = CTREModuleState.optimize(desiredState, getPosition().angle);

    Rotation2d angle =
            (Math.abs(desiredState.speedMetersPerSecond) <= (kMaxSpeed * 0.01))
                    ? lastAngle
                    : desiredState.angle;

    mAngleMotor.set(
            ControlMode.Position, Conversions.degreesToFalcon(angle.getDegrees(), kAngleGearRatio));
    lastAngle = angle;
  }

  public void setDriveMotorNeutralMode(NeutralMode neutralMode) {
    mDriveMotor.setNeutralMode(neutralMode);
  }

  public void setAngleMotorNeutralMode(NeutralMode neutralMode) {
    mAngleMotor.setNeutralMode(neutralMode);
  }

  public WPI_TalonFX getAngleMotor() {
    return mAngleMotor;
  }

  public WPI_TalonFX getDriveMotor() {
    return mDriveMotor;
  }

  public WPI_CANCoder getAngleEncoder() {
    return angleEncoder;
  }

  @Override
  public void logInit() {
    getLayout(kElectricalTabName).add("Turn Motor Bus Voltage", getAngleMotor().getBusVoltage());
    getLayout(kElectricalTabName)
            .add("Turn Motor Output Voltage", getAngleMotor().getMotorOutputVoltage());
    getLayout(kElectricalTabName).add("Drive Motor Bus Voltage", getDriveMotor().getBusVoltage());
    getLayout(kElectricalTabName)
            .add("Drive Motor Output Voltage", getDriveMotor().getMotorOutputVoltage());
  }

  @Override
  public ShuffleboardLayout getLayout(String tabName) {
    return Shuffleboard.getTab(tabName)
            .getLayout("Mod " + moduleNumber, BuiltInLayouts.kList)
            .withSize(2, 1);
  }

  public boolean test() {
    System.out.println("Testing swerve module CAN:");
    boolean result =
            CANDeviceTester.testTalonFX(mDriveMotor)
                    && CANDeviceTester.testTalonFX(mAngleMotor)
                    && CANDeviceTester.testCANCoder(angleEncoder);

    System.out.println("Swerve module CAN connected: " + result);
    SmartDashboard.putBoolean("Swerve module CAN connected", result);
    return result;
  }
}