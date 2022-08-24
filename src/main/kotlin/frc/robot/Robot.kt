package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.networktables.NetworkTableInstance


class Robot : TimedRobot() {
  private val m_robotContainer = RobotContainer();
  
  private var m_autonomousCommand: Command = m_robotContainer.getAutonomousCommand();

  override fun robotInit() {
    NetworkTableInstance.getDefault().setUpdateRate(0.02)
    println(NetworkTableInstance.kDefaultPort)
  }

  override fun robotPeriodic() {
    // schedule/manage commands and run subsystem periodic methods
    CommandScheduler.getInstance().run();
    //master.add("test1", { 123 });
  }

  override fun disabledInit() {
    //m_robotContainer.lightsOff();
  }

  override fun disabledPeriodic() {}

  // runs autonomous command selected in RobotContainer.kt
  override fun autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    m_robotContainer.setDefaults();
    m_autonomousCommand.schedule();
    m_robotContainer.m_drivetrain.resetOdometry();
  }

  override fun autonomousPeriodic() {}

  override fun teleopInit() {
    m_autonomousCommand.cancel();
    //m_robotContainer.m_drivetrain.currentLimit = 20.0
  }

  override fun teleopPeriodic() {}

  override fun testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  override fun testPeriodic() {}
}
