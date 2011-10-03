package ui.isometric;

import java.awt.image.BufferedImage;

import ui.isometric.IsoRendererLibrary.RendererImage;

public class IsoAnimatedImage extends IsoImage {
	private int animationStep;
	
	public IsoAnimatedImage(RendererImage rendererImage, IsoSquare square, int yoff) {
		super(rendererImage, square, yoff);
		animationStep = 0;
	}

	public IsoAnimatedImage(RendererImage rendererImage, IsoSquare square, int yoff, int animationStep) {
		super(rendererImage, square, yoff);
		this.animationStep = animationStep;
	}
	
	@Override
	public BufferedImage image() {
		return rawImage().image(animationStep);
	}
}
