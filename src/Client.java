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
    int port = p;

        Scanner in = new Scanner(System.in);
        int input = 0;

        while(input != 10){

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

            if(in.hasNextInt()){
                input = in.nextInt();
                String filename = "";
                String oldname = "";
                String newname = "";
                int page_num = 0;
                String ip = "";

                switch(input){
                    case 1:
                        System.out.print("Type in the IP address: ");
                        if(in.hasNextLine()) {
                            ip = in.nextLine();
                        }
                        //Port that is being connected to.
                        System.out.print("Type in the port: ");
                        if(in.hasNextInt()){
                            port = in.nextInt();
                        }
                        dfs.join(ip, port);
                        break;
                    case 2:
                        dfs.ls();
                        break;
                    case 3:
                        System.out.print("Type in the file name: ");
                        if(in.hasNextLine()){
                            filename = in.nextLine();
                        }
                        dfs.touch(filename);
                        break;
                    case 4:
                        System.out.print("Type in the file name: ");
                        if(in.hasNextLine()){
                            filename = in.nextLine();
                        }
                        dfs.delete(filename);
                        break;
                    case 5:
                        System.out.print("Type in the file name: ");
                        if(in.hasNextLine()){
                            filename = in.nextLine();
                        }
                        System.out.print("Type in the page number: ");
                        if(in.hasNextInt()){
                            page_num = in.nextInt();
                        }
                        dfs.read(filename, page_num);
                        break;
                    case 6:
                        System.out.print("Type in the file name: ");
                        if(in.hasNextLine()){
                            filename = in.nextLine();
                        }
                        dfs.tail(filename);
                        break;
                    case 7:
                        System.out.print("Type in the file name: ");
                        if(in.hasNextLine()){
                            filename = in.nextLine();
                        }
                        dfs.head(filename);
                        break;
                    case 8:
                        System.out.print("Type in the file name: ");
                        if(in.hasNextLine()){
                            filename = in.nextLine();
                        }
                        //add size
                        Byte[] data = new Byte[10];
                        dfs.append(filename, data);
                        break;
                    case 9:
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