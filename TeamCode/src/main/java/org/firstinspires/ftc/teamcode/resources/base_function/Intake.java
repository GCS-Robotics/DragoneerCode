package org.firstinspires.ftc.teamcode.resources.base_function;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake extends Mechanism{
    DcMotor intake;
    double speed;
    public Intake(HardwareMap hardwareMap, double speed){
        intake = hardwareMap.get(DcMotor.class, "intake");
        this.speed = speed;
    }
    public Intake(HardwareMap hardwareMap){
        this(hardwareMap, 1.0);
    }
    @Override
    public void run(boolean running){
        if(running){
            intake.setPower(speed);
        }
        else{
            intake.setPower(0);
        }
    }
    @Override
    public void postTelemetry(Telemetry telemetry){

    }
}
