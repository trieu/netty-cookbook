package netty.cookbook.chapter2.recipe12;

import netty.cookbook.chapter2.recipe12.WebUrl.FacebookStats;

public class PubSubDemo {

	public static void main(String[] args) {
		WebUrl model = new WebUrl("http://vnexpress.net");
		SocialAnalyticUpdater observer = new SocialAnalyticUpdater(model);
		
		for (FacebookStats stats : model.getFacebookStats()) {
			stats.setLikeCount(stats.getLikeCount() + 1);
		}
		
		for (FacebookStats stats : model.getFacebookStats()) {
			stats.setShareCount(stats.getShareCount() + 1);
		}
	}
}
