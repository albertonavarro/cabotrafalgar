package com.navid.trafalgar.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navid.trafalgar.manager.InitState;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RemoteEventsManager implements ExceptionListener {

    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;

    private final Map<String, MessageConsumer> consumers = new HashMap<>();

    /*@Override
    public void onUnload() {
        consumers.clear();
        System.out.println("onUnload");
        try {
            connection.close();
        } catch (JMSException e) {
            System.out.println("Caught: " + e);
        }

    }*/

    public  RemoteEventsManager() {
        System.out.println("onInit");

        try {
            connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
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

    public void listenMessages(String user, String control, StructureConsumer messageListener) throws JMSException {
        Topic topic = session.createTopic("game/1/" + user + "/" + control);

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

    public static abstract class StructureConsumer<T> implements MessageListener {

        private Class<T> type;

        public StructureConsumer(Class<T> type) {
            this.type = type;
        }

        @Override
        public void onMessage(Message message) {
            ObjectMapper om = new ObjectMapper();
            try {
                T result = om.reader().withType(type).readValue(((ActiveMQTextMessage) message).getText());
                onMessage(result);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

        protected abstract void onMessage(T message);
    }
}
