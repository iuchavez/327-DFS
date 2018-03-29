public class Metadata{
    File file[];

    Metadata(){
        file = new File[2];
    }

    Metadata(File tFiles[]){
        file = tFiles;
    }

    File[] getFile(){
        return file;
    }
    void setFile(File tFile[]){
        file = tFile;
    }
}