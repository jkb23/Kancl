package online.kancl.db;

import online.kancl.db.DatabaseRunner.NoRowSelectedException;
import online.kancl.test.ProductionDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(ProductionDatabase.class)
class DatabaseRunnerTest {

	private final DatabaseRunner dbRunner;

	DatabaseRunnerTest(DatabaseRunner dbRunner) {
		this.dbRunner = dbRunner;
	}

	@BeforeEach
	public void createCustomTable() {
		dbRunner.update("""
				CREATE TABLE TestTable
				(
					id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
					v VARCHAR(50) NOT NULL
				)
				"""
		);
	}

	@Test
	void singleRowQuery() {
		assertThat(dbRunner.select("SELECT count(1) FROM TestTable", (r) -> r.getInt(1)))
				.contains(0);
	}

	@Test
	void selectInt() {
		assertThat(dbRunner.selectInt("SELECT 123 FROM Dual"))
				.isEqualTo(123);
	}

	@Test
	void selectIntThrowsOnNoData() {
		assertThatExceptionOfType(NoRowSelectedException.class)
				.isThrownBy(() -> dbRunner.selectInt("SELECT id FROM TestTable"));
	}

	@Test
	void selectString() {
		assertThat(dbRunner.selectString("SELECT 'value' FROM Dual"))
				.isEqualTo("value");
	}

	@Test
	void selectStringThrowsOnNoData() {
		assertThatExceptionOfType(NoRowSelectedException.class)
				.isThrownBy(() -> dbRunner.selectString("SELECT v FROM TestTable"));
	}

	@Test
	void noRowsQuery() {
		assertThat(dbRunner.select("SELECT * FROM TestTable", (r) -> r.getInt("id")))
				.isEmpty();
	}

	@Test
	void emptyList() {
		assertThat(dbRunner.selectAll("SELECT * FROM TestTable", (r) -> r.getInt("id")))
				.isEmpty();
	}

	@Test
	void firstInsertedId() {
		assertThat(insertRowAndGetId("first"))
				.contains(1);
	}

	@Test
	void secondInsertedId() {
		insertRow("first");

		assertThat(insertRowAndGetId("second"))
				.contains(2);
	}

	@Test
	void queryAllReturnsAllRowsInOrder() {
		insertRow("first");
		insertRow("second");

		assertThat(dbRunner.selectAll("SELECT v FROM TestTable ORDER BY id DESC", (r) -> r.getString(1)))
				.containsExactly("second", "first");
	}

	@Test
	void updateReturnsZeroIfNoRowsAreUpdated() {
		assertThat(dbRunner.update("UPDATE TestTable SET v = ?", "updated"))
				.isEqualTo(0);
	}

	@Test
	void update() {
		insertRow("original");
		assertThat(dbRunner.update("UPDATE TestTable SET v = ?", "updated"))
				.isEqualTo(1);

		assertThat(dbRunner.select("SELECT v FROM TestTable", (r) -> r.getString(1)))
				.contains("updated");
	}

	@Test
	void queryReturnsJustFirstRow() {
		insertRow("first");
		insertRow("second");

		assertThat(dbRunner.select("SELECT v FROM TestTable ORDER BY id", (r) -> r.getString(1)))
				.contains("first");
	}

	private void insertRow(String value) {
		dbRunner.insert("INSERT INTO TestTable (v) VALUES (?)", value);
	}

	private Optional<Integer> insertRowAndGetId(String value) {
		return dbRunner.insert("INSERT INTO TestTable (v) VALUES (?)", (r) -> r.getInt(1), value);
	}
}
