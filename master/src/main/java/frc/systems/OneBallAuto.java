package frc.systems;

import frc.robot.Robot;
import frc.utilities.SoftwareTimer;

public class OneBallAuto extends AutoCode {

    public enum OneBallAutoStep{
        shoot_one,
        back_up
    };

    public SoftwareTimer driveDelay;
    public SoftwareTimer shootDelay;
    public static OneBallAutoStep oneBallAutoStep ;


    public OneBallAuto(){
        shootDelay = new SoftwareTimer();
        driveDelay = new SoftwareTimer();
        oneBallAutoStep = OneBallAutoStep.shoot_one;    
    }

    public void runOneBallAuto(){

        if (oneBallAutoStep == OneBallAutoStep.shoot_one){
            shootDelay.setTimer(5.0);
            if(!shootDelay.isExpired()){
                Robot.shooterControler.runShooter();  
            }
            else{
                Robot.shooterControler.motorStop();
                oneBallAutoStep = OneBallAutoStep.back_up;
            }
        }
        else if (oneBallAutoStep == OneBallAutoStep.back_up){
            driveDelay.setTimer(3.0);
            if(!driveDelay.isExpired())
            Robot.mDrivetrain.assignMotorPower(-0.4, 0.4);
            else 
            Robot.mDrivetrain.assignMotorPower(0, 0);
        }
    }
}
