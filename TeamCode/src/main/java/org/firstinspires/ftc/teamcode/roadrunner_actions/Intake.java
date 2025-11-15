package org.firstinspires.ftc.teamcode.roadrunner_actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class intake {
    private DcMotor motor;
    private ColorSensor colorSensor;
    private DcMotor drum;
    private final int ROTATION_TICK;
    public int[] balls = new int[3];
    public intake(HardwareMap hardwareMap, int rotationTicks) {
        motor = hardwareMap.dcMotor.get("intake");
        ROTATION_TICK = rotationTicks;
    }
    public class Run implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized){
                motor.setPower(0.2);
                initialized = true;
            }
            int drumPos = drum.getTargetPosition();
            if (drumPos % (ROTATION_TICK / 3) <= ROTATION_TICK / 7) {
                drum.setTargetPosition((int) (drumPos / (ROTATION_TICK / 3)) * (ROTATION_TICK / 3) + ROTATION_TICK / 6);
            }
            if(isGreenOrPurple()!=-1
                    && drum.getCurrentPosition() >= drum.getTargetPosition()-ROTATION_TICK/16) {
                drum.setTargetPosition(drumPos+ROTATION_TICK/3);
                // Shift over everything in ball storage
                balls[2] = balls[1];
                balls[1] = balls[0];
                // Adds our new ball
                balls[0] = isGreenOrPurple();
            }
            return true;
        }
    }
    public class Stop implements Action {
        private boolean initialized = false;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if(!initialized){
                motor.setPower(0);
                initialized = true;
            }
            return false;
        }
    }
    public Action run() {
        return new Run();
    }
    public double[] getColor() {
        double red = colorSensor.red();
        double green = colorSensor.green();
        double blue = colorSensor.blue();
        return new double[]{red, green, blue};
    }
    public int isGreenOrPurple(){
        double[] colors = getColor();
        if(colors[1] > colors[2] * 1.5 && colors[1] > colors[0] * 1.5){
            return 0;
        }
        if (colors[2] > colors[1] * 1.2 && colors[0] > colors[1] * 1.2){
            return 1;
        }
        return -1;
    }
    public int[] getBalls(){
        return balls;
    }
}
public Action Run() {
    return new intake.Run();
}