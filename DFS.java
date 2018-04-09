import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.rmi.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.*;
import com.google.gson.stream.JsonToken;

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

        //Port that is being connected to.
        if (!ip.equals(null)) {
            System.out.print("Type in the port: ");
            int port = in.nextInt();
        }

        chord.joinRing(ip, port);
        chord.Print();
    }

    //QUESTION: do we ignore this JsonParser in favor of the GSON?
    /*  public JSonParser readMetaData() throws Exception
    {
        JsonParser jsonParser _ null;
        long guid = md5("Metadata");
        ChordMessageInterface peer = chord.locateSuccessor(guid);
        InputStream metadataraw = peer.get(guid);
        // jsonParser = Json.createParser(metadataraw);
        return jsonParser;
    }*/

    //Will need to throw exception?
    public JsonReader readMetaData() {
        ChordMessageInterface peer = null;
        JsonReader jReader = null;
        InputStream mdRaw = null;
        long guid = md5("Metadata");

        try {
            peer = chord.locateSuccessor(guid);
        } catch (Exception e) {
            System.out.println("The successor could not be found");
            //e.printStackTrace();
        }

        try {
            mdRaw = peer.get(guid);
        } //Retrieve InputStream from Chord
        catch (IOException e) {
            System.out.println("readMetaData() failed at the get function");
            //e.printStackTrace();
        }

        try {
            jReader = new JsonReader(new InputStreamReader(mdRaw, "UTF-8"));
            //System.out.println(getStringFromInputStream(mdRaw));

        } catch (Exception e) {
            System.out.println("readMetaData() failed at creating a jReader");
            //e.printStackTrace();
        }

        // JsonParser parsedJson = new JsonParser();
        // parsedJson.parse(jReader);

        // System.out.println("Err Check @ 125 - readMetaData() before return");
        // System.out.println(jElement.toString());
        return jReader;
    }

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

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
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    //System.out.println("This happened in the getStringFromInputStream() method");
                    e.printStackTrace();
                }
            }
        }

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

    /*public void writeMetaData(InputStream stream) throws Exception
    {
       JsonParser jsonParser _ null;
       long guid = md5("Metadata");
       ChordMessageInterface peer = chord.locateSuccessor(guid);
       peer.put(guid, stream);
    }
    */

    /**
     * To write out the index info that has been read from a file
     * @param InputStream the file system info that is being written to the file system
     */
    public void writeMetaData(InputStream stream) {

        System.out.print(getStringFromInputStream(stream));

        long guid = md5("Metadata");
        ChordMessageInterface peer = null;

        try {
            peer = chord.locateSuccessor(guid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            peer.put(guid, stream);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("There was an error in the writeMetaData() method");
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
        // TODO:  Change the name in Metadata
        // Write Metadata
    }

    public String ls() throws Exception {
        String listOfFiles = "";
        JsonReader reader = readMetaData();
        ;
        //FileSystem fSys = getFileSystem(reader);
        getFileSystem(reader);

        //for all files in metadata
        //  print filename
        //  append to listOfFiles
        return listOfFiles;
    }

    public void touch(Scanner in) throws Exception {
        String filename = "";
        System.out.print("Type in the file name: ");
        if (in.hasNext()) {
            filename = in.next();
        }

        // TODO: Create the file fileName by adding a new entry to the Metadata
        //create file and pass file name as parameter
        //add file to metadata
    }

    public void delete(Scanner in) throws Exception {
        String filename = "";
        System.out.print("Type in the file name: ");
        if (in.hasNext()) {
            filename = in.next();
        }
        // TODO: remove all the pages in the entry fileName in the Metadata and then the entry
        // for each page in Metadata.filename
        //     peer = chord.locateSuccessor(page.guid);
        //     peer.delete(page.guid)
        // delete Metadata.filename
        // Write Metadata 
    }

    public Byte[] read(Scanner in) throws Exception {
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

        //search for file name in file system
        //if file found
        //  return first page AKA page[pageSize - 1]
        //else
        return null;
    }

    public Byte[] head(Scanner in) throws Exception {
        String filename = "";
        System.out.print("Type in the file name: ");
        if (in.hasNext()) {
            filename = in.next();
        }

        // search for file name in file system
        //if file found
        //  return first page AKA page[0]
        //else
        return null;
    }

    public void append(Scanner in) throws Exception {
        String filename = "";
        System.out.print("Type in the file name: ");
        if (in.hasNext()) {
            filename = in.next();
        }

        //add size
        Byte[] data = new Byte[10];

        // TODO: append data to fileName. If it is needed, add a new page.
        // Let guid be the last page in Metadata.filename
        // ChordMessageInterface peer = chord.locateSuccessor(guid);
        // peer.put(guid, data);
        //add to page[pageSize]
        // Write Metadata        
    }

}
