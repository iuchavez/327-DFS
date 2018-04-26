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
    public void setWorkingPeer(Long page) throws IOException;
	public void completePeer(Long page, Long n) throws RemoteException;
    public boolean isPhaseCompleted() throws IOException;
    
    // Assumed we need to alter ChordMessageInterface to Context in reduceContext and mapContext
    //Mark change: MapReduceInterface to Mapper
	public void reduceContext(Long source, Mapper reducer,
	Context context) throws RemoteException;
	public void mapContext(Long page, Mapper mapper,
    Context context) throws RemoteException;

	public void emitMap(Long key, String value) throws RemoteException;
	public void emitReduce(Long page, String value) throws RemoteException;

}
