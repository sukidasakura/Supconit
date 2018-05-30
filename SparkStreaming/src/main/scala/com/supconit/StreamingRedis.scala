package com.supconit

//引入Redis相关的隐式转换
import com.redislabs.provider.redis._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 用SparkStreaming从redis中读取数据，要用到spark-redis包。
  * 这个包公共仓库上没有。且不支持Java
  * Created by Mashencai on 2018/5/30.
  */
object StreamingRedis {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[2]").setAppName("sparkRedisTest")
    conf.set("redis.host", "10.10.99.38")
    conf.set("redis.port", "6379")

    // 创建Spark Context对象
    val sc = new SparkContext(conf)

/*
    // 找出键以`AFTER_VEHICLE`开头的List
    val resultSet:RDD[(String)] = sc.fromRedisList("AFTER_VEHICLE*")
    resultSet.foreach(println)
*/

    // 创建StreamingContext对象，使用Streaming从redis中取数据
    val ssc = new StreamingContext(sc, Seconds(15))
    val redisSsc = new RedisStreamingContext(ssc)
    // Seconds(20)是批处理时间间隔，但并不是每20秒读一次数据，可以理解为数据向水流一样源源不断读取出来（只要定义了DStream，Spark程序就会将接收器在各个节点上启动，接收器会以独立线程的方式源源不断的接受数据），每积累20秒钟的数据作为一个RDD供进行一次处理。

    println("redisSsc ")

    /*
    @param keys：Array[String] 想要监听的所有list的数组
    @param storageLevel：缓存策略，默认是MEMORY_AND_DISK_2
    @return a stream of (listname, value)
    scala中数组Array的用法：var z = Array("Runoob", "Baidu", "Google")
    */

    val redisInputDStream = redisSsc.createRedisStream(Array("AFTER_VEHICLE"))
//    redisInputDStream.foreachRDD(_ -> println)  //// 打印不了
    redisInputDStream.print()

    // 开始spark streaming context
    ssc.start()
    ssc.awaitTermination()

    val receiver = redisInputDStream.getReceiver()
    receiver.onStart()


//    redisInputDStream.start()
//    redisInputDStream.foreachRDD(println)
//    redisInputDStream.getReceiver().onStart()



  }
}
