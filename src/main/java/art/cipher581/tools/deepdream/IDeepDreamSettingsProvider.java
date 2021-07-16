package art.cipher581.tools.deepdream;


@FunctionalInterface
public interface IDeepDreamSettingsProvider {
    
    public DeepDreamSettings getSettings(int frameNr);

}
