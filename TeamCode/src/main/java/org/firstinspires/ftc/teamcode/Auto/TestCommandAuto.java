package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.command.Commands;
import org.firstinspires.ftc.teamcode.command.SequentialAuto;
import org.firstinspires.ftc.teamcode.command.builtin.InstantCommand;
import org.firstinspires.ftc.teamcode.commands.MoveSlideCommand;
import org.firstinspires.ftc.teamcode.commands.MoveToTargetCommand;


public class TestCommandAuto extends SequentialAuto {

    public TestCommandAuto (){
        //sequetial
        addCommands(
                MoveToTargetCommand.forward(2),
                new MoveSlideCommand(2),

                new InstantCommand((hardwareMap,telemetry)->hardwareMap.get(Servo.class, "al").setPosition(2)),
               //same time
                Commands.parallel(
                        MoveToTargetCommand.forward(5),
                        new MoveSlideCommand(3)
                )
        );
    }


}

