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
