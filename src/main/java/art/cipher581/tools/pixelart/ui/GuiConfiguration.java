package art.cipher581.tools.pixelart.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Properties;
import art.cipher581.commons.util.Safely;


public class GuiConfiguration {

    private File file;

    private Properties properties = new Properties();
    
    private boolean changed = false;
    

    public GuiConfiguration(File file) {
        super();

        this.file = file;
    }

    public synchronized void load() throws IOException {
        properties.clear();

        FileInputStream fis = new FileInputStream(file);

        try {
            properties.load(fis);
        } finally {
            Safely.close(fis);
        }
    }

    public boolean isChanged() {
        return changed;
    }
 
    
    private void setChanged(boolean changed) {
        this.changed = changed;
    }
    
    public synchronized int getVerticalDividerLocation() {
        return getPropertyAsInt("vertical-divider-location", 50);
    }
    
    
    public synchronized void setVerticalDividerLocation(int value) {
        setProperty("vertical-divider-location", value);
    }
    
     public synchronized void setProperty(String key, String value) {
         properties.setProperty(key, value);
        
        setChanged(true);
    }
    

    public synchronized void setProperty(String key, int val) {
        String valStr = String.valueOf(val);
        
        setProperty(key, valStr);
    }
    
    public synchronized int getPropertyAsInt(String key, int defaultVal) {
        String str = properties.getProperty(key);
        
        if (str == null || str.trim().equals("")) {
            return defaultVal;
        }
        
        return Integer.parseInt(str);
    }
    
    public synchronized void save() throws  IOException {
        System.out.println("saving gui configuration");
        String comments = "Last saved: " + new Timestamp(System.currentTimeMillis()).toString();
        
        FileOutputStream fos = new FileOutputStream(file);
        
        try {
            properties.store(fos, comments);
        } finally {
            Safely.close(fos);
        }
        
        setChanged(false);
        
        System.out.println("gui configuration successfully saved");
    }

    public void setZoom(int zoom) {
        setProperty("zoom", zoom);
    }
    
    
    public int getZoom() {
        return getPropertyAsInt("zoom", 4);
    }
    
    
    public int getFrameWidth() {
        return getPropertyAsInt("frame-width", 1024);
    }

     public void setFrameWidth(int frameWidth) {
        setProperty("frame-width", frameWidth);
    }
     
     
     public int getFrameHeight() {
        return getPropertyAsInt("frame-height", 768);
    }

     public void setFrameHeight(int frameHeight) {
        setProperty("frame-height", frameHeight);
    }
}
