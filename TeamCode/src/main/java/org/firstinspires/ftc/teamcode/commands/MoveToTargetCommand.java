package org.firstinspires.ftc.teamcode.commands;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.command.Command;


public class MoveToTargetCommand extends Command {

    private DcMotor lf, rf, lb, rb;
    private int lfTicks, rfTicks, lbTicks, rbTicks;

    private double speed;


    public MoveToTargetCommand(int lfTicks, int rfTicks, int lbTicks, int rbTicks, double speed) {

        this.lfTicks =lfTicks;
        this.lbTicks = lbTicks;
        this.rbTicks = rbTicks;
        this.rfTicks = rfTicks;

        this.speed = speed;
    }

    private static int calculateTurnTicks(double angle) {
        double wheelBaseDiameter = 16.0; // Distance between left and right wheels in inches (adjust for your robot)
        double wheelCircumference = Math.PI * wheelBaseDiameter; // Calculate the arc length for a full 360-degree turn
        double arcLength = (angle / 360.0) * wheelCircumference; // Arc length for the given angle
        return (int) (arcLength * Constants.DRIVE_TICKS_PER_INCH); // Convert arc length to encoder ticks
    }

    @Override
    public void init() {
        lf = hardwareMap.get(DcMotor.class, "lf");
        rf = hardwareMap.get(DcMotor.class, "rf");
        lb = hardwareMap.get(DcMotor.class, "lb");
        rb = hardwareMap.get(DcMotor.class, "rb");

        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    @Override
    public void start() {

        lf.setTargetPosition(lf.getCurrentPosition() + lfTicks);
        rf.setTargetPosition(rf.getCurrentPosition() + rfTicks);
        lb.setTargetPosition(lb.getCurrentPosition() + lbTicks);
        rb.setTargetPosition(rb.getCurrentPosition() + rbTicks);

        lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);


    }

    @Override
    public void loop() {

        double maxError = Math.max(Math.max(
                        Math.abs(lf.getTargetPosition() - lf.getCurrentPosition()),
                        Math.abs(rf.getTargetPosition() - rf.getCurrentPosition())),
                Math.max(
                        Math.abs(lb.getTargetPosition() - lb.getCurrentPosition()),
                        Math.abs(rb.getTargetPosition() - rb.getCurrentPosition())));

        double power = speed;
        if (maxError <= Constants.DRIVE_SPOOLING_THRESHOLD) {
            power = Constants.DRIVE_MIN_POWER + (speed - Constants.DRIVE_MIN_POWER) * (maxError / Constants.DRIVE_SPOOLING_THRESHOLD);
        }

        lf.setPower(power);
        rf.setPower(power);
        lb.setPower(power);
        rb.setPower(power);

    }

    @Override
    public void finish(boolean interrupted) {

        lf.setPower(0);
        rf.setPower(0);
        lb.setPower(0);
        rb.setPower(0);
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    @Override
    public boolean isFinished() {


        return !(lf.isBusy() || rf.isBusy() || lb.isBusy() || rb.isBusy());
    }
    public static Command forward(double inches) {
        int ticks = (int) (Constants.DRIVE_TICKS_PER_INCH * inches);
        return new MoveToTargetCommand(ticks, ticks, ticks, ticks, Constants.DRIVE_MAX_POWER);
    }
    public static Command back(double inches) {
        int ticks = (int) (Constants.DRIVE_TICKS_PER_INCH * inches);
        return new MoveToTargetCommand(-ticks, -ticks, -ticks, -ticks, Constants.DRIVE_MAX_POWER);
    }
    public static Command left(double inches) {
        int ticks = (int) (Constants.DRIVE_TICKS_PER_INCH * inches);
        return new MoveToTargetCommand(-ticks, ticks, ticks, -ticks, Constants.DRIVE_MAX_POWER);
    }
    public static Command right(double inches) {
        int ticks = (int) (Constants.DRIVE_TICKS_PER_INCH * inches);
        return new MoveToTargetCommand(ticks, -ticks, -ticks, ticks, Constants.DRIVE_MAX_POWER);
    }

    public static Command turnLeft(double angle) {
        int ticks = calculateTurnTicks(angle);
        return new MoveToTargetCommand(ticks, -ticks, ticks, -ticks, Constants.DRIVE_MAX_POWER);
    }

    public static Command turnRight(double angle) {
        int ticks = calculateTurnTicks(angle);
        return new MoveToTargetCommand(-ticks, ticks, -ticks, ticks, Constants.DRIVE_MAX_POWER);
    }

}
