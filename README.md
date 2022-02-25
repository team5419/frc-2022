Instructions for building:

With Gradle:
gradle build, gradle deploy to push to robot

Gradle deploy also installs java on a new roborio image, so you have to do it at least once.

With Bazel:
bazelisk build //:frc-2022.deploy --platforms=@bazelrio//platforms/roborio,
bazelisk run //:frc-2022.deploy --platforms=@bazelrio//platforms/roborio to push to robot

When changing which build system you want to use, rename the current build file/folder to something like "build_a" and rename the new build file or folder (probably named build_a) back to build. If it's a bazel file, name it BUILD in all caps.

IMPORTANT:
In order to use bazel, we use a slightly modified version of the current bazel repo. Clone https://github.com/bazelRio/bazelRio.git into the surrounding folder (the one that includes frc-2022). Then make the following edit in scripts/wpilib_dependencies.py:

VERSIONS = ["2021.3.1", "2022.1.1-beta-1", "2022.1.1-beta-2"] --> VERSIONS = ["2022.1.1"]

... and run python scripts/wpilib_dependencies.py in the terminal. This should install the new dependencies. Finally, make this edit in bazelrio/deps.bzl:

wpilib_version = "2021.3.1", --> wpilib_version = "2022.1.1",

Now, running bazel should work with the newly imaged roborios.

Other installations:
 - Bazel, bazelisk
 - Gradle (7.3.3)
 - Python (3.8 or 3.9)
 - Java 11
 - Ask Emma or Theo for help
