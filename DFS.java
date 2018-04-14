import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.GsonBuilder;

import java.rmi.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.*;
import com.google.gson.stream.JsonToken;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParseException;



/* JSON Format

 {
    "metadata" :
    {
        file :
        {
            name  : "File1"
            numberOfPages : "3"
            pageSize : "1024"
            size : "2291"
            page :
            {
                number : "1"
                guid   : "22412"
                size   : "1024"
            }
            page :
            {
                number : "2"
                guid   : "46312"
                size   : "1024"
            }
            page :
            {
                number : "3"
                guid   : "93719"
                size   : "243"
            }
        }
    }
}
 
 
 */

public class DFS {
    int port;
    Chord chord;

    private long md5(String objectName) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(objectName.getBytes());
            BigInteger bigInt = new BigInteger(1, m.digest());
            return Math.abs(bigInt.longValue());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public DFS(int port) throws Exception {

        this.port = port;
        long guid = md5("" + port);
        chord = new Chord(port, guid);
        Files.createDirectories(Paths.get(guid + "/repository"));
    }

    public void join(Scanner in) throws Exception {
        System.out.print("Type in the IP address: ");
        String ip = in.next();
        int port = 8080;

        //Port that is being connected to.
        if (!ip.equals(null)) {
            System.out.print("Type in the port: ");
            port = in.nextInt();
        }
        System.out.println(ip + " Port "  + port);
        chord.joinRing(ip, port);
        chord.Print();
    }

    public Metadata readMetaData()
    {
        ChordMessageInterface peer = null;
        InputStream mdRaw = null;
        long guid = md5("Metadata");
        Metadata m;


        try {
            peer = chord.locateSuccessor(guid);
            mdRaw = peer.get(guid);
            Gson gson = new Gson();

                String fileName = "327FS.json";
                FileOutputStream output = new FileOutputStream(fileName);
                while (mdRaw.available() > 0)
                    output.write(mdRaw.read());
                output.close();

                FileReader fReader = new FileReader(fileName); //Create a reader to view json file
                m = gson.fromJson(fReader, Metadata.class); // Retrieve FileSystem object from json file
        } catch (Exception e) {
            m = new Metadata(); //If metadata doesn't exist, Create one
            e.printStackTrace();
        }

        return m;
    }

