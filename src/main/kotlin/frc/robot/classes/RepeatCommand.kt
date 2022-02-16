package edu.wpi.first.wpilibj2.command;

import edu.wpi.first.wpilibj2.command.CommandGroupBase.registerGroupedCommands;
import edu.wpi.first.wpilibj2.command.CommandGroupBase.requireUngrouped;

open class RepeatCommand : CommandBase {
  var m_command : Command

  constructor(command : Command) {
    requireUngrouped(command);
    registerGroupedCommands(command);
    m_command = command;
    m_requirements.addAll(command.getRequirements());
  }

  override fun initialize() {
    m_command.initialize();
  }

  override fun execute() {
    m_command.execute();
    if (m_command.isFinished()) {
      // restart command
      m_command.end(false);
      m_command.initialize();
    }
  }

  override fun isFinished() : Boolean{
    return false;
  }

  override fun end(interrupted : Boolean) {
    m_command.end(interrupted);
  }

  override fun runsWhenDisabled() : Boolean{
    return m_command.runsWhenDisabled();
  }

  public fun repeat() : RepeatCommand {
    return this;
  }
}