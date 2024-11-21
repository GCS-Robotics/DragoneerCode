package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class BaseAuto {
    MecanumDrive drive;
    DcMotor linearSlide;
    DcMotor spinnerPivot;
    CRServo spinner;
    Servo tilt;
    EasyIMU easy;
    DcMotor leftFront;
    public BaseAuto(MecanumDrive d, DcMotor ls, DcMotor sp, CRServo s, Servo t, EasyIMU e, DcMotor lf){
        drive=d;
        linearSlide=ls;
        spinnerPivot=sp;
        spinner=s;
        tilt=t;
        easy=e;
        leftFront=lf;
    }
    public boolean hammerDownUp(double goalPos, double power){
        spinnerPivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spinnerPivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        if(goalPos<0){
            if(spinnerPivot.getCurrentPosition()>goalPos){
                spinnerPivot.setPower(-power);
                return true;
            }
        } if(goalPos>0){
            if(spinnerPivot.getCurrentPosition()<goalPos){
                spinnerPivot.setPower(power);
                return true;
            }
        }
        spinnerPivot.setPower(0);
        return false;
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
        }if (easy.getYaw()<goalPos-1){
            drive.turnLeftTank(power);
        }
        drive.stop();
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        return false;
    }
    public boolean moveSlide(int goalPos){
        linearSlide.setTargetPosition(goalPos);
        linearSlide.setPower(1);
        if(goalPos>=2000){
            return linearSlide.getCurrentPosition()>(goalPos-100);
        }
        return linearSlide.getCurrentPosition()<(goalPos+300);
    }
    private static boolean turnBool(double degrees, double goal){
        return !(goal - 1 < degrees) || !(degrees < goal + 1);
    }
}
