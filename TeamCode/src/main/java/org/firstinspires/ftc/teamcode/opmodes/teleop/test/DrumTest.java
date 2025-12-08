package org.firstinspires.ftc.teamcode.opmodes.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Drum Test", group = "Test")
public class DrumTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor drumRotor = hardwareMap.dcMotor.get("drumRotor");
        drumRotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drumRotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        while(opModeIsActive()){
            telemetry.addData("Drum Pos", drumRotor.getCurrentPosition());
            if(gamepad1.right_trigger > 0){
                drumRotor.setPower(gamepad1.right_trigger/10);
            }else{
                drumRotor.setPower(0);
            }
            telemetry.update();
        }
    }
}
