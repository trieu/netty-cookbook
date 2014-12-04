package netty.cookbook.chapter2.recipe10;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.apache.avro.util.Utf8;

import avro.Mail;
import avro.Message;

public class ServerAvroRPC {
	public static class MailImpl implements Mail {
		public Utf8 send(Message message) {			
			System.out.println("Sending message to " + message.getTo().toString()
					+ " from " + message.getFrom().toString() + " with body "
					+ message.getBody().toString());			
			return new Utf8("Sent to "+ message.getTo());
		}
	}
	private static Server server;
	static void startServer() throws IOException {
		server = new NettyServer(new SpecificResponder(Mail.class,new MailImpl()), new InetSocketAddress(10000));
	}
	public static void main(String[] args) throws Exception {
		System.out.println("Starting ServerAvroRPC");
		startServer();
		System.out.println("ServerAvroRPC started and wait for 10s");		
		Thread.sleep(10000);
		server.close();
	}
}
