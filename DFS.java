import com.google.gson.Gson;

import java.rmi.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.*;
//import com.google.gson.


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


public class DFS
{
    int port;
    Chord  chord;
    
    private long md5(String objectName)
    {
        try
        {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(objectName.getBytes());
            BigInteger bigInt = new BigInteger(1,m.digest());
            return Math.abs(bigInt.longValue());
        }
        catch(NoSuchAlgorithmException e)
        {
                e.printStackTrace(); 
        }
        return 0;
    }
    
    public DFS(int port) throws Exception
    {
        
        this.port = port;
        long guid = md5("" + port);
        chord = new Chord(port, guid);
        Files.createDirectories(Paths.get(guid+"/repository"));
    }
    
    public void join(Scanner in) throws Exception
    {
        System.out.print("Type in the IP address: ");
        String ip = in.next();
        
        //Port that is being connected to.
        if(!ip.equals(null)){
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
    public FileSystem readMetaData(){
        ChordMessageInterface peer = null;
        InputStreamReader isReader = null;
        FileSystem fSys = null;
        InputStream mdRaw = null;
        
        Gson gson = new Gson();
        long guid = md5("Metadata");
            try { peer = chord.locateSuccessor(guid);}
            catch(Exception e){ e.printStackTrace();}
        mdRaw = peer.get(guid); //Retrieve InputStream from Chord
        isReader = new Reader(mdRaw, "UTF-8"); //T
        
        try{fSys = gson.fromJson(mdRaw, FileSystem.class); // Retrieve FileSystem object from json file
        }
        catch(FileNotFoundException e){ e.printStackTrace();}
        finally{
            if(fReader != null){
                try{fReader.close();}
                catch (IOException e){e.printStackTrace();}
            }
        }
        return fSys;
    }

    /*public void writeMetaData(InputStream stream) throws Exception
   {
       JsonParser jsonParser _ null;
       long guid = md5("Metadata");
       ChordMessageInterface peer = chord.locateSuccessor(guid);
       peer.put(guid, stream);
   }
  */
    public void writeMetaData(FileSystem fSys, String fName){
        Gson gson = null;
        long guid = md5(fName);
        ChordMessageInterface peer = chord.locateSuccessor(guid);
        peer.put(guid, fSys);  
    }
    
    public void mv(Scanner in) throws Exception
    {
        String oldname = "";
        String newname = "";
        System.out.print("Type in the old name for the metadata: ");
        if(in.hasNext()) {
            oldname = in.next();
        }

        System.out.print("Type in the new name for the metadata: ");
        if(in.hasNext()) {
            newname = in.next();
        }
        // TODO:  Change the name in Metadata
        // Write Metadata
    }

    public String ls() throws Exception
    {
        String listOfFiles = "";
        // TODO: returns all the files in the Metadata
        FileSystem fSys = readMetaData();

        //for all files in metadata
        //  print filename
        //  append to listOfFiles
        return listOfFiles;
    }
 
    public void touch(Scanner in) throws Exception
    {
        String filename = "";
        System.out.print("Type in the file name: ");
        if(in.hasNext()){
            filename = in.next();
        }

        // TODO: Create the file fileName by adding a new entry to the Metadata
        //create file and pass file name as parameter
        //add file to metadata
    }

    public void delete(Scanner in) throws Exception
    {
        String filename = "";
        System.out.print("Type in the file name: ");
        if(in.hasNext()){
            filename = in.next();
        }
        // TODO: remove all the pages in the entry fileName in the Metadata and then the entry
        // for each page in Metadata.filename
        //     peer = chord.locateSuccessor(page.guid);
        //     peer.delete(page.guid)
        // delete Metadata.filename
        // Write Metadata 
    }
    
    public Byte[] read(Scanner in) throws Exception
    {
        String filename = "";
        int pageNumber = 0;
        System.out.print("Type in the file name: ");
        if(in.hasNext()){
            filename = in.next();
        }
        System.out.print("Type in the page number: ");
        if(in.hasNextInt()){
            pageNumber = in.nextInt();
        }
        
        // TODO: read pageNumber from fileName
        //search for file name in file system
        //if file found
        //  return page[pageNumber]
        //else
        return null;
    }
    
    public Byte[] tail(Scanner in) throws Exception
    {
        String filename = "";
        System.out.print("Type in the file name: ");
        if(in.hasNext()){
            filename = in.next();
        }
        
        //search for file name in file system
        //if file found
        //  return first page AKA page[pageSize - 1]
        //else
        return null;
    }

    public Byte[] head(Scanner in) throws Exception
    {
        String filename = "";
        System.out.print("Type in the file name: ");
        if(in.hasNext()){
            filename = in.next();
        }
        
        // search for file name in file system
        //if file found
        //  return first page AKA page[0]
        //else
        return null;
    }

    public void append(Scanner in) throws Exception
    {
        String filename = "";
        System.out.print("Type in the file name: ");
        if(in.hasNext()){
            filename = in.next();
        }
        
        //add size
        Byte[] data = new Byte[10];
        
        // TODO: append data to fileName. If it is needed, add a new page.
        // Let guid be the last page in Metadata.filename
        ChordMessageInterface peer = chord.locateSuccessor(guid);
        peer.put(guid, data);
        //add to page[pageSize]
        // Write Metadata        
    }
    
}
