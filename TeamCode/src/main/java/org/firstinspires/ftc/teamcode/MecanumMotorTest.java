package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Mecanum Motor Test")
public class MecanumMotorTest extends LinearOpMode {
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;
    @Override
    public void runOpMode() throws InterruptedException {
        leftFront = hardwareMap.dcMotor.get("leftFront");
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);
        rightRear = hardwareMap.dcMotor.get("rightRear");
        waitForStart();
        while(opModeIsActive()){
            boolean[] inputs = {gamepad1.a, gamepad1.b, gamepad1.x, gamepad1.y};
            DcMotor[] motors = {leftFront, rightFront, leftRear, rightRear};
            for(int i=0; i< motors.length; i++){
                if(inputs[i]){
                    motors[i].setPower(0.1);
                }
                else{
                    motors[i].setPower(0);
                }
            }
        }
    }
}
