package org.firstinspires.ftc.teamcode.commands;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.command.Command;

public class MoveSlideCommand extends Command {

    private int targetTicks;

    private DcMotor viperslide;

    public MoveSlideCommand(double inches) {
        if (inches < 0) inches = 0;
        if (inches > Constants.SLIDE_MAX_HEIGHT) inches = Constants.SLIDE_MAX_HEIGHT;

        targetTicks = (int) (inches * Constants.SLIDE_TICKS_PER_INCH);
    }

    @Override
    public void init() {

        viperslide = hardwareMap.get(DcMotor.class, "viperslide");
        viperslide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        viperslide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        viperslide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        viperslide.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void start() {


        viperslide.setTargetPosition(targetTicks);
        viperslide.setMode(DcMotor.RunMode.RUN_TO_POSITION);



    }

    @Override
    public void loop() {
        int error = viperslide.getTargetPosition() - viperslide.getCurrentPosition();
        double power = Constants.SLIDE_MAX_POWER;
        if (Math.abs(error) <= Constants.DRIVE_SPOOLING_THRESHOLD) {
            power = Constants.SLIDE_MIN_POWER +
                    (Constants.SLIDE_MAX_POWER - Constants.SLIDE_MIN_POWER) * (Math.abs(error) / Constants.DRIVE_SPOOLING_THRESHOLD);
        }

        viperslide.setPower(power);
        telemetry.addData("Slide Target", targetTicks);
        telemetry.addData("Slide Position", viperslide.getCurrentPosition());
        telemetry.addData("Slide Power", power);
        telemetry.update();
    }

    @Override
    public void finish(boolean interrupted) {
        viperslide.setPower(0);
        viperslide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public boolean isFinished() {
        return !viperslide.isBusy();
    }
}
