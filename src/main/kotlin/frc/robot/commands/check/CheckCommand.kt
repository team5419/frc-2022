package frc.robot.commands.check; 

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab

open class CheckCommand(_name: String, _tab: ShuffleboardTab, _time: Double, _numMotors: Int) : CommandBase() {
    private val mname: String = _name
    private val tab: ShuffleboardTab = _tab
    private val time: Double = _time
    private val timer: Timer = Timer()
    private val numMotors: Int = _numMotors
    public var currentVelocities: List<Double> = List<Double>(_numMotors) { 0.0 }

    override fun initialize() {
        timer.reset()
        timer.start()
        for(i in 0..numMotors - 1) {
          tab.addBoolean("${mname} motor ${i + 1}", {check(i)})
        }
    }

    open fun check(i: Int): Boolean { return true }
    open fun runMotors() {}
    open fun getVels(): List<Double> { return List<Double>(numMotors) { 0.0 } }

    override fun execute() {
        runMotors()
        if(timer.get() > time / 2 && currentVelocities[0] == 0.0) {
          currentVelocities = getVels();
        }
    }

    override fun end(interrupted: Boolean) {}
    
    override fun isFinished(): Boolean {
        return (time != 0.0 && timer.get() >= time)
    }
}