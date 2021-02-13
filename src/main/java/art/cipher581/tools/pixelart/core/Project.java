package art.cipher581.tools.pixelart.core;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import art.cipher581.commons.gui.util.ColorDistanceProviderEuclideanApproximated;
import art.cipher581.commons.gui.util.IColorDistanceProvider;
import art.cipher581.commons.util.img.ImageUtils;

public class Project {

    private final File projectFile;

    private final File imageFile;

    private BufferedImage image;

    private List<Pixel> done = new LinkedList<>();

    private Pixel current;

    private Pixel last;

    private IColorDistanceProvider colorDistanceProvider = new ColorDistanceProviderEuclideanApproximated();

    private Set<IProjectChangeObserver> projectChangeObservers = new HashSet<IProjectChangeObserver>();

    private long lastSaved = -1;
    
    private boolean changed = false;

    public Project(File projectFile, File imageFile) {
        super();

        this.projectFile = projectFile;
        this.imageFile = imageFile;
    }

    public synchronized void addChangeObserver(IProjectChangeObserver changeObserver) {
        this.projectChangeObservers.add(changeObserver);
    }

    public synchronized void removeChangeObserver(IProjectChangeObserver changeObserver) {
        this.projectChangeObservers.remove(changeObserver);
    }

    public synchronized File getProjectFile() {
        return projectFile;
    }

    public synchronized File getImageFile() {
        return imageFile;
    }

    public synchronized List<Pixel> getDone() {
        return done;
    }

    public synchronized void setColorDistanceProvider(IColorDistanceProvider colorDistanceProvider) {
        this.colorDistanceProvider = colorDistanceProvider;
        
        setChanged(true);
        notifyChange();
    }

    public synchronized IColorDistanceProvider getColorDistanceProvider() {
        return colorDistanceProvider;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
        
        notifyChange();
    }
    
    

    public synchronized List<Pixel> getPixels() {
        if (image == null) {
            throw new IllegalStateException("image has not been loaded");
        }

        List<Pixel> pixels = new LinkedList<>();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color c = new Color(image.getRGB(x, y));
                Pixel pixel = new Pixel(x, y, c);
                pixels.add(pixel);
            }
        }

        return pixels;
    }

    public synchronized List<Pixel> getUndone() {
        Set<Pixel> pixels = new HashSet<Pixel>(getPixels());

        pixels.removeAll(getDone());

        return new LinkedList<Pixel>(pixels);
    }

    public synchronized void setDone(List<Pixel> done) {
        this.done = done;
        setChanged(true);
        notifyChange();
    }

    public synchronized void addDone(Pixel pixel) {
        this.done.add(pixel);
       setChanged(true);
        notifyChange();
    }

    public synchronized BufferedImage getImage() {
        return image;
    }

    public synchronized BufferedImage loadImage() throws IOException {
        if (image == null) {
            image = ImageUtils.load(imageFile);
        }

        return image;
    }

    public synchronized Pixel getCurrent() {
        return current;
    }

    public synchronized void setCurrent(Pixel current) {
        this.current = current;

        setChanged(true);
        notifyChange();
    }

    public synchronized Pixel getLast() {
        return last;
    }

    public synchronized void setLast(Pixel last) {
        this.last = last;
        
        setChanged(true);
        notifyChange();
    }

    public synchronized long getLastSaved() {
        return lastSaved;
    }

    public synchronized void setLastSaved(long lastSaved) {
        this.lastSaved = lastSaved;
        
        setChanged(true);
        notifyChange();
    }

    public synchronized void currentDone() {
        if (current == null) {
            throw new IllegalStateException("current is null");
        }

        last = current;
        addDone(current);

        List<Pixel> undone = getUndone();

        current = Pixel.getNearestByColor(last, undone, colorDistanceProvider);

        setChanged(true);
        notifyChange();
    }

    public synchronized boolean isDone(Pixel pixel) {
        return getDone().contains(pixel);
    }

    public synchronized Pixel getPixel(int x, int y) {
        List<Pixel> pixels = getPixels();

        for (Pixel pixel : pixels) {
            if (pixel.getX() == x && pixel.getY() == y) {
                return pixel;
            }
        }

        return null;
    }

    private void notifyChange() {
        for (IProjectChangeObserver projectChangeObserver : projectChangeObservers) {
            projectChangeObserver.projectChanged();
        }
    }

    public synchronized Pixel getFirstUndone() {
        List<Pixel> pixels = getPixels();

        System.out.println("pixel count: " + pixels.size());

        for (Pixel pixel : pixels) {
            if (!isDone(pixel)) {
                return pixel;
            }
        }

        return null;
    }

}
