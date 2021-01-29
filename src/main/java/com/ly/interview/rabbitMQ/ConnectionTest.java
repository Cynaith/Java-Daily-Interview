package com.ly.interview.rabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: liuyang
 **/

public class ConnectionTest {

  public static void main(String[] args) {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUsername("alimq");
    factory.setPassword("alimq");
    factory.setHost("47.101.171.252");
    factory.setVirtualHost("test");
    factory.setPort(5672);
    try {
      Connection conn = factory.newConnection();
      Channel channel = conn.createChannel();
      System.out.println(channel.isOpen());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TimeoutException e) {
      e.printStackTrace();
    }
  }

}
