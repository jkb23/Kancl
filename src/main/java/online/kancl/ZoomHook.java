package online.kancl;

import spark.Request;

import java.util.stream.Collectors;

public class ZoomHook
{
	public void accept(Request request)
	{
		String queryParams = request.queryParams().stream().map(key -> key + "=" + String.join(",", request.queryParamsValues(key)))
				.collect(Collectors.joining("&"));

		System.out.println(request.requestMethod() + " /zoomhook?" + queryParams + " " + request.body());
	}
}
