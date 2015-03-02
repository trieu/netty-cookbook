package chapter3.recipe6;

import netty.cookbook.common.http.ContentTypePool;

import com.google.gson.Gson;

public class SimpleHttpResponse {
	protected int status = 200;
	protected String contentType = ContentTypePool.TEXT_UTF8;
	protected String head;
	protected String body;
	protected long time;
	
	public SimpleHttpResponse() {
		// TODO Auto-generated constructor stub
	}

	public SimpleHttpResponse(String data) {
		super();
		this.body = data;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String data) {
		this.body = data;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	@Override
	public String toString() {		
		return new Gson().toJson(this);
	}

}