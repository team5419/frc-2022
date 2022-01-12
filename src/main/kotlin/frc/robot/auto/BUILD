load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

kt_jvm_library(
    name = "Baseline",
    srcs = ["Baseline.kt"],
    visibility = ["//visibility:public"],
    deps = [
        "@bazelrio//libraries/java/wpilib/new_commands",
        "@bazelrio//libraries/java/wpilib/wpilibj"
    ],
)

kt_jvm_library(
    name = "TestDrive",
    srcs = ["TestDrive.kt"],
    visibility = ["//visibility:public"],
    deps = [
        "//src/main/kotlin/frc/robot/commands:RamseteAction",
        "//src/main/kotlin/frc/robot/subsystems:Drivetrain",
        "@bazelrio//libraries/java/wpilib/new_commands",
        "@bazelrio//libraries/java/wpilib/wpilibj"
    ],
)
