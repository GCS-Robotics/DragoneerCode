package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp(name = "Gear Grinders Intake Test")
public class GearGrindersIntakeTest extends LinearOpMode{
    CRServo claw;
    @Override
    public void runOpMode() throws InterruptedException {
        claw = hardwareMap.crservo.get("servo1");
        waitForStart();
        // On Play
        while (opModeIsActive()) {
            if(gamepad2.left_stick_y<-.50){

                claw.setPower(100);
            }
            if(gamepad2.left_stick_y>.50){

                claw.setPower(-100);
            }
            else {
                claw.setPower(0);
            }

        }
    }
}

