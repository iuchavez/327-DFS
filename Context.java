public class Context extends ContextInterface {
	Long n = 0;
	Set<Long> set;

	//the following function is intended to be in DFS, add later
	public void runMapReduce(InputStream file){ // may not be inputstream
		Context context = new Context();
		MapReduceInterface mapreduce = new MapReduceInterface();
		//for(Page p: metafile.file)
		//	context.add(p);
		//	peer = process to store page
		//	peer.mapContext(page,mapreduce,context);
		//	if(context.hasCompleted())
		//		reduceContext(guid, mapreduce, context);
		//wait until context.hasCompleted()
	}


	public void setWorkingPeer(Long page) { set.add(page); } 

	public void completePeer(Long page, Long n) throws RemoteException {
		this.n += n;
		set.remove(page);
	}

	public boolean isPhaseCompleted(){
		if(set.isEmpty())
			return true;
		return false;
	}

	public void reduceContext(Long page, ReduceInterface reducer, Context context) throws RemoteException{
		//TODO
		//if source != guid
		//call context.add(guid)
		//successor.reduceContext(source, reducer, context)
		//create new thread
		//read BReduce in order
		//reducer.reduce(key,value[],context)
		//when complete, context.complete(guid,n)
	}

	public void mapContext(Long page, MapReduceInterface mapper, Context context) throws RemoteException{
		//TODO
		//open page
		//read line by line
		//execute mapper.map(key,value,context)
		//once read, context.completePeer(page, n)
		//create new thread
	}
}