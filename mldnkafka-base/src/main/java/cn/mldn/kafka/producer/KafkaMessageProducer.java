package cn.mldn.kafka.producer;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
// 本次消息发送的key类型为Integer、消息value类型为String
public class KafkaMessageProducer {
	public static final String TOPIC_NAME = "mldn-topic" ;	// 主题 
	public static final String SERVERS = "kafka-single:9095" ;	// 主机列表
	public static void main(String[] args) throws Exception {
		// 现在使用的是新一代的Kafka客户端，此时连接的一定是Kafka服务器而不是ZooKeeper主机
		Properties props = new Properties() ;	// 定义Kafka的连接属性
		props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVERS) ; // 主机
		props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName()); // Key类型为Integer
		props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // Value类型为String
		CountDownLatch latch = new CountDownLatch(2000) ;
		long start = System.currentTimeMillis() ;	// 发送的开始时间
		for (int x = 0 ; x < 2000 ; x ++) {	// 要发送很多的消息
			int temp = x ; 
			new Thread(()->{
				Producer<Integer, String> producer = new KafkaProducer<>(props) ;// 发送对象
				for (int y = 0 ; y < 100 ; y ++) {
					producer.send(new ProducerRecord<Integer, String>(TOPIC_NAME, y, 
							"【" + Thread.currentThread().getName() + "】mldnjava - " + temp + " - " + y));
				}
				latch.countDown(); 
			}).start() ; 
		}
		latch.await(); 
		long end = System.currentTimeMillis() ;	// 发送的结束时间
		System.out.println("本次数据发送耗时为：" + (end - start));
	}
}
