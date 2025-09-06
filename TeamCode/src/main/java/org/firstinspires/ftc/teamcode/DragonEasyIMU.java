package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class DragonEasyIMU {
    IMU imu;
    public DragonEasyIMU(IMU i){
        imu = i;
    }
    public double getYaw(){
        YawPitchRollAngles orientation;
        orientation = imu.getRobotYawPitchRollAngles();
        return orientation.getYaw(AngleUnit.DEGREES);
    }
    public double getRoll(){
        YawPitchRollAngles orientation;
        orientation = imu.getRobotYawPitchRollAngles();
        return orientation.getRoll(AngleUnit.DEGREES);
    }
    public double getPitch(){
        YawPitchRollAngles orientation;
        orientation = imu.getRobotYawPitchRollAngles();
        return orientation.getPitch(AngleUnit.DEGREES);
    }
}
