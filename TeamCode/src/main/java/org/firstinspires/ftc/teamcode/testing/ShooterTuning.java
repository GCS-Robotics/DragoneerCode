package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.Shooter;

@TeleOp(name = "Shooter Tuning", group = "Tuning")
@Config
public class ShooterTuning extends LinearOpMode {
    private DcMotorEx shooter;
    private Telemetry dashboardTelemetry;
    public static float P = 0, I = 0, D = 0, F = 0;
    public static float TARGET = 2000;
    @Override
    public void runOpMode() throws InterruptedException {
        dashboardTelemetry = FtcDashboard.getInstance().getTelemetry();
        shooter = hardwareMap.get(DcMotorEx.class, "outtake");
        waitForStart();
        while (opModeIsActive()) {
            shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(P, I, D, F));
            double targetTicksPerSecond = TARGET * 28/60;
            shooter.setVelocity(targetTicksPerSecond);
            telemetry.addData("Current RPM", shooter.getVelocity());
            telemetry.addData("Target RPM", TARGET);
            dashboardTelemetry.addData("Current RPM", shooter.getVelocity());
            dashboardTelemetry.addData("Target RPM", TARGET);
            telemetry.update();
            dashboardTelemetry.update();
        }
    }
}