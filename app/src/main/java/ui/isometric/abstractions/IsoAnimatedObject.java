package ui.isometric.abstractions;

import game.GameThing;

import java.awt.image.BufferedImage;

import ui.isometric.libraries.IsoRendererLibrary.RendererImage;

/**
 * An animated IsoObject
 * 
 * @author melby
 *
 */
public class IsoAnimatedObject extends IsoObject {
	private int animationStep;
	
	/**
	 * Create a standard IsoObject
	 * @param thing
	 * @param rendererImage
	 * @param square
	 * @param yoff
	 */
	public IsoAnimatedObject(GameThing thing, RendererImage rendererImage, IsoSquare square, int yoff) {
		super(thing, rendererImage, square, yoff);
		animationStep = 0;
	}

	/**
	 * Create an IsoAnimatedObject with a given frame of the animation
	 * @param thing
	 * @param rendererImage
	 * @param square
	 * @param yoff
	 * @param animationStep
	 */
	public IsoAnimatedObject(GameThing thing, RendererImage rendererImage, IsoSquare square, int yoff, int animationStep) {
		super(thing, rendererImage, square, yoff);
		this.animationStep = animationStep;
	}
	
	@Override
	public BufferedImage image() {
		return rawImage().image(animationStep);
	}
}
