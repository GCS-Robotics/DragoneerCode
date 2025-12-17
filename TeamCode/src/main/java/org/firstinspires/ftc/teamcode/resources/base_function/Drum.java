package org.firstinspires.ftc.teamcode.resources.base_function;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.resources.States;

public class Drum extends Mechanism{
    DcMotorEx drum;
    public static double targetPosition;
    double power;
    public final double ROTATION_TICK = 1992;
    final double ONE_DEGREE = ROTATION_TICK/360;
    private static States.Artifact[] balls;
    private States.DrumMode mode = States.DrumMode.OUTTAKE;
    public static States.DrumState state = States.DrumState.IDLE;
    /**
     * Constructs a Drum Rotor
     * @param hardwareMap Finds all of our hardware
     * @param pow The power to run the drum at
     * @param b The balls that we have preloaded
     */
    public Drum(HardwareMap hardwareMap, double pow, States.Artifact[] b){
        this(hardwareMap, pow);
        balls = b;
    }

    /**
     * Constructs a Drum Rotor
     * @param hardwareMap Find all of our hardware
     * @param pow The power to run the drum at
     */
    public Drum(HardwareMap hardwareMap, double pow){
        drum = hardwareMap.get(DcMotorEx.class, "drumRotor");
        drum.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        power = pow;
        if(balls == null){
            resetStatics();
        }
    }

    /**
     * Checks if all the balls are full
     * @return Whether or not all the balls are filled
     */
    public int countBalls(){
        int ballCount = 0;
        for(States.Artifact ball : balls){
            if(ball!= States.Artifact.NONE){
                ballCount+=1;
            }
        }
        return ballCount;
    }

    /**
     * Moves the drum into a position to be ready for intake mode.
     */
    public void intakeMode(){
        if (reachedTarget() &&
                mode == States.DrumMode.OUTTAKE){
            targetPosition += ROTATION_TICK / 6.0;
            mode = States.DrumMode.INTAKE;
            States.Artifact temp = balls[0];
            balls[0] = balls[2];
            balls[2] = balls[1];
            balls[1] = temp;
        }
    }

    /**
     * Moves the drum into a position to be ready for outtake mode.
     */
    public void outtakeMode(){
        if (reachedTarget() &&
                mode == States.DrumMode.INTAKE) {
            targetPosition += ROTATION_TICK / 6.0;
            mode = States.DrumMode.OUTTAKE;
        }
    }

    /**
     * Rotates the drum one third of a rotation
     */
    public void rotateThird(){
        targetPosition += ROTATION_TICK/3.0;
        States.Artifact temp = balls[0];
        balls[0] = balls[2];
        balls[2] = balls[1];
        balls[1] = temp;
    }

    /**
     * Rotates the drum backwards a third of a rotation
     */
    public void rotateBackAThird(){
        targetPosition -= ROTATION_TICK/3.0;
        States.Artifact temp = balls[2];
        balls[2] = balls[0];
        balls[0] = balls[1];
        balls[1] = temp;
    }

    /**
     * Actually runs the drum's run to position clone
     */
    @Override
    public void run(boolean running){
        if(!reachedTarget()){
            drum.setPower(power);
            drum.setTargetPosition((int)targetPosition);
            state = States.DrumState.MOVING;
        } else {
            drum.setPower(0);
            state = States.DrumState.IDLE;
        }
    }

    @Override
    public void postTelemetry(Telemetry telemetry){
        drumTelemetry(telemetry);
        telemetry.addLine();
        storageTelemetry(telemetry);
    }

    /**
     * Returns whether or not the drum has reached its target
     * @return If the drum is at or past the target position
     */
    public boolean reachedTarget(){
        return (!drum.isBusy() && abs(targetPosition - drum.getCurrentPosition()) <= ROTATION_TICK/360);
    }

    /**
     * Gives telemetry for how much the drum has rotated
     * @param telemetry The telemetry to post to.
     */
    private void drumTelemetry(Telemetry telemetry){
        telemetry.addData("Drum Target", targetPosition);
        telemetry.addData("Actual Drum", drum.getCurrentPosition());
        telemetry.addData("Thirds of Rotation", targetPosition/(ROTATION_TICK/3));
        telemetry.addLine();
        telemetry.addData("Drum Mode", mode);
        telemetry.addData("Drum State", state);
    }

    /**
     * Gives telemetry for which ball is stored where
     * @param telemetry The telemetry to post to.
     */
    private void storageTelemetry(Telemetry telemetry){
        for (int i = 0; i < balls.length; i++) {
            String ballDesc = "Stored Ball - ";
            if (i == 1) {
                ballDesc = "Launch Ball - ";
            }
            ballDesc = i+" "+ballDesc;
            if (balls[i] == States.Artifact.GREEN) {
                telemetry.addData(ballDesc, "Green");
            } else if (balls[i] == States.Artifact.PURPLE) {
                telemetry.addData(ballDesc, "Purple");
            } else {
                telemetry.addData(ballDesc, "N/A");
            }
        }
    }


    public void intakeBall(States.Artifact newBall){
        targetPosition += ROTATION_TICK/3;
        addBall(newBall);
    }
    public void addBall(States.Artifact newBall){
        balls[0] = balls[2];
        balls[2] = balls[1];
        // Adds our new ball
        balls[1] = newBall;
    }
    /**
     * Prepares the ball in the designated position for launch
     * @param ballType The color of ball we want to launch
     */
    public void setDrumLaunch(States.Artifact ballType){
        outtakeMode();
        int ballLocation = getBestBallPosition(ballType);
        if(ballLocation == 1){
            return;
        }
        if(ballLocation == 0){
            rotateThird();
            return;
        }
        if(ballLocation == 2){
            rotateBackAThird();
        }
    }

    public States.Artifact getBall(int location){
        return balls[location];
    }
    public void rotateSixth(){
        targetPosition += ROTATION_TICK/6;
    }

    /**
     * Figures out the best ball position to rotate to
     * @param color The color we want to load
     * @return The ball that we want to load
     */
    private int getBestBallPosition(States.Artifact color){
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
        balls[1] = States.Artifact.NONE;
    }
    public void resetStatics(){
        balls = new States.Artifact[]{States.Artifact.NONE, States.Artifact.NONE, States.Artifact.NONE};
        targetPosition = 0;
        mode = States.DrumMode.OUTTAKE;
        drum.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drum.setTargetPosition(0);
        drum.setTargetPositionTolerance((int)ONE_DEGREE/2);
        drum.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
