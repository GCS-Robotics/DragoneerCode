package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class BaseAuto {
    MecanumDrive drive;
    EasyIMU easy;
    DcMotor leftFront;
    public BaseAuto(MecanumDrive d, EasyIMU e, DcMotor lf){
        drive=d;
        easy=e;
        leftFront=lf;
    }
    public boolean strafe(double goalPos, double power){
        if(goalPos<0){
            if(leftFront.getCurrentPosition()>goalPos){
                drive.moveLeft(power);
                return true;
            }
        } if(goalPos>0){
            if(leftFront.getCurrentPosition()<goalPos){
                drive.moveRight(power);
                return true;
            }
        }
        drive.stop();
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        return false;
    }
    public boolean forwardBackward(double goalPos, double power){
        if(goalPos<0){
            if(leftFront.getCurrentPosition()>goalPos){
                drive.moveForward(power);
                return true;
            }
        } if(goalPos>0){
            if(leftFront.getCurrentPosition()<goalPos){
                drive.moveBackward(power);
                return true;
            }
        }
        drive.stop();
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        return false;
    }
    double startTime=0;
    public boolean timedGoal(double goalTime){
        double currentTime = System.currentTimeMillis();
        if(startTime==0){
            startTime=currentTime;
        }
        if(currentTime-startTime<goalTime){
            return true;
        }
        startTime=0;
        return false;
    }
    public boolean turn(double goalPos, double power){
        if(easy.getYaw()>goalPos+1){
            drive.turnRightTank(power);
            return true;
        }if (easy.getYaw()<goalPos-1){
            drive.turnLeftTank(power);
            return true;
        }
        drive.stop();
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        return false;
    }
}
