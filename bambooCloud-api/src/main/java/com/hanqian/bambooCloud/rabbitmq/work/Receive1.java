package com.hanqian.bambooCloud.rabbitmq.work;

import com.hanqian.bambooCloud.rabbitmq.RabbitMqConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2019/10/21 。
 * ============================================================================
 */
public class Receive1 {

	private static final String QUEUE_NAME = "test_work_queue";

	public static void main(String[] args) throws IOException {
		Connection connection = RabbitMqConnectionUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		channel.basicQos(1);
		Consumer consumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String msg = new String(body, "UTF-8");
				System.out.println("[work1] 接收到：" + msg);

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println("[1] done");

					//手动回执
					channel.basicAck(envelope.getDeliveryTag(), false);
				}

			}
		};

		/**
		 * （队列名称，是否自动应答，当前消费者处理）
		 */
		channel.basicConsume(QUEUE_NAME, false, consumer);
	}

}
