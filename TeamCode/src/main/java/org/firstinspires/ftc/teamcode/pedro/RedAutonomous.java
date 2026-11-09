package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.api.Paths;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;


public class RedAutonomous extends OpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(56, 8, 90);
    private final Pose startAuto = poseFactory.of(56.0964, 16.5964, 90);
    private final Pose collectCornerStart = poseFactory.of(56.0964, 16.5964, 90);
    private final Pose collectCorner = poseFactory.of(8.1093, 8.7964, 270);
    private final Pose collectCornerControl1 = poseFactory.of(21.5441, 20.8401, 0);
    private final Pose moveToGoal1Start = poseFactory.of(8.1093, 8.7964, 270);
    private final Pose moveToGoal1 = poseFactory.of(55.8963, 18.5748, 90);
    private final Pose moveToGoal1Control1 = poseFactory.of(24.1902, 24.0758, 0);
    private final Pose collectFower1Start = poseFactory.of(55.8963, 18.5748, 90);
    private final Pose collectFower1 = poseFactory.of(46.8695, 133.3289, 90);
    private final Pose collectFower1Control1 = poseFactory.of(63.6348, 88.7824, 0);
    private final Pose moveToGoal2Start = poseFactory.of(46.8695, 133.3289, 90);
    private final Pose moveToGoal2 = poseFactory.of(56.1027, 123.8463, 270);
    private final Pose moveToGoal2Control1 = poseFactory.of(48.7837, 114.951, 0);
    private final Pose collectFlower2Start = poseFactory.of(56.1027, 123.8463, 270);
    private final Pose collectFlower2 = poseFactory.of(132.2757, 94.9151, 360);
    private final Pose collectFlower2Control1 = poseFactory.of(78.1096, 104.9179, 0);
    private final Pose moveToGoal3Start = poseFactory.of(132.2757, 94.9151, 0);
    private final Pose moveToGoal3 = poseFactory.of(56.568, 18.1137, 90);
    private final Pose moveToGoal3Control1 = poseFactory.of(58.5975, 112.4965, 0);
    private final Pose moveToGoal3Control2 = poseFactory.of(76.7889, 91.7916, 0);
    private final Pose parkStart = poseFactory.of(56.568, 18.1137, 90);
    private final Pose park = poseFactory.of(10.1544, 101.8741, 90);
    private final Pose parkControl1 = poseFactory.of(8.2225, 77.9196, 0);

    public Path startAuto() {
        return Paths.line(start, startAuto).linear(start, startAuto);
    }

    public Path collectCorner() {
        return Paths.curve(collectCornerStart, collectCornerControl1, collectCorner).linear(collectCornerStart, collectCorner);
    }

    public Path moveToGoal1() {
        return Paths.curve(moveToGoal1Start, moveToGoal1Control1, moveToGoal1).linear(moveToGoal1Start, moveToGoal1);
    }

    public Path collectFower1() {
        return Paths.curve(collectFower1Start, collectFower1Control1, collectFower1).linear(collectFower1Start, collectFower1);
    }

    public Path moveToGoal2() {
        return Paths.curve(moveToGoal2Start, moveToGoal2Control1, moveToGoal2).linear(moveToGoal2Start, moveToGoal2);
    }

    public Path collectFlower2() {
        return Paths.curve(collectFlower2Start, collectFlower2Control1, collectFlower2).linear(collectFlower2Start, collectFlower2);
    }

    public Path moveToGoal3() {
        return Paths.curve(moveToGoal3Start, moveToGoal3Control1, moveToGoal3Control2, moveToGoal3).linear(moveToGoal3Start, moveToGoal3);
    }

    public Path park() {
        return Paths.curve(parkStart, parkControl1, park).linear(parkStart, park);
    }


    public enum pathState {
        START_AUTO,
        COLLECT_CORNER,
        MOVE_TO_GOAL1,
        COLLECT_FLOWER1,
        MOVE_TO_GOAL2,
        COLLECT_FLOWER2,
        MOVE_TO_GOAL3,
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
                follower.follow(collectCorner());
                pathState = pathState.MOVE_TO_GOAL1;
                break;
            case MOVE_TO_GOAL1:
                follower.follow(moveToGoal1());
                pathState = pathState.COLLECT_FLOWER1;
                break;
            case COLLECT_FLOWER1:
                follower.follow(collectFower1());
                pathState = pathState.MOVE_TO_GOAL2;
                break;
            case MOVE_TO_GOAL2:
                follower.follow(moveToGoal2());
                pathState = pathState.COLLECT_FLOWER2;
                break;
            case MOVE_TO_GOAL3:
                follower.follow(moveToGoal3());
                pathState = pathState.PARK;
                break;
            case PARK:
                follower.follow(park());
        }
    }

    public void setPathState(pathState newState) {
        pathState = newState;
    }


    @Override
    public void init() {
        pathState = pathState.START_AUTO;
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
    }

    @Override
    public void loop() {
        follower.update();
        statePathUpdate();
    }
}
