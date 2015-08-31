package com.navid.trafalgar.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navid.trafalgar.manager.InitState;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RemoteEventsManager implements ExceptionListener, AutoCloseable {

    private final static Logger logger = LoggerFactory.getLogger(RemoteEventsManager.class);
    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;

    private final Map<String, MessageConsumer> consumers = new HashMap<>();


    public  RemoteEventsManager() {

        try {
            connectionFactory = new ActiveMQConnectionFactory("tcp://lazylogin.ws:61616");
            // Create a Connection
            connection = connectionFactory.createConnection();
            connection.start();

            connection.setExceptionListener(this);

            // Create a Session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        } catch (Exception e) {
            System.out.println("Caught: " + e);
        }
    }

    @Override
    public void onException(JMSException exception) {
        System.out.println("onException" + exception.getMessage());
    }

    public void listenMessages(Long gameId, String user, String control, StructureConsumer messageListener) throws JMSException {
        Topic topic = session.createTopic("game/" + gameId + "/" + user + "/" + control);

        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(messageListener);
        consumers.put(user + "_" + control, consumer);
    }

    public void unsubscribe(String user, String control) {
        try {
            consumers.remove(user + "_" + control).close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    public static abstract class StructureConsumer<T> implements MessageListener {

        private Class<T> type;

        public StructureConsumer(Class<T> type) {
            this.type = type;
        }

        @Override
        public void onMessage(Message message) {
            ObjectMapper om = new ObjectMapper();
            try {
                T result = om.reader().withType(type).readValue(((ActiveMQBytesMessage) message).getContent().data);
                onMessage(result);
                logger.info("Received event {}", result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected abstract void onMessage(T message);
    }
}
