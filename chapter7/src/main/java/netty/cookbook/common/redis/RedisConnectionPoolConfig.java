package netty.cookbook.common.redis;

import netty.cookbook.common.FileUtils;
import netty.cookbook.common.StringUtil;
import redis.clients.jedis.JedisPoolConfig;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class RedisConnectionPoolConfig {
	static String baseConfig = "configs/";
	static final String REDIS_CONNECTION_POOL_CONFIG_FILE = "redis-connection-pool-configs.json";	
	protected static RedisConnectionPoolConfig _instance = null;
	
	public static RedisConnectionPoolConfig theInstance() {
		return _instance;
	}
	
	public static String getRedisPoolConnectionConfigFile(){
		return StringUtil.toString(baseConfig,REDIS_CONNECTION_POOL_CONFIG_FILE);
	}
	
	public static JedisPoolConfig getJedisPoolConfigInstance(){
		if(_instance == null){
			try {
				String json = FileUtils.readFileAsString(getRedisPoolConnectionConfigFile());
				_instance = new Gson().fromJson(json, RedisConnectionPoolConfig.class);
			}
			catch (Exception e) {
				if (e instanceof JsonSyntaxException) {
					e.printStackTrace();
					System.err.println("Wrong JSON syntax in file "+getRedisPoolConnectionConfigFile());
				}
				else {
					e.printStackTrace();
				}
			}
		}
		return _instance.getJedisPoolConfig();
	}
	
	int maxTotal = 20;
	int maxIdle = 10;
	int minIdle = 1;
	int maxWaitMillis = 3000;
	int numTestsPerEvictionRun = 10;
	boolean testOnBorrow = true;
	boolean testOnReturn = true;
	boolean testWhileIdle = true;
	int timeBetweenEvictionRunsMillis = 60000;
	
	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxActive) {
		this.maxTotal = maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}


	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}
	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
	public int getNumTestsPerEvictionRun() {
		return numTestsPerEvictionRun;
	}

	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public int getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

	public JedisPoolConfig getJedisPoolConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(this.maxTotal);
		config.setMaxIdle(this.maxIdle);
		config.setMinIdle(this.minIdle);
		config.setMaxWaitMillis(this.maxWaitMillis);
		config.setNumTestsPerEvictionRun(this.numTestsPerEvictionRun);
		config.setTestOnBorrow(this.testOnBorrow);
		config.setTestOnReturn(this.testOnReturn);
		config.setTestWhileIdle(this.testWhileIdle);
		config.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
		return config;
	}
}
