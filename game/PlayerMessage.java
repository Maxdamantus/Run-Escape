package game;
import java.util.List;

public interface PlayerMessage {
	public void logMessage(String message);
	public void showContainer(String name, List<GameThing> contents); //remove if it's not going to work
}
