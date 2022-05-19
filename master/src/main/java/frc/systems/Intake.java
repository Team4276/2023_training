package frc.systems;

import frc.robot.Robot;
import frc.utilities.Xbox;

public class Intake extends IntakeCommands {

    public Intake() {
    }// end constructor

    public void runIntake() {

        /* intake motor control */

        // intake balls
        if (Robot.xboxController.getRawAxis(Xbox.RAxisY) > 0.2)
            intakeBalls();

        // outtake balls
        else if (Robot.xboxController.getRawAxis(Xbox.RAxisY) < -0.2)
            outtakeBalls();

        // stop mototrs
        else
            stopIntakeMotor();

        /* intake pnumatic control */

        // raise intake
        if (Robot.xboxController.getRawAxis(Xbox.LAxisY) < -0.2)
            raiseIntake();

        // lower intake
        else if (Robot.xboxController.getRawAxis(Xbox.LAxisY) > -0.2)
            lowerIntake();

        // dont move intake
        else
            stopIntakeSolenoid();

    }// end runIntake()

}// end class