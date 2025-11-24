package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(
                myBot.getDrive().actionBuilder(new Pose2d(32, 11, 54))

                        // FIRST CYCLE
                        .turnTo(Math.toRadians(-90))
                        .strafeTo(new Vector2d(12, -24))
                        .strafeTo(new Vector2d(12, -34.5))
                        .strafeTo(new Vector2d(12, -39.5))
                        .strafeTo(new Vector2d(12, -44.5))
                        .strafeTo(new Vector2d(12, -24))
                        .strafeTo(new Vector2d(24, -24))
                        .turnTo(Math.toRadians(135))

                        // SECOND CYCLE
                        .turnTo(Math.toRadians(-90))
                        .strafeTo(new Vector2d(-12, -24))
                        .strafeTo(new Vector2d(-12, -34.5))
                        .strafeTo(new Vector2d(-12, -39.5))
                        .strafeTo(new Vector2d(-12, -44.5))
                        .strafeTo(new Vector2d(-12, -24))
                        .strafeTo(new Vector2d(24, -24))
                        .turnTo(Math.toRadians(135))

                        // THIRD CYCLE
                        .turnTo(Math.toRadians(-90))
                        .strafeTo(new Vector2d(-36, -24))
                        .strafeTo(new Vector2d(-36, -34.5))
                        .strafeTo(new Vector2d(-36, -39.5))
                        .strafeTo(new Vector2d(-36, -44.5))
                        .strafeTo(new Vector2d(-36, -24))
                        .strafeTo(new Vector2d(24, -24))
                        .turnTo(Math.toRadians(135))

                        // GO HOME
                        .strafeTo(new Vector2d(24, 36))
                        .strafeTo(new Vector2d(-36, 36))
                        .turnTo(Math.toRadians(0))

                        .build()
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
