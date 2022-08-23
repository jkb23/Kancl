package online.kancl.test;

import online.kancl.Main;
import online.kancl.db.ConnectionProvider;
import online.kancl.db.DatabaseRunner;
import online.kancl.db.SchemaCreator;
import org.junit.jupiter.api.extension.*;

import java.sql.Connection;

/**
 * Provides access to empty DB created from production schema.
 * With this extension, you can use {@link DatabaseRunner} as a parameter.
 */
public class ProductionDatabase implements
		BeforeAllCallback,
		BeforeEachCallback,
		AfterAllCallback,
		ParameterResolver {
	private Connection connection;
	private DatabaseRunner dbRunner;

	@Override
	public void beforeAll(ExtensionContext extensionContext) {
		ConnectionProvider connectionProvider = ConnectionProvider.forInMemoryDatabase("testdb");
		connection = connectionProvider.getConnection();
		dbRunner = new DatabaseRunner(connection);
	}

	@Override
	public void beforeEach(ExtensionContext context) {
		new SchemaCreator().recreateSchema(connection, Main.SQL_SCRATCH_DIRECTORY);
	}

	@Override
	public void afterAll(ExtensionContext extensionContext) throws Exception {
		connection.close();
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		Class<?> parameterType = parameterContext.getParameter().getType();
		return parameterType.equals(DatabaseRunner.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return dbRunner;
	}
}
