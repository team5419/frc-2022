// package frc.robot.commands; 

// import frc.robot.subsystems.Climber;
// import edu.wpi.first.wpilibj2.command.CommandBase;
// import edu.wpi.first.wpilibj.XboxController;
// import edu.wpi.first.wpilibj.Timer

// class AutonomousClimb(_climber: Climber, _index: Int, _ticks: Double, _speed: Double = 1.0) : CommandBase() {
//   private val climber: Climber = _climber;
//   private val index: Int = _index;
//   private val ticks: Double = _ticks;
//   private val speed: Double = _speed;
//   private var starting: Double = 0.0;

//   init {
//     addRequirements(_climber);
//   }

//   override fun initialize() {
//     starting = climber.pairs[index].left.motor.getSelectedSensorPosition();
//   }

//   override fun execute() {
//     climber.setPairVelocity(index, -speed);
//   }

//   override fun end(interrupted: Boolean) {
//     climber.setPairVelocity(index, 0.0);
//   }

//   override fun isFinished(): Boolean {
//     return Math.abs(climber.pairs[index].left.motor.getSelectedSensorPosition() - starting) >= ticks;
//   }
// }
