package netty.cookbook.common.http;

import io.netty.buffer.ByteBuf;

public class HttpOutputResource {
	ByteBuf byteBuf;
	long length;
	long lastModified;
	
	
	public HttpOutputResource(ByteBuf byteBuf, long lastModified) {
		super();
		this.byteBuf = byteBuf;
		this.length = byteBuf.readableBytes();
		this.lastModified = lastModified;
	}

	public ByteBuf getByteBuf() {
		return byteBuf;
	}

	public void setByteBuf(ByteBuf byteBuf) {
		this.byteBuf = byteBuf;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}	
}
