package org.firstinspires.ftc.teamcode.command.builtin;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.command.Command;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class InstantCommand extends Command {
    private BiConsumer<HardwareMap, Telemetry> runnable;
    public InstantCommand(BiConsumer<HardwareMap,Telemetry> runnable) {
        this.runnable = runnable;
    }
    @Override
    public void start() {

    }

    @Override
    public void loop() {
        runnable.accept(hardwareMap,telemetry);
    }

    @Override
    public void finish(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
