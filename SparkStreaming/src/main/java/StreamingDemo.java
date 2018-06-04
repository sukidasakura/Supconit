import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;

/**
 * Created by Mashencai on 2018/6/4.
 */
public class StreamingDemo {
    public static void main(String[] args) throws InterruptedException {
        /*
         * 第一步：配置sparkConf
         * 1.至少2条线程，因为spark streaming 应用程序在运行的时候至少有一条线程用于不断接受数据，并且至少有一个
         * 线程用于处理接受的数据（否则的话，没有线程用于处理数据，随着时间的推移，内存和磁盘都是不堪重负）
         * 2、对于集群而言，每一个Executor一般肯定不止一个Thread，那么对于处理spark Streaming
         * 的程序而言，每个Executor一般分配多少个Core比较合适？core最佳个数一般是奇数：5,7
         */
        SparkConf conf = new SparkConf().setAppName("JavaStreamingDemo").setMaster("local[2]");

        /*
         * 第二步：创建SparkStreamingContext：
         * 这是个SparkStreaming应用程序所有功能的起点和程序调度的核心
         * SparkStreamingContext构建基于SparkConf参数，可以基于持久化的SparkStreamingContext内容
         * 恢复过来（典型的应用场景是Driver崩溃后重新启动，由于Spark Streaming具有连续7*24）不间断运行特征
         所有需要在Driver重新启动后继续上一次的状态，此时的状态恢复需要基于曾记的checkPoint)
         2、在一个spark streaming 应用程序中可以创建若干个SparkStreamContext对象，使用下一个SparkStreamingContext
         之前需要把前面正在运行的sparkStreamContext对象关闭。因此我们获得一个重大的启发，sparkStreaming框架也只是spark core
         上的一个应用程序而已，至不过sparkStreaming 框架想运行的话需要spark 工程师写业务逻辑处理代码
         */
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(6));

        /*
         * 第三步：创建Spark Streaming输入数据来源input Stream：
         * 1，数据输入来源可以基于File、HDFS、Flume、Kafka、Socket等
         * 2, 在这里我们指定数据来源于网络Socket端口，Spark Streaming连接上该端口并在运行的时候一直监听该端口
         * 的数据（当然该端口服务首先必须存在）,并且在后续会根据业务需要不断的有数据产生(当然对于Spark Streaming
         * 应用程序的运行而言，有无数据其处理流程都是一样的)；
         * 3,如果经常在每间隔5秒钟没有数据的话不断的启动空的Job其实是会造成调度资源的浪费，因为并没有数据需要发生计算，所以
         * 实例的企业级生成环境的代码在具体提交Job前会判断是否有数据，如果没有的话就不再提交Job；
         */
        // 这个linesDStream表示将从数据服务器接收的数据流，该流中的每条记录都是一行文本。
        JavaReceiverInputDStream<String> lines = jssc.socketTextStream("10.10.99.37", 9999);

        /*
         * 第四步：
         * 像对于RDD 编程一样基于DStream进行编程。 DStream 是RDD产生的模版，在spark stream具体发生计算前
         * 其实质是 把每一个batch 的操作翻译成对RDD的操作！！
         * 对于初始的DStream进行Transformation级别的处理，例如map、filter等高阶函数的编程来进行具体的数据计算：
         *
         */
        // 将每一行的字符串拆分成单个的单词
        // flatMap是一个DStream操作（是转换操作），它通过从源DStream中的每个记录生成多个新纪录来创建一个新的DStream。
        // 在这种情况下，每行将被分分割成多个单词，单词流表示为wordsDStream。
        JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(x.split(" ")).iterator());
        // 在单词拆分的基础上对每个单词实例计数为1，也就是word => (word, 1)
        // 使用 PairFunction将wordsDStream进一步映射（一对一转换）为一对DStream
        JavaPairDStream<String, Integer> pairs = words.mapToPair(s -> new Tuple2<>(s, 1));
        // 然后使用function2对象减少每个批次数据中的词的频率
        JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey((i1, i2) -> i1 + i2);

        /*
         *  此处的print并不会直接触发Job的执行，因为现在的一切都是在Spark Streaming框架的控制之下的，对于Spark Streaming
         *  而言具体是否触发真正的Job运行是基于设置的Duration时间间隔的
         *
         *  诸位一定要注意的是Spark Streaming应用程序要想执行具体的Job，对Dtream就必须有output Stream操作，
         *  output Stream有很多类型的函数触发，类print、saveAsTextFile、saveAsHadoopFiles等，最为重要的一个
         *  方法是foreachRDD,因为Spark Streaming处理的结果一般都会放在Redis、DB、DashBoard等上面，foreachRDD
         *  主要就是用用来完成这些功能的，而且可以随意的自定义具体数据到底放在哪里！！！
         *
         */
        // 打印每秒生成的一些计数。
        wordCounts.print();

        // 所有转换完成后开始处理。
        jssc.start();

        jssc.awaitTermination();

    }
}
