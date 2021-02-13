package art.cipher581.tools.pixelart.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import art.cipher581.commons.util.FileUtilities;
import art.cipher581.commons.util.Safely;


public class ProjectSaver {

    public void save(Project project) throws IOException {
        System.out.println("Saving project");

        File projectFile = project.getProjectFile();

        if (projectFile == null) {
            throw new IllegalStateException("projectFile is null");
        }
        
        if (projectFile.exists()) {
            // Create a backup
            File backupFile = new File(FileUtilities.getFileNameWithoutExtension(projectFile) + ".bak");
            
            backupFile = FileUtilities.getNumberedFile(backupFile);
            
            System.out.println("creating copy of the project file: " + backupFile);
            
            FileUtils.copyFile(projectFile, backupFile);
        }

        Properties properties = new Properties();

        properties.setProperty("imageFile", project.getImageFile().getPath());

        int i = 1;
        for (Pixel pixel : project.getDone()) {
            String pixelStr = Pixel.toProperty(pixel, true);

            properties.setProperty("done" + i, pixelStr);
            i++;
        }

        Pixel current = project.getCurrent();
        if (current != null) {
            properties.setProperty("current", Pixel.toProperty(current, true));
        }

        Pixel last = project.getLast();
        if (last != null) {
            properties.setProperty("last", Pixel.toProperty(last, true));
        }

        properties.setProperty("colorDistanceProvider", project.getColorDistanceProvider().getClass().getName());

        FileOutputStream fos = new FileOutputStream(projectFile, false);

         Timestamp now = new Timestamp(System.currentTimeMillis());
         
        properties.setProperty("lastSaved", String.valueOf(now.getTime())); 
         
        try {
            String comments = "Save time: " + now.toString();
            properties.store(fos, comments);
        } finally {
            Safely.close(fos);
        }
        
        project.setChanged(false);

        System.out.println("Project has been saved");
    }

}
