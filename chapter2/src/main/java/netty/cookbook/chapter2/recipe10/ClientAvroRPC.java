package netty.cookbook.chapter2.recipe10;


import java.io.IOException;
import java.net.InetSocketAddress;

import netty.cookbook.common.LogUtil;

import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.util.Utf8;

import avro.Mail;
import avro.Message;

/**
 * Start ClientAvroRPC, and send a message.
 */
public class ClientAvroRPC {
    public static void main(String[] args) throws IOException {      
    	if(args.length < 3){
    		args = new String[] {"someone@example.com","myself@example.com","Hello !"};
    	}
        NettyTransceiver client = new NettyTransceiver(new InetSocketAddress(10000));     
        Mail proxy = (Mail) SpecificRequestor.getClient(Mail.class, client);
        LogUtil.println("ClientAvroRPC built OK, got proxy, ready to send data ...");     
        Message message = new Message();
        message.setTo(new Utf8(args[0]));
        message.setFrom(new Utf8(args[1]));
        message.setBody(new Utf8(args[2]));
        LogUtil.println("Calling proxy.send with message:  " + message.toString());
        LogUtil.println("Result from server: " + proxy.send(message)); 
        client.close();        
    }
}