
package org.firebears;

import org.firebears.commands.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;



public class OI {
    public XboxController xboxController;
    private JoystickButton lowerAcqButton; //LB
    private JoystickButton raiseAcqButton; //RB
    private JoystickButton spitButton; //B
    

    public Joystick buttonBox;
    private JoystickButton shootButton; //1
    private JoystickButton indexButton; //2
    private JoystickButton rightClimbUpSwitch;
    private JoystickButton rightClimbDownSwitch;
    private JoystickButton leftClimbUpSwitch;
    private JoystickButton leftClimbDownSwitch;
    

    

    public OI() {
        xboxController = new XboxController(0);

        lowerAcqButton = new JoystickButton(xboxController, 5); //LB
        lowerAcqButton.whenPressed(new AcquisitionLowerCommand());

        raiseAcqButton = new JoystickButton(xboxController, 6); //RB
        raiseAcqButton.whenPressed(new AcquisitionRaiseCommand());

        spitButton = new JoystickButton(xboxController, 2); //B
        spitButton.whenPressed(new SpitCommand());

        buttonBox = new Joystick(1);
        
        shootButton = new JoystickButton(buttonBox, 1);
        shootButton.whenHeld(new IndexShootingCommand());

        indexButton = new JoystickButton(buttonBox, 2);
        indexButton.whenPressed(new BallQueueCommand());

        rightClimbUpSwitch = new JoystickButton(buttonBox, 3);
        rightClimbUpSwitch.whenHeld(new ClimberUpRightCommand());

        rightClimbDownSwitch = new JoystickButton(buttonBox, 4);
        rightClimbDownSwitch.whenHeld(new ClimberDownRightCommand());

        leftClimbUpSwitch = new JoystickButton(buttonBox, 3);
        leftClimbUpSwitch.whenHeld(new ClimberUpLeftCommand());

        leftClimbDownSwitch = new JoystickButton(buttonBox, 3);
        leftClimbDownSwitch.whenHeld(new ClimberDownLeftCommand());
    }

    public XboxController getXboxController() {
        return xboxController;
    }

    public Joystick getJoystick1() {
        return buttonBox;
    }
}