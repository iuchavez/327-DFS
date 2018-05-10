import java.io.*;
import java.util.LinkedList;

/**
 * Currently the map and reduce methods are intended to count the words in a file
 */
public class Mapper implements MapReduceInterface, Serializable {

    /**
     * //TODO
     * Code from spec.
     * For each word in value
     * emit(md5(word), word +":"+1);
     */
    //TODO: CONTEXT
    public void map(Long key, String value, ChordMessageInterface context) throws IOException {
    	context.emitMap(key, value);
    }

    /**
     * //TODO
     * From spec.
     * word = values[0].split(":")[0]
     * emit(key, word +":"+ len(values));
     */
	public void reduce(Long key, LinkedList< String > value, ChordMessageInterface context) throws IOException {
		String word = value.get(0);
        context.emitReduce(key, word + ":" + value.size());
    }

}