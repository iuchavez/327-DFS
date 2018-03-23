import com.google.gson.*;

public class JsonParser {
    static public void main (final String[] args){
        Gson gson = new Gson();
        File_two songOne = new File_two("Fuck The Police", 10);
        File_two songTwo = new File_two("Raining Blood", 15);
        System.out.print(gson.toJson(songOne));
        System.out.print(gson.toJson(songTwo));


    }
}

class File_two{
    public String filename;
    public int filesize;
//    public Page pages[];
    public File_two(String mFilename, int mFileSize){
        filename = mFilename;
        filesize = mFileSize;
    }


}
