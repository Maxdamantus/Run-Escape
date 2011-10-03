package ui.isometric;

import java.awt.image.BufferedImage;

import ui.isometric.IsoRendererLibrary.RendererImage;

/**
 * An animated IsoImage
 * 
 * @author melby
 *
 */
public class IsoAnimatedImage extends IsoImage {
	private int animationStep;
	
	/**
	 * Create a standard IsoImage
	 * @param rendererImage
	 * @param square
	 * @param yoff
	 */
	public IsoAnimatedImage(RendererImage rendererImage, IsoSquare square, int yoff) {
		super(rendererImage, square, yoff);
		animationStep = 0;
	}

	/**
	 * Create an IsoAnimatedImage with a given frame of the animation
	 * @param rendererImage
	 * @param square
	 * @param yoff
	 * @param animationStep
	 */
	public IsoAnimatedImage(RendererImage rendererImage, IsoSquare square, int yoff, int animationStep) {
		super(rendererImage, square, yoff);
		this.animationStep = animationStep;
	}
	
	@Override
	public BufferedImage image() {
		return rawImage().image(animationStep);
	}
}
