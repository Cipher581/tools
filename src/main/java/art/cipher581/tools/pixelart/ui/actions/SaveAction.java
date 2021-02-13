package art.cipher581.tools.pixelart.ui.actions;


import art.cipher581.commons.gui.util.INamedAction;
import art.cipher581.commons.gui.util.PerformActionException;
import art.cipher581.tools.pixelart.GlobalModel;
import art.cipher581.tools.pixelart.core.ProjectSaver;
import art.cipher581.tools.pixelart.ui.GuiConfiguration;


public class SaveAction implements INamedAction {
    
    public void performAction() throws PerformActionException {
        try {
            ProjectSaver projectSaver = new ProjectSaver();
            projectSaver.save(GlobalModel.getProject());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        try {
            GuiConfiguration guiConfiguration = GlobalModel.getGuiConfiguration();
            guiConfiguration.save();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "Save";
    }
    
}
