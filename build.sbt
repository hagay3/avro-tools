import com.jsuereth.sbtpgp.PgpKeys.publishSigned
import sbt.Keys.scalaVersion
import sbt.Opts.resolver.{sonatypeSnapshots, sonatypeStaging}

lazy val scala213               = "2.13.6"
lazy val scala212               = "2.12.15"
lazy val scala211               = "2.11.12"
lazy val supportedScalaVersions = Seq(scala213, scala212, scala211)

lazy val baseName = "avro-tools"

ThisBuild / organization := "io.github.agolovenko"
ThisBuild / homepage := Some(url("https://github.com/agolovenko/avro-tools"))
ThisBuild / scmInfo := Some(ScmInfo(url("https://github.com/agolovenko/avro-tools"), "git@github.com:agolovenko/avro-tools.git"))
ThisBuild / developers := List(Developer("agolovenko", "agolovenko", "ashotik@gmail.com", url("https://github.com/agolovenko")))
ThisBuild / licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-encoding",
  "UTF-8",
  "-unchecked",
  "-deprecation",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Ywarn-unused"
)
ThisBuild / publishMavenStyle := true

ThisBuild / scalaVersion := scala212
ThisBuild / crossScalaVersions := supportedScalaVersions
ThisBuild / versionScheme := Some("early-semver")

lazy val core = project
  .in(file("core"))
  .enablePlugins(GitVersioning)
  .settings(
    name := s"$baseName-core",
    libraryDependencies ++= new Dependencies(scalaVersion.value).core,
    publishTo := Some(if (isSnapshot.value) sonatypeSnapshots else sonatypeStaging)
  )

lazy val json = project
  .in(file("json"))
  .enablePlugins(GitVersioning)
  .settings(
    name := s"$baseName-json",
    libraryDependencies ++= new Dependencies(scalaVersion.value).json,
    publishTo := Some(if (isSnapshot.value) sonatypeSnapshots else sonatypeStaging)
  )
  .dependsOn(core)

lazy val xml = project
  .in(file("xml"))
  .enablePlugins(GitVersioning)
  .settings(
    name := s"$baseName-xml",
    libraryDependencies ++= new Dependencies(scalaVersion.value).xml,
    publishTo := Some(if (isSnapshot.value) sonatypeSnapshots else sonatypeStaging)
  )
  .dependsOn(core)

lazy val root = project
  .in(file("."))
  .enablePlugins(GitVersioning)
  .settings(
    name := baseName,
    publish := {},
    publishLocal := {},
    publishSigned := {}
  )
  .aggregate(core, json, xml)
