package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Intake Test")

public class IntakeTesting extends LinearOpMode {
    DcMotor IntakeMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        IntakeMotor = hardwareMap.dcMotor.get("motor1");
        waitForStart();
        while (opModeIsActive()) {
            IntakeMotor.setPower(1);
        }
    }
}