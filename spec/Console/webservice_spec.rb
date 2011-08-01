require 'orders_web_service'

describe OrdersWebService do
  describe "#session" do 
    it "returns a token on successful login" do
      webservice = OrdersWebService.new( :user_id => "fakeuser", :password => "fakepassword"  )
      webservice.login
      webservice.session.should_not eq( nil )
    end
  end

  describe "#todays_orders" do
    it "returns todays orders" do 
      today = DateTime.now
      yesterday = (today - 1)
      webservice = OrdersWebService.new( :user_id => "fakeuser", :password => "fakepassword" )      
      webservice.login
      webservice.orders( yesterday, today ).should_not eq(nil)
    end
  end
end
