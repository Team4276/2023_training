package frc.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.Robot;
import frc.utilities.RoboRioPorts;
import frc.utilities.SoftwareTimer;

public class IntakeCommands {

    private static VictorSPX intakeMotor;

    private static DoubleSolenoid intakeSolenoid;

    private static double intakeMotorPower = 1.0;

    private static boolean isIntakeRetracted;

    private static SoftwareTimer intakeTimer;

    public IntakeCommands() {
        intakeMotor = new VictorSPX(RoboRioPorts.CAN_INTAKE);
        intakeSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RoboRioPorts.INTAKE_SOLENOID1,
                RoboRioPorts.INTAKE_SOLENOID2);
        intakeTimer = new SoftwareTimer();
    }

    public void intakeBalls() {
        intakeMotor.set(ControlMode.PercentOutput, -intakeMotorPower);
    }

    public static void outtakeBalls() {
        intakeMotor.set(ControlMode.PercentOutput, intakeMotorPower);
        ControlShooter.lowerMotor.set(ControlMode.PercentOutput, -ControlShooter.feederPower);
    }

    public void raiseIntake() {
        intakeSolenoid.set(DoubleSolenoid.Value.kForward);
        isIntakeRetracted = false;
    }

    public void lowerIntake() {
        if (isIntakeRetracted) {
            if (intakeTimer.isExpired())
                intakeSolenoid.set(DoubleSolenoid.Value.kOff);
        } else {
            intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
            isIntakeRetracted = true;
            intakeTimer.setTimer(1.0);
        }
    }

    public static void stopIntakeSolenoid() {
        intakeSolenoid.set(DoubleSolenoid.Value.kOff);
    }

    public void stopIntakeMotor() {
        intakeMotor.set(ControlMode.PercentOutput, 0);
    }

}