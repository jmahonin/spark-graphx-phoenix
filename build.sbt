name := "phoenix-spark-pagerank"

version := "1.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.3.1" % "provided",
  "org.apache.spark" %% "spark-sql" % "1.3.1" % "provided",
  "org.apache.spark" %% "spark-graphx" % "1.3.1" % "provided",
  "org.apache.phoenix" % "phoenix-spark" % "4.4.0-HBase-0.98" % "provided"
)
