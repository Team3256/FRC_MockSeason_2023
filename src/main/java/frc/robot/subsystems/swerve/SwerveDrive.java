package frc.robot.subsystems.swerve;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class SwerveDrive {
//    public SwerveModule frontLeftModule = new SwerveModule(0, FrontLeft.constants);
//    public SwerveModule frontRightModule = new SwerveModule(1, FrontRight.constants);
//    public SwerveModule backLeftModule = new SwerveModule(2, BackLeft.constants);
//    public SwerveModule backRightModule = new SwerveModule(3, BackRight.constants);

    Translation2d kfrontLeftModuleLocation = new Translation2d(1, 1);

    Translation2d kfrontRightModuleLocation = new Translation2d(1, -1);
    Translation2d kbackLeftModuleLocation = new Translation2d(-1, 1);
    Translation2d kbackRightModuleLocation = new Translation2d(-1, -1);

    public SwerveModuleState[] drive(double vx, double vy, double angularVelocity) {
        ChassisSpeeds speeds = new ChassisSpeeds(vx, vy, angularVelocity);
        SwerveDriveKinematics kinematics = new SwerveDriveKinematics(kfrontLeftModuleLocation, kfrontRightModuleLocation, kbackLeftModuleLocation, kbackRightModuleLocation)
        SwerveModuleState[] moduleStates = kinematics.toSwerveModuleStates(speeds);

        SwerveModuleState frontLeftModule = moduleStates[0];
        SwerveModuleState frontRightModule = moduleStates[1];
        SwerveModuleState backLeftModule = moduleStates[2];
        SwerveModuleState backRightModule = moduleStates[3];

        return moduleStates;
    }
}
