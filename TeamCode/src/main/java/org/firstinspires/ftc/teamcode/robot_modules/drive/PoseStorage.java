package org.firstinspires.ftc.teamcode.robot_modules.drive;

import com.pedropathing.math.Pose;

public class PoseStorage {
    public static Pose autonomousEndPose = new Pose(0,0,0);
    public static void setPose(Pose pose){
        autonomousEndPose = pose;
    }
    public static Pose pose(){
        return autonomousEndPose;
    }
}
