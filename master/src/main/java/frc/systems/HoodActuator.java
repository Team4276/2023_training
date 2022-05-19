package frc.systems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.Robot;
import frc.utilities.SoftwareTimer;
import frc.utilities.Xbox;

public class HoodActuator {

    public static DoubleSolenoid hoodSolenoid;
    private SoftwareTimer xboxDelayTimer;

    public static enum HoodState {
        UP,
        DOWN,
    }

    public static HoodState hoodState = HoodState.DOWN;

    public HoodActuator() {
        hoodSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
        xboxDelayTimer = new SoftwareTimer();
    }

    public void runHoodActuator() {

        if (xboxDelayTimer.isExpired()) {
            if (Robot.xboxController.getRawButton(Xbox.A) && (hoodState == HoodState.DOWN)) {
                hoodSolenoid.set(Value.kForward);
                hoodState = HoodState.UP;
                xboxDelayTimer.setTimer(0.5);
            } else if ((Robot.xboxController.getRawButton(Xbox.A)) && (hoodState == HoodState.UP)) {
                hoodSolenoid.set(Value.kReverse);
                hoodState = HoodState.DOWN;
                xboxDelayTimer.setTimer(0.5);
            } else {
                hoodSolenoid.set(Value.kOff);
            }
        }
    }
}
