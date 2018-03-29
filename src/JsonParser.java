import com.google.gson.*;

import java.io.File;


public class JsonParser {
    static public void main (final String[] args){
        Gson gson = new Gson();
        File_two songs[] = new File_two[2];
        songs[0] = new File_two("Fuck The Police", 10);
        songs[1] = new File_two("Raining Blood", 15);
        Metadata mData = new Metadata(songs);
        FileSystem uFS = new FileSystem(mData);
        System.out.print(gson.toJson(uFS));
//        System.out.print(gson.toJson(songTwo));

    }
}

class FileSystem{
    Metadata metadata;

    FileSystem(){
        metadata = new Metadata();
    }

    FileSystem(Metadata md){
        metadata = md;
    }
}
class Metadata{
    File_two files[];

    Metadata(){
        files = new File_two[2];
    }

    Metadata(File_two tFiles[]){
        files = tFiles;
    }
}

class File_two{
    public String filename;
    public int filesize;

    File_two(){
        filename = "";
        filesize = 0;
    }

    File_two(String mFilename, int mFileSize){
        filename = mFilename;
        filesize = mFileSize;
    }

}
