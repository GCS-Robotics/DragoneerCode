package org.firstinspires.ftc.teamcode.robot_modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TankDrive extends Drive {
    DcMotor leftDrive, rightDrive;
    public TankDrive(HardwareMap hardwareMap, String name_left, String name_right){
        // Hardware Mapping
        leftDrive = hardwareMap.get(DcMotor.class, name_left);
        rightDrive = hardwareMap.get(DcMotor.class, name_right);
        // Changing their direction
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        // What the motors do when .setPower(0)
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    @Override
    public void run(Gamepad gamepad) {
        float forward = -gamepad.left_stick_y;
        float rotate = gamepad.right_stick_x;
        leftDrive.setPower(forward+rotate);
        rightDrive.setPower(forward-rotate);
    }

    @Override
    public void stop() {
        leftDrive.setPower(0);
        rightDrive.setPower(0);
    }

    @Override
    public void postTelemetry(Telemetry telemetry) {
        // No Telemetry Needed
    }
}
