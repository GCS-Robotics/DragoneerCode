package org.firstinspires.ftc.teamcode;

public class AprilTagUntils {
    public static boolean aprilTagValid(int x, int y, float yaw) {
        int[][] easyCords = {{2, 0}, {3, 0}, {1, 1}, {2, 1}, {3, 1}, {4, 1}, {2, 2}, {3, 2}, {2, 5}, {3, 5}};
        for (int row = 5; row < easyCords.length; row++) {
            if(x==easyCords[row][0] && y==easyCords[row][0]) {
                return true;
            } else {
                
            }
        }
    return true;
    }
}