import java.util.LinkedList;
import java.io.Serializable;
/** \file */

/** \brief This file implements a DFS file.
 */

public class mFile implements Serializable{
    private String name;
    private long numberOfPages;
    private long pageSize;
    private long size;
    private LinkedList<Page> page;

    /**
 	* Constructs a metadata instance
 	*/
    mFile() {
        name = "";
        numberOfPages = 0;
        pageSize = 0;
        size = 0;
        page = new LinkedList<Page>();
    }

    /**
 	* Constructs a DFS instance with info
 	* \param mFilename filename
 	* \param numPages page count
 	* \param pSize page size
 	* \param sz file size
 	* \param pg pages
 	*/
    mFile(String mFilename, int numPages, int pSize, int sz, LinkedList<Page> pg) {
        name = mFilename;
        numberOfPages = numPages;
        pageSize = pSize;
        size = sz;
        page = pg;
    }

    /**
 	* Returns file name
 	* \return filename
 	*/
    String getName(){ return name; }

    /**
 	* Returns page count
 	* \return page count
 	*/
    long getNumberOfPages(){ return numberOfPages; }

    /**
 	* Returns page size
 	* \return page size
 	*/
    long getPageSize(){ return pageSize; }

    /**
 	* Returns file size
 	* \return file size
 	*/
    long getSize(){ return size; }

    /**
 	* Returns page
 	* \return page
 	*/
    LinkedList<Page> getPage(){ return page; }

    /**
 	* Sets file name
 	* \param nm name
 	*/
    void setName(String nm){ name = nm; }

    /**
 	* Sets page count
 	* \param numOPages page count
 	*/
    void setNumberOfPages(long numOPages){ numberOfPages = numOPages; }

    /**
 	* Sets page size
 	* \param pSize page size
 	*/
    void setPageSize(long pSize){ pageSize = pSize; }

    /**
 	* Sets file size
 	* \param sz size
 	*/
    void setSize(long sz){ size = sz; }

    /**
 	* Sets pages
 	* \param pg pages
 	*/
    void setPage(LinkedList<Page> pg){ page = pg; }
    void addPage(Page pg){ page.add(pg); }
    
    @Override  
    public String toString(){
        super.toString();
        return new String("Name: " + name);
    }
}