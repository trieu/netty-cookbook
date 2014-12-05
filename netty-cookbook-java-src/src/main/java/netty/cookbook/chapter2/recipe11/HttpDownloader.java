package netty.cookbook.chapter2.recipe11;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import netty.cookbook.common.tcp.NettyBootstrapUtil;

public class HttpDownloader {
	public static class HttpDownloadHandler extends
			SimpleChannelInboundHandler<HttpObject> {
		int written = 0;
		File file;
		FileOutputStream outputStream;
		FileChannel writtenFileChannel;
		public HttpDownloadHandler(File file) {
			super();
			this.file = file;
		}
		void openFileChannel() throws FileNotFoundException {
			outputStream = new FileOutputStream(file);
			writtenFileChannel = outputStream.getChannel();
		}
		void writeFileContent(ByteBuf byteBuf) throws IOException {
			int curWritten = 0;
			try {
				ByteBuffer byteBuffer = byteBuf.nioBuffer();
				curWritten += writtenFileChannel.write(byteBuffer, written);
				written += curWritten;
				byteBuf.readerIndex(byteBuf.readerIndex() + curWritten);
				writtenFileChannel.force(false);
			} catch (Exception e) {
				writtenFileChannel.close();
				outputStream.close();
			}
		}
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg)
				throws Exception {
			if (msg instanceof HttpRequest) {
				openFileChannel();
			} else if (msg instanceof HttpContent) {
				if (writtenFileChannel == null) {
					openFileChannel();
				}
				HttpContent content = (HttpContent) msg;
				ByteBuf byteBuf = content.content();
				writeFileContent(byteBuf);
			} else if (msg instanceof LastHttpContent) {
				if (writtenFileChannel != null && outputStream != null) {
					writtenFileChannel.close();
					outputStream.close();
				}
				ctx.close();
			}
		}
	}
	public static void main(String[] args) throws Exception {
		String url = "http://www.mc2ads.com";
		File file = new File("./data/www.mc2ads.com.html");
		ChannelHandler handler = new HttpDownloadHandler(file);
		NettyBootstrapUtil.newHttpClientBootstrap(url, handler);
	}
}
