package frc.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.utilities.RoboRioPorts;
import frc.utilities.SoftwareTimer;
import frc.utilities.Xbox;

public class Climber {

    public VictorSPX leftClimbMotor;
    public VictorSPX rightClimbMotor;

    public DoubleSolenoid climberLatchSolenoid;

    public boolean isLatchExtended = false;

    public SoftwareTimer latchTimer;

    public Climber() {
        leftClimbMotor = new VictorSPX(RoboRioPorts.CAN_LEFT_CLIMB);
        rightClimbMotor = new VictorSPX(RoboRioPorts.CAN_RIGHT_CLIMB);
        leftClimbMotor.setNeutralMode(NeutralMode.Brake);
        rightClimbMotor.setNeutralMode(NeutralMode.Brake);
        climberLatchSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RoboRioPorts.CLIMBER_LATCH_SOLENOID1,
                RoboRioPorts.CLIMBER_LATCH_SOLENOID2);
        latchTimer = new SoftwareTimer();
    }

    public void runClimb() {
        // x = down
        // y = up

        if (Robot.xboxController.getRawButton(Xbox.X)) {
            leftClimbMotor.set(ControlMode.PercentOutput, 1);
            rightClimbMotor.set(ControlMode.PercentOutput, 1);
            // Climber go Down(Make Robot go Up)
        } else if (Robot.xboxController.getRawButton(Xbox.Y)) {
            leftClimbMotor.set(ControlMode.PercentOutput, -0.5);
            rightClimbMotor.set(ControlMode.PercentOutput, -0.5);

        } else {
            leftClimbMotor.set(ControlMode.PercentOutput, 0);
            rightClimbMotor.set(ControlMode.PercentOutput, 0);
        }

        if (Robot.xboxController.getRawButton(Xbox.LB)) {

            if (isLatchExtended == true) {
                if (latchTimer.isExpired()) {
                    climberLatchSolenoid.set(Value.kReverse);
                    isLatchExtended = false;
                    latchTimer.setTimer(0.5);
                }

            } else if (isLatchExtended == false){
                if (latchTimer.isExpired()) {
                    climberLatchSolenoid.set(Value.kForward);
                    isLatchExtended = true;
                    latchTimer.setTimer(0.5);
                }

            }
            SmartDashboard.putBoolean("PINS", isLatchExtended);
        } else {
            try {
                climberLatchSolenoid.set(Value.kOff);
            } catch (Exception e) {
            }
        }

    }

}
