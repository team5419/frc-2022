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

http_archive(
    name = "bazelrio",
    url = "https://github.com/bazelRio/bazelRio/archive/refs/tags/0.3.0.zip",
    sha256 = "f48dd081ccbca0f63d7577e68399d30ecbf85e935cad08dfa24f56691f4e8c85",
    strip_prefix = "bazelRio-0.3.0/bazelrio",
)

load("@bazelrio//:deps.bzl", "setup_bazelrio_dependencies")

setup_bazelrio_dependencies()

load("@bazelrio//:defs.bzl", "setup_bazelrio")

setup_bazelrio()