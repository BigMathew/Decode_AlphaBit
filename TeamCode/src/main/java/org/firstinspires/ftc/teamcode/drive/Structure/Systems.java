package org.firstinspires.ftc.teamcode.drive.Structure;

import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.allianceCase;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.blockArtifactServo_blockPos;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.hoodAngleServoInitPose;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.hoodAngleServoMaxPose;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.hoodAngleServoMinPose;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.pushArtifactServo_retractPos;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.robotVelocityThreshold;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.servoPosPerDegree;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.turretServoInitPose;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.turretServoMaxPose;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.turretServoMinPose;

import android.graphics.Point;
import android.telephony.CarrierConfigManager;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Systems {
    public LimelightVision limelight = new LimelightVision();
    Gamepad gamepad1, gamepad2;
    MultipleTelemetry telemetry;
    Follower follower;

    DcMotorEx Intake_LeftMotor;
    DcMotorEx Intake_RightMotor;
    DcMotorEx Outtake_LeftMotor;
    DcMotorEx Outtake_RightMotor;

    DistanceSensor leftDistanceSensor;
    DistanceSensor rightDistanceSensor;

    Servo TurretServo;
    Servo HoodAngleServo;
    Servo BlockArtifactServo;
    Servo PushArtifactServo;


    public Systems(HardwareMap hwmap, Gamepad gmpd, Gamepad gmpd2, MultipleTelemetry telemetrys ,Follower follower){
        gamepad1 = gmpd;
        gamepad2 = gmpd2;
        this.telemetry = telemetrys;
        this.follower = follower;

        limelight.init(hwmap);


        Intake_LeftMotor = hwmap.get(DcMotorEx.class, "Intake_LeftMotor");
        Intake_RightMotor = hwmap.get(DcMotorEx.class, "Intake_RightMotor");

        Outtake_LeftMotor = hwmap.get(DcMotorEx.class, "Outtake_LeftMotor");
        Outtake_RightMotor = hwmap.get(DcMotorEx.class, "Outtake_RightMotor");

        Intake_LeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        Intake_RightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        Outtake_LeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        Outtake_RightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        Outtake_LeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Outtake_LeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Outtake_RightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Outtake_RightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Intake_LeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Intake_RightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Outtake_LeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Outtake_RightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        //Todo change the names of the servos in driver hub to more accurately represent the code
        TurretServo = hwmap.get(Servo.class,"LeftTurret");
        HoodAngleServo = hwmap.get(Servo.class, "AngleTurret");
        BlockArtifactServo = hwmap.get(Servo.class, "BlockArtifact");
        PushArtifactServo = hwmap.get(Servo.class,"PushLastArtifact");

        leftDistanceSensor = hwmap.get(DistanceSensor.class,"leftDistanceSensor");
        rightDistanceSensor = hwmap.get(DistanceSensor.class, "rightDistanceSensor");

    }

    public double current_TurretServo_position = turretServoInitPose;
    public double current_HoodAngleServo_position = hoodAngleServoInitPose;

    public LLResult llResult;
    public void initServo(){
        HoodAngleServo.setPosition(hoodAngleServoInitPose);
        TurretServo.setPosition(turretServoInitPose);
        PushArtifactServo.setPosition(pushArtifactServo_retractPos);
        pushServoPosIsPush = false;
        BlockArtifactServo.setPosition(blockArtifactServo_blockPos);
        blockServoPosIsBlock = true;
    }

    public double leftDistanceSensor_distance = 0.0;
    public double rightDistanceSensor_distance = 0.0;
    public double headingAngle;
    public double LLHeadingAngle;
    public double x_position;
    public double y_position;
    public double LLXPosition;
    public double LLYPosition;
    public double robotVelocity;

    public boolean isRobotStationary;
    //public double turretAngle;

    public boolean pushServoPosIsPush;
    public boolean blockServoPosIsBlock;
    public boolean isRedAlliance;

    //Todo check basket coordinates
    Point RedBasket = new Point(138,140);
    Point BlueBasket = new Point(6,140);
    Point CommonBasket = new Point(72,-138);
    Point Basket;
    public void systemsIsRedAlliance(){    //also sets the basket coordinates to the correct one based on selected alliance


        isRedAlliance = allianceCase == 0;

        if(isRedAlliance){
            Basket = RedBasket;
        } else{
            Basket = BlueBasket;
        }
    }
    public void Run(){


        follower.update();
        llResult = limelight.getLLResult();

        leftDistanceSensor_distance = leftDistanceSensor.getDistance(DistanceUnit.CM);
        rightDistanceSensor_distance = rightDistanceSensor.getDistance(DistanceUnit.CM);

        headingAngle = Math.toDegrees(follower.getPose().getHeading());
        headingAngle = (headingAngle % 360 + 360) % 360;
        LLHeadingAngle = limelightheadingToPedro(llResult.getBotpose().getOrientation().getYaw()); //degrees

        x_position = follower.getPose().getX();
        y_position = follower.getPose().getY();

        LLXPosition = convertFTCCordsToPedroX(llResult.getBotpose().getPosition().y * 39.37008);
        LLYPosition = convertFTCCordsToPedroY(llResult.getBotpose().getPosition().x * 39.37008);

        robotVelocity = Math.abs(follower.getVelocity().getXComponent()) + Math.abs(follower.getVelocity().getYComponent());

        if(robotVelocity < robotVelocityThreshold){
            isRobotStationary = true;
        }else{
            isRobotStationary = false;
        }

        if(gamepad1.xWasPressed()){
            resetPositionUsingLimeLight();
        }
        if(gamepad1.aWasPressed()){
            intakeArtifacts();
        }
        if(gamepad1.bWasPressed()){
            stopIntake();
        }
        if(gamepad1.guideWasPressed()){
            if(Basket == BlueBasket || Basket == RedBasket){
                Basket = CommonBasket;
            }else{
                systemsIsRedAlliance();//also sets the basket coordinates to the correct one based on selected alliance
            }
        }

        //<---------------------Hood and and turret servo manual movement--------------------->//
        if(gamepad1.dpadLeftWasPressed() && current_TurretServo_position < turretServoMaxPose){
            current_TurretServo_position+=0.01;
            TurretServo.setPosition(current_TurretServo_position);
        } else if (gamepad1.dpadRightWasPressed() && current_TurretServo_position < turretServoMinPose) {
            current_TurretServo_position-=0.01;
            TurretServo.setPosition(current_TurretServo_position);
        }

        if(gamepad1.dpadUpWasPressed() && current_HoodAngleServo_position-0.05 >= hoodAngleServoMaxPose){
            current_HoodAngleServo_position -= 0.05;
            HoodAngleServo.setPosition(current_HoodAngleServo_position);
        }else if(gamepad1.dpadDownWasPressed() && current_HoodAngleServo_position+0.05>=hoodAngleServoMinPose){
            current_HoodAngleServo_position+= 0.05;
            HoodAngleServo.setPosition(current_HoodAngleServo_position);
        }
        //<----------------------------------------------------------------------------------->//



        while(isInShootingZone()){
            double targetAngle = getTargetAngle(
                    x_position,
                    y_position,
                    Basket.x,
                    Basket.y);

            double turretAngle = normalizeTurretServo(
                    targetAngle - headingAngle);

            TurretServo.setPosition(angleToServo(turretAngle));
        }

    }
    public double angleToServo(double angle){

        return turretServoInitPose +
                angle * servoPosPerDegree;
    }

    public double normalizeTurretServo(double angle){
        while (angle > 180){
            angle -=360;
        }
        while (angle < -180){
            angle += 360;
        }
        return angle;
    }
    public double getTargetAngle(double robotX,double robotY,
                                 double targetX,double targetY){
        return Math.toDegrees(
                Math.atan2(targetY-robotY,
                        targetX-robotX));
    }

    public boolean isInShootingZone(){
        return inCommonGoalZone(x_position, y_position) || inFarShootingZone(x_position, y_position) || inCloseShootingZone(x_position, y_position);
    }

    public static boolean inCommonGoalZone(double x, double y) {
        return y >= -144 &&
                y <= -72 &&
                Math.abs(x - 72) <= (y + 144) / 2.0;
    }
    public static boolean inFarShootingZone(double x, double y) {
        return Math.abs(x - 72) + Math.abs(y) <= 24;
    }
    public static boolean inCloseShootingZone(double x, double y) {
        return y >= 72 &&
                y <= 144 &&
                Math.abs(x - 72) <= 144 - y;
    }

    public void stopIntake(){
        Intake_RightMotor.setPower(0);
        Intake_LeftMotor.setPower(0);
    }
    public void intakeArtifacts(){
        BlockArtifactServo.setPosition(blockArtifactServo_blockPos);
        PushArtifactServo.setPosition(pushArtifactServo_retractPos);
        Intake_LeftMotor.setPower(1);
        Intake_RightMotor.setPower(1);
    }
    public void resetPositionUsingLimeLight(){
        if(LLXPosition != 72 && LLYPosition !=72 && LLHeadingAngle != 0){
            follower.setPose(new Pose(LLXPosition,LLYPosition,Math.toRadians(LLHeadingAngle)));
            gamepad1.rumble(100);
        }
    }

    public double convertFTCCordsToPedroX(double x){
        double pedroX;
        if(x>=0){
            pedroX = 72 + x;
        }else{
            pedroX = 72 - Math.abs(x);
        }
        return pedroX;
    }

    public double convertFTCCordsToPedroY(double y){
        double pedroY;
        if(y>=0){
            pedroY = 72 - y;
        }else{
            pedroY = 72 + Math.abs(y);
        }
        return pedroY;
    }
    public double normalizeHeadingTo360(double angle) {
        angle %= 360;
        if (angle < 0) angle += 360;
        return angle;
    }
    public double limelightheadingToPedro(double limelightHeading) {
        double normalized = normalizeHeadingTo360(limelightHeading);
        return normalizeHeadingTo360(normalized - 90);
    }
}
