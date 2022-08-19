package online.kancl.server;

import online.kancl.db.DatabaseRunner;
import online.kancl.db.TransactionJobRunner;
import spark.Request;
import spark.Response;

public class Controller {

	public String handleGet(Request request, Response response) {
		return TransactionJobRunner.runInTransactionAndRelease((dbRunner) -> get(request, response, dbRunner));
	}

	public String handlePost(Request request, Response response) {
		return TransactionJobRunner.runInTransactionAndRelease((dbRunner) -> post(request, response, dbRunner));
	}

	protected String get(Request request, Response response, DatabaseRunner dbRunner) {
		return "";
	}

	protected String post(Request request, Response response, DatabaseRunner dbRunner) {
		return "";
	}
}
