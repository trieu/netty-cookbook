package netty.cookbook.common;

import io.netty.buffer.ByteBuf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author trieunt
 *
 */
public class NettyMonitorIO {
	static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	static ConcurrentMap<String, Long> dataOutStats = new ConcurrentHashMap<String, Long>();
	static ConcurrentMap<String, Long> dataInStats = new ConcurrentHashMap<String, Long>();
	public static long updateDataOut(ByteBuf buf) {
		String time = DATE_TIME_FORMAT.format(new Date());
		long c = dataOutStats.getOrDefault(time, 0L) + buf.readableBytes();
		dataOutStats.put(time, c);
		return c;
	}	
	public static long updateDataIn(ByteBuf buf) {
		String time = DATE_TIME_FORMAT.format(new Date());
		long c = dataInStats.getOrDefault(time, 0L) + buf.writableBytes();
		dataInStats.put(time, c);
		return c;
	}
	static {
		new Timer(true).schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("--------------------------------");
				System.out.println("Data In Stats:");
				dataInStats.forEach((String key, Long val)->{
					System.out.println(key + " : "+val);
				});
				System.out.println("Data Out Stats:");
				dataOutStats.forEach((String key, Long val)->{
					System.out.println(key + " : "+val);
				});
			}
		}, 2000, 2000);
	}
}