    // convert InputStream to String
    public static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            //System.out.println("This happened in the getStringFromInputStream() method");
            e.printStackTrace();
        } 
        // finally {
        //     if (br != null) {
        //         try {
        //             br.close();
        //         } catch (IOException e) {
        //             //System.out.println("This happened in the getStringFromInputStream() method");
        //             e.printStackTrace();
        //         }
        //     }
        // }

        return sb.toString();

    }

    public void getFileSystem(JsonReader jReader) {
        // String json = "{\"metadata\":{\"file\":[{\"name\":\"LOLSMH\",\"numberOfPages\":1,\"pageSize\":10,\"size\":10,\"page\":[null,null]},{\"name\":\"Pheonix\",\"numberOfPages\":1,\"pageSize\":15,\"size\":15,\"page\":[null,null]}]}}";
        // jReader = new JsonReader(new StringReader(json));
        jReader.setLenient(true);

        // try {

            //System.out.println(jReader.peek());
            // String aString = jReader.nextString();
            // System.out.println(aString);
            // System.out.println(jReader.peek());
            // jReader.beginObject();
            // aName = jReader.nextName();
            // System.out.println(aName);
            // System.out.println(jReader.peek());
            // jReader.beginArray();
            // System.out.println(jReader.peek());

            // System.out.print();
            // while(jsonReader.hasNext()){
            //     JsonToken nextToken = jsonReader.peek();
            //     System.out.println(nextToken);

            //     if(JsonToken.BEGIN_ARRAY.equals(nextToken)){
            //         jsonReader.beginArray();
            //     }
            //     if(JsonToken.BEGIN_OBJECT.equals(nextToken)){

            //         jsonReader.beginObject();

            //     } else if(JsonToken.NAME.equals(nextToken)){

            //         String name  =  jsonReader.nextName();
            //         System.out.println(name);

            //     } else if(JsonToken.STRING.equals(nextToken)){

            //         String value =  jsonReader.nextString();
            //         System.out.println(value);

            //     } else if(JsonToken.NUMBER.equals(nextToken)){

            //         long value =  jsonReader.nextLong();
            //         System.out.println(value);

            //     }
            // }

        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        // FileSystem fSys = null;
        // Gson gson = new Gson();

        // try{
        //     fSys = gson.fromJson(jReader, FileSystem.class); // Retrieve FileSystem object from json file
        // }
        // catch(Exception e){ e.printStackTrace();}

        // return fSys;
    }

    public void writeMetaData(Metadata metadata) {
        Gson gson = new GsonBuilder().create();
        FileWriter  writer = null;
        long guid = md5("Metadata");
        InputStream mdRaw = null;
        Metadata m = new Metadata();
        ChordMessageInterface peer = null;

        try {
            writer = new FileWriter("327FS.json");
            writer.write(gson.toJson(metadata));
            writer.close();
            peer = chord.locateSuccessor(guid);
            peer.put(guid, new FileStream("327FS.json"));
        }catch(Exception e){
            e.printStackTrace();
        } 
    }

    public void mv(Scanner in) throws Exception {
        String oldname = "";
        String newname = "";
        System.out.print("Type in the old name for the metadata: ");
        if (in.hasNext()) {
            oldname = in.next();
        }

        System.out.print("Type in the new name for the metadata: ");
        if (in.hasNext()) {
            newname = in.next();
        }

        Metadata md = readMetaData();
        LinkedList<mFile> files = md.getFile();
        for(mFile file: files){
            if(file.getName().equals(oldname)){
                file.setName(newname);
            }
        }
        
        writeMetaData(md);
    }

    public String ls() throws Exception {
        String listOfFiles = "";
        StringBuilder loFiles = new StringBuilder();
        Metadata md = readMetaData();
        LinkedList<mFile> files = md.getFile();

        for(int i = 0; i<files.size(); i++){
            loFiles.append(files.get(i).getName());
            loFiles.append("\n");    
        }

        return loFiles.toString();
    }

    public void touch() throws Exception {
        Scanner in = new Scanner(System.in);
        Metadata md = readMetaData();
        

        String filename = "";
        System.out.print("Type in the file name: ");
        if (in.hasNext()) {
            filename = in.next();
        }

        LinkedList<mFile> files = md.getFile();
        for(int i = 0; i<files.size(); i++){
            System.out.println(files.get(i).toString());
            System.out.println("Is this working?");
        }
        mFile aFile = new mFile();
        aFile.setName(filename);
        System.out.println(aFile.toString());
        files.add(aFile);
        md.setFile(files);

        writeMetaData(md);
    }

    public void delete(Scanner in) throws Exception {
        String filename = "";
        System.out.print("Type in the file name: ");
        if (in.hasNext()) {
            filename = in.next();
        }
        
        ChordMessageInterface peer = null;
        Metadata md = readMetaData();
        LinkedList<mFile> f = md.getFile();

        for(mFile m : f) {
            if(m.getName().equals(filename)){
                LinkedList<Page> pages = m.getPage();
                for(Page p : pages){
                    peer = chord.locateSuccessor(p.getGuid());
                    peer.delete(p.getGuid());
                }
                md.removeFile(m);
            }
        }

        writeMetaData(md);
    }

    public Page read(Scanner in) throws Exception {
        Metadata md = readMetaData();
        String filename = "";
        int pageNumber = 0;
        System.out.print("Type in the file name: ");
        if (in.hasNext()) {
            filename = in.next();
        }
        System.out.print("Type in the page number: ");
        if (in.hasNextInt()) {
            pageNumber = in.nextInt();
        }

        LinkedList<mFile> files = md.getFile();
        for(int i = 0; i<files.size(); i++){
            mFile file = files.get(i);
            if(file.getName() == filename){
                if(pageNumber>file.getNumberOfPages())
                    return null;
                else{
                    LinkedList<Page> pgs = file.getPage();
                    pgs.get(pageNumber);
                }
            }
            else if(i == (files.size()-1) && files.get(i).getName() != filename){
                return null;
            }
        }

        // TODO: read pageNumber from fileName
        //search for file name in file system
        //if file found
        //  return page[pageNumber]
        //else
        return null;
    }

    public Byte[] tail(Scanner in) throws Exception {
        String filename = "";
        System.out.print("Type in the file name: ");
        if (in.hasNext()) {
            filename = in.next();
        }

        Metadata md = readMetaData();
        LinkedList<mFile> files = md.getFile();
        Page p = null;

        for(mFile file: files){
            if(file.getName().equals(filename) && file != null){
                p = file.getPage().getLast();
                break;
            }
        }

        return null;
    }

    public Byte[] head(Scanner in) throws Exception {
        String filename = "";
        System.out.print("Type in the file name: ");
        if (in.hasNext()) {
            filename = in.next();
        }

        Metadata md = readMetaData();
        LinkedList<mFile> files = md.getFile();
        Page p = null;

        for(mFile file: files){
            if(file.getName().equals(filename) && file != null){
                p = file.getPage().getFirst();
                break;
            }
        }

        return null;
    }

    public void append(Scanner in) throws Exception {
        String filepath = "";
        String filename = "";
        Metadata md = readMetaData();
        LinkedList<mFile> files = md.getFile();
        mFile file = new mFile();
        Page pg = new Page();

        System.out.print("Enter File to append to: ");
        if (in.hasNext()) {
            filename = in.next();
        }
        System.out.print("Enter filepath for appending data (default: 327FS.json): ");
        if (in.hasNext()) {
            filepath = in.next();
        }

        FileStream fStream = new FileStream(filepath);
        long guid = md5(filepath);

        //Grab page from metadata and append a page to the last file
        for(mFile f: files){
            if(f.getName().equals(filename)){
                pg.setGuid(guid);
                pg.setSize(fStream.getSize());
                pg.setNumber(f.getPage().size());
                f.addPage(pg);

                f.setNumberOfPages(f.getNumberOfPages() + 1);
                f.setPageSize(file.getPageSize() + 1);
                f.setSize(file.getSize()+pg.getSize());

                md.setFile(files);
                break;
            }
        }

        //Add Files to DFS
        writeMetaData(md);
        chord.put(guid, fStream);     
    }

}
