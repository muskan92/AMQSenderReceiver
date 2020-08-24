/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aksarav
 */

import org.apache.activemq.ActiveMQConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.SQLOutput;
import java.util.logging.Level;

import static java.lang.System.exit;
import static java.lang.System.setOut;


public class QueueReceiver {

  private static final Logger LOGGER = LoggerFactory.getLogger(QueueReceiver.class);
  
  private String clientId;
  private Connection connection;
  private MessageConsumer messageConsumer;
  public String ReceivedMessage;

  private static BufferedReader msgstream = new BufferedReader(new InputStreamReader(System.in));
  private static String readmode = "onebyone";
  
  public void init(String queueName)
      throws JMSException, URISyntaxException, IOException {

    connection = ActiveMQConnection.makeConnection("","","tcp://localhost:61616");
    //connection = ActiveMQConnection.makeConnection("tcp://localhost:61616");


  // create a Session
  Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

  // create the Queue from which messages will be received
  Queue queue = session.createQueue(queueName);
  
  
  /*QueueBrowser qb = session.createBrowser(queue);
  Enumeration qe = qb.getEnumeration();
  System.out.println(qe.hasMoreElements()); */
 
  
  
  
  // create a MessageConsumer for receiving messages
  messageConsumer = session.createConsumer(queue);
  

  // start the connection in order to receive messages
  connection.start();

  }

  public void closeConnection() throws JMSException {
    connection.close();
  }

  public String receivemsg(int timeout, boolean acknowledge)
      throws JMSException, InterruptedException {




    if (readmode.equalsIgnoreCase("allatonce"))
    {
    
    
    //Sleep for 3000 milliseconds, before processing the Queue. Let the consumer get ready
    Thread.sleep(3000);
      
    while(true)
    { 
    Message message = messageConsumer.receive(timeout);    
    // check if a message was received
    if (message instanceof Message)
    {
     TextMessage textMessage = (TextMessage) message;
     
      // retrieve the message content
      String text = textMessage.getText();
      LOGGER.info("received message with text='{}'",
          text);

      if (acknowledge) {
        // acknowledge the successful processing of the message
        message.acknowledge();
        LOGGER.info("message acknowledged");
      } else {
        LOGGER.info("message not acknowledged");
      }
      
      ReceivedMessage = text;
      LOGGER.info(ReceivedMessage);
    } else {
      LOGGER.info("no more messages to read from the queue");
      break;
    }
   
    }
    }
    else if (readmode.equalsIgnoreCase("onebyone"))
    {
        Message message = messageConsumer.receive(timeout);
    // check if a message was received
    
    if (message instanceof Message)
    {
     TextMessage textMessage = (TextMessage) message;
     
      // retrieve the message content
      String text = textMessage.getText();
      LOGGER.info("\nreceived message with text='{}'",
          text);

      if (acknowledge) {
        // acknowledge the successful processing of the message
        message.acknowledge();
        LOGGER.info("message acknowledged");
      } else {
        LOGGER.info("message not acknowledged");
      }
      
      ReceivedMessage = text;
      LOGGER.info(ReceivedMessage);
    } else {
      LOGGER.info("no more message to read from the queue");
      return ReceivedMessage;
    }
    
    }
      
    
    return null;
  }
  
  public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException
  {
      

      
      QueueReceiver sobj = new QueueReceiver();
      try {

              sobj.init("RUC-MRS");
              String rcvMsg = sobj.receivemsg(10, true);
          System.out.println("rcvMsg is :" +rcvMsg);
              sobj.closeConnection();
      } catch (JMSException ex) {
          java.util.logging.Logger.getLogger(QueueReceiver.class.getName()).log(Level.SEVERE, null, ex);
          System.out.println("Exception  is :" +ex);
      }
      } 
  }

