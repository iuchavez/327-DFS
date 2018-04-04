public class Metadata{
    mFile file[];

    Metadata(){
        file = new mFile[2];
    }

    Metadata(mFile tFiles[]){
        file = tFiles;
    }

    mFile[] getFile(){
        return file;
    }
    void setFile(mFile tFile[]){
        file = tFile;
    }
}