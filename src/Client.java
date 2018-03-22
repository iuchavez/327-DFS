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

    public void DFS_move(Scanner in){
        String oldname = "";
        String newname = "";
        System.out.print("Type in the old name for the metadata: ");
        //if(in.hasNextLine()) {
            oldname = in.nextLine();
        //}
        System.out.println();

        System.out.print("Type in the new name for the metadata: ");
        //if(in.hasNextLine()) {
            newname = in.nextLine();
        //}

        dfs.mv(oldname, newname);
    }

    public void DFS_read(Scanner in){
        String filename_page_num = "";
        String page_num_conv = "";
        int page_num = 0;
        System.out.print("Type in the file name and page number: ");
        //if(in.hasNextLine()){
            filename_page_num = in.nextLine();
        //}

        String[] content2 = filename_page_num.split(" ");
        filename = content2[0];
        page_num_conv = content2[1];
        page_num = Integer.parseInt(page_num_conv);

        dfs.read(filename, page_num);
    }

    public void DFS_join(Scanner in){
        String ip_and_port = "";
        String ip = "";
        String port_conv = "";
        int port = 0;
        System.out.print("Type in the IP address and port: ");
                        
        //if(in.hasNextLine()) {
            ip_and_port = in.nextLine();
        //}
                        
        String[] content = ip_and_port.split(" ");
        ip = content[0];
        port_conv = content[1];
        port = Integer.parseInt(port_conv);

        dfs.join(ip, port);
    }

    public Client(int p) throws Exception {
dfs = new DFS(p);
        
        Scanner in = new Scanner(System.in);
        int input = 0;


        //maybe get user to type in actual command
        do{
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
                System.out.println();

                switch(input){
                    case 1:
                        DFS_join(in);
                        break;
                    case 2:
                        dfs.ls();
                        break;
                    case 3:
                        System.out.print("Type in the file name: ");
                        //if(in.hasNextLine()){
                            filename = in.nextLine();
                        //}
                        dfs.touch(filename);
                        break;
                    case 4:
                        System.out.print("Type in the file name: ");
                        //if(in.hasNextLine()){
                            filename = in.nextLine();
                        //}
                        dfs.delete(filename);
                        break;
                    case 5:
                        DFS_read(in);
                        break;
                    case 6:
                        System.out.print("Type in the file name: ");
                        //if(in.hasNextLine()){
                            filename = in.nextLine();
                       // }
                        dfs.tail(filename);
                        break;
                    case 7:
                        System.out.print("Type in the file name: ");
                        //if(in.hasNextLine()){
                            filename = in.nextLine();
                        //}
                        dfs.head(filename);
                        break;
                    case 8:
                        System.out.print("Type in the file name: ");
                        //if(in.hasNextLine()){
                            filename = in.nextLine();
                        //}
                        //add size
                        Byte[] data = new Byte[10];
                        dfs.append(filename, data);
                        break;
                    case 9:
                        DFS_move(in);
                        System.out.println();
                        break;
                    case 10:
                        System.out.println("Exit Client.");
                        break;
                    default:
                        System.out.println("Invalid input.");
                        break;
                }
            }
        }while(input != 10);

        in.close();
    }

    static public void main(String args[]) throws Exception
    {
        if (args.length < 1 ) {
            throw new IllegalArgumentException("Parameter: <port>");
        }
        Client client=new Client( Integer.parseInt(args[0]));

    }
}