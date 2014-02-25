import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.*;

public class Client {

	public static void main(String args[]){

		for (int i = 0; i<10 ; i ++){
			JedisWrapperForReplication jedisWrapper = new JedisWrapperForReplication();
			jedisWrapper.set("foo", "bar");
//			String a1 = jedisWrapper.get("foo");
//			System.out.println(a1);
		}
	}
}
