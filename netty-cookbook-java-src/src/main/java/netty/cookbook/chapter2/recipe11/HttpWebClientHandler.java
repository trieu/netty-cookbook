package netty.cookbook.chapter2.recipe11;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class HttpWebClientHandler extends
		SimpleChannelInboundHandler<HttpObject> {
	File file = new File("./data/www.mc2ads.com.html");
	int written = 0;	
	FileOutputStream outputStream;
	FileChannel localfileChannel;	
	void openFileChannel() throws FileNotFoundException{
		outputStream = new FileOutputStream(file);
		localfileChannel = outputStream.getChannel();
	}	
	void writeFileContent(ByteBuf byteBuf) throws IOException{
		int currentlyWritten = 0;
		try {
			ByteBuffer byteBuffer = byteBuf.nioBuffer();
			currentlyWritten += localfileChannel.write(byteBuffer, written);
			written += currentlyWritten;
			byteBuf.readerIndex(byteBuf.readerIndex() + currentlyWritten);
			localfileChannel.force(false);
		} catch(Exception e) {
			localfileChannel.close();
			outputStream.close();
		}
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg)
			throws Exception {
		if (msg instanceof HttpRequest) {
			openFileChannel();
		}
		else if (msg instanceof HttpContent) {
			if(localfileChannel == null){
				openFileChannel();
			}			
			HttpContent content = (HttpContent) msg;			
			ByteBuf byteBuf = content.content();			
			writeFileContent(byteBuf);
		}
		else if(msg instanceof LastHttpContent){
			if(localfileChannel != null && outputStream != null){
				localfileChannel.close();
				outputStream.close();
			}
			System.out.println("written bytes "+written);
			ctx.close();
		}
	}
}