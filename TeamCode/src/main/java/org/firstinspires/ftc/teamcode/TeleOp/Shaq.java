package org.firstinspires.ftc.teamcode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.SerialNumber;
@TeleOp
public class Shaq extends LinearOpMode {

//Motor Config

// 0:Right Drive Motor
// 1:Left Drive Motor
// 2:Shooter Motor
// 3:Intake Motor

    private DcMotor rightDriveMotor;
    private DcMotor leftDriveMotor;
    private DcMotor shooterMotor;
    private DcMotor intakeMotor;

    @Override
    public void runOpMode() throws InterruptedException {

        rightDriveMotor = hardwareMap.get(DcMotor.class, "rightDriveMotor");
        leftDriveMotor = hardwareMap.get(DcMotor.class, "leftDriveMotor");
        shooterMotor = hardwareMap.get(DcMotor.class, "shooterMotor");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");

        rightDriveMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        double driveMotorPower = .4;

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            if (gamepad1.left_stick_y > .3) {
                rightDriveMotor.setPower(driveMotorPower);
                leftDriveMotor.setPower(driveMotorPower);
            } else if (gamepad1.left_stick_y < -.3) {
                rightDriveMotor.setPower(-driveMotorPower);
                leftDriveMotor.setPower(-driveMotorPower);
            } else if (gamepad1.left_stick_x < -.3) {
                rightDriveMotor.setPower(driveMotorPower);
                leftDriveMotor.setPower(-driveMotorPower);
            } else if (gamepad1.left_stick_x > .3) {
                rightDriveMotor.setPower(-driveMotorPower);
                leftDriveMotor.setPower(driveMotorPower);
            } else {
                rightDriveMotor.setPower(0);
                leftDriveMotor.setPower(0);
            }

            if (gamepad1.a) {
                shooterMotor.setPower(1);
                intakeMotor.setPower(1);
            } else {
                shooterMotor.setPower(0);
                intakeMotor.setPower(0);
            }

            if (gamepad1.right_bumper) {
                intakeMotor.setPower(1);
                shooterMotor.setPower(-.7);
            } else {
                intakeMotor.setPower(0);
                shooterMotor.setPower(0);
            }


        }
    }
}