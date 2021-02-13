package art.cipher581.tools.pixelart.core;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;
import java.util.Set;

import art.cipher581.commons.gui.util.IColorDistanceProvider;
import art.cipher581.commons.util.Safely;

/**
 *
 */
public class ProjectFileLoader {

    public Project load(File file) throws ProjectLoadingException {
        Properties properties = loadProperties(file);

        String imageFileStr = properties.getProperty("imageFile");

        if (imageFileStr == null || imageFileStr.trim().equals("")) {
            throw new ProjectLoadingException("image file is not set in project file " + file);
        }

        File imageFile = new File(imageFileStr);

        Project project = new Project(file, imageFile);

        try {
            project.loadImage();
        } catch (Exception ex) {
            throw new ProjectLoadingException("Error while loading image " + imageFile, ex);
        }

        Set<Object> keys = properties.keySet();
        for (Object keyObj : keys) {
            String key = keyObj.toString();
            String property = properties.getProperty(key);

            if (key.matches("(?i)^done[0-9]*$")) {
                Pixel pixel = Pixel.parseProperty(property);
                project.addDone(pixel);
            }
        }

        String lastStr = properties.getProperty("last");
        if (lastStr != null && !lastStr.trim().equals("")) {
            Pixel last = Pixel.parseProperty(lastStr);
            project.setLast(last);
        }

        String currentStr = properties.getProperty("current");
        if (currentStr != null && !currentStr.trim().equals("")) {
            Pixel current = Pixel.parseProperty(currentStr);
            project.setCurrent(current);
        }

        String colorDistanceProviderClassName = properties.getProperty("colorDistanceProvider");

        if (colorDistanceProviderClassName != null && !colorDistanceProviderClassName.trim().equals("")) {

            try {
                Class<?> clazz = Class.forName(colorDistanceProviderClassName);
                Constructor<?> c = clazz.getDeclaredConstructor();

                project.setColorDistanceProvider((IColorDistanceProvider) c.newInstance());
            } catch (Exception ex) {
                String message = "color distance provider can not be loaded: " + colorDistanceProviderClassName;
                throw new ProjectLoadingException(message, ex);
            }
        }
        
        String lastSavedStr = properties.getProperty("lastSaved");
        
        if (lastSavedStr != null) {
            long lastSaved = Long.valueOf(lastSavedStr);
            
            if (lastSaved > 0) {
                project.setLastSaved(lastSaved);
            }
        }
        
        if (project.getCurrent() == null) {
            Pixel pixel = project.getFirstUndone();
            
            if (pixel != null) {
                project.setCurrent(pixel);
            }
        }
        
        project.setChanged(false);

        return project;
    }

    private Properties loadProperties(File file) throws ProjectLoadingException {
        if (file == null) {
            throw new IllegalArgumentException("file is null");
        }

        if (!file.isFile()) {
            File canonical;
            try {
                canonical = file.getCanonicalFile();
            } catch (@SuppressWarnings("unused") Exception ex) {
                canonical = file;
            }
            throw new ProjectLoadingException("project file " + canonical + " does not exist");
        }

        try {
            Properties properties = new Properties();

            FileInputStream fis = new FileInputStream(file);

            try {
                properties.load(fis);
            } finally {
                Safely.close(fis);
            }

            return properties;
        } catch (Exception ex) {
            throw new ProjectLoadingException("error while loading project file " + file, ex);
        }
    }

}
