package online.kancl.db;

import online.kancl.test.ProductionDatabase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ProductionDatabase.class)
class TransactionJobRunnerTest {

    private final DatabaseRunner runnerOutsideTransaction;
    private final TransactionJobRunner transactionRunner;

    TransactionJobRunnerTest(DatabaseRunner runnerOutsideTransaction, ConnectionProvider connectionProvider) {
        this.runnerOutsideTransaction = runnerOutsideTransaction;
        this.transactionRunner = new TransactionJobRunner(connectionProvider);
    }

    @BeforeEach
    void beforeEach() {
        runnerOutsideTransaction.update("""
                CREATE TABLE TestTable
                (
                	v VARCHAR(50) NOT NULL
                )
                """
        );
    }

    @Test
    void transaction_isIsolated() {
        transactionRunner.runInTransaction((runnerInTransaction) -> {
            insertRow(runnerInTransaction);

            Assertions.assertThat(runnerOutsideTransaction.selectInt("SELECT count(1) FROM TestTable"))
                    .isZero();

            return null;
        });
    }

    @Test
    void noException_transactionIsCommitted() {
        transactionRunner.runInTransaction((runnerInTransaction) -> {
            insertRow(runnerInTransaction);
            return null;
        });

        Assertions.assertThat(runnerOutsideTransaction.selectInt("SELECT count(1) FROM TestTable"))
                .isEqualTo(1);
    }

    @Test
    void exceptionThrown_transactionIsRolledBack() {
        try {
            transactionRunner.runInTransaction((runnerInTransaction) -> {
                insertRow(runnerInTransaction);
                throw new ExpectedException();
            });
        } catch (Exception expected) {
        }

        Assertions.assertThat(runnerOutsideTransaction.selectInt("SELECT count(1) FROM TestTable"))
                .isZero();
    }

    private void insertRow(DatabaseRunner dbRunner) {
        dbRunner.insert("INSERT INTO TestTable (v) VALUES (?)", "value");
    }

    private static class ExpectedException extends RuntimeException {
    }
}
