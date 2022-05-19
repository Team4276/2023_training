package frc.utilities;

import frc.robot.Robot;

public class Toggler {

	private int button;
	private boolean state = false;
	private boolean currentButtonStatus = false;
	private boolean previousButtonStatus;

	public Toggler(int joystickButton) {
		button = joystickButton;
	}

	public void updateMechanismStateLJoy() {
		previousButtonStatus = currentButtonStatus;
		currentButtonStatus = Robot.leftJoystick.getRawButton(button);
		if (currentButtonStatus == true) {
			if (previousButtonStatus == false) {
				if (state == true) {
					state = false;
				} else {
					state = true;
				}
			}
		}
	}

	public void setMechanismState(boolean desiredState) {
		state = desiredState;
	}

	public boolean getMechanismState() {
		return state;
	}
}
