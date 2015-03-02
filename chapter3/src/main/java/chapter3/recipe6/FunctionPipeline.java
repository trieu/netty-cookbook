package chapter3.recipe6;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import chapter3.recipe6.functions.Decorator;
import chapter3.recipe6.functions.Filter;
import chapter3.recipe6.functions.FinalProcessor;

public class FunctionPipeline {
	
	
	List<Filter> filterFunctions = new ArrayList<>();
	public FunctionPipeline apply(Filter f){
		filterFunctions.add(f);
		return this;
	}
	
	List<Function<SimpleHttpRequest , SimpleHttpResponse>> processorFunctions = new ArrayList<>();
	public FunctionPipeline apply(Function<SimpleHttpRequest , SimpleHttpResponse> f){
		processorFunctions.add(f);
		return this;
	}
	
	List<Decorator> decoratorFunctions = new ArrayList<>();
	public FunctionPipeline apply(Decorator f){
		decoratorFunctions.add(f);
		return this;
	}
	
	SimpleHttpResponse apply(SimpleHttpRequest req){
		SimpleHttpResponse resp = null;
		int s = 0; 
		
		//apply all filter functions
		s = filterFunctions.size();
		int i = 0;
		while(s > 0){			
			req = filterFunctions.get(i).apply(req);
			if(req.isNotAuthorized()){
				return new SimpleHttpResponse("Not Authorized Request");
			}
			i++;
			if(i >= s){
				break;
			}
		}
		
		//apply all Event Processor functions
		s = processorFunctions.size();
		int j = 0;
		while(s > 0){			
			resp = processorFunctions.get(j).apply(req);
			if(resp == null){
				return new SimpleHttpResponse("empty data");
			}
			j++;
			if(j >= s){
				break;
			}			
		}
		
		//apply all decorator functions (backward to first)
		s = decoratorFunctions.size();
		while(s > 0){
			s--;
			resp = decoratorFunctions.get(s).apply(resp);
			if(resp == null){
				return new SimpleHttpResponse("empty data");
			}
			if(s == 0){
				break;
			}			
		}
		
		return resp;
	}
	


	public static void main(String[] args) throws Exception {

		FunctionPipeline processor = new FunctionPipeline();

//		SimpleHttpRequest httpRequest = new SimpleHttpRequest();
//		httpRequest.setUri("/get/123");
//
//		SimpleHttpResponse resp1 = processor.process(httpRequest);
//		System.out.println(resp1.getData());
		//System.out.println(resp.getTime());
		
		Filter filterAccessAdmin = req -> {
			req.setNotAuthorized(req.getUri().contains("admin"));
			return req;			
		};		
		FinalProcessor logic123Function = req -> {
			if(req.getUri().contains("123")){
				return new SimpleHttpResponse("123 data");
			}
			return new SimpleHttpResponse();
		};
		Decorator formatingResult = resp -> {
			resp.setBody("(" + resp.getBody() + ")");
			return resp;
		};
				
		processor.apply(filterAccessAdmin).apply(logic123Function).apply(formatingResult);		
		System.out.println(processor.apply(new SimpleHttpRequest("admin/edit/123")));
		System.out.println(processor.apply(new SimpleHttpRequest("user/edit/123")));
		System.out.println(processor.apply(new SimpleHttpRequest("user/edit/456")));
				
		
		FinalProcessor logic456Function = req -> {
			if(req.getUri().contains("456")){
				return new SimpleHttpResponse("456 data");
			}
			return null;
		};
		processor.apply(logic456Function);
		
		System.out.println(processor.apply(new SimpleHttpRequest("user/edit/456")));
		
		
	}
}
