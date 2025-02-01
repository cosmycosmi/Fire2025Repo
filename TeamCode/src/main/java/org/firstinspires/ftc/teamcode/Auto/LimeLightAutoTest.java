package org.firstinspires.ftc.teamcode.Auto;




import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
//import org.firstinspires.ftc.robotcore.external.networktables.NetworkTable;
//import org.firstinspires.ftc.robotcore.external.networktables.NetworkTableInstance;


@Autonomous
public class LimeLightAutoTest extends LinearOpMode {


    // Motors and servos (same as in MecanumWithHang2)
    private DcMotor lf, lb, rf, rb, Viperslide;
    private CRServo intakeleft, intakeright, hang1, hang2, hang3, hang4;
    private Servo al, intakeextend, intakerotateright;


    // Limelight NetworkTable for vision processing
    private double limelightTable;


    // Limelight-specific constants
    private static final double MIN_SPEED = 0.3; // Min speed for rotation
    private static final double TARGET_AREA_THRESHOLD = 1000; // Threshold for target area


    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize hardware
        lf = hardwareMap.get(DcMotor.class, "lf");
        lb = hardwareMap.get(DcMotor.class, "lb");
        rf = hardwareMap.get(DcMotor.class, "rf");
        rb = hardwareMap.get(DcMotor.class, "rb");
        al = hardwareMap.get(Servo.class, "al");
        Viperslide = hardwareMap.get(DcMotor.class, "Viperslide");
        intakeleft = hardwareMap.get(CRServo.class, "intakeleft");
        intakeright = hardwareMap.get(CRServo.class, "intakeright");
        intakeextend = hardwareMap.get(Servo.class, "intakeextend");
        intakerotateright = hardwareMap.get(Servo.class, "intakerotateright");
        hang1 = hardwareMap.get(CRServo.class, "hang1");
        hang2 = hardwareMap.get(CRServo.class, "hang2");
        hang3 = hardwareMap.get(CRServo.class, "hang3");
        hang4 = hardwareMap.get(CRServo.class, "hang4");


        // Initialize Limelight NetworkTable
    //    limelightTable = NetworkTableInstance.getDefault().getTable("limelight");


        // Set motor directions and encoders
        setupMotors();


        waitForStart();


        if (isStopRequested()) return;


        while (opModeIsActive()) {
            // Get Limelight data
            //double tx = limelightTable.getEntry("tx").getDouble(0.0); // Horizontal offset
           // double ta = limelightTable.getEntry("ta").getDouble(0.0); // Target area

            double tx = limelightTable;("tx").equals(0.0); // Horizontal offset
            double ta = limelightTable;("ta").equals(0.0); // Target area

            // Telemetry for debugging
            telemetry.addData("tx", tx);  // Horizontal offset
            telemetry.addData("ta", ta);  // Target area
            telemetry.update();


            // Calculate centering adjustment based on tx (horizontal offset)
            double x = 0;
            if (tx > 1) {
                // Target is to the right, adjust robot to the left
                x += MIN_SPEED;
            } else if (tx < -1) {
                // Target is to the left, adjust robot to the right
                x -= MIN_SPEED;
            }


            // We are only adjusting the X-axis to center
            double y = 0;  // No forward/backward movement in this example
            double rx = 0; // No rotation either (keep robot straight)


            // Apply mecanum drive logic based on Limelight centering adjustments
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;


            // Set powers to the motors to drive the robot
            setMotorPowers(frontLeftPower, backLeftPower, frontRightPower, backRightPower);


            // Stop if target is centered (optional: you can add a stopping condition when centered)
            if (Math.abs(tx) < 1) {
                break;  // Target is centered, so we stop the robot
            }
        }


        // Optionally stop all motors at the end of the operation
        stopAllMotors();
    }


    private void setupMotors() {
        // Motor directions
        lf.setDirection(DcMotor.Direction.FORWARD);
        lb.setDirection(DcMotor.Direction.FORWARD);
        rf.setDirection(DcMotor.Direction.REVERSE);
        rb.setDirection(DcMotor.Direction.REVERSE);
        Viperslide.setDirection(DcMotor.Direction.REVERSE);


        // Set motor modes
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Viperslide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Viperslide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }


    private void setMotorPowers(double frontLeftPower, double backLeftPower, double frontRightPower, double backRightPower) {
        lf.setPower(frontLeftPower);
        lb.setPower(backLeftPower);
        rf.setPower(frontRightPower);
        rb.setPower(backRightPower);
    }


    private void stopAllMotors() {
        lf.setPower(0);
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);
        Viperslide.setPower(0);
    }
}

