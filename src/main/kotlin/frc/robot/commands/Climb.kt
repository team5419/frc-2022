package frc.robot.commands; 

import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import kotlin.math.roundToInt
import frc.robot.classes.SubsystemHolder
import frc.robot.classes.RGB

class Climb(_subsystems: SubsystemHolder, _codriver: XboxController) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private val codriver: XboxController = _codriver;
  //private var selected: Int = 0;
  private val timer: Timer = Timer()
  private var hasUpdated: Boolean = false;
  init {
    addRequirements(_subsystems.climber);
  }

  override fun initialize() {
    //selected = 0;
    timer.reset()
  }

  override fun execute() {
    val righty: Double = codriver.getRightY();
   val rightx: Double = codriver.getRightX();
   val lefty: Double = codriver.getLeftY();
   val leftx: Double = codriver.getLeftX();
    if(!hasUpdated) {
      if(Math.abs(righty) > 0.1 || Math.abs(rightx) > 0.1 || Math.abs(lefty) > 0.1 || Math.abs(leftx) > 0.1) {
        subsystems.lights.setColor(RGB(0, 255, 255));
        timer.start();
        hasUpdated = true;
      } else if(subsystems.lights.isEqualTo(0, 255, 255)) {
        subsystems.lights.setColor(RGB(0, 0, 0))
        timer.start()
        hasUpdated = true;
      }
    }
    if(timer.get() >= 5.0) {
      timer.stop();
      timer.reset();
      hasUpdated = false;
    }
   // ok have fun testing and relax knowing that you aren't responsible for the faulty code
   // i've labeled each section with numbers
   // test out one section at a time, and pay attention to any special instructions
   // by "test out", i mean uncomment the ~two lines of code directly below the description of the step

   // step 1: normal velocity control. if this doesn't work, stop. something is wrong. tell theo and emma.
   // once you know that it's working, pull up shuffleboard and find the climber section
   // find the value labeled "setpoint"
   // enable the robot and watch the setpoint as karan raises the climbers. 
   // if it's negative, then you're all good. if it's positive, you're slightly fucked.
   // but that's okay because programming team is awesome and there are instructions for you to follow
   // (i'll mention them in future steps)
   // anyway continue to step 2
   subsystems.climber.setPairVelocity(0, righty, rightx * 0.2)
   subsystems.climber.setPairVelocity(1, lefty, leftx * 0.2)

   // step 2: normal control with minimum and maximum hard stops. before you begin, follow these steps:
   // - go back and uncomment step 1 (normal control)
   // - enable the robot and pull up the shuffleboard tab
   // - find the "climber" section. there should be values for each sensor posted
   // - stow the climbers and redeploy or restart the code. the values should be at 0
   // - if they're not quite 0 for some reason, go to constants and change the minimum values to reflect your results
   // - tell karan to raise the climbers all the way
   // - check the values on shuffleboard and edit the maximum values in constants to match them
   // - (you might want to go a little under because the motors will only stop once the maximum values are reached)
   // - make sure the maximum values are greater than the minimum values. if not, the sensors are the wrong way so flip them around.
   // - before you try out this new function, remember whether your setpoint value from step 1 ended up being positive.
   // - if it was, head over to subsystems/Climber.kt and scroll to line 189 where there are some instructions. follow them.
   // - ok you're ready to uncomment the following two lines and try it out
   // climber.setPairWithStops(0, codriver.getRightY(), codriver.getRightX() * 0.2)
   // climber.setPairWithStops(1, codriver.getLeftY(), codriver.getLeftX() * 0.2)

   // step 3: pid control
   // there's not much to adjust with this one, just try it out and stop if it's acting really weird
   // if it is acting weird, please @emma on slack and say "told you so"
   // ok try it out
   // climber.setPairPIDAdjust(0, codriver.getRightY(), codriver.getRightX() * 0.2)
   // climber.setPairPIDAdjust(1, codriver.getLeftY(), codriver.getLeftX() * 0.2)

   // step 4: adjustment control
   // this one is not going to work at first, because there are a lot of unknowns
   // go to subsystems/Climber.kt and scroll to line 223 where there are some notes about how to tweak the function
   // so try it out, and then make adjustments accordingly
   // also it's worth noting that this one has a higher deadband for manual adjusting because 
   // the automatic adjustment control will only turn on when the codriver isn't adjusting manually
   // climber.setPairSlightAdjust(0, codriver.getRightY(), codriver.getRightX() * 0.2)
   // climber.setPairSlightAdjust(1, codriver.getLeftY(), codriver.getLeftX() * 0.2)

   // step 5: normal control with adjustment button
   // go back and uncomment the code for step 1
   // (or, if step 2 worked, do that instead)
   // go to robotcontainer.kt and scroll to line 115
   // comment out line 115 and uncomment line 116 in its place
   // basically, the codriver is going to control the climbers as usual
   // when they get unleveled, the codriver should be able to hold X in order to enable the pid control from step 3.
   // have fun testing
   // when you're done, undo your changes in robotcontainer.kt so that the codriver can use the X button as usual to invert the drivetrain

   // step 6: normal control with funky d-pad
   // - this one makes use of the values you got for min and max in step 2, so make sure you have those set in constants
   // - pay attention, because the controls are really strange.
   // - as usual, the codriver can climb and adjust with the joysticks
   // - however, they can also use the d-pad to automatically raise/lower the various climber pairs
   // - to their maximums and minimums using PID control
   // - the codriver can use the left and right buttons on the d-pad to switch between selected climber pair
   // - (and no, i don't know which one corresponds to outer climbers and which one goes with middle climbers)
   // - they can use the up and down buttons on the d-pad to either raise or lower the selected climber pair
   // - note that this is pid control, which means that the climbers should stay where they are until 
   // - the codriver touches the joysticks once again (i've set a deadband to make sure no functions are called accidentally)
   // - if the climbers move really fast or really slow (or not at all) when using pid, head to the constants
   // - file because you have some work to do. find the PID object for the climber and tune those values
   // - if you don't know how, ask mr. wright or somebody who can explain it to you
   // - it's 11:30 PM EST. i am hunched over on a hotel bed in the Marriott in Cambridge. i don't know how much longer i can take this
   // - (AKA i am not going to explain pid right now)
   // - anywho try it out! make sure you uncomment all of the code below this, because there's a bit more than usual.
   // - P.S. when tf does fahrenheit 452 come out bc that shit was fire
   // - bradbury been lacking recently istg
  //  var dpad: Int = codriver.getPOV();
  //  if(dpad != -1) {
  //    var angle: Double = dpad / 90.0;
  //    var final: Int = angle.roundToInt();
  //    if(final == 4) {
  //      final = 0;
  //    }
  //    if(final == 3) {
  //      selected = 0;
  //    } else if(final == 1) {
  //      selected = 1;
  //    } else if(final == 0) {
  //      climber.toMax(selected);
  //    } else if(final == 2) {
  //      climber.toMin(selected);
  //    }
  //  } else {
  //    val righty: Double = codriver.getRightY();
  //    val rightx: Double = codriver.getRightX();
  //    val lefty: Double = codriver.getLeftY();
  //    val leftx: Double = codriver.getLeftX();
  //    if(Math.abs(righty) > 0.05 && Math.abs(rightx) > 0.05) {
  //     climber.setPairVelocity(0, righty, rightx * 0.2)
  //    }
  //    if(Math.abs(lefty) > 0.05 && Math.abs(leftx) > 0.05) {
  //     climber.setPairVelocity(0, lefty, leftx * 0.2)
  //    }
  //  }
  }

  override fun end(interrupted: Boolean) {
  }

  override fun isFinished(): Boolean {
    return false;
  }
}
