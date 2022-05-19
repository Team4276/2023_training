package frc.systems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.systems.HoodActuator.HoodState;
import frc.utilities.LimitSwitch;
import frc.utilities.LimitSwitch.BallState;
import frc.utilities.Xbox;

public class ControlShooter extends ShooterCommands {

    /** See super for details on shooter commands **/

    static boolean isShooterStarted = false;

    public ControlShooter() {
    }

    public void runShooter() {

        if (!Robot.isAutoMode) {

            
            // load shooter action
            if (Robot.xboxController.getRawAxis(Xbox.LT) > 0.1) {
                feedIndexer();
            }

            // shooter action (high and low depending on if the hood is up or down )
            if (Robot.xboxController.getRawAxis(Xbox.RT) > 0.1) {
                if (!isShooterStarted) {
                    isShooterStarted = true;
                    startShooter();
                } else {
                    runBaseShooter(HoodActuator.hoodState == HoodState.DOWN);
                }
            } else {
                isShooterStarted = false;
            }

            // stop motor action
            if (Robot.xboxController.getRawAxis(Xbox.LT) == 0 && Robot.xboxController.getRawAxis(Xbox.RT) == 0) {
                motorStop();
            }
        }

        // Smart Dashboard outputs
        SmartDashboard.putNumber("Upper Motor Speed", upperMotor.getMotorOutputPercent());
        SmartDashboard.putNumber("Lower Motor Speed", lowerMotor.getMotorOutputPercent());

        if (LimitSwitch.ballState == BallState.NONE)
            SmartDashboard.putString("INDEX STATUS:", "INDEXER EMPTY. PRESS LT TO LOAD");
        else if (LimitSwitch.ballState == BallState.LOWER)
            SmartDashboard.putString("INDEX STATUS:", "ONE BALL IN LOW POSITION. PRESS LT TO LOAD");
        else if (LimitSwitch.ballState == BallState.UPPER)
            SmartDashboard.putString("INDEX STATUS:", "ONE BALL IN UPPER POSITION. PRESS LT TO LOAD");
        else if (LimitSwitch.ballState == BallState.BOTH)
            SmartDashboard.putString("INDEX STATUS:", "INDEXER FULL. BOMBS AWAY!!");

    }// end runShooter()

    public void runPIDControl() {
        // TODO: remember to setPID init
        super.displayPID();

        if (Robot.xboxController.getRawAxis(Xbox.LT) > 0.1) {
            super.runPID();
        }
    }

}// end class
