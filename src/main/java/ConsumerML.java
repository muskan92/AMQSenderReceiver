/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aksarav
 */

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ConsumerML implements MessageListener{
    
    private String NameOfConsumer;
    
	public ConsumerML(String NameOfConsumer) {
		this.NameOfConsumer = NameOfConsumer;
	}
        
        
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			System.out.println( " INFO | " +NameOfConsumer + " received " + textMessage.getText());
                        
		} catch (JMSException e) {			
			e.printStackTrace();
		} 
	}
            
        
}
