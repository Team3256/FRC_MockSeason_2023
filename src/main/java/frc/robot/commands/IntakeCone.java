// Copyright (c) 2023 FRC 3256
// https://github.com/Team3256
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Intake;

// import static frc.robot.led.LEDConstants.kSuccess;

// import frc.robot.helpers.DebugCommandBase;
// import frc.robot.helpers.TimedBoolean;
// import frc.robot.intake.Intake;
// // import frc.robot.led.LED;
// // import frc.robot.led.commands.SetAllColor;

public class IntakeCone {
  private Intake intakeSubsystem = new Intake();

//   private LED ledSubsystem;
//   private TimedBoolean isCurrentSpiking;



  public IntakeCone(Intake intakeSubsystem) {
    this.intakeSubsystem = intakeSubsystem;
    // this.isCurrentSpiking = new TimedBoolean(intakeSubsystem::isCurrentSpiking, 0.3);

    // addRequirements(intakeSubsystem);
  }


  public void initalize(){
    intakeSubsystem.activate();
  }


}