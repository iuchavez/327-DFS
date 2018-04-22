
public class Mapper implements MapReduceInterface {

    /**
     * //TODO
     * Code from spec.
     * For each word in value
     * emit(md5(word), word +":"+1);
     */
    public void map(Long key, String value, ChordMessageInterface context) throws IOException{

    }

    /**
     * //TODO
     * From spec.
     * word = values[0].split(":")[0]
     * emit(key, word +":"+ len(values));
     */
	public void reduce(Long key, List< String > value, ChordMessageInterface context){

    }

}