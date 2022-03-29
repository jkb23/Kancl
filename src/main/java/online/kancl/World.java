package online.kancl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class World
{
	private final Set<Point> setPoints = new HashSet<>();
	private final Pattern setPointMessagePattern = Pattern.compile("^set (-?[0-9]+) (-?[0-9]+) = ([01])$");
	private final WebSocketHandler webSocketHandler;

	public World(WebSocketHandler webSocketHandler)
	{
		this.webSocketHandler = webSocketHandler;
		webSocketHandler.setOnMessageHandler(this::onWebSocketMessage);
	}

	public String getAllPoints() {
		return setPoints.stream()
				.map(p -> "set " + p.x + " " + p.y + " = 1")
				.collect(Collectors.joining("\n"));
	}

	private Optional<String> onWebSocketMessage(String message) {
		Matcher setPointMatcher = setPointMessagePattern.matcher(message);
		if (setPointMatcher.matches())
		{
			updatePoint(setPointMatcher);
			webSocketHandler.sendToAll(message);
			return Optional.empty();
		}

		if (message.equals("get"))
		{
			return Optional.of(getAllPoints());
		}

		return Optional.empty();
	}

	private void updatePoint(Matcher setPointMatcher)
	{
		int x = Integer.parseInt(setPointMatcher.group(1));
		int y = Integer.parseInt(setPointMatcher.group(2));
		int value = Integer.parseInt(setPointMatcher.group(3));

		if (value == 1)
			setPoints.add(new Point(x, y));
		else
			setPoints.remove(new Point(x, y));
	}
}
