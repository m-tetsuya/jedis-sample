/*
 * @author tetsuya, @date 14/02/17 17:46
 */
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.*;

public class JedisWrapperForReplication {

    //private final String = "192.168.2.2,192.168.2.3";

    static protected String currentMaster = "192.168.2.2";
    static protected String currentSlave = "192.168.2.3";

    static protected int numOfMasterFail = 0;
    static protected final int LIMIT_OF_MASTER_FAIL = 3;

	static final int expire = 60 * 60 ;

	protected synchronized void countUpMasterFail(){
		numOfMasterFail ++;
		if(numOfMasterFail >= LIMIT_OF_MASTER_FAIL){
			swapMasterAndSlave();
			numOfMasterFail = 0;
		}
	}

	protected void swapMasterAndSlave(){
		System.out.println("swap!");
		String temp = currentMaster;
		currentMaster = currentSlave;
		currentSlave = temp;
	}

    public void set(String key,String value){

    	String master = currentMaster;
    	String slave = currentSlave;

    	System.out.println("connecting to " + master);
        Jedis jedis = new Jedis(master);
        try {
            jedis.set(key,value);
            jedis.expire(key, expire);
        } catch (JedisConnectionException e1){
        	failOverOnSet(key, value,slave);
        } catch (JedisDataException e2){
        	failOverOnSet(key, value,slave);
        }
    }


	private void failOverOnSet(String key, String value,String slave) {
		System.out.println("fail over!");

		countUpMasterFail();

		Jedis jedis = new Jedis(slave);
		try{
			jedis.set(key,value);
			jedis.expire(key, expire);
		} catch (JedisConnectionException e2){
			System.out.println("fail finally!");
			return;
		}
	}


    public String get(String key){

    	String master = currentMaster;
    	String slave = currentSlave;

        Jedis jedis = new Jedis(master);

        try {
            return jedis.get(key);
        } catch (JedisConnectionException e1){
        	countUpMasterFail();
        	jedis = new Jedis(slave);
        	try{
        		return jedis.get(key);
        	} catch (JedisConnectionException e2){
        		return null;
        	}
        }
    }

}
