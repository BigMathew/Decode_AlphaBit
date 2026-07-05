package org.firstinspires.ftc.teamcode.drive.Structure;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drive.OpMode.Decode_TeleOp;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;


public class Chasis {
    Gamepad gamepad;
     Follower  CHASISfollower;
    //HardwareMap hardwareMap;
   // Decode_TeleOp decode_teleOp;

    public Chasis(Gamepad gamepad,Follower follower){
        this.gamepad = gamepad;
        this.CHASISfollower = follower;
    }
    public void setTeleopDrive(){
        CHASISfollower.startTeleOpDrive(true);
    }
    public void run(){

        CHASISfollower.setTeleOpDrive(
                -gamepad.left_stick_y,
                -gamepad.left_stick_x,
                -gamepad.right_stick_x,
                true
        );
        CHASISfollower.update();
    }
}
