package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class BlueBotAuto {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setColorScheme(new ColorSchemeBlueDark())
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();


        myBot.runAction(
                myBot.getDrive().actionBuilder(new Pose2d(48, -52, Math.toRadians(45)))

                        // FIRST CYCLE
                        .strafeTo(new Vector2d(-12, -24))
                        .strafeTo(new Vector2d(-12, -34.5))
                        .strafeTo(new Vector2d(-12, -39.5))
                        .strafeTo(new Vector2d(-12, -44.5))
                        .strafeTo(new Vector2d(-12, -24))
                        .strafeTo(new Vector2d(-24, -24))
                        .turnTo(Math.toRadians(225))

                        // SECOND CYCLE
                        .turnTo(Math.toRadians(90))
                        .strafeTo(new Vector2d(12, -24))
                        .strafeTo(new Vector2d(12, -34.5))
                        .strafeTo(new Vector2d(12, -39.5))
                        .strafeTo(new Vector2d(12, -44.5))
                        .strafeTo(new Vector2d(12, -24))
                        .strafeTo(new Vector2d(-24, -24))
                        .turnTo(Math.toRadians(225))

                        // THIRD CYCLE
                        .turnTo(Math.toRadians(90))
                        .strafeTo(new Vector2d(36, -24))
                        .strafeTo(new Vector2d(36, -34.5))
                        .strafeTo(new Vector2d(36, -39.5))
                        .strafeTo(new Vector2d(36, -44.5))
                        .strafeTo(new Vector2d(36, -24))
                        .strafeTo(new Vector2d(-24, -24))
                        .turnTo(Math.toRadians(225))
                        .build()
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_BLACK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
