package org.firstinspires.ftc.teamcode.resources.base_function;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.resources.States;

public class Drum extends Mechanism{
    // ===== Anti-Jam Tuning =====
    private static final double JAM_TIMEOUT_SECONDS = 0.5; // <-- tune this
    private static final int JAM_ENCODER_TOLERANCE = 3;      // ticks considered "no movement"
    private static final long UNJAM_REVERSE_MS = 200;        // how long to reverse
    private long lastMovementTime = 0;
    private int lastEncoderPosition = 0;
    private boolean unjamming = false;
    private long unjamStartTime = 0;
    private int jamDirection = 1;
    // Other
    public final double ROTATION_TICK = 756;
    private final double ONE_DEGREE = ROTATION_TICK/360;
    private final DcMotorEx drum;
    private final double power;
    public static double targetPosition;
    private static States.Artifact[] balls;
    private static States.DrumMode mode = States.DrumMode.OUTTAKE;
    public States.DrumState state = States.DrumState.IDLE;
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
        } else{
            resetDrum();
        }
    }
    public boolean hasBall(States.Artifact ball){
        for(States.Artifact b : balls){
            if(ball == b){
                return true;
            }
        }
        return false;
    }
    /**
     * Checks if all the balls are full
     * @return Whether or not all the balls are filled
     */
    public int countBalls(){
        int ballCount = 0;
        for(States.Artifact ball : balls){
            if(ball != States.Artifact.NONE){
                ballCount+=1;
            }
        }
        return ballCount;
    }

    /**
     * Moves the drum into a position to be ready for intake mode.
     */
    public void intakeMode(){
        if (mode == States.DrumMode.OUTTAKE){
            rotateSixth();
            mode = States.DrumMode.INTAKE;
            States.Artifact temp = balls[0];
            balls[0] = balls[2];
            balls[2] = balls[1];
            balls[1] = temp;
        }
    }

    private boolean encoderMoved() {
        int current = drum.getCurrentPosition();
        boolean moved = abs(current - lastEncoderPosition) > JAM_ENCODER_TOLERANCE;
        if (moved) {
            lastEncoderPosition = current;
            lastMovementTime = System.nanoTime();
        }
        return moved && abs(current - targetPosition) > JAM_ENCODER_TOLERANCE*10;
    }


    /**
     * Moves the drum into a position to be ready for outtake mode.
     */
    public void outtakeMode(){
        if (mode == States.DrumMode.INTAKE) {
            rotateSixth();
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

        // First call setup
        if (lastMovementTime == 0) {
            lastMovementTime = System.nanoTime();
            lastEncoderPosition = drum.getCurrentPosition();
        }

        // Handle unjamming
        if (unjamming) {
            if ((System.currentTimeMillis() - unjamStartTime) < UNJAM_REVERSE_MS) {
                drum.setPower((-power /5) * jamDirection);
                state = States.DrumState.MOVING;
                return;
            } else {
                // Resume normal motion
                unjamming = false;
                drum.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
        }
        // Normal movement
        else if (!reachedTarget()) {
            drum.setTargetPosition((int) targetPosition);
            drum.setPower(power);
            state = States.DrumState.MOVING;

            if(!encoderMoved()){
                double secondsStalled =
                        (System.nanoTime() - lastMovementTime) / 1e9;
                if (secondsStalled > JAM_TIMEOUT_SECONDS) {
                    triggerUnjam();
                }
            } else{
                lastMovementTime = System.nanoTime();
            }
        } else {
            drum.setPower(0);
            lastMovementTime = System.nanoTime();
            if (drum.getVelocity(AngleUnit.DEGREES) < 1) {
                state = States.DrumState.IDLE;
            }
        }
    }
    private void triggerUnjam() {
        unjamming = true;
        unjamStartTime = System.currentTimeMillis();

        // Determine direction toward target
        jamDirection = (targetPosition > drum.getCurrentPosition()) ? 1 : -1;

        drum.setPower(0);
        drum.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void postTelemetry(Telemetry telemetry){
        storageTelemetry(telemetry);
    }

    /**
     * Returns whether or not the drum has reached its target
     * @return If the drum is at or past the target position
     */
    private boolean reachedTarget(){
        return (!drum.isBusy() && abs(targetPosition - drum.getCurrentPosition()) <= (ONE_DEGREE*2));
    }

    /**
     * Gives telemetry for how much the drum has rotated
     * @param telemetry The telemetry to post to.
     */
    private void drumTelemetry(Telemetry telemetry){
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
        resetDrum();

    }
    private void resetDrum(){
        drum.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drum.setTargetPosition((int)targetPosition);
        drum.setTargetPositionTolerance((int)ONE_DEGREE/2);
        drum.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
