import java.util.*;
/** \file */

/** \brief This file implements a given distributed file system's metadata.
 */

public class Metadata{
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

}