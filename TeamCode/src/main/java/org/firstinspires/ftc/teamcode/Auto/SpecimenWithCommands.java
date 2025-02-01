package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.command.Commands;
import org.firstinspires.ftc.teamcode.command.SequentialAuto;
import org.firstinspires.ftc.teamcode.command.builtin.InstantCommand;
import org.firstinspires.ftc.teamcode.command.builtin.WaitCommand;
import org.firstinspires.ftc.teamcode.commands.MoveSlideCommand;
import org.firstinspires.ftc.teamcode.commands.MoveToTargetCommand;

@Autonomous(name = "SpecimenWithCommands")

public class SpecimenWithCommands extends SequentialAuto {

    public SpecimenWithCommands (){
        addCommands(
                Commands.parallel(
                new InstantCommand((hardwareMap, telemetry)->hardwareMap.get(Servo.class, "al").setPosition(1)),
                        new InstantCommand((hardwareMap, telemetry)->hardwareMap.get(Servo.class, "intakerotateright").setPosition(.05))),

                Commands.parallel(
                        new MoveSlideCommand(30).withTimeout(3),
                        new WaitCommand(1).andThen(MoveToTargetCommand.forward(21).withTimeout(2))),

        new MoveSlideCommand(25).withTimeout(1.5),

                new InstantCommand((hardwareMap, telemetry)->hardwareMap.get(Servo.class, "al").setPosition(.8)),
//4 sec

                Commands.parallel(
                        new InstantCommand((hardwareMap, telemetry)->hardwareMap.get(Servo.class, "al").setPosition(1)),
                        new MoveSlideCommand(0).withTimeout(3),
                        MoveToTargetCommand.back(4).withTimeout(2)),
                MoveToTargetCommand.turnRight(80).withTimeout(2),
                Commands.parallel(
                MoveToTargetCommand.forward(2).withTimeout(2),
                        new InstantCommand((hardwareMap, telemetry)->hardwareMap.get(DcMotor.class, "intakeextend").setPower(-1)).withTimeout(1))

                //10 sec


                );

    }
}
