package org.firstinspires.ftc.teamcode.drive.Structure;

import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.allianceCase;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.blockArtifactServo_blockPos;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.blockArtifactServo_unblockPos;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.hoodAngleServoInitPose;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.hoodAngleServoMaxPose;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.hoodAngleServoMinPose;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.pushArtifactServo_pushPose;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.pushArtifactServo_retractPos;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.robotVelocityThreshold;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.servoPosPerDegree;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.turretServoInitPose;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.turretServoMaxPose;
import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.turretServoMinPose;

import android.graphics.Point;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.bylazar.configurables.annotations.Configurable;
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
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.OpMode.Decode_TeleOp;

@Config
@Configurable
public class Systems {
    public LimelightVision limelight = new LimelightVision();
    Gamepad gamepad1, gamepad2;
    MultipleTelemetry telemetry;
    Decode_TeleOp decode_teleOp;
    Follower follower;

    DcMotorEx Intake_LeftMotor;
    DcMotorEx Intake_RightMotor;
    DcMotorEx Outtake_LeftMotor;
    public DcMotorEx Outtake_RightMotor;

    DistanceSensor leftDistanceSensor;
    DistanceSensor rightDistanceSensor;

    Servo TurretServo;
    Servo HoodAngleServo;
    Servo BlockArtifactServo;
    Servo PushArtifactServo;


    public Systems(HardwareMap hwmap, Gamepad gmpd, Gamepad gmpd2, Telemetry telemetrys , Follower TeleopFollower){
        gamepad1 = gmpd;
        gamepad2 = gmpd2;
        //this.telemetry = telemetrys;
        this.follower = TeleopFollower;

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
    public double currentFlywheelPower = 0;
    public static double targetFlywheelVelocity = 1500;
    public double basketDistance;
    public boolean isRobotStationary;
    //public double turretAngle;

    public boolean pushServoPosIsPush;
    public boolean blockServoPosIsBlock;
    public boolean isRedAlliance;
    public double currentFlywheelRpm;
    public static double targetFlywheelRPM = 0;

    public static double kV = 0.0002;
    public static double kS = 0.09;
    public static double kP = 0.001;

    public ElapsedTime timer = new ElapsedTime();


    public enum ShooterState{
        IDLE,
        SPIN_UP,
        FEED
    }



    //Todo check basket coordinates
    public Point RedBasket = new Point(138,140);
    public Point BlueBasket = new Point(6,140);
    Point CommonBasket = new Point(72,-138);
    public Point Basket;
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
        updateFlywheel();
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

        basketDistance = Math.hypot(Basket.x-x_position , Basket.y-y_position);

        currentFlywheelRpm = getRpm();

        isRobotStationary = robotVelocity < robotVelocityThreshold;


        if(gamepad1.leftTriggerWasPressed()){
            intakeArtifacts();
            shotCount = 0;
        }
        if(gamepad1.leftBumperWasPressed()){
            stopIntake();
        }
        if(gamepad1.rightBumperWasPressed()){
            stopFlywheel();
        }
        if(gamepad1.guideWasPressed()){
            if(Basket == BlueBasket || Basket == RedBasket){
                Basket = CommonBasket;
            }else{
                systemsIsRedAlliance();//also sets the basket coordinates to the correct one based on selected alliance
            }
        }

        //<---------------------Hood and and turret servo manual movement--------------------->//
        if(gamepad1.dpadLeftWasPressed() && current_TurretServo_position > turretServoMinPose){
            current_TurretServo_position-=0.01;
            TurretServo.setPosition(current_TurretServo_position);
        }
        if (gamepad1.dpadRightWasPressed() && current_TurretServo_position < turretServoMaxPose) {
            current_TurretServo_position+=0.01;
            TurretServo.setPosition(current_TurretServo_position);
        }

        if(gamepad1.dpadUpWasPressed() && current_HoodAngleServo_position-0.05 >= hoodAngleServoMaxPose){
            current_HoodAngleServo_position -= 0.05;
            HoodAngleServo.setPosition(current_HoodAngleServo_position);
        }
        if(gamepad1.dpadDownWasPressed() && current_HoodAngleServo_position+0.05 <= hoodAngleServoMinPose ){
            current_HoodAngleServo_position+= 0.05;
            HoodAngleServo.setPosition(current_HoodAngleServo_position);
        }
        //<----------------------------------------------------------------------------------->//

        //<--------------------- Flywheel --------------------->//
        if(gamepad1.bWasPressed()){
            if(currentFlywheelPower<1){
                currentFlywheelPower+=0.1;
               // setFlyWheelMotorPower(currentFlywheelPower);
            }else{
                currentFlywheelPower = 1;
                //setFlyWheelMotorPower(currentFlywheelPower);
            }
        }
        if(gamepad1.xWasPressed()){
            if(currentFlywheelPower>0){
                currentFlywheelPower-=0.1;
                //setFlyWheelMotorPower(currentFlywheelPower);
            }else{
                currentFlywheelPower = 0;
                //setFlyWheelMotorPower(currentFlywheelPower);
            }
        }
        if(gamepad1.rightTriggerWasPressed()){
            setFlyWheelMotorPower(currentFlywheelPower);//todo add here the calculation for flywheel speed using formula
        }
        //<---------------------------------------------------->//


        //<--------------------- LimeLight camera position reset --------------------->//
        if(gamepad1.yWasPressed()){
            resetPositionUsingLimeLight();
        }
        //<--------------------------------------------------------------------------->//

        if(isInShootingZone()){
            double targetAngle = getTargetAngle(x_position, y_position, Basket.x, Basket.y);

            double turretAngle = normalizeTurretServo(targetAngle - headingAngle);

            TurretServo.setPosition(angleToServo(turretAngle));
            current_TurretServo_position = TurretServo.getPosition();

            calculateFlywheelMotorPowerRPM(basketDistance);
            calculateHoodAngle(basketDistance);

            double feedForward = (kV * targetFlywheelRPM) + kS;
            double error = targetFlywheelRPM - currentFlywheelRpm;
            double feedBack = error * kP;

        }

        if(gamepad1.aWasPressed()){
            BlockArtifactServo.setPosition(blockArtifactServo_unblockPos);
            //Outtake_RightMotor.setVelocity(targetFlywheelVelocity);
            //Outtake_LeftMotor.setVelocity(targetFlywheelVelocity);
            shotCount = 0;
            shootState = ShooterState.SPIN_UP;
        }

        switch (shootState){
            case SPIN_UP:
                if(Math.abs(Outtake_RightMotor.getVelocity() - targetFlywheelVelocity) < 30){

                    BlockArtifactServo.setPosition(blockArtifactServo_unblockPos);

                    if(shotCount == 2){
                        PushArtifactServo.setPosition(pushArtifactServo_pushPose);
                    }

                    timer.reset();

                    Intake_LeftMotor.setPower(1);
                    Intake_RightMotor.setPower(1);


                    shootState = ShooterState.FEED;
                }
                break;
            case FEED:
                if (Outtake_RightMotor.getVelocity() < targetFlywheelVelocity - rpmDrop || timer.milliseconds()>500){
                    Intake_RightMotor.setPower(0);
                    Intake_LeftMotor.setPower(0);

                    BlockArtifactServo.setPosition(blockArtifactServo_blockPos);

                    shotCount++;

                    if(shotCount >= 3){
                        shootState = ShooterState.IDLE;
                    }else{
                        shootState = ShooterState.SPIN_UP;
                    }
                }
                break;
            case IDLE:
                targetFlywheelRPM = 500;
                PushArtifactServo.setPosition(pushArtifactServo_retractPos);
                BlockArtifactServo.setPosition(blockArtifactServo_blockPos);
                Outtake_RightMotor.setVelocity(0);
                Outtake_LeftMotor.setVelocity(0);
                break;
        }

    }
    public ShooterState shootState = ShooterState.IDLE;
    public int shotCount = 0;
    public static int rpmDrop = 200;

