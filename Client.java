import java.rmi.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;

public class Client {
    DFS dfs;

    public Client(int p) throws Exception {
        // Instantiate data member
        dfs = new DFS(p);

        // rename the input
        int port = p;

        // Initialize IO obejects to NULL
        FileWriter fWriter = null;
        FileReader fReader = null;
        FileStream jsonFileStream = null;

        // Instantiate a GSON object
        Gson gson = new Gson();

        // Populare FileSystem Object with dummy data
        Metadata dummyData = dummyData();

        Scanner in = new Scanner(System.in);
        int input = 0;

        while (input != 10) {

            System.out.println("------MAIN MENU------");
            System.out.println("-------1. JOIN-------");
            System.out.println("-------2. LIST-------");
            System.out.println("-------3. TOUCH------");
            System.out.println("-------4. DELETE-----");
            System.out.println("-------5. READ-------");
            System.out.println("-------6. TAIL-------");
            System.out.println("-------7. HEAD-------");
            System.out.println("-------8. APPEND-----");
            System.out.println("-------9. MOVE-------");
            System.out.println("------10. EXIT-------");
            System.out.print("Type in an option number: ");

            if (in.hasNextInt()) {
                input = in.nextInt();

                switch (input) {
                case 1:
                    dfs.join(in);
                    break;
                case 2:
                    dfs.ls();
                    break;
                case 3:
                    dfs.touch(in);
                    break;
                case 4:
                    dfs.delete(in);
                    break;
                case 5:
                    dfs.read(in);
                    break;
                case 6:
                    dfs.tail(in);
                    break;
                case 7:
                    dfs.head(in);
                    break;
                case 8:
                    dfs.append(in);
                    break;
                case 9:
                    dfs.mv(in);
                    break;
                default:
                    System.out.println("Invalid input.");
                    break;
                }
                System.out.println();
            }
        }

        System.out.println("Exit Client.");
    }

    public Metadata dummyData() {
        LinkedList<Page> page = new LinkedList<Page>(); //Blank Pages - Shows up as null
        page.add(new Page());
        
        //Creating Song Files to be stored within metadata - Arbitrary Values
        LinkedList<mFile> songs = new LinkedList<mFile>();
        songs.add(new mFile("LOLSMH", 1, 10, 10, page));
        songs.add(new mFile("Pheonix", 1, 15, 15, page));

        //Creating Metadata objects and file strucuture object
        Metadata mData = new Metadata(songs);
        //FileSystem fileSystem = new FileSystem(mData);
        return mData;
    }

    static public void main(String args[]) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("Parameter: <port>");
        }
        Client client = new Client(Integer.parseInt(args[0]));

    }
}