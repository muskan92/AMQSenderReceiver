import org.apache.activemq.ActiveMQConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.logging.Level;

import static java.lang.System.exit;

public class QueueSender {
   
    private static final Logger LOGGER =
      LoggerFactory.getLogger(QueueSender.class);

  //private String clientId;
  private Connection connection;
  private Session session;
  private MessageProducer messageProducer;
  private static String QN="RUC-MRS";
  private static BufferedReader msgstream = new BufferedReader(new InputStreamReader(System.in));
  
    public void init(String queueName) 
      throws JMSException, URISyntaxException, IOException {

    connection = ActiveMQConnection.makeConnection("","","tcp://localhost:61616");
    


    
    // create a Session
    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

    //System.out.print ("what is session"+session);

    // create the Queue to which messages will be sent . If the Queue is not there it will be auto created
    Queue queue = session.createQueue(queueName);

    // create a MessageProducer for sending messages
    messageProducer = session.createProducer(queue);
  }

  public void closeConnection() throws JMSException {
    connection.close();
  }

  public void sendmessage(String Message)
      throws JMSException {
      
    String text = Message;

    // create a JMS TextMessage
    TextMessage textMessage = session.createTextMessage(text);

    // send the message to the queue destination
    messageProducer.send(textMessage);

    
    LOGGER.info("sent message with text='{}'", text);
  }
  
  public static void main(String[] args) throws IOException, JMSException, URISyntaxException
  {
      QueueSender sobj = new QueueSender();
      BufferedReader msgStream = new BufferedReader(new InputStreamReader(System.in));
      
      try {
//do {
              sobj.init(QN);

              sobj.sendmessage("Heloo there");
              sobj.closeConnection();
         // }while(true);
      } catch (JMSException ex) {
          java.util.logging.Logger.getLogger(QueueSender.class.getName()).log(Level.SEVERE, null, ex);
          System.out.println("exception is : "+ex);
      }
      System.out.println("exception is : ");
  }
}