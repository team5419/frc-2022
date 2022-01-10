load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

kt_jvm_library(
    name = "Constants",
    srcs = ["Constants.kt"],
    visibility = ["//visibility:public"],
)

kt_jvm_library(
    name = "Main",
    srcs = ["Main.kt"],
    visibility = ["//visibility:public"],
    deps = [
        ":Robot",
        "@bazelrio//libraries/java/wpilib/wpilibj",
    ],
)

kt_jvm_library(
    name = "Robot",
    srcs = ["Robot.kt"],
    visibility = ["//visibility:public"],
    deps = [
        ":RobotContainer",
        "@bazelrio//libraries/java/wpilib/new_commands",
        "@bazelrio//libraries/java/wpilib/wpilibj",
        "@bazelrio//libraries/java/wpilib/shuffleboard",
        "@bazelrio//libraries/java/wpilib/ntcore"
    ],
)

kt_jvm_library(
    name = "RobotContainer",
    srcs = ["RobotContainer.kt"],
    visibility = ["//visibility:public"],
    deps = [
        "//src/main/kotlin/frc/robot/commands:ExampleCommand",
        "//src/main/kotlin/frc/robot/commands:Drive",
        "//src/main/kotlin/frc/robot/subsystems:ExampleSubsystem",
        "//src/main/kotlin/frc/robot/subsystems:Drivetrain",
        "@bazelrio//libraries/java/wpilib/new_commands",
        "@bazelrio//libraries/java/wpilib/wpilibj",
        "@bazelrio//libraries/java/wpilib/shuffleboard"
    ],
)