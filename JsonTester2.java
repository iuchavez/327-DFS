import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class JsonTester2 {
    static public void main (final String[] args){
        Gson gson = new Gson();
        // }
       // Example of Reading from a JSON
       try{
           FileReader fReader = new FileReader(args[0]); //Create a reader to view json file
           Metadata f = gson.fromJson(fReader, Metadata.class); // Retrieve FileSystem object from json file
           
          f.list();
           //System.out.print("ZFS\n"+gson.toJson(zFS)); //Display Json Contents
       }
       catch(FileNotFoundException e){ 
           e.printStackTrace();
     
       }
}
}