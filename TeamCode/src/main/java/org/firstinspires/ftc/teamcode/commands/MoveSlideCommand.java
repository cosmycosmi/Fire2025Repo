package org.firstinspires.ftc.teamcode.commands;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.command.Command;

public class MoveSlideCommand extends Command {

    private int targetTicks;

    private DcMotor slides;

    public MoveSlideCommand(double inches) {
        if (inches < 0) inches = 0;
        if (inches > Constants.SLIDE_MAX_HEIGHT) inches = Constants.SLIDE_MAX_HEIGHT;

        targetTicks = (int) (inches * Constants.SLIDE_TICKS_PER_INCH);
    }

    @Override
    public void init() {

        slides = hardwareMap.get(DcMotor.class, "slides");
        slides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slides.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void start() {


        slides.setTargetPosition(targetTicks);
        slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);



    }

    @Override
    public void loop() {
        int error = slides.getTargetPosition() - slides.getCurrentPosition();
        double power = Constants.SLIDE_MAX_POWER;
        if (Math.abs(error) <= Constants.DRIVE_SPOOLING_THRESHOLD) {
            power = Constants.SLIDE_MIN_POWER +
                    (Constants.SLIDE_MAX_POWER - Constants.SLIDE_MIN_POWER) * (Math.abs(error) / Constants.DRIVE_SPOOLING_THRESHOLD);
        }

        slides.setPower(power);
        telemetry.addData("Slide Target", targetTicks);
        telemetry.addData("Slide Position", slides.getCurrentPosition());
        telemetry.addData("Slide Power", power);
        telemetry.update();
    }

    @Override
    public void finish(boolean interrupted) {
        slides.setPower(0);
        slides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public boolean isFinished() {
        return !slides.isBusy();
    }
}
