package com.eris.orderhistory.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.BeforeClass;
import com.eris.orderhistory.SessionTokenManager;
import com.currenex.webservice.definitions.AuthenticationPortType;
import com.currenex.webservice.definitions.OrdersHistoryPortType;
import com.currenex.webservice.orderhistory.OrdersHistoryRequest;
import com.currenex.webservice.orderhistory.OrdersHistoryResponse;
import com.currenex.webservice.definitions.Webservice;
import com.currenex.webservice.shared.LoginRequest;
import com.currenex.webservice.shared.LoginResponse;
import com.currenex.webservice.shared.DateTimePeriod;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import java.util.GregorianCalendar;
import com.currenex.webservice.shared.Session;


public class SessionTokenManagerTest {

    private static LoginResponse loginResponse = null;
    
    @BeforeClass 
    public static void oneTimeSetup() {
	Webservice webservice = new Webservice();
	AuthenticationPortType service = webservice.getAuthenticationService() ;
	LoginRequest loginRequest = new LoginRequest();
	loginRequest.setUserID( "fakeuser" );
	loginRequest.setPassword( "fakepassword" );
	loginRequest.setLoginType( "CreateSession" );
	try {
	    loginResponse = service.login( loginRequest );
	} catch( Exception e ) {
	    System.out.println( e );
	}

    }


    @Test 
    public void testLogin()  {
	
	assertNotNull(loginResponse);

    }

    @Test
    public void testOrderHistory() throws Exception  {
	DatatypeFactory factory = DatatypeFactory.newInstance();
	Webservice ws = new Webservice();
	OrdersHistoryPortType service = ws.getOrdersHistoryService();
	DateTimePeriod period = new DateTimePeriod();
	GregorianCalendar today = new GregorianCalendar();
	
	period.setEnd( factory.newXMLGregorianCalendar(today) );
	Duration oneDay = factory.newDuration( 60 * 60 * 24 * -1000 );  
	XMLGregorianCalendar beginDate = factory.newXMLGregorianCalendar( today );
	beginDate.add( oneDay ); // actually going back a day
	period.setBegin( beginDate );
	
	OrdersHistoryRequest request = new OrdersHistoryRequest();
	request.setDtPeriod( period );
	Session session = new Session();
	session.setUserID( loginResponse.getUserID() );
	session.setSessionTicket( loginResponse.getSessionTicket() );
	OrdersHistoryResponse response = service.ordersHistory( request, session );
	assertNotNull( response );
    }
    
}