/*
 * @author tetsuya, @date 14/02/17 17:46
 */
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.*;

public class JedisWrapper {

    //private final String = "192.168.2.2,192.168.2.3";

	static final int expire = 60 * 60 ;

    public void set(String key,String value){

        Jedis jedis = new Jedis("192.168.2.2");
        try {
            jedis.set(key,value);
            jedis.expire(key, expire);
        } catch (JedisConnectionException e1){
        	failOverOnSet(key, value);
        } catch (JedisDataException e2){
        	failOverOnSet(key, value);
        }
    }


	private void failOverOnSet(String key, String value) {
		Jedis jedis = new Jedis("192.168.2.3");
		try{
			jedis.set(key,value);
			jedis.expire(key, expire);
		} catch (JedisConnectionException e2){
			return;
		}
	}


    public String get(String key){

        Jedis jedis ;

        jedis = new Jedis("192.168.2.2");
        try {
            return jedis.get(key);
        } catch (JedisConnectionException e1){
        	jedis = new Jedis("192.168.2.3");
        	try{
        		return jedis.get(key);
        	} catch (JedisConnectionException e2){
        		return null;
        	}
        }
    }

}
