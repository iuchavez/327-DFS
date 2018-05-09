import java.io.IOException;
import java.util.*;

public interface MapReduceInterface {
	//mapper functions goes into MapReduceInterface we need to impement them in another file totally independent, testing classes
	public void map(Long key, String value, ChordMessageInterface context) throws IOException;
	public void reduce(Long key, LinkedList< String > value, ChordMessageInterface context) throws IOException;
}