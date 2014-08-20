import scala.io.Source
import com.mongodb.casbah.Imports._
import com.typesafe.config._ 


object Loader {

  val conf = ConfigFactory.load 
  val hostname = conf.getString("hostname") 
  val port = conf.getInt("port") 
  val dbname = conf.getString("dbname")
  val colname = conf.getString("colname")
  val sort = conf.getString("sort")
  val distinctField = conf.getString("distinctField")
  val filename = conf.getString("filename")
  

  def buildDoc(vals: Array[String] ) : DBObject = {
    val builder = MongoDBObject.newBuilder
    //stripping out the project component of lang
    builder += "lang" -> vals(0).split('.')(0) 
    builder += "pagename" -> vals(1)
    builder += "views" -> vals(2).toString.toInt
    builder += "size" -> vals(3)
    return builder.result 
  }

  def sendLine(collection: MongoCollection, line : String) : Unit = {
    val tokens = line.split(" ")
    //Don't have definitive list of prefixes to ignore so using regex
    val specialPrefix  = """(^\w+:)""".r 
    if ( (specialPrefix  findFirstIn tokens(1)).isDefined) {}  
    else { collection.insert(buildDoc(tokens))}  
  }

  def main(args: Array[String]) {
    try {
      val coll = MongoClient(hostname,port)(dbname)(colname)
      //clear out docs from any previous load
      coll.remove( DBObject.empty )
      coll.ensureIndex(MongoDBObject(distinctField -> 1) ++ MongoDBObject(sort -> -1)) 
      for (line <- Source.fromURL(getClass.getResource(filename)).getLines)  { sendLine(coll,line)}    
    } catch {
      case ex: Exception => println(ex.toString)
    }
  }

}










  


