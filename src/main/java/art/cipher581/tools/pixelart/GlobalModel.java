package art.cipher581.tools.pixelart;


import art.cipher581.commons.gui.event.EventRegistry;
import art.cipher581.tools.pixelart.core.Project;
import art.cipher581.tools.pixelart.ui.GuiConfiguration;


/**
 *
 */
public class GlobalModel {

    private static GuiConfiguration guiConfiguration;

    private static Project project;

    private static EventRegistry eventRegistry;

    public static Project getProject() {
        return project;
    }

    public static void setProject(Project project) {
        GlobalModel.project = project;
    }

    public static GuiConfiguration getGuiConfiguration() {
        return guiConfiguration;
    }

    public static void setGuiConfiguration(GuiConfiguration guiConfiguration) {
        GlobalModel.guiConfiguration = guiConfiguration;
    }

    public static EventRegistry getEventRegistry() {
        return eventRegistry;
    }

    public static void setEventRegistry(EventRegistry eventRegistry) {
        GlobalModel.eventRegistry = eventRegistry;
    }

}
