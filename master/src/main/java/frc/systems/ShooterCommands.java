package frc.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.utilities.LimitSwitch;
import frc.utilities.LimitSwitch.BallState;
import frc.utilities.RoboRioPorts;
import frc.utilities.SoftwareTimer;
import frc.utilities.Xbox;

public class ShooterCommands {

    public static VictorSPX upperMotor;
    public static VictorSPX lowerMotor;
    public static CANSparkMax shooterMotor;

    public static SoftwareTimer shootDelay;

    public static SoftwareTimer shootLowDelay;

    private static SoftwareTimer shootTimer;

    public final static double highShooterPower = -0.55;// high power can range from 0.9 to 1.0 at a full battery

    private SparkMaxPIDController pidController;
    private RelativeEncoder encoder;
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;

    public static int stepShooter = 0;

    public static double feederPower = 1.0;
    public static double lowShooterPower = -0.35;

    public ShooterCommands() {

        // motor initialization
        upperMotor = new VictorSPX(RoboRioPorts.CAN_SHOOT_UPPER);
        lowerMotor = new VictorSPX(RoboRioPorts.CAN_SHOOT_LOWER);
        shooterMotor = new CANSparkMax(RoboRioPorts.CAN_SHOOTER, MotorType.kBrushless);
        shooterMotor.restoreFactoryDefaults();
        pidController = shooterMotor.getPIDController();
        encoder = shooterMotor.getEncoder();

        shootTimer = new SoftwareTimer();

        // PID Coefficients
        kP = 6e-5;
        kI = 0;
        kD = 0;
        kIz = 0;
        kFF = 0.000015;
        kMaxOutput = 1;
        kMinOutput = -1;
        maxRPM = 5700;

    }

    public void setPID() {

        // set PID coefficients
        pidController.setP(kP);
        pidController.setI(kI);
        pidController.setD(kD);
        pidController.setIZone(kIz);
        pidController.setFF(kFF);
        pidController.setOutputRange(kMinOutput, kMaxOutput);
    }

    public void displayPID() {

        // display PID coefficients on SmartDashboard

        SmartDashboard.putNumber("P Gain", kP);
        SmartDashboard.putNumber("I Gain", kI);
        SmartDashboard.putNumber("D Gain", kD);
        SmartDashboard.putNumber("I Zone", kIz);
        SmartDashboard.putNumber("Feed Forward", kFF);
        SmartDashboard.putNumber("Max Output", kMaxOutput);
        SmartDashboard.putNumber("Min Output", kMinOutput);
    }

    public void runPID() {

        // read PID coefficients from SmartDashboard
        double p = SmartDashboard.getNumber("P Gain", 0);
        double i = SmartDashboard.getNumber("I Gain", 0);
        double d = SmartDashboard.getNumber("D Gain", 0);
        double iz = SmartDashboard.getNumber("I Zone", 0);
        double ff = SmartDashboard.getNumber("Feed Forward", 0);
        double max = SmartDashboard.getNumber("Max Output", 0);
        double min = SmartDashboard.getNumber("Min Output", 0);

        // if PID coefficients on SmartDashboard have changed, write new values to
        // controller

        if ((p != kP)) {
            pidController.setP(p);
            kP = p;
        }
        if ((i != kI)) {
            pidController.setI(i);
            kI = i;
        }
        if ((d != kD)) {
            pidController.setD(d);
            kD = d;
        }
        if ((iz != kIz)) {
            pidController.setIZone(iz);
            kIz = iz;
        }
        if ((ff != kFF)) {
            pidController.setFF(ff);
            kFF = ff;
        }
        if ((max != kMaxOutput) || (min != kMinOutput)) {
            pidController.setOutputRange(min, max);
            kMinOutput = min;
            kMaxOutput = max;
        }

        //double setPoint = highShooterPower * maxRPM;
        //pidController.setReference(setPoint, CANSparkMax.ControlType.kVelocity);

        //SmartDashboard.putNumber("SetPoint", setPoint);
        SmartDashboard.putNumber("ProcessVariable", encoder.getVelocity());
    }

    public static void feedIndexer() {

        /**
         * This method uses the upper and lower limit switches on the indexer
         * to feed the balls into the indexer. It runs/stops the feeder motors
         * depending on the position of balls on the limit switches.
         * (See LimitSwitch.java for more info on how the limit switches work).
         */

        if (LimitSwitch.ballState == BallState.NONE) {
            if (Robot.xboxController.getRawAxis(Xbox.RT) < 0.1) {
            // upper open; lower open
            upperMotor.set(ControlMode.PercentOutput, feederPower);
            lowerMotor.set(ControlMode.PercentOutput, feederPower);
            }
        }

        else if (LimitSwitch.ballState == BallState.LOWER) {
            if (Robot.xboxController.getRawAxis(Xbox.RT) < 0.1) {
            // upper open; lower closed
            upperMotor.set(ControlMode.PercentOutput, feederPower);
            lowerMotor.set(ControlMode.PercentOutput, feederPower);
            }
        }

        else if (LimitSwitch.ballState == BallState.UPPER) {
            if (Robot.xboxController.getRawAxis(Xbox.RT) < 0.1) {
                // upper occupied; lower open
                upperMotor.set(ControlMode.PercentOutput, 0);
                lowerMotor.set(ControlMode.PercentOutput, feederPower);
            }
        }

        else if (LimitSwitch.ballState == BallState.BOTH) {
            if (Robot.xboxController.getRawAxis(Xbox.RT) < 0.1) {
                // upper occupied; lower occupied
                upperMotor.set(ControlMode.PercentOutput, 0);
                lowerMotor.set(ControlMode.PercentOutput, 0);
            }
        }
    }// end feedIndexer()

    public void startShooter() {
        stepShooter = 1;
    }

    public static void runBaseShooter(boolean isLowGoal) {

        /**
         * Sends high power to the shooter action. Delays allow time
         * for the shooter motor to increase its velocity before feeding
         * the next ball.
         */

        switch (stepShooter) {
            case 0:
                // IDLE - do nothing
                break;
            case 1:
                if (isLowGoal) {
                    shooterMotor.set(lowShooterPower);
                    shootTimer.setTimer(0.015);
                } else {
                    shootTimer.setTimer(1.25);
                    shooterMotor.set(highShooterPower);
                }
                stepShooter++;
                break;
            case 2:
                if (shootTimer.isExpired()) {
                    stepShooter++;
                }
                break;
            case 3:
                upperMotor.set(ControlMode.PercentOutput, feederPower);
                lowerMotor.set(ControlMode.PercentOutput, feederPower);
                //shootTimer.setTimer(0.75);
                stepShooter++;
                break;
            case 4:
//                if (shootTimer.isExpired()) {
                    stepShooter++;
  //              }
                break;
            case 5:
                upperMotor.set(ControlMode.PercentOutput, 0);
                lowerMotor.set(ControlMode.PercentOutput, 0);
                shootTimer.setTimer(0.75);
                stepShooter++;
                break;
            case 6:
                if (shootTimer.isExpired()) {
                    stepShooter++;
                }
                break;
            case 7:
                upperMotor.set(ControlMode.PercentOutput, feederPower);
                lowerMotor.set(ControlMode.PercentOutput, feederPower);
                stepShooter = 0;
        }

    }// end shootHigh()

    public void motorStop() {

        /** Method that stops all the motors in the shooter assembly **/

        upperMotor.set(ControlMode.PercentOutput, 0);
        lowerMotor.set(ControlMode.PercentOutput, 0);
        shooterMotor.set(0);

    }// end motorStop()

}// end class