    public void calculateFlywheelMotorPowerRPM(double distance){
        double power;
        power = 0*distance;//todo add the tested function
        //targetFlywheelRPM = power;

    }

    public void updateFlywheel() {

        double currentRPM = getRpm();

        double error = targetFlywheelRPM - currentRPM;

        double power = kS + kV * targetFlywheelRPM + kP * error;

        power = Math.max(0, Math.min(1, power));

        Outtake_LeftMotor.setPower(power);
        Outtake_RightMotor.setPower(power);
    }

    public void calculateHoodAngle(double distance){
        double hoodAnglePosition;

        hoodAnglePosition = 0*distance;

//        if(hoodAnglePosition > 0.75){
//            hoodAnglePosition = 0.75;
//        }else if(hoodAnglePosition < 0.25){
//            hoodAnglePosition = 0.25;
//        }adauga daca este necesar si imi iese din roata
    }
    public void setFlyWheelMotorPower(double power){
        Outtake_LeftMotor.setPower(power);
        Outtake_RightMotor.setPower(power);
        currentFlywheelPower = power;
    }
    public double angleToServo(double angle){

        return turretServoInitPose -
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
    public void stopFlywheel(){
        Outtake_LeftMotor.setPower(0);
        Outtake_RightMotor.setPower(0);
        currentFlywheelPower = 0;
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
    public double getTicksPerSec(){
        return Outtake_RightMotor.getVelocity();
    }

    public double getRpm(){
        double encoderCPM = 28;
        return ((getTicksPerSec()/encoderCPM)*60);// / gearRatio;
    }

}
