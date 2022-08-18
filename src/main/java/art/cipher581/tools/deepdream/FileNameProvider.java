package art.cipher581.tools.deepdream;


import java.text.DecimalFormat;


public class FileNameProvider implements IFileNameProvider {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("000000");

    private String prefix = "frame-";
    
    public FileNameProvider withPrefix(String prefix) {
        this.prefix = prefix;
        
        return this;
    }

    @Override
    public String getFileName(int frameNr) {
        return prefix + DECIMAL_FORMAT.format(frameNr) + ".jpg";
    }

}
