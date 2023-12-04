// Copyright (c) 2023 FRC 3256
// https://github.com/Team3256
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.utils;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CANDeviceTester {
  private static void log(String message, boolean value) {
    System.out.println(message + " | " + value);
    SmartDashboard.putBoolean(message, value);
  }

  //log names and descriptions of status codes

  private static void logStatusCode(String name, String descriptions ){
    System.out.println(name + " | " + descriptions);
    SmartDashboard.putString(name, descriptions);
  }

  /**
   * Helper method to test PDP
   *
   * <p>We can't use the new APIs here as this is a WPILib-provided class
   *
   * @return Returns whether the PDP is online
   */
  public static boolean testPDP(PowerDistribution device) {
    double voltage = device.getVoltage();
    log("Is PDP online?", voltage != 0);
    return voltage != 0;
  }

  /**
   * @param device talon fx id to test
   * @return Returns whether all the TalonFXs are online
   */
  public static boolean testTalonFX(TalonFX device) {
    // double temp = device.getTemperature();
    // if (temp == 0)
    // System.out.println("TalonFX " + device.getDeviceID() + " offline");
    // return temp != 0;
    StatusCode outputTalon = device.getVersion().getError();

    boolean isTalonAlive = outputTalon.isOK();

    log(
        "TalonFX Motor ID "
            + device.getDeviceID()
            + " (description: '"
            + device.getDescription()
            + "', CAN Bus: "
            + device.getCANBus()
            + ")",
        isTalonAlive);
    
    logStatusCode(outputTalon.getName(), outputTalon.getDescription());

    return isTalonAlive;
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

    StatusCode output = device.getVersion().getError();

    boolean isPigeonAlive = output.isOK();

    log(
        "Pigeon Motor ID " + device.getDeviceID() + " (CAN Bus: " + device.getCANBus() + ")",
        isPigeonAlive);

    logStatusCode(output.getName(), output.getDescription());
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

    log(
        "CANCoder Motor ID " + device.getDeviceID() + " (CAN Bus: " + device.getCANBus() + ")",
        isCANCoderAlive);

    logStatusCode(output.getName(), output.getDescription());
    return isCANCoderAlive;
  }
}