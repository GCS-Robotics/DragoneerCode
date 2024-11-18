package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Encoder Read-Out")
public class EncoderReadOut extends LinearOpMode {
    DcMotor leftFront; //port 0 Gobilda 5202/3/4
    DcMotor linearSlide; //expansionHub port 0 Gobilda 5202/3/4
    // Allowed to go from 0 to -4500
    DcMotor spinnerPivot;
    CRServo spinner;
    Servo tilt;
    @Override
    public void runOpMode() throws InterruptedException {
        leftFront=hardwareMap.dcMotor.get("motor1");
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        spinnerPivot=hardwareMap.dcMotor.get("motor6");
        spinnerPivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spinnerPivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        linearSlide=hardwareMap.dcMotor.get("motor5");
        linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        while(opModeIsActive()){
            telemetry.addData("Wheel", leftFront.getCurrentPosition());
            telemetry.addData("Spinner", spinnerPivot.getCurrentPosition());
            telemetry.update();
        }
    }
}
