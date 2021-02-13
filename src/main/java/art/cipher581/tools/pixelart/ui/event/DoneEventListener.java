package art.cipher581.tools.pixelart.ui.event;

import art.cipher581.commons.gui.event.IEventListener;
import art.cipher581.commons.gui.event.UIEvent;
import art.cipher581.tools.pixelart.GlobalModel;
import art.cipher581.tools.pixelart.core.Project;


public class DoneEventListener implements IEventListener {

    @Override
    public void eventOccured(UIEvent event) {
        try {
            Project project = GlobalModel.getProject();
            
           project.currentDone();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
