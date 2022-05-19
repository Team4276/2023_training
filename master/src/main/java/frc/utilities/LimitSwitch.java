package frc.utilities;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class LimitSwitch extends DigitalInput {

    public enum BallState {

        NONE,
        UPPER,
        LOWER,
        BOTH
    };

    public static BallState ballState;

    public LimitSwitch(int channel) {
        super(channel);
    }// end constructor

    // get data from limit switches
    public void determineCase() {

        boolean lowerActive = Robot.lowerLimitSwitch.get();
        boolean upperActive = Robot.upperLimitSwitch.get();

        // flips value of upper limit switch to make 0 ground state (this can be removed
        // if we use a different switch)
        if (upperActive == true)
            upperActive = false;
        else
            upperActive = true;

        if (lowerActive == true)
            lowerActive = false;
        else
            lowerActive = true;

        // ocupied = true
        // open = false
        if (upperActive) {
            if (lowerActive) {
                ballState = BallState.BOTH;// upper OCCUPIED; lower OCCUPIED
                upperActive = true;
                lowerActive = true;
            } // end if
            else {
                ballState = BallState.UPPER;// upper OCCUPIED; lower OPEN
                upperActive = true;
                lowerActive = false;
            } // end else
        } // end if
        else if (lowerActive) {
            ballState = BallState.LOWER;// upper OPEN; lower OCCUPIED
            upperActive = false;
            lowerActive = true;
        } // end else if
        else {
            ballState = BallState.NONE;// upper OPEN; lower OPEN
            upperActive = false;
            lowerActive = false;
        } // end else

        // outputs limit switch values and ball state to the smart dashboard
        SmartDashboard.putBoolean("Upper Limit Switch:", upperActive);
        SmartDashboard.putBoolean("Lower Limit Switch:", lowerActive);
        SmartDashboard.putNumber("Ball State:", ballState.ordinal());
    }// end determine case()

}// end class
