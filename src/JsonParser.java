import com.google.gson.*;

import java.io.FileWriter;
import java.io.IOException;


public class JsonParser {
    static public void main (final String[] args){
        Gson gson = new Gson();
        int nId = 312;
        FileWriter fOutput = null;
        Page page[] = new Page[2];
        File songs[] = new File[2];
        //String mFilename, int numPages, int pSize, int sz, Page pg[]
        songs[0] = new File("LOLSMH", 1, 10, 10, page);
        songs[1] = new File("Pheonix", 1, 15, 15, page);
        Metadata mData = new Metadata(songs);
        FileSystem uFS = new FileSystem(mData);
        String jsonExtrac = gson.toJson(uFS);
        try {
            fOutput = new FileWriter("327FS.json");
            fOutput.write(jsonExtrac);
        }
        catch (IOException e){ e.printStackTrace();}
        finally {
            if(fOutput != null){
                try{fOutput.close();}
                catch (IOException e){e.printStackTrace();}
            }
        }
//        System.out.print(gson.toJson(songTwo));
    }
    // static class FileSystem{
    //     Metadata metadata;

    //     FileSystem(){
    //         metadata = new Metadata();
    //     }

    //     FileSystem(Metadata md){
    //         metadata = md;
    //     }

    //     Metadata getMetadata(){
    //         return metadata;
    //     }
    //     void setMetadata(Metadata md){
    //         metadata = md;
    //     }
    // }

    // static class Metadata{
    //     File file[];

    //     Metadata(){
    //         file = new File[2];
    //     }

    //     Metadata(File tFiles[]){
    //         file = tFiles;
    //     }

    //     File[] getFile(){
    //         return file;
    //     }
    //     void setFile(File tFile[]){
    //         file = tFile;
    //     }
    // }

    // static class File {
    //     private String name;
    //     private int numberOfPages;
    //     private int pageSize;
    //     private int size;
    //     private Page page[];

    //     File() {
    //         name = "";
    //         numberOfPages = 0;
    //         pageSize = 0;
    //         size = 0;
    //         page = new Page[10];
    //     }

    //     File(String mFilename, int numPages, int pSize, int sz, Page pg[]) {
    //         name = mFilename;
    //         numberOfPages = numPages;
    //         pageSize = pSize;
    //         size = sz;
    //         page = pg;
    //     }
    // }

    // static class Page{
    //     int number;
    //     int guid;
    //     int size;

    //     Page(){
    //         number = 0;
    //         int guid = 0;
    //         int size = 0;
    //     }

    //     Page(int num, int gid, int sz){
    //         number = num;
    //         guid = gid;
    //         size = sz;
    //     }

    //     int getNumber(){
    //         return number;
    //     }
    //     int getGuid(){
    //         return guid;
    //     }
    //     int getSize(){
    //         return size;
    //     }

    //     void setNumber(int num){
    //         number = num;
    //     }
    //     void setGuid(int gid){
    //         guid = gid;
    //     }
    //     void setSize(int sz){
    //         size = sz;
    //     }
    // }

}
