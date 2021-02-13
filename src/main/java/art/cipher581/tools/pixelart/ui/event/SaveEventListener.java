package art.cipher581.tools.pixelart.ui.event;

import art.cipher581.commons.gui.event.IEventListener;
import art.cipher581.commons.gui.event.UIEvent;
import art.cipher581.tools.pixelart.ui.actions.SaveAction;


public class SaveEventListener implements IEventListener {

    @Override
    public void eventOccured(UIEvent event) {
        try {
            new SaveAction().performAction();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
