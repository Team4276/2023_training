package frc.utilities;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SoftwareTimer {

	private double expirationTime = 0;

	public void setTimer(double timerValue) {
		expirationTime = Timer.getFPGATimestamp() + timerValue;
		SmartDashboard.putNumber("TIME", Timer.getFPGATimestamp());
	}

	public boolean isExpired() {
		return (Timer.getFPGATimestamp() > expirationTime);
		// if robotTime exceeds expirationTime, then this returns true
	}

}
