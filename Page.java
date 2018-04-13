import java.io.Serializable;
/** \file */

/** \brief This file implements a DFS file page.
 */

public class Page implements Serializable{
    long number;
    long guid;
    long size;

    /**
 	* Constructs a page instance
 	*/
    Page(){
        number = 0;
        int guid = 0;
        int size = 0;
    }

    /**
 	* Constructs a page instance with info
 	* \param num number
 	* \param guid id
 	* \param sz size
 	*/
    Page(long num, long gid, long sz){
        number = num;
        guid = gid;
        size = sz;
    }

    /**
 	* Returns number
 	* \return number
 	*/
    long getNumber(){ return number; }

    /**
 	* Returns guid
 	* \return guid
 	*/
    long getGuid(){ return guid; }

    /**
 	* Returns page size
 	* \return page size
 	*/
    long getSize(){ return size; }

    /**
 	* Sets page number
 	* \param num page number
 	*/
    void setNumber(long num){ number = num; }

    /**
 	* Sets guid
 	* \param gid id
 	*/
    void setGuid(long gid){ guid = gid; }

    /**
 	* Sets page size
 	* \param sz size
 	*/
    void setSize(long sz){ size = sz; }
}