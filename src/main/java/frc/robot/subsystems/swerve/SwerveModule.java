// Copyright (c) 2023 FRC 3256
// https://github.com/Team3256
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.subsystems.swerve;

import static frc.robot.Constants.ShuffleboardConstants.kElectricalTabName;
import static frc.robot.subsystems.swerve.swerveConstants.*;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionDutyCycle;

import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.CANcoder;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.drivers.CANDeviceTester;
import frc.robot.logging.Loggable;
import frc.robot.subsystems.swerve.helpers.CTREConfigs;
import frc.robot.subsystems.swerve.helpers.CTREModuleState;
import frc.robot.subsystems.swerve.helpers.Conversions;

public class SwerveModule implements Loggable {
    public int moduleNumber;
    private TalonFX mAngleMotor;
    private TalonFX mDriveMotor;
    private CANcoder angleEncoder;
    private Rotation2d angleOffset;
    private Rotation2d lastAngle;

    private TalonFXConfiguration TalonFXConfigs = new TalonFXConfiguration();

    SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(kDriveKS, kDriveKV, kDriveKA);

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

    private Rotation2d getAngle() {
        return Rotation2d.fromDegrees(
                Conversions.falconToDegrees(mAngleMotor.getSelectedSensorPosition(), kAngleGearRatio));
    }

    public Rotation2d getCanCoder() {
        return Rotation2d.fromDegrees(angleEncoder.getAbsolutePosition());
    }

    public void resetToAbsolute() {
        double absolutePosition =
                Conversions.degreesToFalcon(
                        getCanCoder().getDegrees() - angleOffset.getDegrees(), kAngleGearRatio);
        mAngleMotor.setSelectedSensorPosition(absolutePosition);
    }

    private void configAngleEncoder() {
        angleEncoder.configFactoryDefault();
        angleEncoder.configAllSettings(ctreConfigs.swerveCanCoderConfig);
    }

    private void configAngleMotor() {
        mAngleMotor.configFactoryDefault();
        mAngleMotor.configAllSettings(CTREConfigs.swerveAngleFXConfig);
        mAngleMotor.setInverted(kAngleMotorInvert);
        mAngleMotor.setNeutralMode(kAngleNeutralMode);
        resetToAbsolute();
    }

    private void configDriveMotor() {
        mDriveMotor.getConfigurator().apply(TalonFXConfigs);
        mDriveMotor.setInverted(kDriveMotorInvert);

//    mDriveMotor.configFactoryDefault();
//    mDriveMotor.configAllSettings(ctreConfigs.swerveDriveFXConfig);
//    mDriveMotor.setInverted(kDriveMotorInvert);
//    mDriveMotor.setNeutralMode(kDriveNeutralMode);
//    mDriveMotor.setSelectedSensorPosition(0);
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

    public void setDriveMotorNeutralMode(NeutralModeValue neutralMode) {
        mDriveMotor.setNeutralMode(neutralMode);
    }

    public void setAngleMotorNeutralMode(NeutralModeValue neutralMode) {
        mAngleMotor.setNeutralMode(neutralMode);
    }

    public CANcoder getAngleMotor() {
        return mAngleMotor;
    }

    public CANcoder getDriveMotor() {
        return mDriveMotor;
    }

    public CANcoder getAngleEncoder() {
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