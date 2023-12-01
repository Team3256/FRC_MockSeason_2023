package frc.robot.subsystems.swerve;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;

import static frc.robot.subsystems.swerve.swerveConstants.*;

public class SwerveModule {
    public int moduleNumber;
    private TalonFX mAngleMotor;
    private TalonFX mDriveMotor;
    private CANCoder angleEncoder;
    private Rotation2d angleOffset;
    private Rotation2d lastAngle;

    private CTREConfigs ctreConfigs = new CTREConfigs();
    public SwerveModule(int moduleNumber, SwerveModuleConstants moduleConstants) {

    }
    public void set(SwerveModule swerveModule, SwerveModuleState SMS) {
        SwerveModuleState
        SwerveModuleState swerveModule =
    }
    ublic void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop) {
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
            mDriveMotor.set(ControlMode.PercentOutput, percentOutput);
        } else {
            double velocity =
                    Conversions.MPSToFalcon(
                            desiredState.speedMetersPerSecond, kWheelCircumference, kDriveGearRatio);
            mDriveMotor.set(
                    ControlMode.Velocity,
                    velocity,
                    DemandType.ArbitraryFeedForward,
                    feedforward.calculate(desiredState.speedMetersPerSecond));
        }
    }

    private void setAngle(SwerveModuleState desiredState) {
        Rotation2d angle =
                (Math.abs(desiredState.speedMetersPerSecond) <= (kMaxSpeed * 0.01))
                        ? lastAngle
                        : desiredState
                        .angle; // Prevent rotating module if speed is less than 1%. Prevents Jittering.

        mAngleMotor.set(
                ControlMode.Position, Conversions.degreesToFalcon(angle.getDegrees(), kAngleGearRatio));
        lastAngle = angle;
    }
}
