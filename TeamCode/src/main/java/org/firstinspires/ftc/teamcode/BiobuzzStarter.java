package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot_modules.TankDrive;

@TeleOp(name = "Robert's Biobuzz Test", group = "Starter Bot")
public class BiobuzzStarter extends LinearOpMode {
    public TankDrive drive;
    private DcMotor intake, outtake;
    private CRServo leftIntake, rightIntake, transition;
    @Override
    public void runOpMode() throws InterruptedException {
        // Hardware Mapping Motors
        drive = new TankDrive(hardwareMap, "left_drive", "right_drive");
        intake = hardwareMap.get(DcMotor.class, "intake");
        outtake = hardwareMap.get(DcMotor.class, "outtake");
        leftIntake = hardwareMap.get(CRServo.class, "left_intake_servo");
        rightIntake = hardwareMap.get(CRServo.class, "right_intake_servo");
        transition = hardwareMap.get(CRServo.class, "transition");
        // Direction Configs
        rightIntake.setDirection(DcMotorSimple.Direction.REVERSE);
        // Other Initialization Things
        leftIntake.setPower(0);
        rightIntake.setPower(0);
        // Waiting
        waitForStart();
        while(opModeIsActive()){
            drive.run(gamepad1);
            intake.setPower(gamepad2.left_trigger);
            outtake.setPower(gamepad2.right_trigger);
            if(gamepad2.a) {
                leftIntake.setPower(1);
                rightIntake.setPower(1);
            } else{
                leftIntake.setPower(0);
                rightIntake.setPower(0);
            }
            if(gamepad2.b){
                transition.setPower(1);
            } else if(gamepad2.x){
                transition.setPower(-1);
            } else{
                transition.setPower(0);
            }
        }
        drive.stop();
    }
}