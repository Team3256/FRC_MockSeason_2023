// Copyright (c) 2023 FRC 3256
// https://github.com/Team3256
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.CANDeviceTester;
import frc.robot.utils.UsesCANDevices;
import java.util.ArrayList;
import java.util.List;

public class Intake extends SubsystemBase implements UsesCANDevices {
  private TalonFX intakeMotor;

  public Intake() {
    if (RobotBase.isReal()) {
      configureRealHardware();
    }
  }

  public ArrayList<ParentDevice> getDevices() {
    return new ArrayList<>(List.of(new TalonFX[] {intakeMotor}));
  }

  private void configureRealHardware() {
    intakeMotor = new TalonFX(Constants.IntakeConstants.kIntakeMotorID);
  }

  public void start() {
    intakeMotor.setVoltage(7);
  }

  public void stop() {
    intakeMotor.stopMotor();
  }

  @Override
  public void periodic() {
    StatusSignal<Double> intakeCurrent = intakeMotor.getSupplyCurrent();
    String description = CANDeviceTester.getLogDescription(intakeMotor) + "intake current";
    if (intakeCurrent.getError().isOK()) {
      SmartDashboard.putNumber(description, intakeCurrent.getValue());
    } else {
      // Funny number for funny business
      SmartDashboard.putNumber(description, -69420);
    }
  }
}
