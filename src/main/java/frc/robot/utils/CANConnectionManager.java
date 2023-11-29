package frc.robot.utils;

import java.util.ArrayList; // import the ArrayList class

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.PowerDistribution;

public class CANConnectionManager {
    private ArrayList<CANTestable> devices = new ArrayList<CANTestable>();

    public CANConnectionManager(ArrayList<CANTestable> initialDevices) {
        devices = initialDevices;

    }

    public CANConnectionManager() {
    }

    public void add(CANTestable device) {
        devices.add(device);
    }

    // Output CAN BE NULL!!
    // Should I use Optional instead?
    public CANTestable getBrokenDevice() {
        for (CANTestable device : devices) {
            if (device instanceof PowerDistribution) {
                if (!CANDeviceTester.testPDP((PowerDistribution) device)) {
                    return device;
                }
            } else if (device instanceof TalonFX) {
                if (!CANDeviceTester.testTalonFX((TalonFX) device)) {
                    return device;
                }
            } else if (device instanceof Pigeon2) {
                if (!CANDeviceTester.testPigeon((Pigeon2) device)) {
                    return device;
                }
            } else if (device instanceof CANcoder) {
                if (!CANDeviceTester.testCANCoder((CANcoder) device)) {
                    return device;
                }
            }
        }
        return null;
    }
}
