package org.firstinspires.ftc.teamcode.robot_modules.drive;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public class PedroMecanum extends Drive<Gamepad> {
    private Follower follower;
    public PedroMecanum(HardwareMap hardwareMap){
        follower = Constants.create(hardwareMap);
        assert follower != null;
        follower.setPose(PoseStorage.pose());
    }
    @Override
    public void run(Gamepad gamepad) {
        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad.left_stick_y,
                gamepad.left_stick_x,
                gamepad.right_stick_x,
                follower.pose().heading()
        );
        ManualDrive.driveOrHold(follower, powers);
        follower.update();
    }

    @Override
    public void stop() {
        PoseStorage.setPose(follower.pose());
    }

    public void setPosition(Pose newPose){
        follower.setPose(newPose);
    }

    public void setHeading(double radians){
        follower.setHeading(radians);
    }

    @Override
    public void postTelemetry(Telemetry telemetry) {
        Pose robotPose = follower.pose();
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
    }
}
