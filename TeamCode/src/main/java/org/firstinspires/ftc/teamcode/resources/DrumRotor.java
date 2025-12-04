package org.firstinspires.ftc.teamcode.resources;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DrumRotor {
    DcMotorEx drum;
    double targetPosition;
    double power;
    final int ROTATION_TICK = 288;
    public DrumRotor(HardwareMap hardwareMap, double pow){
        drum = hardwareMap.get(DcMotorEx.class, "drumRotor");
        drum.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drum.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drum.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        power = pow;
    }
    public void intakeMode(){
        if (targetPosition % (ROTATION_TICK / 3.0) <= ROTATION_TICK / 7.0) {
            targetPosition += ROTATION_TICK / 6.0;
        }
    }
    public void outtakeMode(){
        if (targetPosition % (ROTATION_TICK / 3.0) > ROTATION_TICK / 7.0) {
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
        double progress;
        if (targetPosition%ROTATION_TICK != 0){progress = (position%ROTATION_TICK) / (targetPosition%ROTATION_TICK);}
        else{ progress = 0;}
        drum.setPower(power * (1-progress));
        if(drum.getPower() < power * 0.2){
            drum.setPower(power * 0.2);
        }
    }
    public boolean reachedTarget(){
        return (!drum.isBusy() && targetPosition - drum.getCurrentPosition() <= 0);
    }
}
