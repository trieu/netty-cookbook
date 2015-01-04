package netty.cookbook.chapter2.recipe3;

import java.io.Serializable;

import com.google.gson.Gson;

public class PurchaseData implements Serializable{
	private static final long serialVersionUID = -5467453661148034694L;
	private final int itemId;
	private final float price;
	private final String buyer;
	private final String seller;
	private final int unixTime;
	private final boolean processed;
	public PurchaseData(int itemId, float price, String buyer, String seller,
			int unixTime, boolean processed) {
		super();
		this.itemId = itemId;
		this.price = price;
		this.buyer = buyer;
		this.seller = seller;
		this.unixTime = unixTime;
		this.processed = processed;
	}	
	public PurchaseData(Object obj, boolean processed) {
		super();
		PurchaseData data = (PurchaseData)obj;
		this.itemId = data.itemId;
		this.price = data.price;
		this.buyer = data.buyer;
		this.seller = data.seller;
		this.unixTime = data.unixTime;
		this.processed = processed;
	}
	public float getPrice() {
		return price;
	}
	public String getBuyer() {
		return buyer;
	}
	public String getSeller() {
		return seller;
	}
	public int getUnixTime() {
		return unixTime;
	}		
	public int getItemId() {
		return itemId;
	}	
	public boolean isProcessed() {
		return processed;
	} 
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
