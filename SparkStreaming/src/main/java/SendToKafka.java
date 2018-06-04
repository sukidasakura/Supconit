import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;
import java.util.concurrent.Executors;

/**
 * Created by Mashencai on 2018/6/4.
 */
public class SendToKafka {

    private Producer<String, String> producer;

    public static void main(String[] args) {
        new SendToKafka().start();
    }

    /**
     * 初始化Kafka Producer
     */
    public void init(){
        Properties props = new Properties();
        // 用于自举（bootstrapping），producer只是用它来获得元数据（topic, partition, replicas）
        // 实际用户发送消息的socket会根据返回的元数据来确定
        props.put("metadata.broker.list", "10.10.77.136:2181,10.10.77.137:2181,10.10.99.38:2181");

        // 消息的序列化类，默认是kafka.serializer.DefaultEncoder，输入byte[] 返回是同样的字节数组
        props.put("serializer.class", "kafka.serializer.StringEncoder");

        // producer发送消息后是否等待broker的ACK，默认是0；1表示等待ACK，保证消息的可靠性。
        props.put("request.required.acks", "1");

        ProducerConfig config = new ProducerConfig(props);
        // 泛型参数分别表示：第一个表示Partition key的类型，第二个表示message的类型
        producer = new Producer<String, String>(config);
    }

    /**
     * 构建发送的消息
     */
    public void producerMsg(){
        long timestamp = System.currentTimeMillis();
        String msg = "Msg" + timestamp;
        String topic = "test"; //确保有这个topic
        System.out.println("发送消息" + msg);
        String key = "Msg-key" + timestamp;

        /*
         * topic：消息的主题
         * key：消息的key，同时也会作为partition的key
         * message：发送的消息
         */
        KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, key, msg);

        producer.send(data);
    }

    public void start(){
        System.out.println("开始发送消息...");
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                init();
                while (true){
                    try {
                        producerMsg();
                        Thread.sleep(10 * 1000);
                    } catch (Throwable e) {
                        if (producer != null){
                            try {
                                producer.close();
                            } catch (Exception e1) {
                                System.out.println("Turn off kafka producer error!" + e1);
                            }
                        }
                    }
                }
            }
        });
    }


}
