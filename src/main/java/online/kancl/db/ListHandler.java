package online.kancl.db;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListHandler<T> implements ResultSetHandler<List<T>>
{
	private final ResultSetHandler<Optional<T>> rowHandler;

	public static <T> ListHandler<T> of(ResultSetHandler<Optional<T>> rowHandler)
	{
		return new ListHandler<>(rowHandler);
	}

	private ListHandler(ResultSetHandler<Optional<T>> rowHandler)
	{
		this.rowHandler = rowHandler;
	}

	@Override
	public List<T> handle(ResultSet rs) throws SQLException
	{
		List<T> items = new ArrayList<>();

		while (true)
		{
			Optional<T> optionalItem = rowHandler.handle(rs);

			if (optionalItem.isPresent())
			{
				items.add(optionalItem.get());
			}
			else
			{
				return items;
			}
		}
	}
}
