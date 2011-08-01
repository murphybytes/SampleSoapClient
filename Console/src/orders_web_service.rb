require 'date'
require 'java'
require 'WebServiceClient/dist/OrdersHistoryService.jar'

java_import com.currenex.webservice.definitions.AuthenticationPortType
java_import com.currenex.webservice.definitions.OrdersHistoryPortType
java_import com.currenex.webservice.orderhistory.OrdersHistoryRequest
java_import com.currenex.webservice.orderhistory.OrdersHistoryResponse
java_import com.currenex.webservice.definitions.Webservice
java_import com.currenex.webservice.shared.LoginRequest
java_import com.currenex.webservice.shared.LoginResponse
java_import com.currenex.webservice.shared.DateTimePeriod
java_import javax.xml.datatype.XMLGregorianCalendar
java_import javax.xml.datatype.DatatypeFactory
java_import java.util.GregorianCalendar
java_import com.currenex.webservice.shared.Session

class OrdersWebService
  attr_reader :session
  def initialize( options = {} )
    raise "Missing user id" unless options.key?( :user_id )
    raise "Missing password" unless options.key?( :password )
    @options = options
    @webservice = Webservice.new
    @authentication_port = @webservice.getAuthenticationService
    @session = nil
    @response = nil
  end

  def login
    request = LoginRequest.new
    request.setUserID( @options[:user_id] )
    request.setPassword( @options[:password] )
    request.setLoginType( "CreateSession" )
    @response = @authentication_port.login( request )
    @session = @response.getSessionTicket if @response
  end



  def orders( begin_date, end_date ) 
    orders_port = @webservice.getOrdersHistoryService

    xml_begin_date = to_xml_time( begin_date )
    xml_end_date = to_xml_time( end_date )
    period = DateTimePeriod.new
    period.setBegin( xml_begin_date )
    period.setEnd( xml_end_date )
    request = OrdersHistoryRequest.new
    request.setDtPeriod( period )
    session
    response = orders_port.ordersHistory( request )
    response
  end

  private

  def to_xml_time( dt )
    cal = GregorianCalendar.new( dt.year, dt.month, dt.day, dt.hour, dt.min, dt.sec )
    factory = DatatypeFactory.newInstance
    result = factory.newXMLGregorianCalendar( cal )
    result 
  end

  
end
