package stratstuff;

public interface Saveable {

	public String save();

	public Saveable load(String fromString);
}
