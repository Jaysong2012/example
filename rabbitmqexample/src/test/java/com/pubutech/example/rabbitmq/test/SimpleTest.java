/*
 * SC License
 *
 * Copyright (c)  SongChao 2018 .
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copyof this software and associated documentation files (the "Software").
 *
 * This software is only used for learning and communication.
 * Without permission,please do not use the software illegally.
 */

package com.pubutech.example.rabbitmq.test;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/25
 * @since 1.0
 */
public class SimpleTest {

    @Test
    public void test(){
        //第一步配置连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("test");
        factory.setPassword("test123");
        factory.setVirtualHost("test");
        factory.setHost("192.168.199.133");
        factory.setPort(5672);
        Connection conn = null;

        //或者用下面配置连接工厂
        try {
            factory.setUri("amqp://test:test123@192.168.199.133:5672/test");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //创建连接
        try {
            conn = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        //创建channel
        Channel channel = null;

        if(conn != null){
            try {
                channel = conn.createChannel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //exchanges和queues是Client端应用所必须的。在使用之前必须先“declared”(声明)，确保在使用之前已经存在，如果不存在则创建它，这些操作都包含在declare里。
        if(channel !=null){
            /**
             * Declare an exchange, via an interface that allows the complete set of arguments.
             * @param exchange 名称
             * @param type 类型
             * @param durable 持久化 defaul true
             * @param autoDelete 自动删除 defacult true
             * @param internal 内部使用 true if the exchange is internal, i.e. can't be directly published to by a client.
             * @param arguments 额外参数
             * @return a declaration-confirm method to indicate the exchange was successfully declared
             * @throws java.io.IOException if an error is encountered
             */
            /*Exchange.DeclareOk exchangeDeclare(String exchange,
                    BuiltinExchangeType type,
            boolean durable,
            boolean autoDelete,
            boolean internal,
            Map<String, Object> arguments) throws IOException;*/
            try {
                Map<String, Object> arguments =new HashMap<>();
                /*
                当一个消息不能被route的时候，如果exchange设定了AE，则消息会被投递到AE。如果存在AE链，则会按此继续投递，直到消息被route或AE链结束或遇到已经尝试route过消息的AE。
                 */
                arguments.put("alternate-exchange", "amq.direct");
                AMQP.Exchange.DeclareOk declareOk = channel.exchangeDeclare("test", BuiltinExchangeType.DIRECT, true, false,false,arguments);
                //死信队列用的
                channel.exchangeDeclare("dead", BuiltinExchangeType.DIRECT, true, false,false,null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            /**
             * Declare a queue
             * @param queue 名称
             * @param durable 持久化 defaul true
             * @param exclusive 专用性 defaul true
             * @param autoDelete 自动删除 defacult true
             * @param arguments 额外参数
             * @return a declaration-confirm method to indicate the queue was successfully declared
             * @throws java.io.IOException if an error is encountered
             */
            /* Queue.DeclareOk queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) throws IOException;*/

            Map<String, Object> map = new HashMap<>();
            //标志队列中的消息存活时间，也就是说队列中的消息超过了制定时间会被删除(数字类型，标志时间，以豪秒为单位)
            map.put("x-message-ttl", 1 * 24 * 60 * 60 * 10000);
            //队列自身的空闲存活时间，当前的queue在指定的时间内，没有consumer、basic.get也就是未被访问，就会被删除。(数字类型，标志时间，以豪秒为单位)
            map.put("x-expires",60 * 60 * 10000);
            //最大长度和最大占用空间，设置了最大长度的队列，在超过了最大长度后进行插入会删除之前插入的消息为本次的留出空间,相应的最大占用大小也是这个道理，当超过了这个大小的时候，会删除之前插入的消息为本次的留出空间。
            map.put("x-max-length",10000);
            map.put("x-max-length-bytes",100 * 1024 * 1024);

            //队列超出最大长度的处理方案 ，队列溢出的默认处理方案：drop-head (default) 或者拒绝消息 reject-publish
            map.put("x-overflow","reject-publish");
            //惰性队列会尽可能的将消息存入磁盘中，而在消费者消费到相应的消息时才会被加载到内存中，它的一个重要的设计目标是能够支持更长的队列，即支持更多的消息存储。当消费者由于各种各样的原因（比如消费者下线、宕机亦或者是由于维护而关闭等）而致使长时间内不能消费消息造成堆积时，惰性队列就很有必要了。
            map.put("x-queue-mode","lazy");
            //集群属性，我们这里暂不讨论
            map.put("x-queue-master-locator","");

            /*
            消息因为超时或超过限制在队列里消失，这样我们就丢失了一些消息，也许里面就有一些是我们做需要获知的。而rabbitmq的死信功能则为我们带来了解决方案。
            设置了dead letter exchange与dead letter routingkey（要么都设定，要么都不设定）那些因为超时或超出限制而被删除的消息会被推动到我们设置的exchange中，
            再根据routingkey推到queue中.
             */
            map.put("x-dead-letter-exchange","dead");
            map.put("x-dead-letter-routing-key","key");

            //队列所支持的优先级别，列如设置为5，表示队列支持0到5六个优先级别，5最高，0最低，当然这需要生产者在发送消息时指定消息的优先级别，消息按照优先级别从高到低的顺序分发给消费者
            map.put("x-max-priority",5);

            try {
                channel.queueDeclare("test", true, false, false, map);
            } catch (IOException e) {
                e.printStackTrace();
            }


            /**
             * Bind a queue to an exchange.
             * @param queue 队列名称
             * @param exchange 交换机名称
             * @param routingKey 绑定的 routingKey
             * @param arguments 额外的参数
             * @return a binding-confirm method if the binding was successfully created
             * @throws java.io.IOException if an error is encountered
             */
            /*Queue.BindOk queueBind(String queue, String exchange, String routingKey, Map<String, Object> arguments) throws IOException;*/
            try {
                AMQP.Queue.BindOk bindOK = channel.queueBind("test", "test", "test", null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //传递消息
            byte[] messageBodyBytes = "Hello, world!".getBytes();

            Map<String, Object> headers = new HashMap<String, Object>();
            headers.put("latitude",  51.5252949);
            headers.put("longitude", -0.0905493);

            /**
             * Publish a message.
             * @param exchange the exchange to publish the message to
             * @param routingKey the routing key
             * @param mandatory true mandatory标志告诉服务器至少将该消息route到一个队列中，否则将消息返还给生产者
             * @param props other properties for the message - routing headers etc
             * @param body the message body
             * @throws java.io.IOException if an error is encountered
             */
            /*void basicPublish(String exchange, String routingKey, boolean mandatory, BasicProperties props, byte[] body)
            throws IOException;*/

            try {
                channel.basicPublish("test", "test", true,
                        (new AMQP.BasicProperties.Builder()
                                .headers(headers)
                                .contentType("text/plain")
                                .deliveryMode(2)
                                .priority(1)
                                .userId("test")
                                .appId("test")
                                .expiration("60000")
                                .build()),
                        messageBodyBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                channel.queueDeclare("request", true, false, false, null);
                channel.queueDeclare("response", true, false, false, null);
                channel.queueBind("test", "request", "request", null);
                channel.queueBind("test", "response", "response", null);

                //correlationId - 每个请求独一无二的标识
                String correlationId = UUID.randomUUID().toString();

                AMQP.BasicProperties props = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(correlationId)
                        .replyTo("response")
                        .build();

                channel.basicPublish("test", "request", props, messageBodyBytes);

                final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

                channel.basicConsume("response", true, new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        if (properties.getCorrelationId().equals(correlationId)) {
                            response.offer(new String(body, "UTF-8"));
                        }
                    }
                });

                channel.basicQos(0,1,true);

                System.out.println(" [x] Awaiting RPC requests");

                Channel finalChannel1 = channel;
                Consumer consumer = new DefaultConsumer(finalChannel1) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                                .Builder()
                                .correlationId(properties.getCorrelationId())
                                .build();

                        String response = "success";
                        finalChannel1.basicPublish( "test", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));

                        finalChannel1.basicAck(envelope.getDeliveryTag(), false);

                        // RabbitMq consumer worker thread notifies the RPC server owner thread
                        synchronized(this) {
                            this.notify();
                        }
                    }
                };

                channel.basicConsume("response", false, consumer);

                // Wait and be prepared to consume the message from RPC client.
                while (true) {
                    synchronized(consumer) {
                        try {
                            consumer.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //队列拒收处理
            channel.addReturnListener(new ReturnListener() {

                @Override
                public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {

                }

            });

            try {
                // 开启事务
                channel.txSelect();
                // 往test队列中发出一条消息
                channel.basicPublish("test", "test", null, messageBodyBytes);
                // 提交事务
                channel.txCommit();
            } catch (Exception e) {
                e.printStackTrace();
                // 事务回滚
                try {
                    channel.txRollback();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
            try {
                channel.confirmSelect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            channel.addConfirmListener(new ConfirmListener() {

                //处理消息成功回执
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("Ack, SeqNo: " + deliveryTag + ", multiple: " + multiple);
                    if (multiple) {
                        confirmSet.headSet(deliveryTag + 1).clear();
                    } else {
                        confirmSet.remove(deliveryTag);
                    }
                }

                //处理失败回执
                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("Nack, SeqNo: " + deliveryTag + ", multiple: " + multiple);
                    if (multiple) {
                        confirmSet.headSet(deliveryTag + 1).clear();
                    } else {
                        confirmSet.remove(deliveryTag);
                    }
                }

            });

            for(int i =0;i<100;i++) {
                // 查看下一个要发送的消息的序号
                long nextSeqNo = channel.getNextPublishSeqNo();
                try {
                    channel.basicPublish("test", "test", MessageProperties.PERSISTENT_TEXT_PLAIN, messageBodyBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                confirmSet.add(nextSeqNo);
            }

            /**
             * Request specific "quality of service" settings.
             *
             * These settings impose limits on the amount of data the server
             * will deliver to consumers before requiring acknowledgements.
             * Thus they provide a means of consumer-initiated flow control.
             * @see com.rabbitmq.client.AMQP.Basic.Qos
             * @param prefetchSize maximum amount of content (measured in
             * octets) that the server will deliver, 0 if unlimited
             * @param prefetchCount maximum number of messages that the server
             * will deliver, 0 if unlimited
             * @param global true if the settings should be applied to the
             * entire channel rather than each consumer
             * @throws java.io.IOException if an error is encountered
             */
            /*void basicQos(int prefetchSize, int prefetchCount, boolean global) throws IOException;*/
            try {
                //我们可以通过prefetchCount 限制每个消费者在收到下一个确认回执前一次可以最大接受多少条消息。即如果设置prefetchCount =1，RabbitMQ向这个消费者发送一个消息后，再这个消息的消费者对这个消息进行ack之前，RabbitMQ不会向这个消费者发送新的消息
                channel.basicQos(0,1,true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            /**
             * Retrieve a message from a queue using {@link com.rabbitmq.client.AMQP.Basic.Get}
             * @param queue the name of the queue
             * @param autoAck 自动确认消息
             * @return a {@link GetResponse} containing the retrieved message data
             * @throws java.io.IOException if an error is encountered
             */
            /*GetResponse basicGet(String queue, boolean autoAck) throws IOException;*/
            try {
                //自动获取消息（pull）下面是Push方式，二选一
                GetResponse response = channel.basicGet("test",true);
            } catch (IOException e) {
                e.printStackTrace();
            }


            /**
             * Start a non-nolocal, non-exclusive consumer.
             * @param queue 队列名称
             * @param autoAck true if the server should consider messages
             * acknowledged once delivered; false if the server should expect
             * explicit acknowledgements
             * @param consumerTag a client-generated consumer tag to establish context
             * @param callback an interface to the consumer object
             * @return the consumerTag associated with the new consumer
             * @throws java.io.IOException if an error is encountered
             * @see #basicConsume(String, boolean, String, boolean, boolean, Map, Consumer)
             */
            /*String basicConsume(String queue, boolean autoAck, String consumerTag, Consumer callback) throws IOException;*/

            boolean autoAck = false;
            Channel finalChannel = channel;
            try {
                channel.basicConsume("test", autoAck, "test-consumer-tag",
                        new DefaultConsumer(finalChannel) {
                            @Override
                            public void handleDelivery(String consumerTag,
                                                       Envelope envelope,
                                                       AMQP.BasicProperties properties,
                                                       byte[] body)
                                    throws IOException
                            {
                                long deliveryTag = envelope.getDeliveryTag();
                                // positively acknowledge all deliveries up to
                                // this delivery tag
                                finalChannel.basicAck(deliveryTag, true);
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        if(channel!=null){

            try {
                //申明死信队列用的交换机
                channel.exchangeDeclare("dead", BuiltinExchangeType.DIRECT, true, false,false,null);
                //申明死信队列
                channel.queueDeclare("dead-queue", true, false, false, null);
                //绑定
                channel.queueBind("dead", "dead-queue", "dead-queue", null);

                //这里直接借用我们上面API演示用的队列test，不再申明exchange和绑定
                Map<String, Object> map = new HashMap<>();

                //配置正常队列test死信队列信息
                map.put("x-dead-letter-exchange","dead");
                map.put("x-dead-letter-routing-key","dead-queue");

                channel.queueDeclare("test", true, false, false, map);

                //把消息投递到等待超时的正常队列
                channel.basicPublish("test", "test", true,
                        (new AMQP.BasicProperties.Builder()
                                .contentType("text/plain")
                                //为消息设置超时时间
                                .expiration("6000")
                                .build()),
                        "test".getBytes());

                //消费失信队列里面的过时消息
                channel.basicConsume("dead-queue", true, "dead-queue-consumer-tag",
                        new DefaultConsumer(channel) {
                            @Override
                            public void handleDelivery(String consumerTag,
                                                       Envelope envelope,
                                                       AMQP.BasicProperties properties,
                                                       byte[] body)
                                    throws IOException
                            {
                                //这里执行我们的延时任务
                            }
                        });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //最后关闭channel
        if(channel != null){
            try {
                channel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //最后关闭conn
        if(conn != null){
            try {
                conn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
