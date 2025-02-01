package org.firstinspires.ftc.teamcode.commands;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.command.Command;

public class MoveIntakeWithComand extends Command {


    private DcMotor intakeextend;

    private int intakeextendPower;


    public MoveIntakeWithComand( int intakeextendPower){

this.intakeextendPower = intakeextendPower;



    }
    @Override
    public void init() {
        intakeextend = hardwareMap.get(DcMotor.class, "intakeextend");

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {

        intakeextend.setPower(intakeextendPower);

    }

    @Override
    public void finish(boolean interrupted) {
        intakeextend.setPower(0);
    }

    @Override
    public boolean isFinished() {
        return !intakeextend.isBusy();
    }
}
