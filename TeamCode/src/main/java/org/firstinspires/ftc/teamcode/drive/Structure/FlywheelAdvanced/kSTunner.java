package org.firstinspires.ftc.teamcode.drive.Structure.FlywheelAdvanced;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
@Config
public class kSTunner extends OpMode {
    // sa sa fie kS pana cand aproape porneste de pe loc da nu chiar aproape e acolo
    Flywheel flywheel = new Flywheel();

    public static double kS = 0;

    @Override
    public void init() {
        flywheel.init(hardwareMap);
    }

    @Override
    public void loop() {

        flywheel.setMotorPower(kS);

        telemetry.addData("kV","%,.6f",kS);
        telemetry.addData("RPM",flywheel.getRpm());
        telemetry.addData("Ticks per Sec", flywheel.getTicksPerSec());
    }
}
