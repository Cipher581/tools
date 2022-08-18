package art.cipher581.common.color;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import art.cipher581.commons.util.img.ImageUtils;

public class PixelatedImage {
	
	private Color[][] colors;

	public PixelatedImage(int width, int height) {
		super();

		this.colors = new Color[height][width];
	}
	
	public int getWidth() {
		return colors[0].length;
	}
	
	public int getHeight() {
		return colors.length;
	}
	
	public void setColor(int x, int y, Color c) {
		this.colors[y][x] = c;
	}
	
	public Color getColor(int x, int y) {
		return colors[y][x];
	}

	public Set<Color> getColors() {
		Set<Color> set = new HashSet<>();

		for (Color[] row : colors) {
			for (Color color : row) {
				set.add(color);
			}
		}
		
		return set;
	}
	
	public List<Position> getPositions(Color c) {
		List<Position> positions = new LinkedList<>();

		int y = 0;

		for (Color[] row : colors) {
			int x = 0;

			for (Color color : row) {
				if (color.equals(c)) {
					positions.add(new Position(x, y));
				}
				
				x++;
			}

			y++;
		}
		
		return positions;
	}
	
	
	public BufferedImage getImage() {
		int width = getWidth();
		int height = getHeight();
		
		BufferedImage img = ImageUtils.createImage(width, height, java.awt.Color.WHITE);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				img.setRGB(x, y, getColor(x, y).getAwtColor().getRGB());
			}
		}

		return img;
	}
	
	public String getPrintOutput() {
		StringBuilder sb = new StringBuilder();
		
		List<Color> colors = getColors().stream().sorted().collect(Collectors.toList());
		
		for (Color color : colors) {
			sb.append(color.getProduct() + " - " + color.getName() + " - " + color.getCode() + ":\t");
			
			List<Position> positions = getPositions(color).stream().sorted(Position::compareByX).collect(Collectors.toList());

			sb.append(positions.stream().map(Position::getPrintString).collect(Collectors.joining(", ")));
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
}
