package netty.cookbook.recipe1;

@FunctionalInterface
public interface CallbackProcessor {    	
	public void process(String res);
}
