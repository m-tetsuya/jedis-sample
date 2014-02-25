
public class JedisWrapper {

	static String getCustomerSeriesKey(int customerId){
		return "customer-series-json-"+customerId;
	}

	static void setCustmerSeries(int customerId ,String json){
			JedisWrapperForReplication jedisWrapper = new JedisWrapperForReplication();
			jedisWrapper.set(getCustomerSeriesKey(customerId), json);
	}

	static String getCustmerSeries(int customerId){
			JedisWrapperForReplication jedisWrapper = new JedisWrapperForReplication();
			return jedisWrapper.get(getCustomerSeriesKey(customerId));
	}

}
