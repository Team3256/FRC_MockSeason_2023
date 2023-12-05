// Copyright (c) 2023 FRC 3256
// https://github.com/Team3256
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.utils;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.PowerDistribution;
import java.util.ArrayList; // import the ArrayList class

public class CANConnectionManager<T> {
  private ArrayList<T> devices = new ArrayList<T>();

  public CANConnectionManager(ArrayList<T> initialDevices) {
    devices = initialDevices;
  }

  public CANConnectionManager() {}

  public void add(T device) {
    devices.add(device);
  }

  /*
   * Returns the first broken device in the CAN daisy chain or null if all devices
   * are online
   */
  public T getBrokenDevice() {
    for (T device : devices) {
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
