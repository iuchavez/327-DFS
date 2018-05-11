import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.net.*;
import java.util.*;
import java.io.*;

 
public class Chord extends java.rmi.server.UnicastRemoteObject implements ChordMessageInterface
{
    public static final int M = 2;
    
    Registry registry;    // rmi registry for lookup the remote objects.
    ChordMessageInterface successor;
    ChordMessageInterface predecessor;
    ChordMessageInterface[] finger;
    int nextFinger;
    long guid;   		// GUID (i)
    TreeMap<Long, LinkedList<String>> BMap = new TreeMap<Long, LinkedList<String>>();
    TreeMap<Long, String > BReduce = new TreeMap<Long, String>();
    Long n = 0l;
	Set<Long> set = new HashSet<Long>();
    
	/**
	 * Getter for the Reduce Tree for use in other Classes
	 * @return The Tree containinf all the reduced Keys and Values
	 */
	public TreeMap<Long, LinkedList<String>> getReduceTree(){
		return BMap;
	}
	
    public Boolean isKeyInSemiCloseInterval(long key, long key1, long key2)
    {
       if (key1 < key2)
           return (key > key1 && key <= key2);
      else
          return (key > key1 || key <= key2);
    }

    public Boolean isKeyInOpenInterval(long key, long key1, long key2)
    {
      if (key1 < key2)
          return (key > key1 && key < key2);
      else
          return (key > key1 || key < key2);
    }
    
    
    public void put(long guidObject, InputStream stream) throws RemoteException {
      try {
          String fileName = "./"+guid+"/repository/" + guidObject;
          FileOutputStream output = new FileOutputStream(fileName);
          while (stream.available() > 0)
              output.write(stream.read());
          output.close();
      }
      catch (IOException e) {
          System.out.println(e);
      }
    }
    
    
    public InputStream get(long guidObject) throws RemoteException {
        FileStream file = null;
        try {
             file = new FileStream("./"+guid+"/repository/" + guidObject);
        } catch (IOException e)
        {
            throw(new RemoteException("File does not exists"));
        }
        return file;
    }
    
    public void delete(long guidObject) throws RemoteException {
        File file = new File("./"+guid+"/repository/" + guidObject);
        file.delete();
    }
    
    public long getId() throws RemoteException {
        return guid;
    }
    public boolean isAlive() throws RemoteException {
	    return true;
    }
    
    public ChordMessageInterface getPredecessor() throws RemoteException {
	    return predecessor;
    }
    
    public ChordMessageInterface locateSuccessor(long key) throws RemoteException {
	    if (key == guid)
            throw new IllegalArgumentException("Key must be distinct that  " + guid);
	    if (successor.getId() != guid)
	    {
	      if (isKeyInSemiCloseInterval(key, guid, successor.getId()))
	        return successor;
	      ChordMessageInterface j = closestPrecedingNode(key);
	      
          if (j == null)
	        return null;
	      return j.locateSuccessor(key);
        }
        return successor;
    }
    
    public ChordMessageInterface closestPrecedingNode(long key) throws RemoteException {
        // todo
        if(key != guid) {
            int i = M - 1;
            while (i >= 0) {
                try{
       
                    if(isKeyInSemiCloseInterval(finger[i].getId(), guid, key)) {
                        if(finger[i].getId() != key)
                            return finger[i];
                        else {
                            return successor;
                        }
                    }
                }
                catch(Exception e)
                {
                    // Skip ;
                }
                i--;
            }
        }
        return successor;
    }
    
    public void joinRing(String ip, int port)  throws RemoteException {
        try{
            System.out.println("Get Registry to joining ring");
            Registry registry = LocateRegistry.getRegistry(ip, port);
            ChordMessageInterface chord = (ChordMessageInterface)(registry.lookup("Chord"));
            predecessor = null;
            successor = chord.locateSuccessor(this.getId());
            System.out.println("Joining ring");
        }
        catch(RemoteException | NotBoundException e){
            successor = this;
        }   
    }
    
