import java.rmi.*;

public interface ContextInterface extends Remote {
    public void setWorkingPeer(Long page);
    public void completePeer(Long page, Long n) throws RemoteException;
    public boolean isPhaseCompleted();
    public void reduceContext(Long source, Mapper reducer,
    Context context) throws RemoteException;
    public void mapContext(Long page, Mapper mapper,
    Context context) throws RemoteException;
    }