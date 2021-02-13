package art.cipher581.tools.pixelart;

import java.io.File;
import art.cipher581.commons.gui.event.EventRegistry;
import art.cipher581.commons.gui.event.GlobalEventRegistry;
import art.cipher581.tools.pixelart.core.Project;
import art.cipher581.tools.pixelart.core.ProjectFileLoader;
import art.cipher581.tools.pixelart.ui.GuiConfiguration;
import art.cipher581.tools.pixelart.ui.MainFrame;
import art.cipher581.tools.pixelart.ui.event.DoneEventListener;
import art.cipher581.tools.pixelart.ui.event.SaveEventListener;
import art.cipher581.tools.pixelart.util.FileUtilities;


public class PixelArt {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                throw new IllegalArgumentException("no parameters set");
            }
            
            File projectFile = new File(args[0]);
            
            System.out.println("Loading project");
            ProjectFileLoader projectFileLoader = new ProjectFileLoader();
            Project project = projectFileLoader.load(projectFile);
            
            GlobalModel.setProject(project);
            
            System.out.println("Project has been loaded");
            
          File workingDir = FileUtilities.getWorkingDir();
         
            File guiConfigurationFile = new File(workingDir, "gui.config");
            GuiConfiguration guiConfiguration = new GuiConfiguration(guiConfigurationFile);
            guiConfiguration.load();
            GlobalModel.setGuiConfiguration(guiConfiguration);
            
            EventRegistry eventRegistry = GlobalEventRegistry.getInstance();
            eventRegistry.addListener(new SaveEventListener(), "Save");
            eventRegistry.addListener(new DoneEventListener(), "Done");
            GlobalModel.setEventRegistry(eventRegistry);
            
           MainFrame mainFrame = new MainFrame(project);
           
           java.awt.EventQueue.invokeLater(() -> {
                mainFrame.setVisible(true);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
    
            System.exit(8);
        }
    }
    
}
