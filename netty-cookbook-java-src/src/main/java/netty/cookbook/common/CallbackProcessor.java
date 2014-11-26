package netty.cookbook.common;

@FunctionalInterface
public interface CallbackProcessor {    	
	public void process(Object obj);
}