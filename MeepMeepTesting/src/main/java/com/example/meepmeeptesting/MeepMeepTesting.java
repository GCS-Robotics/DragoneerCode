package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(
                myBot.getDrive().actionBuilder(new Pose2d(0,0,0))
                        .turnTo(-90)
                        .lineToX(12)
                        .lineToY(-24)
                        .build());

        myBot.runAction(
                myBot.getDrive().actionBuilder(new Pose2d(12, -24, -90))
                        .lineToY(-34.5)
                        .lineToY(-39.5)
                        .lineToY(-44.5)
                        .build());
        myBot.runAction(
                myBot.getDrive().actionBuilder(new Pose2d(12, -44.5, -90))
                        .lineToY(-24)
                        .lineToX(24)
                        .turnTo(45)
                        .build());
        /// TODO: myBot.runAction(bobot.runOuttake());
        // Launch Balls



        myBot.runAction(
                myBot.getDrive().actionBuilder(new Pose2d(24, -24, 45))
                        .turnTo(-90)
                        .lineToX(-12)
                        .lineToY(-24)
                        .build());
        myBot.runAction(
                myBot.getDrive().actionBuilder(new Pose2d(-12, -24, -90))
                        .lineToY(-34.5)
                        .lineToY(-39.5)
                        .lineToY(-44.5)
                        .build());
        myBot.runAction(
                myBot.getDrive().actionBuilder(new Pose2d(-12, -44.5, -90))
                        .lineToY(-24)
                        .lineToX(24)
                        .turnTo(45)
                        .build());
        // Launch Balls




        myBot.runAction(
                myBot.getDrive().actionBuilder(new Pose2d(24, -24, 45))
                        .turnTo(-90)
                        .lineToX(-36)
                        .lineToY(-24)
                        .build());
        myBot.runAction(
                myBot.getDrive().actionBuilder(new Pose2d(-36, -24, -90))
                        .lineToY(-34.5)
                        .lineToY(-39.5)
                        .lineToY(-44.5)
                        .build());
        myBot.runAction(
                myBot.getDrive().actionBuilder(new Pose2d(-36, -44.5, -90))
                        .lineToY(-24)
                        .lineToX(24)
                        .turnTo(45)
                        .build());
        // Launch Balls

        //Go to home
        myBot.runAction(
                myBot.getDrive().actionBuilder(new Pose2d(24, -24, 45))
                        .lineToY(36)
                        .lineToX(-36)
                        .turnTo(0)
                        .build());




        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}