package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.Shooter;

@TeleOp(name = "Shooter Tuning", group = "Tuning")
public class ShooterTuning extends LinearOpMode {
    private Shooter shooter;
    private Telemetry dashboardTelemetry;
    public static float P = 0, I = 0, D = 0, F = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        dashboardTelemetry = FtcDashboard.getInstance().getTelemetry();
        shooter = new Shooter(hardwareMap, "outtake");
        waitForStart();
        while (opModeIsActive()) {
            shooter.run();
            Shooter.setPIDF(P, I, D, F);
            shooter.postTelemetry(telemetry);
            shooter.postTelemetry(dashboardTelemetry);
        }
    }
}
