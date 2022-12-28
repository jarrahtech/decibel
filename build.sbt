ThisBuild / scalaVersion := "3.2.0"
ThisBuild / organization := "com.jarrahtechnology"
ThisBuild / versionScheme := Some("early-semver")

lazy val root = (project in file("."))
  .settings(
    name := "decibel",
    version := "0.1",
    githubOwner := "jarrahtech",
    githubRepository := "decibel",

    scalacOptions ++= Seq(
      "-encoding", "utf8", // Option and arguments on same line
      "-Xfatal-warnings",  // New lines for each options
      "-deprecation",
      //"-Vprofile-details", "5", "-feature"
    ),

    wartremoverErrors ++= Warts.unsafe,
    wartremoverErrors -= Wart.Var, // using vars internally
    wartremoverErrors -= Wart.DefaultArguments, // TODO: check if code can be changed to remove this

    resolvers ++= Resolver.sonatypeOssRepos("public"),
    resolvers += Resolver.githubPackages("jarrahtech"),
    libraryDependencies += "com.jarrahtechnology" %% "arbitrary" % "0.1", // includes jarrah-util
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",
  )

