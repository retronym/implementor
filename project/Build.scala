import sbt._
import Keys._

object BuildSettings {
  val buildVersion = "0.1"
  val buildScalaVersion = "2.10.0"

  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.scalamock",
    version := buildVersion,
    scalaVersion := buildScalaVersion,
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Ycheck:typer"),
    scalaHome := Some(file("/Users/jason/code/scala2/build/pack")),
    shellPrompt := ShellPrompt.buildShellPrompt
  )
}

object ShellPrompt {
  object devnull extends ProcessLogger {
    def info (s: => String) {}
    def error (s: => String) { }
    def buffer[T] (f: => T): T = f
  }
  def currBranch = (
    ("git status -sb" lines_! devnull headOption)
      getOrElse "-" stripPrefix "## "
  )

  val buildShellPrompt = {
    (state: State) => {
      val currProject = Project.extract (state).currentProject.id
      "%s:%s:%s> ".format (
        currProject, currBranch, BuildSettings.buildVersion
      )
    }
  }
}

object Dependencies {
  val scalatest =  "org.scalatest" % "scalatest_2.10.0" % "2.0.M5"
  val reflect = "org.scala-lang" % "scala-reflect" % BuildSettings.buildScalaVersion
}

object ScalaMockBuild extends Build {
  import BuildSettings._
  import Dependencies._

  lazy val scalamock = Project(
    "Implementor",
    file("."),
    settings = buildSettings) aggregate(core, core_tests)

  lazy val core = Project(
    "core",
    file("core"),
    settings = buildSettings ++ Seq(
      name := "Implementor Core",
	    libraryDependencies ++= Seq(reflect)
    ))

  lazy val core_tests = Project(
    "core_tests",
    file("core_tests"),
    settings = buildSettings ++ Seq(
      name := "Implementor Core Tests"
    )) dependsOn(core)
}