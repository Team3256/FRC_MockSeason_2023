// Copyright (c) 2023 FRC 3256
// https://github.com/Team3256
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.utils;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.CANcoder;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CANDeviceTester {
    /**
     * Helper method to test PDP
     *
     * We can't use the new APIs here as this is a WPILib-provided class
     *
     * @return Returns whether the PDP is online
     */
    public static boolean testPDP(PowerDistribution device) {
        double voltage = device.getVoltage();
        if (voltage == 0)
            System.out.println("PDP offline");
        return voltage != 0;
    }

    /**
     * @param device talon fx id to test
     * @return Returns whether all the TalonFXs are online
     */
    public static boolean testTalonFX(TalonFX device) {
        // XXX: There's a TalonFX.isAlive method.
        // Does that also do what we want?
        boolean output = StatusSignal.isAllGood(device.getVersion());
        SmartDashboard.putBoolean("Talon device ID: " + device.getDeviceID() + " CAN connected", output);
        return output;
    }

    /**
     * @param device pigeon to test
     * @return Returns whether the Pigeon is online
     */
    public static boolean testPigeon(Pigeon2 device) {
        // double temp = device.getTemp();
        // if (temp == 0)
        // System.out.println("Pigeon " + device.getDeviceID() + " offline");
        // return temp != 0;
    }

    // /**
    // * @param device spark max to test
    // * @return Returns whether the SparkMax is online
    // */
    // public static boolean testSparkMax(CANSparkMax device) {
    // double temp = device.getMotorTemperature();
    // if (temp == 0)
    // System.out.println("SparkMax " + device.getDeviceId() + " offline");
    // return temp != 0;
    // }

    /**
     * @param device CANCoder to test
     * @return Returns whether the CanCoder is online
     */
    public static boolean testCANCoder(CANcoder device) {
        StatusSignal<Integer> version = device.getVersion();
        if (StatusSignal.isAllGood(version))
            System.out.println("CANCoder " + device.getDeviceID() + " offline");
        return StatusSignal.isAllGood(version);
    }
}
