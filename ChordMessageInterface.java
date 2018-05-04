import java.rmi.*;
import java.io.*;
import java.util.LinkedList;

public interface ChordMessageInterface extends Remote
{
    public ChordMessageInterface getPredecessor()  throws RemoteException;
    ChordMessageInterface locateSuccessor(long key) throws RemoteException;
    ChordMessageInterface closestPrecedingNode(long key) throws RemoteException;
    public void joinRing(String Ip, int port)  throws RemoteException;
    public void notify(ChordMessageInterface j) throws RemoteException;
    public boolean isAlive() throws RemoteException;
    public long getId() throws RemoteException;
    
    public void put(long guidObject, InputStream inputStream) throws IOException, RemoteException;
    public InputStream get(long guidObject) throws IOException, RemoteException;
    public void delete(long guidObject) throws IOException, RemoteException;

    //Context inferface functions
    public void setWorkingPeer(Long page) throws IOException;
	public void completePeer(Long page, Long n) throws RemoteException;
    public boolean isPhaseCompleted() throws IOException;
    public void reduceContext(Long source, ChordMessageInterface context) throws RemoteException;
	public void mapContext(Long page, ChordMessageInterface context) throws RemoteException;

	public void emitMap(Long key, String value) throws RemoteException;
	public void emitReduce(Long page, String value) throws RemoteException;

	//Mapper interface functions
	public void map(Long key, String value,
		ChordMessageInterface context) throws IOException;
	public void reduce(Long key, LinkedList<String> values,
		ChordMessageInterface context) throws IOException;
}
