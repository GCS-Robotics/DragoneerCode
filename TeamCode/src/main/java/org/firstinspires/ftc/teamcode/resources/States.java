package org.firstinspires.ftc.teamcode.resources;

public class States {
    public enum General{
        IDLE,
        INTAKE,
        PRIMED,
        LAUNCHING
    }
    public enum Intake{
        IDLE,
        RUNNING
    }
    public enum Outtake{
        IDLE,
        PRIMED,
        READY,
        BRAKING,
    }
    public enum DrumMode{
        INTAKE,
        OUTTAKE
    }
    public enum DrumState{
        IDLE,
        MOVING
    }
}
