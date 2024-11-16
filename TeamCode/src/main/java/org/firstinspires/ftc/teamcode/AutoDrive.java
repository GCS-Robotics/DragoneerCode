package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

// Currently Unused
public class AutoDrive {
    private MecanumDrive drive;
    private DcMotor linearSlide;
    private DcMotor spinnerPivot;
    private CRServo spinner;
    private Servo tilt;
    public AutoDrive(MecanumDrive d, DcMotor ls, DcMotor sp, CRServo s, Servo t){
        drive = d;
        linearSlide = ls;
        spinnerPivot = sp;
        spinner = s;
        tilt = t;
    }

}
