How to Set-Up, Build, and Deploy the 5419 Robot Code for the 2022 Season!

INSTALLATIONS:

Before you're ready to build, you need to install a few things onto your local computer.

- Java 11 (https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
- Python 3.8 or 3.9 (https://www.python.org/downloads/release/python-390/)
- Gradle 7.3.3 (https://gradle.org/releases/)

You can check that they are properly installed by running "java -version", "python --version", and "gradle -version" in the command prompt.

BUILDING AND DEPLOYING:

- "gradle build" (Building will compile/package your code and check for errors. When you don't have access to the robot, you can build to make sure that your code doesn't have syntax errors.)
- "gradle deploy" (Deploying will build your code and copy them to the robot server to be executed. For this command to work, you must be connected to the robot via ethernet or wifi.)

_____________________________________________________________


(Only for Bazel users - NOT RECOMMENDED)
Instructions for building:

Before you start, make sure that you've deployed to the roborio image with gradle, as gradle handles the Java installation.

When changing which build system you want to use, rename the current build file/folder to something like "build_a" and rename the new build file or folder (probably named build_a) back to build. If it's a bazel file, name it BUILD in all caps.

With Bazel:
bazelisk build //:frc-2022.deploy --platforms=@bazelrio//platforms/roborio,
bazelisk run //:frc-2022.deploy --platforms=@bazelrio//platforms/roborio to push to robot

IMPORTANT:
In order to use bazel, we use a slightly modified version of the current bazel repo. Clone https://github.com/bazelRio/bazelRio.git into the surrounding folder (the one that includes frc-2022). Then make the following edit in scripts/wpilib_dependencies.py:

VERSIONS = ["2021.3.1", "2022.1.1-beta-1", "2022.1.1-beta-2"] --> VERSIONS = ["2022.1.1"]

... and run python scripts/wpilib_dependencies.py in the terminal. This should install the new dependencies. Finally, make this edit in bazelrio/deps.bzl:

wpilib_version = "2021.3.1", --> wpilib_version = "2022.1.1",

Now, running bazel should work with the newly imaged roborios.