package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.commands.MoveToTargetCommand;
import org.firstinspires.ftc.teamcode.command.Commands;
import org.firstinspires.ftc.teamcode.commands.MoveSlideCommand;
import org.firstinspires.ftc.teamcode.command.builtin.InstantCommand;

@TeleOp
public class MecanumWithHang3 extends LinearOpMode {

//configuration

//control hub

//motors
//1:rb
//3:rf

//servos
//0:intakeright
//1:intakeleft
//2:hang1
//3:intakeextend
//4:hang2
//5:intakerotateright

//expansion hub

//motors
//0:lf
//1:lb
//2:viperslide

//servos
//1:hang3
//2:hang4
//5:al

    // Declare motors and servos
    private DcMotor viperslide;
    private Servo al;
    private DcMotor activepower;
    private DcMotor activepowertwo;
    //private Servo clawServo;

    private DcMotor lf, lb, rf, rb;
    private CRServo intakeleft;
    private CRServo intakeright;
    private DcMotor intakelifterright;
    private DcMotor intakelifterleft;
    private Servo intakelifter;
    private DcMotor intakeextend;
    private Servo intakerotateright;

    private DcMotorSimple climeLeft;


    private DcMotorSimple climeRight;





    // Define constants
    private static final double TICKS_PER_ROTATION = 537.7; // Example value

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize motors and servos
        lf = hardwareMap.get(DcMotor.class, "lf");
        lb = hardwareMap.get(DcMotor.class, "lb");
        rf = hardwareMap.get(DcMotor.class, "rf");
        rb = hardwareMap.get(DcMotor.class, "rb");
        al = hardwareMap.get(Servo.class, "al");
        //  clawServo = hardwareMap.get(Servo.class, "clawServo");
        viperslide = hardwareMap.get(DcMotor.class, "viperslide");
        intakeleft = hardwareMap.get(CRServo.class, "intakeleft");
        intakeright = hardwareMap.get(CRServo.class, "intakeright");
        //  intakeextend = hardwareMap.get(DcMotor.class, "intakeextend");
        intakerotateright = hardwareMap.get(Servo.class, "intakerotateright");

        climeLeft = hardwareMap.get(DcMotor.class, "climeLeft");
        climeRight = hardwareMap.get(DcMotor.class, "climeRight");
        intakeextend = hardwareMap.get(DcMotor.class, "intakeextend");

        // Set motor directions
        lf.setDirection(DcMotorSimple.Direction.FORWARD);
        lb.setDirection(DcMotorSimple.Direction.FORWARD);
        viperslide.setDirection(DcMotor.Direction.REVERSE);

        // Set motors to use encoders
        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        viperslide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        viperslide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // intakeextend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //   intakeextend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {



            // Drive control
            double y = 0, x = 0, rx = 0;
            if (gamepad1.right_trigger < 0.5) {
                y = -gamepad1.left_stick_y * 0.6;
                x = gamepad1.left_stick_x * 0.6;
                rx = -gamepad1.right_stick_x;
            } else {
                y = -gamepad1.left_stick_y;
                x = gamepad1.left_stick_x;
                rx = -gamepad1.right_stick_x * 0.3;
            }

            // Calculate powers for each motor
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            // Set power to drive motors
            setMotorPowers(frontLeftPower, backLeftPower, frontRightPower, backRightPower);

            //added later
            // Control for the servo
            //  if (gamepad2.b) {
            //     al.setPosition(0.8); // open
            //  } else {
            //      al.setPosition(1); // closed
            //  }

//not used
            // Control for the claw servo
            //  if (gamepad2.left_stick_x > 0.1) {
            //        clawServo.setPosition(0.1);
            //    } else if (gamepad2.left_stick_x < -0.1) {
            //     clawServo.setPosition(0.8); // extended
            //   } else {
            //        clawServo.setPosition(0.4);
            //  }

//added later
            //     if (gamepad2.dpad_right) {
            //        intakerotateright.setPosition(0.8);
            //    } else {
            //        intakerotateright.setPosition(0.375);
            //    }

//added later
            // Control for viperslide
            //   if (gamepad2.right_stick_y > -.3) {
            //      viperslide.setPower(1);
            //   } else if (gamepad2.right_stick_y < .3) {
            //      viperslide.setPower(-1);
            //   //} else if (gamepad2.dpad_up) {
            //  //      viperslide.setPower(0.2);
            //   } else {
            //      viperslide.setPower(0);
            // }

//added later
            // Control for the intake
            // if (gamepad2.x) {
            //        intakeleft.setPower(0.3);
            //      intakeright.setPower(-0.3);
            //   } else if (gamepad2.a) {
            //       intakeleft.setPower(-1);
            //     intakeright.setPower(1);
            //   } else {
            //     intakeleft.setPower(0);
            //      intakeright.setPower(0);
            //  }

//added later
            //  if (gamepad2.dpad_up) {
            // Get the current position of the servo
            //     double currentPosition = intakeextend.getPosition();
//intakeextend.setPosition(currentPosition + 0.001);
            //           }
            //         if (gamepad2.dpad_left) {  // Increase the position by a small amount (e.g., 0.01)

