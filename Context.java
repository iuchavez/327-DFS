import java.util.Set;
import java.rmi.*;

public class Context implements ContextInterface {
    Long n = 0l;
    Set<Long> set;

    public void setWorkingPeer(Long page)
    {
        set.add(page);
    }
    public void completePeer(Long page, Long n) throws RemoteException
    {
        this.n += n;
        set.remove(page);
    }
    public Boolean isPhaseCompleted()
    {
        if (set.isEmpty())
        return true;
        return false;
    }
    public void reduceContext(Long source, MapReduceInterface reducer,
    Context context) throws RemoteException
    {
        // TODO
    }
    public void mapContext(Long page, MapReduceInterface mapper,
    Context context) throws RemoteException
    {
        // TODO
    }
}