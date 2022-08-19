package online.kancl.server;

import spark.Request;
import spark.Response;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHandler
{
	public void handleException(Exception exception, Request request, Response response)
	{
		response.status(500);

		response.body(renderException(exception));
	}

	private String renderException(Exception exception)
	{
		var stringWriter = new StringWriter();
		var printWriter = new PrintWriter(stringWriter);

		exception.printStackTrace(printWriter);

		return "<h1>Unexpected error</h1>"
				+ "<pre>" + stringWriter + "</pre>";
	}
}
