package org.firstinspires.ftc.teamcode.commands;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.command.Command;

import java.util.Arrays;

public class LimelightCenterOnYellowSample extends Command {

    private Limelight3A limelight;
    private DcMotor lf, rf, lb, rb;
    private Servo al;

    private boolean finished = false;

    public LimelightCenterOnYellowSample() {

        //private boolean finished=false;
    }
        @Override
        public void init () {

            lf = hardwareMap.get(DcMotor.class, "lf");
            rf = hardwareMap.get(DcMotor.class, "rf");
            lb = hardwareMap.get(DcMotor.class, "lb");
            rb = hardwareMap.get(DcMotor.class, "rb");

            limelight = hardwareMap.get(Limelight3A.class, "limelight");
            telemetry.setMsTransmissionInterval(11);

            limelight.pipelineSwitch(3);
            limelight.start();

            telemetry.update();

        }


        @Override
        public void start () {
            LLStatus status = limelight.getStatus();
            telemetry.addData("Name", "%s",
                    status.getName());
            telemetry.addData("LL", "Temp: %.1fC, CPU: %.1f%%, FPS: %d",
                    status.getTemp(), status.getCpu(), (int) status.getFps());
            telemetry.addData("Pipeline", "Index: %d, Type: %s",
                    status.getPipelineIndex(), status.getPipelineType());

            telemetry.update();
        }

        @Override
        public void loop () {

            LLResult result = limelight.getLatestResult();

            if (result != null) {
                // Access general information
                Pose3D botpose = result.getBotpose();
                double captureLatency = result.getCaptureLatency();
                double targetingLatency = result.getTargetingLatency();
                double parseLatency = result.getParseLatency();
                telemetry.addData("LL Latency", captureLatency + targetingLatency);
                telemetry.addData("Parse Latency", parseLatency);
                telemetry.addData("PythonOutput", Arrays.toString(result.getPythonOutput()));

                if (result.isValid()) {
                    telemetry.addData("tx", result.getTx());
                    telemetry.addData("txnc", result.getTxNC());
                    telemetry.addData("ty", result.getTy());
                    telemetry.addData("tync", result.getTyNC());

                    telemetry.addData("Botpose", botpose.toString());
                    if (result.getTy() > 6) {

                        telemetry.addLine("its doing magic");

                      //  al.setPosition(.8);


                        lf.setPower(-.1);
                        lb.setPower(.1);
                        rf.setPower(-.1);
                        rb.setPower(.1);

                        //  telemetry.update();

                    } else if ((result.getTy()) < .5) {

                        telemetry.addLine("its doing negative magic");

                       // al.setPosition(.8);


                        lf.setPower(.1);
                        lb.setPower(-.1);
                        rf.setPower(.1);
                        rb.setPower(-.1);


                    } else {
                        //lf.setPower(0);
                        //lb.setPower(0);
                        //rf.setPower(0);
                        //rb.setPower(0);
                        finished = true;
                    }


                    telemetry.update();
                }


            } else {
                telemetry.addData("Limelight", "No data available");
            }

        }

        @Override
        public void finish ( boolean interrupted){
            lf.setPower(0);
            lb.setPower(0);
            rf.setPower(0);
            rb.setPower(0);
        }

        @Override
        public boolean isFinished () {
            return finished;
        }


    }


