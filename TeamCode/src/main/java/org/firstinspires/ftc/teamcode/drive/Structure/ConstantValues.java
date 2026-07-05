package org.firstinspires.ftc.teamcode.drive.Structure;

import com.acmerobotics.dashboard.config.Config;

@Config
public class ConstantValues {
    //alliance case
    public static int allianceCase = 0; //0 for red, 1 for blue


    public static double turretServoInitPose = 0.74;
    public static double turretServoMaxPose = 0.95;
    public static double turretServoMinPose = 0.46;

    public static double hoodAngleServoInitPose = 0.45;
    public static double hoodAngleServoMaxPose = 0.15;
    public static double hoodAngleServoMinPose = 0.9;

    public static double blockArtifactServo_blockPos = 0.15;
    public static double blockArtifactServo_unblockPos = 0.02;

    public static double pushArtifactServo_pushPose = 0.55;
    public static double pushArtifactServo_retractPos = 0.8;


    // other static values that I might need to change later
    public static double robotVelocityThreshold = 2.5;
    public static double servoPosPerDegree = (0.74-0.51)/90;
}
