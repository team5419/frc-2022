load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

kt_jvm_library(
    name = "Drive",
    srcs = ["Drive.kt"],
    visibility = ["//visibility:public"],
    deps = [
        "//src/main/kotlin/frc/robot/subsystems:Drivetrain",
        "@bazelrio//libraries/java/wpilib/new_commands",
        "@bazelrio//libraries/java/wpilib/wpilibj",
        #"@bazelrio//libraries/java/wpilib/wpilibj",
    ],
)

kt_jvm_library(
    name = "Shoot",
    srcs = ["Shoot.kt"],
    visibility = ["//visibility:public"],
    deps = [
        "//src/main/kotlin/frc/robot/subsystems:Shooter",
        "@bazelrio//libraries/java/wpilib/new_commands",
        "@bazelrio//libraries/java/wpilib/wpilibj",
        #"@bazelrio//libraries/java/wpilib/wpilibj",
    ],
)

kt_jvm_library(
    name = "PrototypeSpin",
    srcs = ["PrototypeSpin.kt"],
    visibility = ["//visibility:public"],
    deps = [
        "//src/main/kotlin/frc/robot/subsystems:PrototypeMotor",
        "@bazelrio//libraries/java/wpilib/new_commands",
        "@bazelrio//libraries/java/wpilib/wpilibj",
        #"@bazelrio//libraries/java/wpilib/wpilibj",
    ],
)

kt_jvm_library(
    name = "RamseteAction",
    srcs = ["RamseteAction.kt"],
    visibility = ["//visibility:public"],
    deps = [
        "//src/main/kotlin/frc/robot/subsystems:Drivetrain",
        "@bazelrio//libraries/java/wpilib/new_commands",
        "@bazelrio//libraries/java/wpilib/wpilibj", 
        "//src/main/kotlin/frc/robot:Constants"
    ],
)
