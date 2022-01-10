load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# Kotlin

rules_kotlin_version = "v1.5.0-beta-4"

rules_kotlin_sha = "6cbd4e5768bdfae1598662e40272729ec9ece8b7bded8f0d2c81c8ff96dc139d"

http_archive(
    name = "io_bazel_rules_kotlin",
    sha256 = rules_kotlin_sha,
    urls = ["https://github.com/bazelbuild/rules_kotlin/releases/download/%s/rules_kotlin_release.tgz" % rules_kotlin_version],
)

load("@io_bazel_rules_kotlin//kotlin:repositories.bzl", "kotlin_repositories")

kotlin_repositories()

load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_register_toolchains")

kt_register_toolchains()

# BazelRIO

local_repository(
    name = "bazelrio",
    path = "../bazelRio/bazelrio",
)

load("@bazelrio//:deps.bzl", "setup_bazelrio_dependencies")

setup_bazelrio_dependencies()

load("@bazelrio//:defs.bzl", "setup_bazelrio")

setup_bazelrio()