// Copyright (c) 2023 FRC 3256
// https://github.com/Team3256
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.utils;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;

import java.util.ArrayList; // import the ArrayList class

/*
 * This class is used to manage the CAN daisy chain and test for broken devices
 *
 * Add devices to the manager using the add() method, so we can test them later
 *
 * Use the getBrokenDevice() method to get the first broken device in the daisy chain
 */
public class CANConnectionManager {
    private ArrayList<ParentDevice> devices = new ArrayList<>();

    public CANConnectionManager(ArrayList<ParentDevice> initialDevices) {
        devices = initialDevices;
    }

    public CANConnectionManager() {
    }

    /*
     * Add a single `ParentDevice` to the CAN chain
     */
    public void add(ParentDevice device) {
        devices.add(device);
    }

    /*
     * Add a subsystem that uses devices to the CAN chain
     */
    public void add(UsesCANDevices device) {
        devices.addAll(device.getDevices());
    }

    /*
     * Returns the first broken device in the CAN daisy
     * chain or null if all devices are online
     */
    public ParentDevice getBrokenDevice() {
        for (ParentDevice device : devices) {
            if (device instanceof TalonFX) {
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
