package netty.cookbook.chapter2.recipe10;

import java.io.IOException;
import java.net.InetSocketAddress;

import netty.cookbook.common.LogUtil;

import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.apache.avro.util.Utf8;

import avro.Mail;
import avro.Message;

/**
 * @author trieu
 *
 */
public class ProxyServerAvroRPC {
	public static class MailImpl implements Mail {
		public Utf8 send(Message message) {			
			String s = String.format("message details, to:%s from:%s body:%s", message.getTo(),  message.getFrom(), message.getBody());
			LogUtil.println(s);
			return new Utf8("Sent OK to "+ message.getTo());
		}
	}
	private static Server server;
	static void startServer() throws IOException {
		server = new NettyServer(new SpecificResponder(Mail.class,new MailImpl()), new InetSocketAddress(10000));
	}
	public static void main(String[] args) throws Exception {
		LogUtil.println("Starting ServerAvroRPC");
		startServer();
		LogUtil.println("ServerAvroRPC started and wait for 15s");		
		Thread.sleep(15000);
		server.close();
	}
}
