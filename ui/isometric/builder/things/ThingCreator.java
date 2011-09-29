package ui.isometric.builder.things;

import game.GameThing;
import game.GameWorld;

import java.awt.image.BufferedImage;

public interface ThingCreator {
	public GameThing createThing(GameWorld w);
	public BufferedImage image();
	public String rendererName();
}