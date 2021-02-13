package art.cipher581.tools.af;


public interface IConfiguration {

	public String getValue(String key);
	
	public String getValue(String key, String defaultValue);

	public int getValueAsInt(String key, int defaultValue);
	
}