            //   intakeextend.setPosition(.75);
            //intakeextend.setPosition(.75);
            //    }

            //need to change viper slide control

            if (gamepad2.right_bumper) {


                if (gamepad2.dpad_up) {
                    climeLeft.setPower(1);
                } else if (gamepad2.dpad_down) {
                    climeLeft.setPower(-1);
                } else if (gamepad2.left_stick_y > .5) {
                    climeLeft.setPower(-1);
                } else if (gamepad2.left_stick_y < -.5) {
                    climeLeft.setPower(1);
                } else {
                    climeLeft.setPower(0);
                }

                if (gamepad2.y) {
                    climeRight.setPower(1);
                } else if (gamepad2.a) {
                    climeRight.setPower(-1);
                } else if (gamepad2.left_stick_y > .5) {
                    climeRight.setPower(-1);
                } else if (gamepad2.left_stick_y < -.5) {
                    climeRight.setPower(1);
                } else {
                    climeRight.setPower(0);
                }

                if (gamepad2.right_stick_y < -.5) {
                    viperslide.setPower(1);
                } else if (gamepad2.right_stick_y > .5) {
                    viperslide.setPower(-1);
                } else {
                    viperslide.setPower(0);
                }

                // Optional: Add a small delay to avoid consuming too much CPU time
                idle();
            } else {

                // Control for the intake
                if (gamepad2.x) {
                    intakeleft.setPower(0.3);
                    intakeright.setPower(-0.3);
                } else if (gamepad2.a) {
                    intakeleft.setPower(-1);
                    intakeright.setPower(1);
                } else {
                    intakeleft.setPower(0);
                    intakeright.setPower(0);
                }

                if (gamepad2.right_stick_y < -.5) {
                    viperslide.setPower(1);
                } else if (gamepad2.right_stick_y > .5) {
                    viperslide.setPower(-1);
                } else {
                    viperslide.setPower(0);
                }

                if (gamepad2.dpad_right) {
                    intakerotateright.setPosition(0.5);
                } else {
                    intakerotateright.setPosition(0.05);
                }

                // // Control for the servo
                if (gamepad2.b) {
                    al.setPosition(0.8); // open
                } else {
                    al.setPosition(1); // closed
                }


                //if (intakeextend.getCurrentPosition() > 135) {
                //  intakeextend.setPower(-0.8);
                //} else if (intakeextend.getCurrentPosition()<1) {
                //    intakeextend.setPower(0.8);
                //}

                if (gamepad2.left_stick_y < -.5) {
                    intakeextend.setPower(-1);
                } else if (gamepad2.left_stick_y > .5) {
                    intakeextend.setPower(1);
                } else {
                    intakeextend.setPower(0);
                }

                if (gamepad2.dpad_up) {
                   // addCommands(
                    new MoveSlideCommand(11);

                }

                if (gamepad2.dpad_left) {
                    new MoveSlideCommand(30);

                }

                // Control for intake extend
                // if (Math.abs(gamepad2.right_stick_y) > 0.1) {
                //   intakeextend.setPosition(0.5 + (gamepad2.right_stick_y * 0.5));  // Clamp this to a reasonable range
                //    } else {
                //      intakeextend.setPosition(0.5);  // Default position
                //    }

                // Telemetry for debugging
                telemetry.addData("Front Left Position", lf.getCurrentPosition());
                telemetry.addData("Front Right Position", rf.getCurrentPosition());
                telemetry.addData("Back Left Position", lb.getCurrentPosition());
                telemetry.addData("Back Right Power", rb.getCurrentPosition());
                //  telemetry.addData("Claw Position", clawServo.getPosition());
                telemetry.addData("viperslide Power", viperslide.getPower());
                telemetry.addData("AL Position", al.getPosition());
                telemetry.addData("Intake Left Power", intakeleft.getPower());
                telemetry.addData("Intake Right Power", intakeright.getPower());
                telemetry.addData("Intake Extend Position", intakeextend.getPower());
                telemetry.update();
            }
        }
    }

    void setMotorPowers(double frontLeftPower, double backLeftPower, double frontRightPower, double backRightPower) {
        lf.setPower(frontLeftPower);
        lb.setPower(backLeftPower);
        rf.setPower(frontRightPower);
        rb.setPower(backRightPower);
    }


}