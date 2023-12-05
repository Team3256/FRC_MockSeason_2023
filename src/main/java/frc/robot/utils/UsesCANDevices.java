// Copyright (c) 2023 FRC 3256
// https://github.com/Team3256
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.utils;

import com.ctre.phoenix6.hardware.ParentDevice;

/*
 * If your subsystem uses CAN devices, implement this interface
 */
public interface UsesCANDevices {
  ParentDevice[] getCANDevices();
}
