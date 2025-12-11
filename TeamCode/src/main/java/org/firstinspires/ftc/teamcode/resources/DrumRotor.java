package org.firstinspires.ftc.teamcode.resources;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DrumRotor {
    DcMotorEx drum;
    double targetPosition;
    double power;
    final double ROTATION_TICK = 1978.7;
    private final int[] balls;

    /**
     * Constrcts a Drum Rotor
     * @param hardwareMap Finds all of our hardware
     * @param pow The power to run the drum at
     * @param b The balls that we have preloaded
     */
    public DrumRotor(HardwareMap hardwareMap, double pow, int[] b){
        drum = hardwareMap.get(DcMotorEx.class, "drumRotor");
        drum.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drum.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drum.setTargetPosition(0);
        drum.setTargetPositionTolerance((int)ROTATION_TICK/360);
        drum.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        power = pow;
        balls = b;
    }

    /**
     * Constructs a Drum Rotor
     * @param hardwareMap Find all of our hardware
     * @param pow The power to run the drum at
     */
    public DrumRotor(HardwareMap hardwareMap, double pow){
        this(hardwareMap, pow, new int[]{-1, -1, -1});
    }

    /**
     * Checks if all the balls are full
     * @return Whether or not all the balls are filled
     */
    public boolean ballsFull(){
        int ballCount = 0;
        for(int ball : balls){
            if(ball>=0){
                ballCount+=1;
            }
        }
        return ballCount >= 3;
    }

    /**
     * Moves the drum into a position to be ready for intake mode.
     */
    public void intakeMode(){
        if (targetPosition/(ROTATION_TICK/3) % 1.0 <= 0.1) {
            targetPosition += ROTATION_TICK / 6.0;
            int temp = balls[0];
            balls[0] = balls[2];
            balls[2] = balls[1];
            balls[1] = temp;
        }
    }

    /**
     * Moves the drum into a position to be ready for outtake mode.
     */
    public void outtakeMode(){
        if (targetPosition/(ROTATION_TICK/3) % 1.0 <= 0.6 && targetPosition/(ROTATION_TICK/3) % 1.0 >= 0.4) {
            targetPosition += ROTATION_TICK / 6.0;
        }
    }

    /**
     * Rotates the drum one third of a rotation
     */
    public void rotateThird(){
        targetPosition += ROTATION_TICK/3.0;
    }

    /**
     * Rotates the drum two thirds of a rotation
     */
    public void rotateTwoThirds(){
        targetPosition += ROTATION_TICK*2.0/3.0;
    }

    /**
     * Actually runs the drum's run to position clone
     */
    public void run(){
        drum.setPower(power);
        drum.setTargetPosition((int)targetPosition);
    }

    /**
     * Returns whether or not the drum has reached its target
     * @return If the drum is at or past the target position
     */
    public boolean reachedTarget(){
        return (!drum.isBusy() && targetPosition - drum.getCurrentPosition() <= ROTATION_TICK/360);
    }

    /**
     * Gives telemetry for how much the drum has rotated
     * @param telemetry The telemetry to post to.
     */
    public void drumTelemetry(Telemetry telemetry){
        telemetry.addData("Drum Target", targetPosition);
        telemetry.addData("Thirds of Rotation", targetPosition/(ROTATION_TICK/3));
    }

    /**
     * Gives telemetry for which ball is stored where
     * @param telemetry The telemetry to post to.
     */
    public void storageTelemetry(Telemetry telemetry){
        for (int i = 0; i < balls.length; i++) {
            String ballDesc = "Stored Ball - ";
            if (i == 1) {
                ballDesc = "Launch Ball - ";
            }
            ballDesc = i+" "+ballDesc;
            if (balls[i] == 0) {
                telemetry.addData(ballDesc, "Green");
            } else if (balls[i] == 1) {
                telemetry.addData(ballDesc, "Purple");
            } else {
                telemetry.addData(ballDesc, "N/A");
            }
        }
    }

    /**
     * Rotates and intakes a new ball
     * @param newBall The integer representing the new ball
     */
    public void intakeBall(int newBall){
        rotateThird();
        // Shift over everything in ball storage
        balls[0] = balls[2];
        balls[2] = balls[1];
        // Adds our new ball
        balls[1] = newBall;
    }
    /**
     * Prepares the ball in the designated position for launch
     * @param ballType The color of ball we want to launch
     */
    public void setDrumLaunch(int ballType){
        int ballLocation = getBestBallPosition(ballType);
        if(ballLocation == 1){
            return;
        }
        if(ballLocation == 0){
            rotateThird();
            int temp = balls[0];
            balls[0] = balls[2];
            balls[2] = balls[1];
            balls[1] = temp;
            return;
        }
        if(ballLocation == 2){
            rotateTwoThirds();
            int temp = balls[2];
            balls[2] = balls[0];
            balls[0] = balls[1];
            balls[1] = temp;
        }
    }

    /**
     * Figures out the best ball position to rotate to
     * @param color The color we want to load
     * @return The ball that we want to load
     */
    private int getBestBallPosition(int color){
        int location = -1;
        for(int i=0; i<balls.length; i++){
            if(balls[i] == color){
                if(location == -1){
                    location = i;
                }
                else if(i == 1){
                    location = i;
                } else if (i < location){
                    location = i;
                }
            }
        }
        return location;
    }
    public void launchBall(){
        balls[1] = -1;
    }
}
