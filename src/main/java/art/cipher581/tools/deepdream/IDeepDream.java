package art.cipher581.tools.deepdream;


import java.io.File;


public interface IDeepDream {
    
    public void run(File file, File targetFile, DeepDreamSettings settings) throws DeepDreamException;

}
