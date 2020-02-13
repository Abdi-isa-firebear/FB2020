package org.firebears.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.firebears.commands.*;
import org.firebears.util.PIDSparkMotor;

public class Chassis extends SubsystemBase {

    private final Preferences config = Preferences.getInstance();

    private final CANSparkMax frontRight;
    private final CANSparkMax rearRight;
    private final CANSparkMax frontLeft;
    private final CANSparkMax rearLeft;
    private final PIDSparkMotor pidFrontRight;
    private final PIDSparkMotor pidFrontLeft;
    private final DifferentialDrive robotDrive;
    private final CANEncoder frontRightEncoder;
    private final CANEncoder frontLeftEncoder;

    public Chassis() {
        double stallLimit = config.getDouble("chassis.stallLimit", 25.0);
        double freeLimit = config.getDouble("chassis.freeLimit", 65.0);
        double limitRPM = config.getDouble("chassis.limitRPM", 1000.0);
        double kP = config.getDouble("chassis.p", 0.00015);
        double kI = config.getDouble("chassis.i", 0.0);
        double kD = config.getDouble("chassis.d", 0.0);
        double kP2 = config.getDouble("chassis.secondary.p", 0.015);
        double kI2 = config.getDouble("chassis.secondary.i", 0.0);
        double kD2 = config.getDouble("chassis.secondary.d", 0.0);
        boolean closedLoop = config.getBoolean("chassis.closedLoop", false);
        int chassisFrontRightCanID = config.getInt("chassis.frontright.canID", 3);
        int chassisRearRightCanID = config.getInt("chassis.rearright.canID", 2);
        int chassisFrontLeftCanID = config.getInt("chassis.frontleft.canID", 4);
        int chassisRearLeftCanID = config.getInt("chassis.rearleft.canID", 5);

        frontRight = new CANSparkMax(chassisFrontRightCanID, MotorType.kBrushless);
        frontRight.setInverted(false);
        frontRight.setSmartCurrentLimit(stallLimit, freeLimit, limitRPM);
        frontRightEncoder = frontRight.getEncoder();

        pidFrontRight = new PIDSparkMotor(frontRight, kP, kI, kD);
        pidFrontRight.setClosedLoop(closedLoop);
        pidFrontRight.setInvertEncoder(true);
        pidFrontRight.setSecondaryPID(kP2, kI2, kD2);

        rearRight = new CANSparkMax(chassisRearRightCanID, MotorType.kBrushless);
        rearRight.setInverted(false);
        rearRight.setSmartCurrentLimit(stallLimit, freeLimit, limitRPM);
        rearRight.follow(frontRight);

        frontLeft = new CANSparkMax(chassisFrontLeftCanID, MotorType.kBrushless);
        frontLeft.setInverted(false);
        frontLeft.setSmartCurrentLimit(stallLimit, freeLimit, limitRPM);
        frontLeftEncoder = frontLeft.getEncoder();
        pidFrontLeft = new PIDSparkMotor(frontLeft, kP, kI, kD);
        pidFrontLeft.setClosedLoop(closedLoop);
        pidFrontLeft.setInvertEncoder(false);

        rearLeft = new CANSparkMax(chassisRearLeftCanID, MotorType.kBrushless);
        rearLeft.setInverted(false);
        rearLeft.setSmartCurrentLimit(stallLimit, freeLimit, limitRPM);
        rearLeft.follow(frontLeft);

        robotDrive = new DifferentialDrive(pidFrontLeft, pidFrontRight);
        addChild("RobotDrive", robotDrive);

        setDefaultCommand(new DriveCommand(this));
    }

    @Override
    public void periodic() {

    }

    public double averageDistance() {
        double conversionFactor = config.getDouble("chassis.ticksToFeetConversionFactor", 0);
        return ((frontRightEncoder.getPosition() + frontLeftEncoder.getPosition()) * conversionFactor) / 2;
    }

    public double rotation() {
        double rotationConversionFactor = config.getDouble("chassis.ticksToDegreesConversionFactor", 0);
        return ((frontRightEncoder.getPosition() - frontLeftEncoder.getPosition()) * rotationConversionFactor);
    }

    public void drive(double speed, double rotation) {
        robotDrive.arcadeDrive(speed, rotation);
    }
}
