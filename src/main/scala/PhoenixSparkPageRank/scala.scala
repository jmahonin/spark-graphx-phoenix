import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.graphx._
import org.apache.phoenix.spark._

object PhoenixSparkPageRank extends App {

  val phoenixUrl = "localhost:2181"

  val conf = new SparkConf().setAppName("PhoenixSparkPageRank")
  val sc = new SparkContext(conf)

  // Load the phoenix table
  val rdd = sc.phoenixTableAsRDD("EMAIL_ENRON", Seq("MAIL_FROM", "MAIL_TO"), zkUrl=Some(phoenixUrl))

  // Convert to an RDD of VertexIds
  val rawEdges = rdd.map{ e => (e("MAIL_FROM").asInstanceOf[VertexId], e("MAIL_TO").asInstanceOf[VertexId]) }

  // Create a graph
  val graph = Graph.fromEdgeTuples(rawEdges, 1.0)

  // Run page rank
  val pr = graph.pageRank(0.001)

  // Save to Phoenix
  pr.vertices.saveToPhoenix("EMAIL_ENRON_PAGERANK", Seq("ID", "RANK"), zkUrl = Some(phoenixUrl))
}
