package chapter3.recipe6.functions;

import java.util.function.UnaryOperator;

import chapter3.recipe6.SimpleHttpRequest;



@FunctionalInterface
public interface Filter extends UnaryOperator<SimpleHttpRequest> {
	
	
}
