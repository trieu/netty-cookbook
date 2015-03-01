package chapter3.recipe6;

import com.google.gson.Gson;

public class SimpleHttpResponse {
	protected String data;
	protected long time;
	
	public SimpleHttpResponse() {
		// TODO Auto-generated constructor stub
	}

	public SimpleHttpResponse(String data) {
		super();
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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