// Copyright (c) 2023 FRC 3256
// https://github.com/Team3256
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.subsystems.swerve;

import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.lib.math.Conversions;
import frc.lib.util.CTREModuleState;
import frc.robot.subsystems.swerve.helpers.CTREConfigs;

public class SwerveModule {
  public int moduleNumber;
  private Rotation2d angleOffset;
  private Rotation2d lastAngle;

  private TalonFX mAngleMotor;
  private TalonFX mDriveMotor;
  private CANcoder angleEncoder;

  private final SimpleMotorFeedforward driveFeedForward =
      new SimpleMotorFeedforward(
          swerveConstants.kDriveKS, swerveConstants.kDriveKV, swerveConstants.kDriveKA);

  /* drive motor control requests */
  private final DutyCycleOut driveDutyCycle = new DutyCycleOut(0);
  private final VelocityVoltage driveVelocity = new VelocityVoltage(0);

  /* angle motor control requests */
  private final PositionVoltage anglePosition = new PositionVoltage(0);

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
    /* This is a custom optimize function, since default WPILib optimize assumes continuous controller which CTRE and Rev onboard is not */
    desiredState = CTREModuleState.optimize(desiredState, getState().angle);
    setAngle(desiredState);
    setSpeed(desiredState, isOpenLoop);
  }

  private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop) {
    if (isOpenLoop) {
      driveDutyCycle.Output = desiredState.speedMetersPerSecond / swerveConstants.kMaxSpeed;
      mDriveMotor.setControl(driveDutyCycle);
    } else {
      driveVelocity.Velocity =
          Conversions.MPSToTalon(
              desiredState.speedMetersPerSecond,
              swerveConstants.kWheelCircumference,
              swerveConstants.kDriveGearRatio);
      driveVelocity.FeedForward = driveFeedForward.calculate(desiredState.speedMetersPerSecond);
      mDriveMotor.setControl(driveVelocity);
    }
  }

  private void setAngle(SwerveModuleState desiredState) {
    Rotation2d angle =
        (Math.abs(desiredState.speedMetersPerSecond) <= (swerveConstants.kMaxSpeed * 0.01))
            ? lastAngle
            : desiredState
                .angle; // Prevent rotating module if speed is less then 1%. Prevents Jittering.

    anglePosition.Position =
        Conversions.degreesToTalon(angle.getDegrees(), swerveConstants.kAngleGearRatio);
    mAngleMotor.setControl(anglePosition);
    lastAngle = angle;
  }

  private Rotation2d getAngle() {
    return Rotation2d.fromDegrees(
        Conversions.talonToDegrees(
            mAngleMotor.getPosition().getValue(), swerveConstants.kAngleGearRatio));
  }

  public Rotation2d getCANcoder() {
    return Rotation2d.fromRotations(angleEncoder.getAbsolutePosition().getValue());
  }

  private Rotation2d waitForCANcoder() {
    /* wait for up to 250ms for a new CANcoder position */
    return Rotation2d.fromRotations(
        angleEncoder.getAbsolutePosition().waitForUpdate(250).getValue());
  }

  public void resetToAbsolute() {
    double absolutePosition =
        Conversions.degreesToTalon(
            waitForCANcoder().getDegrees() - angleOffset.getDegrees(),
            swerveConstants.kAngleGearRatio);
    mAngleMotor.setRotorPosition(absolutePosition);
  }

  private void configAngleEncoder() {
    angleEncoder.getConfigurator().apply(CTREConfigs.swerveCANcoderConfig);
  }

  private void configAngleMotor() {
    mAngleMotor.getConfigurator().apply(CTREConfigs.swerveAngleFXConfig);
    resetToAbsolute();
  }

  private void configDriveMotor() {
    mDriveMotor.getConfigurator().apply(CTREConfigs.swerveDriveFXConfig);
    mDriveMotor.getConfigurator().setRotorPosition(0);
  }

  public SwerveModuleState getState() {
    return new SwerveModuleState(
        Conversions.talonToMPS(
            mDriveMotor.getVelocity().getValue(),
            swerveConstants.kWheelCircumference,
            swerveConstants.kDriveGearRatio),
        getAngle());
  }

  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(
        Conversions.talonToMeters(
            mDriveMotor.getPosition().getValue(),
            swerveConstants.kWheelCircumference,
            swerveConstants.kDriveGearRatio),
        getAngle());
  }
}
