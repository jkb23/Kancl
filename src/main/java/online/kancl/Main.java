package online.kancl;

import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.stream.Collectors;

public class Main
{
	public static void main(String[] args)
	{
		Spark.port(8080);

		Spark.staticFiles.location("/public");
		Spark.get("/zoomhook", Main::zoomHook);
		Spark.post("/zoomhook", Main::zoomHook);

		Spark.init();

		System.out.println("Server running");
	}

	private static Object zoomHook(Request request, Response response)
	{
		String queryParams = request.queryParams().stream().map(key -> key + "=" + String.join(",", request.queryParamsValues(key)))
				.collect(Collectors.joining("&"));

		System.out.println(request.requestMethod() + " /zoomhook?" + queryParams + " " + request.body());

		return "OK";
	}
}
