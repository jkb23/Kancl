package online.kancl.db;

import online.kancl.db.DatabaseRunner.NoRowSelectedException;
import online.kancl.test.ProductionDatabase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ProductionDatabase.class)
class DatabaseRunnerTest {
	private final DatabaseRunner dbRunner;

	DatabaseRunnerTest(DatabaseRunner dbRunner) {
		this.dbRunner = dbRunner;
	}

	@BeforeEach
	public void createCustomTable() {
		dbRunner.update(
				"""
						CREATE TABLE TestTable
						(
							id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
							v VARCHAR(50) NOT NULL
						)
						"""
		);
	}

	@Test
	void testSingleRowQuery() {
		assertThat(dbRunner.select("SELECT count(1) FROM TestTable", (r) -> r.getInt(1)))
				.contains(0);
	}

	@Test
	void testSelectInt() {
		assertThat(dbRunner.selectInt("SELECT 1 FROM Dual"))
				.isEqualTo(1);
	}

	@Test
	void testSelectIntThrowsOnNoData() {
		Assertions.assertThatThrownBy(() -> dbRunner.selectInt("SELECT 1 FROM TestTable"))
				.isInstanceOf(NoRowSelectedException.class);
	}

	@Test
	void testSelectString() {
		assertThat(dbRunner.selectString("SELECT 'value' FROM Dual"))
				.isEqualTo("value");
	}

	@Test
	void testSelectStringThrowsOnNoData() {
		Assertions.assertThatThrownBy(() -> dbRunner.selectInt("SELECT v FROM TestTable"))
				.isInstanceOf(NoRowSelectedException.class);
	}

	@Test
	void testNoRowsQuery() {
		assertThat(dbRunner.select("SELECT 1 FROM TestTable", (r) -> r.getInt(1)))
				.isEmpty();
	}

	@Test
	void testEmptyList() {
		assertThat(dbRunner.selectAll("SELECT 1 FROM TestTable", (r) -> r.getInt(1)))
				.isEmpty();
	}

	@Test
	void testInsert() {
		assertThat(insertRowAndGetId("first"))
				.contains(1);
	}

	@Test
	void testInsertReturnsGeneratedId() {
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
	void testUpdateReturnsZeroIfNoRowsAreUpdated() {
		assertThat(dbRunner.update("UPDATE TestTable SET v = ?", "updated"))
				.isEqualTo(0);
	}

	@Test
	void testUpdate() {
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

		assertThat(dbRunner.select("SELECT id FROM TestTable ORDER BY id", (r) -> r.getInt(1)))
				.contains(1);
	}

	private void insertRow(String value) {
		dbRunner.insert("INSERT INTO TestTable (v) VALUES (?)", value);
	}

	private Optional<Integer> insertRowAndGetId(String value) {
		return dbRunner.insert("INSERT INTO TestTable (v) VALUES (?)", (r) -> r.getInt(1), value);
	}
}