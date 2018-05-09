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
        dfs = new DFS(p);
        int port = p;

        FileWriter fWriter = null;
        FileReader fReader = null;
        FileStream jsonFileStream = null;

        Gson gson = new Gson();

        Scanner in = new Scanner(System.in);
        int input = 0;

        while (input != -1) {

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
            System.out.println("------10. MapReduce-------");
            System.out.println("-------0. EXIT-------");
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
                    dfs.touch();
                    break;
                case 4:
                    dfs.delete();
                    break;
                case 5:
                    InputStream iStream = dfs.read(in);
                    if(iStream == null){System.out.println("Page doesn't exist.");}
                    break;
                case 6:
                    dfs.tail(in);
                    break;
                case 7:
                    dfs.head(in);
                    break;
                case 8:
                    dfs.append();
                    break;
                case 9:
                    dfs.mv(in);
                    break;
                case 10:
                    dfs.runMapReduce();
                    break;
                case 0:
                    input = -1;
                    System.out.println("Exit Client.");
                    break;
                default:
                    System.out.println("Invalid input.");
                    break;
                }
                System.out.println();
            }
        }

    }

    static public void main(String args[]) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("Parameter: <port>");
        }
        Client client = new Client(Integer.parseInt(args[0]));
        System.exit(0);
    }
}