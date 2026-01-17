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
    private boolean shooterSwap = true;

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
        telemetry.addLine("Ready to tune shooter PIDF. Press A to toggle shooter. Press B to change which shooter.");
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
            if (gamepad1.bWasPressed()){
                shooterSwap = !shooterSwap;
            }
            //This sends a telemetry packet to the FTC Dashboard so we can graph values
            TelemetryPacket packet = new TelemetryPacket();
            DcMotorEx[] shooters = new DcMotorEx[]{launcherLeft, launcherRight};
            DcMotorEx shooter = shooters[0];
            if(shooterSwap){
                shooter = shooters[1];
            }
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
            packet.put("Motor in Use", shooterSwap ? "Motor 0" : "Motor 1");
            telemetry.addData("Motor in Use", shooterSwap ? "Motor 0" : "Motor 1");
            packet.put("Status", shooterEnabled ? "ENABLED" : "DISABLED");
            telemetry.addData("Status", shooterEnabled ? "ENABLED" : "DISABLED");
            packet.put("Target RPM", targetRPM);
            telemetry.addData("Target RPM", targetRPM);
            packet.put("Actual RPM", currentRPM);
            telemetry.addData("Actual RPM", currentRPM);
            packet.put("Output Power", outputPower);
            telemetry.addData("Output Power", outputPower);
            packet.addLine("");
            telemetry.addLine();
            telemetry.update();
            dashboard.sendTelemetryPacket(packet);
        }
    }
    private double ticksPerSecondToRPM(double tps) {
        return tps * 60.0 / TICKS_PER_REV;
    }
}
