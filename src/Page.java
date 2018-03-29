class Page{
    int number;
    int guid;
    int size;

    Page(){
        number = 0;
        int guid = 0;
        int size = 0;
    }

    Page(int num, int gid, int sz){
        number = num;
        guid = gid;
        size = sz;
    }

    int getNumber(){
        return number;
    }
    int getGuid(){
        return guid;
    }
    int getSize(){
        return size;
    }

    void setNumber(int num){
        number = num;
    }
    void setGuid(int gid){
        guid = gid;
    }
    void setSize(int sz){
        size = sz;
    }
}