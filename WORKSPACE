load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# BazelRIO

http_archive(
    name = "bazelrio",
    url = "https://github.com/bazelRio/bazelRio/archive/refs/tags/0.2.1.zip",
    sha256 = "7c33b1f3be4a697aca1ce49b6a88ec3f2d829e8d1e259ef18976dc6edcb6ae39",
    strip_prefix = "bazelRio-0.2.1/bazelrio",
)

load("@bazelrio//:deps.bzl", "setup_bazelrio_dependencies")
load("@bazelrio//toolchains/jdk:toolchain_config.bzl", "configure_java_toolchain")

setup_bazelrio_dependencies()

register_toolchains("@bazelrio//toolchains/roborio")
configure_java_toolchain()

load("@rules_python//python:pip.bzl", "pip_install")

pip_install(
    name = "__bazelrio_deploy_pip_deps",
    requirements = "@bazelrio//scripts/deploy:requirements.txt",
)

pip_install(
    name = "__bazelrio_wpiformat_pip_deps",
    requirements = "@bazelrio//scripts/wpiformat:requirements.txt",
)

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