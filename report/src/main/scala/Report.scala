import scala.io.Source
import com.mongodb.casbah.Imports._
import com.typesafe.config._ 


object Report {

  val conf = ConfigFactory.load 
  val hostname = conf.getString("hostname") 
  val port = conf.getInt("port") 
  val dbname = conf.getString("dbname")
  val colname = conf.getString("colname")
  val sort = conf.getString("sort")
  val distinctField = conf.getString("distinctField")
  val numResults = conf.getInt("numResults")
 

  def getTopN(collection: MongoCollection, name: String, values: List[String]): Map[String,List[Any]] = {
    var result:Map[String, List[Any]] = Map()
    for (value <- values) {
      val docs = collection.find(MongoDBObject(name -> value), MongoDBObject("_id" ->0)).sort(MongoDBObject(sort -> -1)).limit(numResults).toList
      result += (value -> docs)   
    }
   return result   
  }

  def getDistinctValues(collection: MongoCollection, name:String) : List[String]  =  {
    val vals = collection.distinct(name).toList.map(x=>x.toString)
    return vals
  }

  def main(args: Array[String]) {
    val col = MongoClient(hostname,port)(dbname)(colname)
    val langs = getDistinctValues(col, distinctField)
    val report = getTopN(col, distinctField,langs)
    println("Top " ++ numResults.toString ++ " pages per " ++ distinctField)
    for (key <- report.keys) { 
        println(key)
        for (doc <- report(key) ) println(doc)
    }

  }

}