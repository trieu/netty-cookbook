package netty.cookbook.chapter2.recipe10;


import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.util.Utf8;

import avro.Mail;
import avro.Message;

/**
 * Start a server, attach a client, and send a message.
 */
public class ClientAvroRPC {
    public static void main(String[] args) throws IOException {      
    	if(args.length < 3){
    		args = new String[] {"someone@example.com","myself@example.com","Hello !"};
    	}
        NettyTransceiver client = new NettyTransceiver(new InetSocketAddress(10000));
        // client code - attach to the server and send a message
        Mail proxy = (Mail) SpecificRequestor.getClient(Mail.class, client);
        System.out.println("Client built, got proxy");

        // fill in the Message record and send it
        Message message = new Message();
        message.setTo(new Utf8(args[0]));
        message.setFrom(new Utf8(args[1]));
        message.setBody(new Utf8(args[2]));
        System.out.println("Calling proxy.send with message:  " + message.toString());
        System.out.println("Result: " + proxy.send(message));
        // cleanup
        client.close();        
    }
}