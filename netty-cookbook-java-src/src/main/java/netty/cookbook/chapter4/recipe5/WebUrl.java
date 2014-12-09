package netty.cookbook.chapter4.recipe5;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class WebUrl {
	public static final String LIKE_COUNT = "likeCount";
	public static final String SHARE_COUNT = "shareCount";

	private List<FacebookStats> stats = new ArrayList<FacebookStats>();
	private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();
	private String url;

	public class FacebookStats {		
		private long likeCount;
		private long shareCount;

		public FacebookStats(long like, long share) {
			this.likeCount = like;
			this.shareCount = share;
		}

		public long getLikeCount() {
			return likeCount;
		}

		public void setLikeCount(long like) {
			notifyListeners(this, LIKE_COUNT, this.likeCount,	this.likeCount = like);

		}

		public long getShareCount() {
			return shareCount;
		}

		public void setShareCount(long shareCount) {
			notifyListeners(this, SHARE_COUNT, this.shareCount,	this.shareCount = shareCount);
		}
	}

	public List<FacebookStats> getFacebookStats() {
		return stats;
	}
	
	public String getUrl() {
		return url;
	}

	public WebUrl(String url) {
		this.url = url;
		stats.add(new FacebookStats(0, 0));		
	}

	private void notifyListeners(Object object, String property,
			long oldValue, long newValue) {
		for (PropertyChangeListener name : listener) {
			name.propertyChange(new PropertyChangeEvent(this, property,	oldValue, newValue));
		}
	}

	public void addChangeListener(PropertyChangeListener newListener) {
		listener.add(newListener);
	}

}