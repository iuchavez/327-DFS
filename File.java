public class File {
    private String name;
    private int numberOfPages;
    private int pageSize;
    private int size;
    private Page page[];

    File() {
        name = "";
        numberOfPages = 0;
        pageSize = 0;
        size = 0;
        page = new Page[10];
    }

    File(String mFilename, int numPages, int pSize, int sz, Page pg[]) {
        name = mFilename;
        numberOfPages = numPages;
        pageSize = pSize;
        size = sz;
        page = pg;
    }

    String getName(){
        return name;
    }
    int getNumberOfPages(){
        return numberOfPages;
    }
    int getPageSize(){
        return pageSize;
    }
    int getSize(){
        return size;
    }
    Page[] getPage(){
        return page;
    }

    void setName(String nm){
        name = nm;
    }
    void setNumberOfPages(int numOPages){
        numberOfPages = numOPages;
    }
    void setPageSize(int pSize){
        pageSize = pSize;
    }
    void setSize(int sz){
        size = sz;
    }
    void setPage(Page pg[]){
        page = pg;
    }
}