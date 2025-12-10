package org.firstinspires.ftc.teamcode.opmodes.teleop.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name = "Test Firing", group = "Test")
@Config
public class LaunchTuning extends LinearOpMode {
    public static double kP = 0.0;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kF = 6000.0; // Start with 1 / maxRPM
    public static double targetRPM = 1000;
    DcMotorEx launcherRight;
    DcMotorEx launcherLeft;
    private final double TICKS_PER_REV = 28.0;
    private boolean shooterEnabled = false;

    @Override
    public void runOpMode() throws InterruptedException {
        launcherRight = hardwareMap.get(DcMotorEx.class, "launcherRight");
        launcherRight.setDirection(DcMotorSimple.Direction.REVERSE);
        launcherRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launcherRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launcherLeft = hardwareMap.get(DcMotorEx.class, "launcherLeft");
        launcherLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launcherLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        FtcDashboard dashboard = FtcDashboard.getInstance();
        PIDFController pidfController = new PIDFController(kP, kI, kD, kF);
        telemetry.addLine("Ready to tune shooter PIDF. Press A to toggle shooter.");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {
            if (gamepad1.aWasPressed()) {
                shooterEnabled = !shooterEnabled;
                if (!shooterEnabled) {
                    launcherLeft.setPower(0);
                    launcherRight.setPower(0);
                }
            }
            //This sends a telemetry packet to the FTC Dashboard so we can graph values
            TelemetryPacket packet = new TelemetryPacket();
            DcMotorEx[] shooters = new DcMotorEx[]{launcherLeft, launcherRight};
            for(int i = 0; i < shooters.length; i++){
                DcMotorEx shooter = shooters[i];
                double outputPower = 0;
                double currentRPM = ticksPerSecondToRPM(shooter.getVelocity());
                double temp = targetRPM;
                if (!shooterEnabled) {
                    targetRPM = 0;
                }
                pidfController.setPIDF(kP, kI, kD, kF);

                outputPower = pidfController.calculate(currentRPM, targetRPM);

                shooter.setPower(outputPower);

                targetRPM = temp;
                //Here is us defining the "packet" of values to send to the Dashboard
                packet.put("Launcher "+i+" Status", shooterEnabled ? "ENABLED" : "DISABLED");
                telemetry.addData("Launcher "+i+" Status", shooterEnabled ? "ENABLED" : "DISABLED");
                packet.put("Target RPM", targetRPM);
                telemetry.addData("Target RPM", targetRPM);
                packet.put("Motor "+i+" Actual RPM", currentRPM);
                telemetry.addData("Motor "+i+" Actual RPM", currentRPM);
                packet.put("Motor "+i+" Output Power", outputPower);
                telemetry.addData("Motor "+i+" Output Power", outputPower);
                packet.addLine("");
                telemetry.addLine();
            }
            telemetry.update();
            dashboard.sendTelemetryPacket(packet);
        }
    }
    private double ticksPerSecondToRPM(double tps) {
        return tps * 60.0 / TICKS_PER_REV;
    }
}
