package org.firstinspires.ftc.teamcode.drive.Structure.FlywheelAdvanced;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Flywheel {
    private DcMotorEx Outtake_LeftMotor;
    private DcMotorEx Outtake_RightMotor;

    private double encoderCPM = 28;
    private double gearRatio = 1;

    private double kV,kS,kP;

    public void init(HardwareMap hwMap){
        Outtake_LeftMotor = hwMap.get(DcMotorEx.class, "Outtake_LeftMotor");
        Outtake_RightMotor = hwMap.get(DcMotorEx.class, "Outtake_RightMotor");
        Outtake_LeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Outtake_RightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Outtake_LeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        Outtake_RightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void setMotorPower(double power){
        Outtake_RightMotor.setPower(power);
        Outtake_LeftMotor.setPower(power);
    }
    public double getTicksPerSec(){
        return Outtake_LeftMotor.getVelocity();
    }

    public double getRpm(){
        return ((getTicksPerSec()/encoderCPM)*60) / gearRatio;
    }


}
