import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class JsonParser {
    static public void main (final String[] args){
        Gson gson = new Gson();
        int nId = 312;
        FileWriter fWriter = null;
        FileReader fReader = null;
        Page page[] = new Page[2];

        File songs[] = new File[2];
        songs[0] = new File("LOLSMH", 1, 10, 10, page);
        songs[1] = new File("Pheonix", 1, 15, 15, page);

        Metadata mData = new Metadata(songs);
        FileSystem uFS = new FileSystem(mData);
        FileSystem zFS = new FileSystem();

        // Examples of Writing to JSON
        try {
            fWriter = new FileWriter("327FS.json");
            fWriter.write(gson.toJson(uFS));
            System.out.println("OG\n" + gson.toJson(uFS));
        }
        catch (IOException e){ e.printStackTrace();}
        finally {
            if(fWriter != null){
                try{fWriter.close();}
                catch (IOException e){e.printStackTrace();}
            }
        }

        // Example of Reading from a JSON
        try{
            fReader = new FileReader("327FS.json");
            zFS = gson.fromJson(fReader, FileSystem.class);
            System.out.print("ZFS\n"+gson.toJson(zFS));
        }
        catch(FileNotFoundException e){ e.printStackTrace();}
        finally{
            if(fReader != null){
                try{fReader.close();}
                catch (IOException e){e.printStackTrace();}
            }
        }
    }
}