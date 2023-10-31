package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class LimelightInterface extends SubsystemBase{
    private final NetworkTable table;
    public LimelightInterface() {
        table = NetworkTableInstance.getDefault().getTable(Constants.limeLightTableName);
    }
    public double getDouble(String key) {
        return table.getEntry(key).getDouble(0);
    }
    public double[] getDoubleArray(String key) {
        return table.getEntry(key).getDoubleArray(new double[0]);
    }
    Pose2d getPose() {
        double[] pose  = getDoubleArray(Constants.limeLightCordEntryName);
        return new Pose2d(new Translation2d(pose[0],pose[1]),
                Rotation2d.fromDegrees(((pose[5] + 360) % 360)));
    }
    Field2d convertToField(Pose2d pose) {
        Field2d output = new Field2d();
        output.setRobotPose(pose);
        // My Rust experience tells
        // that something fishy might happen here
        // something related with potential
        // memory leaks. Or dangling pointers
        return output;
    }

    public void toSmartDashboard() {
        SmartDashboard.putData("pose",convertToField(getPose()));
    }

}