    public void findingNextSuccessor()
    {
        int i;
        successor = this;
        for (i = 0;  i< M; i++)
        {   
            try
            {
                if (finger[i].isAlive())
                {
                    successor = finger[i];
                }
            }
            catch(RemoteException | NullPointerException e)
            {
                finger[i] = null;
            }
        }
    }
    
    public void stabilize() {
      try {
          if (successor != null)
          {
              ChordMessageInterface x = successor.getPredecessor();
	   
              if (x != null && x.getId() != this.getId() && isKeyInOpenInterval(x.getId(), this.getId(), successor.getId()))
              {
                  successor = x;
              }
              if (successor.getId() != getId())
              {
                  successor.notify(this);
              }
          }
      } catch(RemoteException | NullPointerException e1) {
          findingNextSuccessor();

      }
    }
    
    public void notify(ChordMessageInterface j) throws RemoteException {
         if (predecessor == null || (predecessor != null
                    && isKeyInOpenInterval(j.getId(), predecessor.getId(), guid)))
             predecessor = j;
            try {
                File folder = new File("./"+guid+"/repository/");
                File[] files = folder.listFiles();
                for (File file : files) {
                    long guidObject = Long.valueOf(file.getName());
                    if(guidObject < predecessor.getId() && predecessor.getId() < guid) {
                        predecessor.put(guidObject, new FileStream(file.getPath()));
                        file.delete();
                    }
                }
                } catch (ArrayIndexOutOfBoundsException e) {
                //happens sometimes when a new file is added during foreach loop
            } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    public void fixFingers() {
    
        long id= guid;
        try {
            long nextId = this.getId() + 1<< (nextFinger+1);
            finger[nextFinger] = locateSuccessor(nextId);
	    
            if (finger[nextFinger].getId() == guid)
                finger[nextFinger] = null;
            else
                nextFinger = (nextFinger + 1) % M;
        }
        catch(RemoteException | NullPointerException e){
            e.printStackTrace();
        }
    }
    
    public void checkPredecessor() { 	
      try {
          if (predecessor != null && !predecessor.isAlive())
              predecessor = null;
      } 
      catch(RemoteException e) 
      {
          predecessor = null;
//           e.printStackTrace();
      }
    }
       
    public Chord(int port, long guid) throws RemoteException {
        int j;
	    finger = new ChordMessageInterface[M];
        for (j=0;j<M; j++){
	       finger[j] = null;
     	}
        this.guid = guid;
	
        predecessor = null;
        successor = this;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
	    @Override
	    public void run() {
            stabilize();
            fixFingers();
            checkPredecessor();
            }
        }, 500, 500);
        try{
            // create the registry and bind the name and object.
            System.out.println(guid + " is starting RMI at port="+port);
            registry = LocateRegistry.createRegistry( port );
            registry.rebind("Chord", this);
        }
        catch(RemoteException e){
	       throw e;
        } 
    }
    
    void Print()
    {   
        int i;
        try {
            if (successor != null)
                System.out.println("successor "+ successor.getId());
            if (predecessor != null)
                System.out.println("predecessor "+ predecessor.getId());
            for (i=0; i<M; i++)
            {
                try {
                    if (finger != null)
                        System.out.println("Finger "+ i + " " + finger[i].getId());
                } catch(NullPointerException e)
                {
                    finger[i] = null;
                }
            }
        }
        catch(RemoteException e){
	       System.out.println("Cannot retrive id");
        }
    }

	public void setWorkingPeer(Long page) throws IOException { 
        System.out.println("Added a peer");
		set.add(page); 
    } 

	public void completePeer(Long page, Long n) throws RemoteException {
		System.out.println("Removed a peer");
		this.n += n;
		set.remove(page);
	}

	public boolean isPhaseCompleted() throws IOException {
		if(set == null)
			return false;
		return set.isEmpty();
	}

	public void reduceContext(Long source, ChordMessageInterface context, MapReduceInterface reducer, DFS dfs, String filename) throws RemoteException{
		if(source != this.guid) {
			successor.reduceContext(source, context, reducer, dfs, filename);
		}
		
		Thread reduceThread = new Thread() {
			@Override
			public void run() {
				System.out.print("Entered reduce thread");

				for(Map.Entry<Long, LinkedList<String>> entry: BMap.entrySet()){
					try{
						reducer.reduce(entry.getKey(), entry.getValue(), context);
					}
					catch(IOException e){
						System.out.print("cannot reduce");
					}
				}

				String tempFileName = "tempFile.txt";
                try{
                	dfs.touch(tempFileName); //adds in logical file to DFS
                	FileWriter temp = new FileWriter(tempFileName); //creates literal file
                
                	//add BReduce objects into literal temp file
                	for(Map.Entry<Long, String> entry: BReduce.entrySet()){
                	    temp.write(entry.getKey() + ";" + entry.getValue());
                	}
                	temp.flush();
                	temp.close();
				}catch(IOException e){
					System.out.println("FileWriter error");
				}
                //filename should have fileName_reduce
                try{
                	dfs.append(filename, tempFileName);
            	}
            	catch(Exception e){
            		System.out.println("append error");
            	}
			}
		};
		
		reduceThread.start();
	}
	
