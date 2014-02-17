import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.*;

public class Client {

	public static void main(String args[]){
		JedisWrapper jedisWrapper = new JedisWrapper();

		jedisWrapper.set("foo", "bar");
		String a1 = jedisWrapper.get("foo");
		System.out.println(a1);
	}
}
