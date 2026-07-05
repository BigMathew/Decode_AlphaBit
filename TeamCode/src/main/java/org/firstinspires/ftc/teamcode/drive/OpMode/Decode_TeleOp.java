package org.firstinspires.ftc.teamcode.drive.OpMode;

import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.allianceCase;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drive.Structure.Chasis;
import org.firstinspires.ftc.teamcode.drive.Structure.Systems;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp
public class Decode_TeleOp extends LinearOpMode {


    public Follower follower;
    Chasis chasis;
    Systems systems;
    MultipleTelemetry telemetrys;
    //public int AllianceCase = allianceCase;



    @Override
    public void runOpMode() throws InterruptedException {
        follower = Constants.createFollower(hardwareMap);
        telemetrys = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        chasis = new Chasis(gamepad1,follower);
        systems = new Systems(hardwareMap,gamepad1,gamepad2,telemetrys,follower);

        while (opModeInInit()){
            if(gamepad1.dpadLeftWasPressed() || gamepad2.dpadLeftWasPressed()){
                allianceCase = 0;
            }else if (gamepad1.dpadRightWasPressed() || gamepad2.dpadRightWasPressed()){
                allianceCase = 1;
            }

            telemetrys.addData("Alliance", allianceCase == 0 ? "Red" : "Blue");
            telemetrys.update();

            systems.systemsIsRedAlliance(); //set the isRedAlliance boolean to true or false

            systems.initServo();

            idle();// remove if I cant change alliance more than once
        }

        waitForStart();

        chasis.setTeleopDrive();

        while(opModeIsActive()){
            systems.Run();
            chasis.run();

            telemetrys.addData("X position",systems.x_position);
            telemetrys.addData("Y position",systems.y_position);
            telemetrys.addData("heading",systems.headingAngle);
            telemetrys.addData("Limelight X position",systems.LLXPosition);
            telemetrys.addData("Limelight Y position",systems.LLYPosition);
            telemetrys.addData("Limelight Heading",systems.LLHeadingAngle);
            telemetrys.addData("-------------------","--------------");
            telemetrys.addData("is red alliance",systems.isRedAlliance);
            if(systems.Basket.x == systems.RedBasket.x){
                telemetrys.addData("Current selected basket","is red");
            }else if(systems.Basket.x == systems.BlueBasket.x){
                telemetrys.addData("Current selected basket","is blue");

            }else {
                telemetrys.addData("Current selected basket","is common");

            }
            telemetrys.addData("Robot is in shooting zone",systems.isInShootingZone());
            telemetrys.addData("robot is stationary",systems.isRobotStationary);
            telemetrys.addData("current hoodAngleServo position",systems.current_HoodAngleServo_position);
            telemetrys.addData("current turret servo positon",systems.current_TurretServo_position);

            telemetrys.update();
        }
    }


}
