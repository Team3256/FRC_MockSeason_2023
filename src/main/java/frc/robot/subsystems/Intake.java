// Copyright (c) 2023 FRC 3256
// https://github.com/Team3256
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
  private TalonFX intakeMotor;

  public Intake() {
    if (RobotBase.isReal()) {
      configureRealHardware();
    }
  }

  private void configureRealHardware() {
    intakeMotor = new TalonFX(Constants.IntakeConstants.kIntakeMotorID);
  }

  /**
   * Example command factory method.
   *
   * @return a command
   */
  public CommandBase exampleMethodCommand() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
          intakeMotor.setVoltage(2);
        });
  }

  public void activate() {
    intakeMotor.setVoltage(2);
  }

  public void stop() {
    intakeMotor.stopMotor();
  }

  // private WPI_TalonFX intakeMotor;
  // private TimeOfFlight leftDistanceSensor;
  // private TimeOfFlight rightDistanceSensor;
  //
  // private double leftDistance;
  // private double rightDistance;
  //
  // public Intake() {
  // if (RobotBase.isReal()) {
  // configureRealHardware();
  // } else {
  // configureSimHardware();
  // }
  // off();
  // System.out.println("Intake initialized");
  // }
  //
  // private void configureRealHardware() {
  // intakeMotor = TalonFXFactory.createDefaultTalon(kIntakeCANDevice);
  // intakeMotor.setNeutralMode(NeutralMode.Brake);
  // configIntakeCurrentLimit();
  //
  // if (FeatureFlags.kIntakeAutoScoreDistanceSensorOffset) {
  // leftDistanceSensor = new TimeOfFlight(kLeftDistanceSensorID);
  // rightDistanceSensor = new TimeOfFlight(kRightDistanceSensorID);
  //
  // leftDistanceSensor.setRangingMode(TimeOfFlight.RangingMode.Short, 0.05);
  // rightDistanceSensor.setRangingMode(TimeOfFlight.RangingMode.Short, 0.05);
  //
  // if (Constants.kDebugEnabled) {
  // SmartDashboard.putData("Intake motor", intakeMotor);
  // SmartDashboard.putData("Left distance sensor", leftDistanceSensor);
  // SmartDashboard.putData("Right distance sensor", rightDistanceSensor);
  // }
  // }
  // }
  //
  // public void configIntakeCurrentLimit() {
  // intakeMotor.configSupplyCurrentLimit(new
  // SupplyCurrentLimitConfiguration(true, 60, 60, 0.2));
  // }
  //
  // public double getGamePieceOffset() {
  // if (FeatureFlags.kIntakeAutoScoreDistanceSensorOffset) {
  // updateSensorDistances();
  // return (rightDistance - leftDistance) / 2;
  // }
  // return 0;
  // }
  //
  // public void updateSensorDistances() {
  // leftDistance = leftDistanceSensor.getRange();
  // rightDistance = rightDistanceSensor.getRange();
  // }
  //
  // public void setIntakeSpeed(double speed) {
  // intakeMotor.set(speed);
  // }
  //
  // public void intake() {
  // setIntakeSpeed(kIntakeSpeed);
  // }
  //
  // public void outtake() {
  // setIntakeSpeed(kOuttakeSpeed);
  // }
  //
  // public void off() {
  // setIntakeSpeed(0);
  // }
  //
  // public void periodic() {
  // if (Constants.kDebugEnabled) {
  // SmartDashboard.putNumber("Intake current", intakeMotor.getSupplyCurrent());
  // }
  // }
}
