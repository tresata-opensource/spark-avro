name := "spark-avro"

organization := "com.databricks"

scalaVersion := "2.11.8"

spName := "databricks/spark-avro"

sparkVersion := "2.3.1"

val testSparkVersion = settingKey[String]("The version of Spark to test against.")

testSparkVersion := sys.props.getOrElse("spark.testVersion", sparkVersion.value)

val testHadoopVersion = settingKey[String]("The version of Hadoop to test against.")

testHadoopVersion := sys.props.getOrElse("hadoop.testVersion", "2.6.5")

val testAvroVersion = settingKey[String]("The version of Avro to test against.")

testAvroVersion := sys.props.getOrElse("avro.testVersion", "1.7.6")

val testAvroMapredVersion = settingKey[String]("The version of avro-mapred to test against.")

testAvroMapredVersion := sys.props.getOrElse("avroMapred.testVersion", "1.7.7")

spAppendScalaVersion := true

spIncludeMaven := true

spIgnoreProvided := true

sparkComponents := Seq("sql")

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.apache.avro" % "avro" % "1.7.6" exclude("org.mortbay.jetty", "servlet-api"),
  "org.apache.avro" % "avro-mapred" % "1.7.7"  % "provided" classifier("hadoop2") exclude("org.mortbay.jetty", "servlet-api"),
  // Kryo is provided by Spark, but we need this here in order to be able to import the @DefaultSerializer annotation:
  "com.esotericsoftware" % "kryo-shaded" % "3.0.3" % "provided",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "commons-io" % "commons-io" % "2.4" % "test"
)

libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-client" % testHadoopVersion.value % "test",
  "org.apache.spark" %% "spark-core" % testSparkVersion.value % "test" exclude("org.apache.hadoop", "hadoop-client"),
  "org.apache.spark" %% "spark-sql" % testSparkVersion.value % "test" exclude("org.apache.hadoop", "hadoop-client"),
  "org.apache.avro" % "avro" % testAvroVersion.value % "test" exclude("org.mortbay.jetty", "servlet-api"),
  "org.apache.avro" % "avro-mapred" % testAvroMapredVersion.value  % "test" classifier("hadoop2") exclude("org.mortbay.jetty", "servlet-api"),
  "com.google.guava" % "guava" % "14.0.1" % "test" force()
)

// Display full-length stacktraces from ScalaTest:
testOptions in Test += Tests.Argument("-oF")

scalacOptions ++= Seq("-target:jvm-1.8")

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

/********************
 * Release settings *
 ********************/

publishMavenStyle := true

pomIncludeRepository := { x => false }

publishArtifact in Test := false

publishTo := {
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("tresata-snapshots" at "http://server02.tresata.com:8081/artifactory/oss-libs-snapshot-local")
  else
    Some("tresata-releases"  at "http://server02.tresata.com:8081/artifactory/oss-libs-release-local")
}

credentials += Credentials(Path.userHome / ".m2" / "credentials_artifactory")

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))

pomExtra :=
  <url>https://github.com/databricks/spark-avro</url>
  <scm>
    <url>git@github.com:databricks/spark-avro.git</url>
    <connection>scm:git:git@github.com:databricks/spark-avro.git</connection>
  </scm>
  <developers>
    <developer>
      <id>marmbrus</id>
      <name>Michael Armbrust</name>
      <url>https://github.com/marmbrus</url>
    </developer>
    <developer>
      <id>JoshRosen</id>
      <name>Josh Rosen</name>
      <url>https://github.com/JoshRosen</url>
    </developer>
    <developer>
      <id>vlyubin</id>
      <name>Volodymyr Lyubinets</name>
      <url>https://github.com/vlyubin</url>
    </developer>
  </developers>

