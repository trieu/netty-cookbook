package chapter3.recipe6.functions;

import java.util.function.Function;

import chapter3.recipe6.SimpleHttpRequest;
import chapter3.recipe6.SimpleHttpResponse;

@FunctionalInterface
public interface FinalProcessor extends Function<SimpleHttpRequest , SimpleHttpResponse>{

}
