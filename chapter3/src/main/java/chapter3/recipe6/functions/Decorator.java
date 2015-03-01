package chapter3.recipe6.functions;

import java.util.function.UnaryOperator;

import chapter3.recipe6.SimpleHttpResponse;

@FunctionalInterface
public interface Decorator extends UnaryOperator<SimpleHttpResponse>{

}
