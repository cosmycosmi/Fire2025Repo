package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "BucketAuto")
public class bucketauto extends LinearOpMode {

    // Declare hardware variables
    private DcMotor lf, rf, lb, rb, Viperslide;
    private Servo al, clawServo;
    private DcMotor intakeextend;
    private Servo intakerotateright;
    private BNO055IMU imu;

    // Constants for drivetrain
    private static final double DRIVE_TICKS_PER_INCH = 41.8;
    private static final double DRIVE_MAX_POWER = 0.5;
    private static final double DRIVE_MIN_POWER = 0.2;
    private static final double DRIVE_SPOOLING_THRESHOLD = 800;

    // Constants for Viperslide
    private static final double SLIDE_TICKS_PER_INCH = 537.7 / 5; // 537.7 ticks per rotation, 5 inches per rotation
    private static final double SLIDE_MAX_HEIGHT = 100.0; // Max height in inches
    private static final double SLIDE_MAX_POWER = 1.0;
    private static final double SLIDE_MIN_POWER = 0.4; // Increased to 0.4
    private static final double SLIDE_SPOOLING_THRESHOLD = 400; // Separate threshold for slide

    @Override
    public void runOpMode() {
        // Initialize hardware mapping
        lf = hardwareMap.get(DcMotor.class, "lf");
        rf = hardwareMap.get(DcMotor.class, "rf");
        lb = hardwareMap.get(DcMotor.class, "lb");
        rb = hardwareMap.get(DcMotor.class, "rb");
        Viperslide = hardwareMap.get(DcMotor.class, "Viperslide");
        al = hardwareMap.get(Servo.class, "al");
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        intakeextend = hardwareMap.get(DcMotor.class, "intakeextend");
        intakerotateright = hardwareMap.get(Servo.class, "intakerotateright");

        // IMU initialization
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(parameters);

        // Set motor directions
        lf.setDirection(DcMotor.Direction.FORWARD);
        rf.setDirection(DcMotor.Direction.FORWARD);
        lb.setDirection(DcMotor.Direction.FORWARD);
        rb.setDirection(DcMotor.Direction.FORWARD);
        Viperslide.setDirection(DcMotor.Direction.REVERSE);

        // Set motors to use encoders
        setMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Viperslide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Viperslide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Viperslide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize servos
        al.setPosition(1);
        intakeextend.setPower(0);
        intakerotateright.setPosition(0.05);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the start button to be pressed
        waitForStart();

        // Start your autonomous movements
        Right(15,0.5);
        Forward(10);
        TurnLeft(46);
        MoveViperslideToHeight(44.5);
        Forward(16);
        al.setPosition(0.8);
        Backward(7);
        MoveViperslideToHeight(10);

    }

    // Reset and set mode for all drive motors
    private void setMotorsMode(DcMotor.RunMode mode) {
        lf.setMode(mode);
        rf.setMode(mode);
        lb.setMode(mode);
        rb.setMode(mode);
    }

    // Function to move forward/backward a specified distance (in inches)
    private void Forward(double inches) {
        int ticks = (int) (DRIVE_TICKS_PER_INCH * inches);
        moveToTarget(ticks, ticks, ticks, ticks, DRIVE_MAX_POWER);
    }

    private void Backward(double inches) {
        int ticks = (int) (DRIVE_TICKS_PER_INCH * inches);
        moveToTarget(-ticks, -ticks, -ticks, -ticks, DRIVE_MAX_POWER);
    }

    // Function to strafe left/right a specified distance (in inches)
    private void Left(double inches) {
        int ticks = (int) (DRIVE_TICKS_PER_INCH * inches);
        moveToTarget(-ticks, ticks, ticks, -ticks, DRIVE_MAX_POWER);
    }

    private void Right(double inches, double speed) {
        int ticks = (int) (DRIVE_TICKS_PER_INCH * inches);
        moveToTarget(ticks, -ticks, -ticks, ticks, speed);
    }

    private void Right(double inches) {
        Right(inches, DRIVE_MAX_POWER);
    }

    // Function to turn right by a specific angle using encoders
    private void TurnRight(double angle) {
        int ticks = calculateTurnTicks(angle);
        moveToTarget(-ticks, ticks, -ticks, ticks, DRIVE_MAX_POWER);
    }

