import java.util.List;

public interface PlayerMessage {
	public void logMessage(String message);
	public void showContainer(String name, List<client.GameThing> contents);
}
