// Copyright (c) 2023 FRC 3256
// https://github.com/Team3256
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.utils;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.ArrayList; // import the ArrayList class
import java.util.List;

/*
 * This class is used to manage the CAN daisy chain and test for broken devices
 *
 * Add devices to the manager using the add() method, so we can test them later
 *
 * Use the getBrokenDevice() method to get the first broken device in the daisy chain
 */
public class CANConnectionManager {
  private ArrayList<ParentDevice> devices = new ArrayList<>();

  static class CANDeviceTester {
    public static String getLogDescription(TalonFX device) {
      return "TalonFX Motor ID "
          + device.getDeviceID()
          + " (description: '"
          + device.getDescription()
          + "', CAN Bus: "
          + device.getCANBus()
          + ") ";
    }

    public static String getLogDescription(String name, ParentDevice device) {
      return name + " Motor ID " + device.getDeviceID() + " (CAN Bus: " + device.getCANBus() + ") ";
    }

    private static void log(String message, boolean value) {
      System.out.println(message + " | " + value);
      SmartDashboard.putBoolean(message, value);
    }

    /*
     * Helper method to test PDP
     *
     * <p>We can't use the new APIs here as this is a WPILib-provided class
     *
     * @return Returns whether the PDP is online
     */
    //    public static boolean testPDP(PowerDistribution device) {
    //        double voltage = device.getVoltage();
    //        log("Is PDP online?", voltage != 0);
    //        return voltage != 0;
    //    }

    /**
     * @param device talon fx id to test
     * @return Returns whether all the TalonFXs are online
     */
    public static boolean testTalonFX(TalonFX device) {
      StatusCode outputTalon = device.getVersion().getError();

      boolean isTalonAlive = outputTalon.isOK();

      log(getLogDescription(device) + "is alive?", isTalonAlive);

      return isTalonAlive;
    }

    /**
     * @param device pigeon to test
     * @return Returns whether the Pigeon is online
     */
    public static boolean testPigeon(Pigeon2 device) {

      StatusCode output = device.getVersion().getError();

      boolean isPigeonAlive = output.isOK();

      log(getLogDescription("Pigeon", device) + "is alive?", isPigeonAlive);

      return isPigeonAlive;
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
      StatusCode output = device.getVersion().getError();
      boolean isCANCoderAlive = output.isOK();

      log(getLogDescription("CANCoder", device) + "is alive?", isCANCoderAlive);
      return isCANCoderAlive;
    }
  }

  public CANConnectionManager(ArrayList<ParentDevice> initialDevices) {
    // XXX: I probably should've used .copy here
    devices = initialDevices;
  }

  public CANConnectionManager() {}

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
    devices.addAll(List.of(device.getCANDevices()));
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