    // Function to turn left by a specific angle using encoders
    private void TurnLeft(double angle) {
        int ticks = calculateTurnTicks(angle);
        moveToTarget(ticks, -ticks, ticks, -ticks, DRIVE_MAX_POWER);
    }

    // Function to calculate encoder ticks for a specific turn angle
    private int calculateTurnTicks(double angle) {
        double wheelBaseDiameter = 16.0; // Distance between left and right wheels in inches (adjust for your robot)
        double wheelCircumference = Math.PI * wheelBaseDiameter; // Calculate the arc length for a full 360-degree turn
        double arcLength = (angle / 360.0) * wheelCircumference; // Arc length for the given angle
        return (int) (arcLength * DRIVE_TICKS_PER_INCH); // Convert arc length to encoder ticks
    }

    // Move motors to target positions with timeout (reduced timeout for quicker execution)
    private void moveToTarget(int lfTicks, int rfTicks, int lbTicks, int rbTicks, double speed) {
        lf.setTargetPosition(lf.getCurrentPosition() + lfTicks);
        rf.setTargetPosition(rf.getCurrentPosition() + rfTicks);
        lb.setTargetPosition(lb.getCurrentPosition() + lbTicks);
        rb.setTargetPosition(rb.getCurrentPosition() + rbTicks);

        setMotorsMode(DcMotor.RunMode.RUN_TO_POSITION);

        ElapsedTime timer = new ElapsedTime();
        double timeout = 2.0; // Reduced timeout for faster movements

        while (opModeIsActive() && timer.seconds() < timeout &&
                (lf.isBusy() || rf.isBusy() || lb.isBusy() || rb.isBusy())) {
            double maxError = Math.max(Math.max(
                            Math.abs(lf.getTargetPosition() - lf.getCurrentPosition()),
                            Math.abs(rf.getTargetPosition() - rf.getCurrentPosition())),
                    Math.max(
                            Math.abs(lb.getTargetPosition() - lb.getCurrentPosition()),
                            Math.abs(rb.getTargetPosition() - rb.getCurrentPosition())));

            double power = speed;
            if (maxError <= DRIVE_SPOOLING_THRESHOLD) {
                power = DRIVE_MIN_POWER + (speed - DRIVE_MIN_POWER) * (maxError / DRIVE_SPOOLING_THRESHOLD);
            }

            lf.setPower(power);
            rf.setPower(power);
            lb.setPower(power);
            rb.setPower(power);
        }

        setMotorsPower(0);
        setMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // Function to stop all motors
    private void setMotorsPower(double power) {
        lf.setPower(power);
        rf.setPower(power);
        lb.setPower(power);
        rb.setPower(power);
    }

    // Function to move the Viperslide to a specific height in inches with timeout
    private void MoveViperslideToHeight(double heightInches) {
        if (heightInches < 0) heightInches = 0;
        if (heightInches > SLIDE_MAX_HEIGHT) heightInches = SLIDE_MAX_HEIGHT;

        int targetTicks = (int) (heightInches * SLIDE_TICKS_PER_INCH);
        Viperslide.setTargetPosition(targetTicks);
        Viperslide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        ElapsedTime timer = new ElapsedTime();
        double timeout = 3.0; // Extended timeout for Viperslide

        while (opModeIsActive() && timer.seconds() < timeout && Viperslide.isBusy()) {
            int error = Viperslide.getTargetPosition() - Viperslide.getCurrentPosition();
            double power = SLIDE_MAX_POWER;
            if (Math.abs(error) <= SLIDE_SPOOLING_THRESHOLD) {
                power = SLIDE_MIN_POWER +
                        (SLIDE_MAX_POWER - SLIDE_MIN_POWER) * (Math.abs(error) / SLIDE_SPOOLING_THRESHOLD);
            }

            Viperslide.setPower(power);
            telemetry.addData("Slide Target", targetTicks);
            telemetry.addData("Slide Position", Viperslide.getCurrentPosition());
            telemetry.addData("Slide Power", power);
            telemetry.update();
        }

        Viperslide.setPower(0);
        Viperslide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}