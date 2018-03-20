import java.rmi.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.nio.file.*;


public class Client
{
    DFS dfs;
    public Client(int p) throws Exception {
        dfs = new DFS(p);
        
        Scanner in = new Scanner(System.in);
        String input = "";

        while(!input.equals("exit")){
            if(in.hasNextLine()){
                input = in.nextLine();
                String filename = "";
                String oldname = "";
                String newname = "";
                int page_num = 0;
                String ip = "";
                int port = 0;
                
                switch(input){
                    case "join":
                        System.out.print("Type in the IP address: ");
                        if(in.hasNextLine()) {
                            ip = in.nextLine();
                        }
                        
                        System.out.print("Type in the port: ");
                        if(in.hasNextInt()){
                            port = in.nextInt();
                        }
                        dfs.join(ip, port);
                        break;
                    case "ls":
                        dfs.ls();
                        break;
                    case "touch":
                        System.out.print("Type in the file name: ");
                        if(in.hasNextLine()){
                            filename = in.nextLine();
                        }
                        dfs.touch(filename);
                        break;
                    case "delete":
                        System.out.print("Type in the file name: ");
                        if(in.hasNextLine()){
                            filename = in.nextLine();
                        }
                        dfs.delete(filename);
                        break;
                    case "read":
                        System.out.print("Type in the file name: ");
                        if(in.hasNextLine()){
                            filename = in.nextLine();
                        }
                        System.out.print("Type in the page number: ");
                        if(in.hasNextInt()){
                            pagenum = in.nextInt();
                        }
                        dfs.read(filename, page_num);
                        break;
                    case "tail":
                        System.out.print("Type in the file name: ");
                        if(in.hasNextLine()){
                            filename = in.nextLine();
                        }
                        dfs.tail(filename);
                        break;
                    case "head":
                        System.out.print("Type in the file name: ");
                        if(in.hasNextLine()){
                            filename = in.nextLine();
                        }
                        dfs.head(filename);
                        break;
                    case "append":
                        System.out.print("Type in the file name: ");
                        if(in.hasNextLine()){
                            filename = in.nextLine();
                        }
                        //add size
                        Byte[] data = new Byte[10];
                        dfs.append(filename, data);
                        break;
                    case "move":
                        System.out.print("Type in the old name for the metadata: ");
                        if(in.hasNextLine()) {
                            oldname = in.nextLine();
                        }
                        System.out.println();

                        System.out.print("Type in the new name for the metadata: ");
                        if(in.hasNextLine()) {
                            newname = in.nextLine();
                        }

                        dfs.mv(oldname, newname);
                        System.out.println();
                        break;
                    default:
                        System.out.println("Invalid input.");
                        break;
                }

            }
        }

        System.out.println("Exit Client.");
    }
    
    static public void main(String args[]) throws Exception
    {
        if (args.length < 1 ) {
            throw new IllegalArgumentException("Parameter: <port>");
        }
        Client client=new Client( Integer.parseInt(args[0]));
        
     } 
}