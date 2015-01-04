package netty.cookbook.common.redis;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

public class RedisInfo {
	public static final String LOCALHOST_STR = "localhost";

	private String host;
	private int port;
	private String auth;
	private ShardedJedisPool shardedJedisPool; 
	
	protected void initThePool(){
		List<JedisShardInfo> shardInfos = new ArrayList<JedisShardInfo>(1);
		shardInfos.add(new JedisShardInfo(getHost(), getPort(), 0));
		shardedJedisPool = new ShardedJedisPool(RedisConnectionPoolConfig.getJedisPoolConfigInstance(), shardInfos);
	}

	public RedisInfo(String host, int port) {
		this.host = host;
		this.port = port;
		initThePool();
	}
	
	public RedisInfo(String host, int port,String auth) {
		this.host = host;
		this.port = port;
		this.auth = auth;
		initThePool();
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
	
	public String getAuth() {
		return auth;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RedisInfo) {
			RedisInfo hp = (RedisInfo) obj;

			String thisHost = convertHost(host);
			String hpHost = convertHost(hp.host);
			return port == hp.port && thisHost.equals(hpHost);

		}

		return false;
	}
	
	public ShardedJedisPool getShardedJedisPool() {
		return shardedJedisPool;
	}

	@Override
	public String toString() {
		return host + ":" + port;
	}

	private String convertHost(String host) {
		if (host.equals("127.0.0.1"))
			return LOCALHOST_STR;
		else if (host.equals("::1"))
			return LOCALHOST_STR;

		return host;
	}
}