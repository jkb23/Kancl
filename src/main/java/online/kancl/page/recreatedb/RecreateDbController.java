package online.kancl.page.recreatedb;

import online.kancl.db.SchemaCreator;
import online.kancl.server.Controller;
import spark.Request;
import spark.Response;

public class RecreateDbController extends Controller {

	private final SchemaCreator schemaCreator;

	public RecreateDbController(SchemaCreator schemaCreator) {
		this.schemaCreator = schemaCreator;
	}

	@Override
	public String get(Request request, Response response) {
		schemaCreator.recreateSchema();
		return "DB Recreated";
	}
}