//	create a remote method that accepts the DFS , make is serializable and then pass it, and pass the file name

	public void mapContext(Long page, ChordMessageInterface context, MapReduceInterface mapper) throws RemoteException{
		// Context is the current context
		Thread mapThread = new Thread() {
			@Override
			public void run() {
				System.out.print("Entered map thread");
				try {
					context.setWorkingPeer(page);

					//find file with page title
					//open page(guid)
                    // ChordMessageInterface peer = this.locateSuccessor(page);
                    InputStream fstream = get(page);
                    
                    //FileReader fReader = new FileReader(fstream);
                    BufferedReader scan = new BufferedReader(new InputStreamReader(fstream));
                    //File 
                    //FileReader fReader = new FileReader();
//                    Scanner scan = new Scanner(fstream);
                    String line;
                    while((line = scan.readLine()) != null){
                        mapLine(line, context, mapper);
                        ++n;
                    }
                    
                    
                    System.out.println("Loop is done");
                    
					context.completePeer(page, n); //
					fstream.close();
                    scan.close();
					System.out.print("MapContext Complete: " + page);
				} catch(IOException e){
					System.out.println("Set working peer threw an IO exceptions");
				} 
				//	catch (InterruptedException e) {
				//	System.out.println("Sleep was interupted");
				//}
				
			}
		};
		
		mapThread.start();
	}

	/**
	 * Complete
	 */
    public void emitMap(Long key, String value) throws RemoteException
    {
    	if (isKeyInOpenInterval(key, predecessor.getId(), successor.getId()))
    	{
    	// insert in the BMap. Allows repetition
    		if (!BMap.containsKey(key))
    		{
    			LinkedList< String > list = new LinkedList< String >();
				BMap.put(key,list);
			} 
			BMap.get(key).add(value);
		}
		else
		{
			ChordMessageInterface peer = this.locateSuccessor(key);
			peer.emitMap(key, value);
		}
}
    
    /**
     * Complete
     */
	public void emitReduce(Long key, String value) throws RemoteException
	{ 
		if (isKeyInOpenInterval(key, predecessor.getId(), successor.getId()))
		{
			// insert in the BReduce
			BReduce.put(key, value);
		} 
		else
		{
			ChordMessageInterface peer = this.locateSuccessor(key);
			peer.emitReduce(key, value);
		}
	}

    public void mapLine(String line, ChordMessageInterface context, MapReduceInterface mapper) throws IOException{
    	System.out.println(line);
    	String[] kvPair = line.split(";");
        mapper.map(Long.parseLong(kvPair[0]), kvPair[kvPair.length-1], context);
    }
}
