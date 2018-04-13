import java.util.*;
import java.io.Serializable;
/** \file */

/** \brief This file implements a given distributed file system's metadata.
 */

public class Metadata implements Serializable{
    LinkedList<mFile> file;

    /**
 	* Constructs a metadata instance
 	*/
    Metadata(){ file = new LinkedList<mFile>();}

    /**
 	* Constructs a DFS instance with files
 	* @param tFiles files
 	*/
    Metadata(LinkedList<mFile> tFiles){ file = tFiles; }


	public void list()
	{
		System.out.println("Number of file" + file.size());			

	} 

    /**
 	* Returns file
 	* @return file
 	*/
    LinkedList<mFile> getFile(){ return file; }

    /**
 	* Sets file in metadata
 	* @param tFile file
 	*/
	void setFile(LinkedList<mFile> tFile){ file = tFile; }

	/**
	 * Adds a file to the file linked list
	 */
	void addFile(mFile tFile){ file.add(tFile); }

	mFile getLastFile(){
		return file.peekLast();
	}

}