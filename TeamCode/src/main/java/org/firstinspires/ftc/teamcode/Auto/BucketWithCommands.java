package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.command.Commands;
import org.firstinspires.ftc.teamcode.command.SequentialAuto;
import org.firstinspires.ftc.teamcode.command.builtin.InstantCommand;
import org.firstinspires.ftc.teamcode.command.builtin.WaitCommand;
import org.firstinspires.ftc.teamcode.commands.LimelightCenterOnYellowSample;
import org.firstinspires.ftc.teamcode.commands.MoveSlideCommand;
import org.firstinspires.ftc.teamcode.commands.MoveToTargetCommand;

@Autonomous(name = "BucketWithLimelightAndCommands")
public class BucketWithCommands extends SequentialAuto {
public BucketWithCommands (){
    addCommands(

            new InstantCommand((hardwareMap, telemetry)->hardwareMap.get(Servo.class, "intakerotateright").setPosition(.05)),
            new InstantCommand((hardwareMap, telemetry)->hardwareMap.get(Servo.class, "al").setPosition(1)),
            MoveToTargetCommand.right(13).withTimeout(2),
            MoveToTargetCommand.forward(8).withTimeout(1),
            MoveToTargetCommand.turnLeft(45).withTimeout(1),

            Commands.parallel(
                    new MoveSlideCommand(50).withTimeout(4.3),
                    new WaitCommand(2.3).andThen(MoveToTargetCommand.forward(10).withTimeout(2))
            ),
            //new WaitCommand(.1),
            MoveToTargetCommand.forward(4.5).withTimeout(1),
            new InstantCommand((hardwareMap, telemetry)->hardwareMap.get(Servo.class, "al").setPosition(.75)),
            new MoveSlideCommand(50).withTimeout(.5),
            MoveToTargetCommand.back(4.7).withTimeout(1),
            Commands.parallel(
                    new InstantCommand((hardwareMap, telemetry)->hardwareMap.get(Servo.class, "al").setPosition(1)),
                    new MoveSlideCommand(0).withTimeout(3),
                    MoveToTargetCommand.turnRight(122).withTimeout(2.5)
            ),



            //Commands.parallel(
                    //new InstantCommand((hardwareMap, telemetry)->hardwareMap.get(Servo.class, "al").setPosition(.75)),
            MoveToTargetCommand.forward(2).withTimeout(1),
            MoveToTargetCommand.left(8).withTimeout(3),
            new InstantCommand((hardwareMap, telemetry)->hardwareMap.get(Servo.class, "al").setPosition(.75)),
            //MoveToTargetCommand.right(2).withTimeout(1),
           // new LimelightCenterOnYellowSample().withTimeout(4),
            MoveToTargetCommand.forward(12).withTimeout(5),
            new InstantCommand((hardwareMap, telemetry)->hardwareMap.get(Servo.class, "al").setPosition(1)),
            MoveToTargetCommand.back(5).withTimeout(2),
            MoveToTargetCommand.turnRight(180).withTimeout(2),
            MoveToTargetCommand.right(7)




    );




  }


}
