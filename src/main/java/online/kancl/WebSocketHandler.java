package online.kancl;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@WebSocket
public class WebSocketHandler
{
	private final Set<Session> sessions = new HashSet<>();

	private Function<String, Optional<String>> onMessage = null;

	@OnWebSocketConnect
	public void onConnect(Session session)
	{
		sessions.add(session);
	}

	@OnWebSocketClose
	public void onClose(Session session, int statusCode, String reason)
	{
		sessions.remove(session);
	}

	@OnWebSocketMessage
	public void onMessage(Session session, String message)
	{
		Optional<String> response = onMessage.apply(message);
		if (response.isPresent())
		{
			try
			{
				session.getRemote().sendString(response.get());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void sendToAll(String message) {
		sessions.stream()
				.filter(Session::isOpen)
				.map(Session::getRemote)
				.forEach(remoteEndpoint -> {
					try
					{
						remoteEndpoint.sendString(message);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				});
	}

	public void setOnMessageHandler(Function<String, Optional<String>> onMessage) {
		this.onMessage = onMessage;
	}
}
