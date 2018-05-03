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
    public boolean isPhaseCompleted()
    {
        return set.isEmpty();
    }
    public void reduceContext(Long source, Mapper reducer,
    Context context) throws RemoteException
    {
		//if(source != guid) {
		//	successor.reduceContext(source, reducer, context);
		//}
        if(source != this.guid) {
			successor.reduceContext(source, reducer, context);
		}

		
		//create new thread
		//in this thread:
			//read BReduce in order
			//reducer.reduce(key,value[],context)
			//when complete, context.complete(guid,context.n)
    }
    public void mapContext(Long page, Mapper mapper,
    Context context) throws RemoteException
    {
		//setWorkingPeer(page);
		//open page(guid)
		//read line by line
		//mapper.map(key, value, context);
		//once read, context.completePeer(page, context.n)
		//create new thread
        setWorkingPeer(page);
        FileStream pageStream = this.get(page.getGuid());
        BufferedReader pageReader
        while(pageStream.hasNext()){
             
        }
    }
}