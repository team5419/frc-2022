load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

kt_jvm_library(
    name = "ExampleSubsystem",
    srcs = ["ExampleSubsystem.kt"],
    visibility = ["//visibility:public"],
    deps = [
        "@bazelrio//libraries/java/wpilib/new_commands",
        #"@bazelrio//libraries/java/wpilib/wpilibj",
    ],
)

kt_jvm_library(
    name = "Drivetrain",
    srcs = ["Drivetrain.kt"],
    visibility = ["//visibility:public"],
    deps = [
        "@bazelrio//libraries/java/wpilib/new_commands",
        "@bazelrio//libraries/java/ctre/phoenix",
        "//src/main/kotlin/frc/robot:Constants",
        "@bazelrio//libraries/java/wpilib/shuffleboard"
        #"@bazelrio//libraries/java/wpilib/wpilibj",
    ],
)

kt_jvm_library(
    name = "Vision",
    srcs = ["Vision.kt"],
    visibility = ["//visibility:public"],
    deps = [
        "@bazelrio//libraries/java/wpilib/new_commands",
        "@bazelrio//libraries/java/ctre/phoenix",
        "//src/main/kotlin/frc/robot:Constants",
        "@bazelrio//libraries/java/wpilib/shuffleboard",
        "@bazelrio//libraries/java/wpilib/ntcore", 
        "//src/main/kotlin/frc/robot/subsystems:Drivetrain",
        "@bazelrio//libraries/java/wpilib/wpilibj", 
        "//src/main/kotlin/frc/robot/classes:DriveSignal"
    ],
)

kt_jvm_library(
    name = "Shooter",
    srcs = ["Shooter.kt"],
    visibility = ["//visibility:public"],
    deps = [
        "@bazelrio//libraries/java/wpilib/new_commands",
        "@bazelrio//libraries/java/ctre/phoenix",
        "//src/main/kotlin/frc/robot:Constants",
        "@bazelrio//libraries/java/wpilib/shuffleboard",
        "@bazelrio//libraries/java/wpilib/ntcore",
        "@bazelrio//libraries/java/wpilib/wpilibj"
    ],
)

kt_jvm_library(
    name = "PrototypeMotor",
    srcs = ["PrototypeMotor.kt"],
    visibility = ["//visibility:public"],
    deps = [
        "@bazelrio//libraries/java/wpilib/new_commands",
        "@bazelrio//libraries/java/ctre/phoenix",
        "//src/main/kotlin/frc/robot:Constants",
        "@bazelrio//libraries/java/wpilib/shuffleboard"
        #"@bazelrio//libraries/java/wpilib/wpilibj",
    ],
)
