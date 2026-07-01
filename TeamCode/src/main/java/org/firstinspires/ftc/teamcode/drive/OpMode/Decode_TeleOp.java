package org.firstinspires.ftc.teamcode.drive.OpMode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.drive.Structure.Chasis;
import org.firstinspires.ftc.teamcode.drive.Structure.Systems;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp
public class Decode_TeleOp extends LinearOpMode {


    public Follower follower;
    Chasis chasis;
    MultipleTelemetry telemetry;
    Systems systems;

    public int allianceCase = 0; //case 0 for red 1 for blue

    @Override
    public void runOpMode() throws InterruptedException {
        follower = Constants.createFollower(hardwareMap);
        this.telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        chasis = new Chasis(follower,gamepad1);
        systems = new Systems(hardwareMap,gamepad1,gamepad2,telemetry,follower);

        while (opModeInInit()){
            if(gamepad1.dpadLeftWasPressed() || gamepad2.dpadLeftWasPressed()){
                allianceCase = 0;
            }else if (gamepad1.dpadRightWasPressed() || gamepad2.dpadRightWasPressed()){
                allianceCase = 1;
            }

            telemetry.addData("Alliance", allianceCase == 0 ? "Red" : "Blue");
            telemetry.update();

            idle();
        }

        waitForStart();

        systems.initServo();

        while(opModeIsActive()){
            chasis.run();

        }
    }


}
