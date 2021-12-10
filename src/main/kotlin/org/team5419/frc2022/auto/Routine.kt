package org.team5419.frc2022.auto
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.geometry.Pose2d

open class Routine {
    constructor(_name: String, _actions: Array<Action>, _startPose: Pose2d = Pose2d(0.0, 0.0, Rotation2d(0.0))) {
        this.name = _name;
        this.actions = _actions;
        this.startPose = _startPose;
        this.index = 0;
        this.actions[this.index].start()
    }

    open fun isDone() {
        return this.index > this.actions.length - 1
    }

    open fun update() {
        this.actions[this.index].update()
        if(this.actions[this.index].next()) {
            this.actions[this.index].finish();
            this.index++;
            if(this.index > this.actions.length - 1) {
                return;
            }
            this.actions[this.index].start()
        }
    }
}
