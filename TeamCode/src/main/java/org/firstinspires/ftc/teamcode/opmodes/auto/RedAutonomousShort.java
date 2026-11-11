package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot_modules.Intake;
import org.firstinspires.ftc.teamcode.robot_modules.flowers.DeployableRamp;
import org.firstinspires.ftc.teamcode.robot_modules.outtake.AdjustableOuttake;
import org.firstinspires.ftc.teamcode.robot_modules.transition.MotorTransition;

public class RedAutonomousShort extends OpMode {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private Follower follower;
    boolean forwards = true;
    boolean backwards = false;
    public Boolean[] directions = {forwards, backwards};
    Intake intake;

    DeployableRamp deployableRamp;

    MotorTransition motorTransition;

    AdjustableOuttake adjustableOuttake;

    private final Pose start = poseFactory.of(56, 8, 90);
    private final Pose startAuto = poseFactory.of(56.724, 12.8304, 90);
    private final Pose collectCornerStart = poseFactory.of(56.724, 12.8304, 90);
    private final Pose collectCorner = poseFactory.of(8.5973, 9.127, 270);
    private final Pose collectCornerControl1 = poseFactory.of(19.4365, 18.8109, 0);
    private final Pose moveToGoal1Start = poseFactory.of(8.5973, 9.127, 270);
    private final Pose moveToGoal1 = poseFactory.of(58.5447, 118.2873, 270);
    private final Pose moveToGoal1Control1 = poseFactory.of(13.9493, 86.0099, 0);
    private final Pose collectFlowerStart = poseFactory.of(58.5447, 118.2873, 270);
    private final Pose collectFlower = poseFactory.of(46.6854, 130.0885, 90);
    private final Pose moveToGoal2Start = poseFactory.of(46.6854, 130.0885, 90);
    private final Pose moveToGoal2 = poseFactory.of(58.4523, 118.1762, 270);
    private final Pose point6Start = poseFactory.of(58.4523, 118.1762, 270);
    private final Pose point6 = poseFactory.of(10.0575, 102.06, 270);

    public Path startAuto() {
        return Paths.line(start, startAuto).linear(start, startAuto);
    }

    public Path collectCorner() {
        return Paths.curve(collectCornerStart, collectCornerControl1, collectCorner).linear(collectCornerStart, collectCorner);
    }

    public Path moveToGoal1() {
        return Paths.curve(moveToGoal1Start, moveToGoal1Control1, moveToGoal1).linear(moveToGoal1Start, moveToGoal1);
    }

    public Path collectFlower() {
        return Paths.line(collectFlowerStart, collectFlower).linear(collectFlowerStart, collectFlower);
    }

    public Path moveToGoal2() {
        return Paths.line(moveToGoal2Start, moveToGoal2).linear(moveToGoal2Start, moveToGoal2);
    }

    public Path park() {
        return Paths.line(point6Start, point6).linear(point6Start, point6);
    }

    public enum pathState {
        START_AUTO,
        COLLECT_CORNER,
        MOVE_TO_GOAL1,
        COLLECT_FLOWER1,
        MOVE_TO_GOAL2,
        PARK
    }

    pathState pathState;

    public void statePathUpdate() {
        switch(pathState) {
            case START_AUTO:
                follower.follow(startAuto());
                pathState = pathState.COLLECT_CORNER;
                break;
            case COLLECT_CORNER:
                intake.run(true);
                follower.follow(collectCorner());
                pathState = pathState.MOVE_TO_GOAL1;
                break;
            case MOVE_TO_GOAL1:
                intake.stop();
                motorTransition.run(directions);
                follower.follow(moveToGoal1());
                adjustableOuttake.run(true);
                pathState = pathState.COLLECT_FLOWER1;
                break;
            case COLLECT_FLOWER1:
                motorTransition.stop();
                deployableRamp.run(true);
                intake.run(true);
                follower.follow(collectFlower());
                pathState = pathState.MOVE_TO_GOAL2;
                break;
            case MOVE_TO_GOAL2:
                deployableRamp.stop();
                intake.stop();
                motorTransition.run(directions);
                follower.follow(moveToGoal2());
                pathState = pathState.PARK;
                break;
            case PARK:
                motorTransition.stop();
                follower.follow(park());
        }
    }

    @Override
    public void init() {
        pathState = pathState.START_AUTO;
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        intake = new Intake(hardwareMap, "intake");
        deployableRamp = new DeployableRamp(hardwareMap, "deployableRamp");
        motorTransition = new MotorTransition(hardwareMap, "transition");
        adjustableOuttake = new AdjustableOuttake(hardwareMap, "flywheel", "hood", "gap", "yaw", "limelight");
    }

    @Override
    public void loop() {
        follower.update();
        statePathUpdate();
    }
}
