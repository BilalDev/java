package plugin;

import helpers.ImagePanel;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Binary implements IPlugin, Runnable {
	private ImagePanel image, newImage;
	private String name;
	
	public Binary (ImagePanel image, String name) {
		this.image = image;
		this.name = name;
	}

	@Override
	public BufferedImage perform(BufferedImage img) {
		Greyscale grey = new Greyscale(image, "");
		grey.run();
		img = grey.getImgBI();
		
		int pixel;
		int r, a;
		
		for (int i = 0; i < img.getWidth(); ++i) {
			for (int j = 0; j < img.getHeight(); ++j) {
				r = new Color(img.getRGB(i, j)).getRed();
				a = new Color(img.getRGB(i, j)).getAlpha();
				
				if (r >= 125) {
					pixel = 255;
				}
				else
					pixel = 0;
				pixel = toRGB(a, pixel, pixel, pixel);
				img.setRGB(i, j, pixel);
			}
		}
		
		this.image.setImage(img);
		
		return img;
	}

	public int toRGB(int a, int r, int g, int b) {
		int pixel = 0;
		pixel += a;
		pixel = pixel << 8;
		pixel += r;
		pixel = pixel << 8;
		pixel += g;
		pixel = pixel << 8;
		pixel += b;
		
		return pixel;
	}
	
	public ImagePanel getNewImage () {
		return this.newImage;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void run() {
		this.newImage = new ImagePanel(this.perform(this.image.getImage()), "");
	}
}
