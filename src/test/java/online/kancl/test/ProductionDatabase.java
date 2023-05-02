package online.kancl.test;

import online.kancl.Main;
import online.kancl.db.ConnectionProvider;
import online.kancl.db.DatabaseRunner;
import online.kancl.db.SchemaCreator;
import online.kancl.util.DirectoryHashCalculator;
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
    private ConnectionProvider connectionProvider;
    private DatabaseRunner dbRunner;

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        connectionProvider = ConnectionProvider.forInMemoryDatabase("testdb");
        connection = connectionProvider.getConnection();
        dbRunner = new DatabaseRunner(connection);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        DirectoryHashCalculator directoryHashCalculator = new DirectoryHashCalculator();
        new SchemaCreator(directoryHashCalculator, connectionProvider, Main.SQL_SCRATCH_DIRECTORY)
                .recreateSchema();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        connection.close();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = getParameterType(parameterContext);
        return parameterType.equals(DatabaseRunner.class) || parameterType.equals(ConnectionProvider.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = getParameterType(parameterContext);

        if (parameterType.equals(DatabaseRunner.class))
            return dbRunner;
        else if (parameterType.equals(ConnectionProvider.class))
            return connectionProvider;
        else
            throw new IllegalArgumentException("Unknown parameter type " + parameterType.getSimpleName());
    }

    private Class<?> getParameterType(ParameterContext parameterContext) {
        return parameterContext.getParameter().getType();
    }
}
