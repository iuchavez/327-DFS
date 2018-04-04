public class FileSystem{
    Metadata metadata;

    FileSystem(){
        metadata = new Metadata();
    }

    FileSystem(Metadata md){
        metadata = md;
    }

    Metadata getMetadata(){
        return metadata;
    }
    void setMetadata(Metadata md){
        metadata = md;
    }
}