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
		FileOutputStream outStream;
		FileChannel fileChnl;
		public HttpDownloadHandler(File file) {
			super();
			this.file = file;
		}
		void initFileChannel() throws FileNotFoundException {
			outStream = new FileOutputStream(file);
			fileChnl = outStream.getChannel();
		}
		void writeBytesToFile(ByteBuf byteBuf) throws IOException {
			int writtenIndex = 0;
			try {
				ByteBuffer byteBuffer = byteBuf.nioBuffer();
				writtenIndex += fileChnl.write(byteBuffer, written);
				written += writtenIndex;
				byteBuf.readerIndex(byteBuf.readerIndex() + writtenIndex);
				fileChnl.force(false);
			} catch (Exception e) {
				fileChnl.close();
				outStream.close();
			}
		}
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
			try {
				if (msg instanceof HttpRequest) {
					initFileChannel();
				} else if (msg instanceof HttpContent) {
					if (fileChnl == null) {
						initFileChannel();
					}
					ByteBuf byteBuf = ((HttpContent) msg).content();
					writeBytesToFile(byteBuf);
				} else if (msg instanceof LastHttpContent) {
					if (fileChnl != null && outStream != null) {
						fileChnl.close();
						outStream.close();
					}
					ctx.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
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
