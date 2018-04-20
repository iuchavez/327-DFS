import java.rmi.*;
import java.io.*;

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


    //context inferface functions
    public void setWorkingPeer(Long page);
	public void completePeer(Long page, Long n) throws RemoteException;
    public boolean isPhaseCompleted();
    
	public void reduceContext(Long source, MapReduceInterface reducer,
	ChordMessageInterface context) throws RemoteException;
	public void mapContext(Long page, MapReduceInterface mapper,
    
    ChordMessageInterface context) throws RemoteException;

	public void emitMap(Long key, String value) throws RemoteException;
	public void emitReduce(Long page, String value) throws RemoteException;

	//mapper functions goes into MapReduceInterface we need to impement them in another file totally independent, testing classes
	public void map(Long key, String value, ChordMessageInterface context);
	public void reduce(Long key, List< String > value, ChordMessageInterface context);
}
