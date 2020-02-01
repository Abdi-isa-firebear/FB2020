package org.firebears.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends SubsystemBase {

    static final Preferences config = Preferences.getInstance();

    static private final int PID_LOOP_IDX = 0;
    static private final int TIMEOUT_MS = config.getInt("shooter.timeout", 30);
    static private final double P = config.getDouble("shooter.P", 1.0);
    static private final double I = config.getDouble("shooter.I", 0.0);
    static private final double D = config.getDouble("shooter.D", 0.0);
    static private final double F = config.getDouble("shooter.F", 0.0);
    static private final double RPM = 350.0 * 5.0;
    static private final double SENSOR_UNITS_PER_REV = 4096;
    static private final double GEAR_RATIO = 13.56;
    static private final double PER_MINUTE_100_MS = 600.0;

    double targetVelocity = RPM * SENSOR_UNITS_PER_REV / (PER_MINUTE_100_MS * GEAR_RATIO);

    // private final WPI_TalonSRX srx;
    private final TalonSRX srx;

    public Shooter() {
        super();
        srx = new TalonSRX(config.getInt("shooter.motor1", 12));
        srx.configFactoryDefault();
        srx.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, PID_LOOP_IDX, TIMEOUT_MS);
        srx.setSensorPhase(true);
        // Configure nominal / peak outputs
        srx.configNominalOutputForward(0, TIMEOUT_MS);
        srx.configNominalOutputReverse(0, TIMEOUT_MS);
        srx.configPeakOutputForward(1, TIMEOUT_MS);
        srx.configPeakOutputReverse(1, TIMEOUT_MS);
        // Config PIDF
        srx.config_kP(PID_LOOP_IDX, P, TIMEOUT_MS);
        srx.config_kI(PID_LOOP_IDX, I, TIMEOUT_MS);
        srx.config_kD(PID_LOOP_IDX, D, TIMEOUT_MS);
        srx.config_kF(PID_LOOP_IDX, F, TIMEOUT_MS);
    }

    public void periodic() {
        double output = srx.getMotorOutputPercent();
        SmartDashboard.putNumber("Output", output);
        int velocity = srx.getSelectedSensorVelocity(PID_LOOP_IDX);
        SmartDashboard.putNumber("Velocity", velocity);
        // velocity in units per 100 ms
        srx.set(ControlMode.Velocity, targetVelocity);
    }

    public boolean isWheelSpunUp() {
        return srx.getSelectedSensorVelocity(PID_LOOP_IDX) >= targetVelocity;
    }

}
