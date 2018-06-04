import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Mashencai on 2018/6/4.
 */
public class KafkaConsumer {
    private ConsumerConnector consumer;
    private String topic;

    public static void main(String[] args) {

    }

    public void init(){
        String zookeeper = "10.10.77.136:2181,10.10.77.137:2181,10.10.99.38:2181";
        String topic = "test";
        String groupId = "test-group";

        Properties props = new Properties();

        // 必须的配置
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId); // 代表消费者所属的consumer group
        props.put("zookeeper.session.timeout.ms", 6000); //多长时间没有发送心跳信息到zk就认为其挂掉了，默认是6000
        props.put("zookeeper.sync.time.ms", "200"); //可以允许zk follower比leader慢的时长
        props.put("auto.commit.interval.ms", "1000"); //控制consumer offsets提交到zookeeper的频率，默认是60*1000

        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        this.topic = topic;
    }

    public void consume(){
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 1);

        /**
         * 为每个topic创建message stream
         */
        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = consumer.createMessageStreams(topicCountMap);
    }

}
