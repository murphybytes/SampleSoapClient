package com.eris.orderhistory;

import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.ws.handler.MessageContext;
import javax.xml.namespace.QName;
import java.util.Set;
import com.currenex.webservice.shared.Session;

public class SessionTokenManager implements SOAPHandler< SOAPMessageContext > {
    private String token;
    private String userId;

    public SessionTokenManager( String token ) {
	this.token = token;
	//	this.userID = userID;
    }

    @Override   public void close( MessageContext context ) {
   }

    @Override public boolean handleFault(SOAPMessageContext context ) {
	return false;
    }

    @Override	public boolean handleMessage(SOAPMessageContext context) {
	try {
	    SOAPMessage message = context.getMessage();

	    if( (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)) {
		SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
		SOAPHeader header = envelope.addHeader();
		//Session session = new Session();
		//session.setUserID( this.userID );
		//session.setSessionTicket( this.token );
		SOAPHeaderElement element = header.addHeaderElement( envelope.createName( "sessionTicket" ) );
		element.setValue( this.token );
		message.saveChanges();
	    }
	} catch( Exception e ) {
	    System.out.println( e );
	    return false;
	}
	return true;
	
    }

    @Override public Set<QName> getHeaders() {
	return null;
    }

    
   
}