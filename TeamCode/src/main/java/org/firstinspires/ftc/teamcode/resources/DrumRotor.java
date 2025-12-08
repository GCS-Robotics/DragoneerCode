package org.firstinspires.ftc.teamcode.resources;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DrumRotor {
    DcMotorEx drum;
    double targetPosition;
    double power;
    final double ROTATION_TICK = 1993.6;
    public DrumRotor(HardwareMap hardwareMap, double pow){
        drum = hardwareMap.get(DcMotorEx.class, "drumRotor");
        drum.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drum.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drum.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        power = pow;
    }
    public void intakeMode(){
        if (targetPosition/(ROTATION_TICK/3) % 1.0 <= 0.1) {
            targetPosition += ROTATION_TICK / 6.0;
        }
    }
    public void outtakeMode(){
        if (targetPosition/(ROTATION_TICK/3) % 1.0 <= 0.6 && targetPosition/(ROTATION_TICK/3) % 1.0 >= 0.4) {
            targetPosition += ROTATION_TICK / 6.0;
        }
    }
    public void rotateThird(){
        targetPosition += ROTATION_TICK/3.0;
    }
    public void rotateTwoThirds(){
        targetPosition += ROTATION_TICK*2.0/3.0;
    }
    public void run(){
        int position = drum.getCurrentPosition();
        if(targetPosition-position <= 0){
            drum.setPower(0);
            return;
        }
        drum.setPower(power);
    }
    public boolean reachedTarget(){
        return (!drum.isBusy() && targetPosition - drum.getCurrentPosition() <= 0);
    }
    public void telemetry(Telemetry tel){
        tel.addData("Drum Target", targetPosition);
        tel.addData("Thirds of Rotation", targetPosition/(ROTATION_TICK/3));
    }
}
