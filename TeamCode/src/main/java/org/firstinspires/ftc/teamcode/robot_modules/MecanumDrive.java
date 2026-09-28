package org.firstinspires.ftc.teamcode.robot_modules;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public abstract class MecanumDrive extends Drive {
    DcMotor front_right, back_right, front_left, back_left;


    public MecanumDrive(HardwareMap hardwareMap, String frontRight, String backRight, String frontLeft, String backLeft) {
        front_right = hardwareMap.get(DcMotor.class, frontRight);
        back_right = hardwareMap.get(DcMotor.class, backRight);
        front_left = hardwareMap.get(DcMotor.class, frontLeft);
        back_left = hardwareMap.get(DcMotor.class, backLeft);

        front_right.setDirection(DcMotor.Direction.FORWARD);
        back_right.setDirection(DcMotor.Direction.REVERSE);
        front_left.setDirection(DcMotor.Direction.FORWARD);
        back_left.setDirection(DcMotor.Direction.REVERSE);

        front_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        front_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


    }

    @Override
    public void run(Gamepad gamepad1) {

        double speed = 1-(gamepad1.right_trigger/1.4);
        if (speed <= 0.1) {
            speed = .1;
        }

        if (gamepad1.left_trigger>.3){
            speed = speed*(-1);
        }

        double axial = -gamepad1.left_stick_y;
        double lateral = gamepad1.left_stick_x;
        double yaw =  gamepad1.right_stick_x;


        double frontLeftPower = axial + lateral + yaw;
        double frontRightPower = axial - lateral - yaw;
        double backLeftPower = axial - lateral + yaw;
        double backRightPower = axial + lateral - yaw;


        double max = Math.max(Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower)),
                Math.max(Math.abs(backLeftPower), Math.abs(backRightPower)));

        if (max > 1.0) {
            frontLeftPower /= max;
            frontRightPower /= max;
            backLeftPower /= max;
            backRightPower /= max;
        }


        front_left.setPower(frontLeftPower * speed);
        front_right.setPower(frontRightPower * speed);
        back_left.setPower(backLeftPower * speed);
        back_right.setPower(backRightPower * speed);
    }
    @Override
    public void stop() {
        front_right.setPower(0);
        back_right.setPower(0);
        front_left.setPower(0);
        back_left.setPower(0);

    }
}
