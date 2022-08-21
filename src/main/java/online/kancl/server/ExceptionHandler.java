package online.kancl.server;

import spark.Request;
import spark.Response;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHandler {

	private static final String RESET_COLOR = "\u001B[0m";
	private static final String RED = "\u001B[31m";

	public void handleException(Exception exception, Request request, Response response) {
		logExceptionInRequest(exception, request);

		response.status(500);
		response.body(renderException(exception));
	}

	private void logExceptionInRequest(Exception exception, Request request) {
		System.out.println(
				RED + "Exception when handling " + request.requestMethod() + " to " + request.url() + RESET_COLOR + "\n"
				+ getExceptionText(exception) + "\n"
		);
	}

	private String renderException(Exception exception) {
		return "<h1>Unexpected error</h1>"
				+ "<pre>" + getExceptionText(exception) + "</pre>";
	}

	private String getExceptionText(Exception exception) {
		var stringWriter = new StringWriter();
		var printWriter = new PrintWriter(stringWriter);

		exception.printStackTrace(printWriter);
		return stringWriter.toString();
	}
}
