package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Shooter Tuning", group = "Tuning")
public class ShooterTuning extends LinearOpMode {

    // =========================
    // TUNABLE VALUES
    // =========================

    private double kP = 0.0;
    private double kI = 0.0;
    private double kD = 0.0;
    private double kF = 0.0;
    private double targetRPM = 1000.0;

    // =========================
    // STEP SIZES
    // =========================

    private static final double KP_STEP = 0.001;
    private static final double KI_STEP = 0.001;
    private static final double KD_STEP = 0.001;
    private static final double KF_STEP = 1;
    private static final double TARGET_RPM_STEP = 10.0;

    private static final double TICKS_PER_REV = 28.0;

    private DcMotorEx shooter;
    private boolean shooterEnabled = false;

    // 0 = kP
    // 1 = kI
    // 2 = kD
    // 3 = kF
    // 4 = target RPM
    private int selectedParameter = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        shooter = hardwareMap.get(DcMotorEx.class, "outtake");

        shooter.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        FtcDashboard dashboard = FtcDashboard.getInstance();

        PIDFController pidfController =
                new PIDFController(kP, kI, kD, kF);

        telemetry.addLine(
                "Ready to tune shooter PIDF. " +
                        "Press A to toggle shooter."
        );
        telemetry.addLine(
                "D-Pad Left/Right selects value, " +
                        "Up/Down changes value, B resets."
        );
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // =========================
            // SELECT PARAMETER
            // =========================

            if (gamepad1.dpad_left) {
                selectedParameter--;

                if (selectedParameter < 0) {
                    selectedParameter = 4;
                }

                sleep(150);
            }

            if (gamepad1.dpad_right) {
                selectedParameter++;

                if (selectedParameter > 4) {
                    selectedParameter = 0;
                }

                sleep(150);
            }

            // =========================
            // CHANGE PARAMETER
            // =========================

            if (gamepad1.dpad_up) {
                changeSelectedValue(1);
                sleep(100);
            }

            if (gamepad1.dpad_down) {
                changeSelectedValue(-1);
                sleep(100);
            }

            // =========================
            // RESET PARAMETER
            // =========================

            if (gamepad1.bWasPressed()) {
                resetSelectedValue();
            }

            // =========================
            // TOGGLE SHOOTER
            // =========================

            if (gamepad1.aWasPressed()) {
                shooterEnabled = !shooterEnabled;

                if (!shooterEnabled) {
                    shooter.setPower(0);
                }
            }

            // =========================
            // UPDATE PIDF
            // =========================

            pidfController.setPIDF(kP, kI, kD, kF);

            // =========================
            // MOTOR DATA
            // =========================

            double outputPower = 0;

            double currentRPM =
                    ticksPerSecondToRPM(shooter.getVelocity());

            double target = shooterEnabled ? targetRPM : 0;

            outputPower =
                    pidfController.calculate(currentRPM, target);

            shooter.setPower(
                    shooterEnabled ? outputPower : 0
            );

            // =========================
            // DASHBOARD TELEMETRY
            // =========================

            TelemetryPacket packet = new TelemetryPacket();

            packet.put(
                    "Status",
                    shooterEnabled ? "ENABLED" : "DISABLED"
            );

            packet.put("Target RPM", targetRPM);
            packet.put("Actual RPM", currentRPM);
            packet.put("Output Power", outputPower);

            // Also send the PID values to Dashboard
            // so you can see what you're currently tuning.
            packet.put("kP", kP);
            packet.put("kI", kI);
            packet.put("kD", kD);
            packet.put("kF", kF);

            // =========================
            // DRIVER STATION TELEMETRY
            // =========================

            telemetry.addData(
                    "Status",
                    shooterEnabled ? "ENABLED" : "DISABLED"
            );

            telemetry.addData(
                    "Target RPM",
                    targetRPM
            );

            telemetry.addData(
                    "Actual RPM",
                    currentRPM
            );

            telemetry.addData(
                    "Output Power",
                    outputPower
            );

            // Small addition showing which value is selected
            telemetry.addData(
                    "Tuning",
                    getSelectedParameterName()
            );

            telemetry.addData(
                    "kP",
                    "%.3f",
                    kP
            );

            telemetry.addData(
                    "kI",
                    "%.4f",
                    kI
            );

            telemetry.addData(
                    "kD",
                    "%.3f",
                    kD
            );

            telemetry.addData(
                    "kF",
                    "%.3f",
                    kF
            );

            telemetry.addLine();

            telemetry.addLine(
                    "DPad L/R: Select | DPad U/D: Change | B: Reset"
            );

            telemetry.update();

            // Keep sending packets to FTC Dashboard
            // for graphing.
            dashboard.sendTelemetryPacket(packet);
        }
    }

    /**
     * Changes the currently selected parameter.
     *
     * direction:
     * +1 = increase
     * -1 = decrease
     */
    private void changeSelectedValue(int direction) {

        switch (selectedParameter) {

            case 0:
                kP += KP_STEP * direction;
                kP = Math.max(0, kP);
                break;

            case 1:
                kI += KI_STEP * direction;
                kI = Math.max(0, kI);
                break;

            case 2:
                kD += KD_STEP * direction;
                kD = Math.max(0, kD);
                break;

            case 3:
                kF += KF_STEP * direction;
                kF = Math.max(0, kF);
                break;

            case 4:
                targetRPM += TARGET_RPM_STEP * direction;
                targetRPM = Math.max(0, targetRPM);
                break;
        }
    }

    /**
     * Resets the selected parameter.
     */
    private void resetSelectedValue() {

        switch (selectedParameter) {

            case 0:
                kP = 0.0;
                break;

            case 1:
                kI = 0.0;
                break;

            case 2:
                kD = 0.0;
                break;

            case 3:
                kF = 0.0;
                break;

            case 4:
                targetRPM = 1000.0;
                break;
        }
    }

    private String getSelectedParameterName() {

        switch (selectedParameter) {

            case 0:
                return "kP";

            case 1:
                return "kI";

            case 2:
                return "kD";

            case 3:
                return "kF";

            case 4:
                return "Target RPM";

            default:
                return "Unknown";
        }
    }

    private double ticksPerSecondToRPM(double tps) {
        return tps * 60.0 / TICKS_PER_REV;
    }
}