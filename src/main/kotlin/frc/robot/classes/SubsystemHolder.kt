package frc.robot.classes
import frc.robot.subsystems.*;

open class SubsystemHolder {
    public val drivetrain: Drivetrain;
    public val shooter: Shooter;
    public val intake: Intake;
    public val deploy: DeploySubsystem;
    public val lights: Lights;
    public val vision: Vision;
    public val climber: Climber;
    public val feeder: Feeder;
    public val indexer: Indexer;
    public val colorsensor: 
    constructor(_drivetrain: Drivetrain, _shooter: Shooter, _intake: Intake, _deploy: DeploySubsystem, _lights: Lights, _vision: Vision, _climber: Climber, _feeder: Feeder, _indexer: Indexer) {
        this.drivetrain = _drivetrain;
        this.shooter = _shooter;
        this.intake = _intake;
        this.deploy = _deploy;
        this.lights = _lights;
        this.vision = _vision;
        this.climber = _climber;
        this.feeder = _feeder;
        this.indexer = _indexer;
    }
}