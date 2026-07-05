package org.firstinspires.ftc.teamcode.drive.Structure.FlywheelAdvanced;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@TeleOp
@Config
public class kSTunner extends OpMode {
    // sa sa fie kS pana cand aproape porneste de pe loc da nu chiar aproape e acolo
    Flywheel flywheel = new Flywheel();

    public static double kS = 0.09;

    @Override
    public void init() {
        flywheel.init(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    @Override
    public void loop() {

        flywheel.setMotorPower(kS);

        telemetry.addData("kS","%,.6f",kS);
        telemetry.addData("RPM",flywheel.getRpm());
        telemetry.addData("Ticks per Sec", flywheel.getTicksPerSec());
    }
}
