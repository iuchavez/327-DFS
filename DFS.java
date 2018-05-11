import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.GsonBuilder;

import java.rmi.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.*;
import com.google.gson.stream.JsonToken;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParseException;

/* JSON Format

 {
    "metadata" :
    {
        file :
        {
            name  : "File1"
            numberOfPages : "3"
            pageSize : "1024"
            size : "2291"
            page :
            {
                number : "1"
                guid   : "22412"
                size   : "1024"
            }
            page :
            {
                number : "2"
                guid   : "46312"
                size   : "1024"
            }
            page :
            {
                number : "3"
                guid   : "93719"
                size   : "243"
            }
        }
    }
}
 
 
 */

public class DFS {
	int port;
	Chord chord;
	public static final String METADATA = "Metadata";

	public static long md5(String objectName) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(objectName.getBytes());
			BigInteger bigInt = new BigInteger(1, m.digest());
			return Math.abs(bigInt.longValue());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public DFS(int port) throws Exception {

		this.port = port;
		long guid = md5("" + port);
		chord = new Chord(port, guid);
		Files.createDirectories(Paths.get(guid + "/repository"));
	}

	public void join(Scanner in) throws Exception {
		System.out.print("Type in the IP address: ");
		String ip = in.next();
		int port = 8080;

		if (!ip.equals(null)) {
			System.out.print("Type in the port: ");
			port = in.nextInt();
		}
		System.out.println(ip + " Port " + port);
		try {
			chord.joinRing(ip, port);
		} catch (IllegalArgumentException e) {
			System.out.println("That was not a valid port on the DFS");
		}
		chord.Print();
	}

	public Metadata readMetaData() {
		ChordMessageInterface peer = null;
		InputStream mdRaw = null;
		long guid = md5(METADATA);
		Metadata m = null;

		try {
			peer = chord.locateSuccessor(guid);
			mdRaw = peer.get(guid);
			Gson gson = new Gson();

			String fileName = "327FS.json";
			FileOutputStream output = new FileOutputStream(fileName);
			while (mdRaw.available() > 0)
				output.write(mdRaw.read());
			output.close();

			// Convert json file to object
			FileReader fReader = new FileReader(fileName);
			m = gson.fromJson(fReader, Metadata.class);
		} catch (RemoteException e) {
			// If metadata doesn't exist, Create one
			m = new Metadata();
			System.out.println("No metadata on DFS. A new one has been created.");
			// e.printStackTrace();
		} catch (IOException f) {
			System.out.println("There was an IO Exception");
			f.printStackTrace();
		}

		return m;
	}

	public void writeMetaData(Metadata metadata) {
		Gson gson = new GsonBuilder().create();
		FileWriter writer = null;
		long guid = md5(METADATA);
		InputStream mdRaw = null;
		Metadata m = new Metadata();
		ChordMessageInterface peer = null;

		try {
			writer = new FileWriter("327FS.json");
			writer.write(gson.toJson(metadata));
			writer.close();
			peer = chord.locateSuccessor(guid);
			peer.put(guid, new FileStream("327FS.json"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mv(Scanner in) throws Exception {
		String oldname = "";
		String newname = "";
		System.out.print("Type in the old name for the metadata: ");
		if (in.hasNext()) {
			oldname = in.nextLine();
		}

		System.out.print("Type in the new name for the metadata: ");
		if (in.hasNext()) {
			newname = in.nextLine();
		}

		Metadata md = readMetaData();
		LinkedList<mFile> files = md.getFiles();
		for (mFile file : files) {
			if (file.getName().equals(oldname)) {
				file.setName(newname);
			}
		}

		writeMetaData(md);
	}

	public void ls() throws Exception {
		String listOfFiles = "";
		// StringBuilder loFiles = new StringBuilder();

		Metadata md = readMetaData();
		LinkedList<mFile> files = md.getFiles();

		for (int i = 0; i < files.size(); i++) {
			listOfFiles = listOfFiles + files.get(i).getName();
			listOfFiles = listOfFiles + "\n";
		}

		System.out.println(listOfFiles);
	}

	/**
	 * This is a simplified version of the touch method, it takes in a file name and
	 * writes a new logical file to the official Metadata
	 * 
	 * @param filename
	 *            is the desired name of the new logical file
	 * @throws Exception
	 */
	public void touch(String filename) {
		// Create a file with the given filename
		mFile aFile = new mFile(filename);

		// Retrieve Metadata
		Metadata metaData = readMetaData();
		metaData.addFile(aFile);

		// write it back into the official Metadata
		writeMetaData(metaData);
	}

	/**
	 * This method is intended to add data to the DFS and to associate that data to
	 * a file.
	 **/
	public void append(String filename, String filepath) throws Exception {

		// Search for the file linearly
		Metadata metaData = readMetaData();
		LinkedList<mFile> files = metaData.getFiles();
		mFile fileToAppend = findLogicalFile(files, filename);
		if (fileToAppend == null) {
			// If the file was not found then return to the calling method
			System.out.print("The file was not found.");
			return;
		}

		// Read in the data that will be appended
		FileStream fStream = null;
		ChordMessageInterface peer = null;
		Page pg = new Page();
		long guid = 0;
		try {
			fStream = new FileStream(filepath);
			guid = md5(filepath);
			pg.setGuid(guid);
			peer = chord.locateSuccessor(guid);
			pg.setSize(fStream.getSize());
			pg.setNumber(fileToAppend.getPage().size());
		} catch (FileNotFoundException e) {
			System.out.println("File was not found.");
			return;
		}
		// Grab page from metadata and append a page to the last file
		fileToAppend.addPage(pg);

		// update appended file
		fileToAppend.setNumberOfPages(fileToAppend.getNumberOfPages() + 1);
		fileToAppend.setPageSize(fileToAppend.getSize());
		fileToAppend.setSize(fileToAppend.getSize() + pg.getSize());

		// Add Files to DFS and write updated metadata back to the authoritative index
		writeMetaData(metaData);
		peer.put(guid, fStream);
		System.out.print("File was successfully written.");
	}

	public void delete() throws Exception {
		Scanner in = new Scanner(System.in);
		String filename = "";
		ChordMessageInterface peer = null;
		Metadata md = readMetaData();
		LinkedList<mFile> files = md.getFiles();
		LinkedList<Page> pages = null;
		boolean found = false;
		mFile fileToDelete = null;

		// Prompt user for file name input
		System.out.print("Type in the file name: ");
		if (in.hasNext()) {
			filename = in.nextLine();
		}

		// Attempting to delete all page within a given file
		for (mFile f : files) {
			if (f.getName().equals(filename)) {
				fileToDelete = f;
				found = true;
			}
		}

		// If it is not found then return to calling method
		if (!found) {
			System.out.println("That file was not found");
			return;
		}

		// Delete all pages associated with that file
		pages = fileToDelete.getPage();
		for (Page p : pages) {
			peer = chord.locateSuccessor(p.getGuid());
			peer.delete(p.getGuid());
		}
		md.removeFile(fileToDelete);

		// Write the metadata back to the authoritative index
		writeMetaData(md);
	}

	public InputStream read(Scanner in) throws Exception {
		Metadata md = readMetaData();
		LinkedList<mFile> files = md.getFiles();
		String filename = "";
		int pageNumber = 0;
		Page p = null;
		InputStream iStream = null;
		ChordMessageInterface peer = null;

		// Take input from user
		System.out.print("Type in the file name: ");
		if (in.hasNext()) {
			filename = in.next();
		}
		System.out.print("Type in the page number: ");
		if (in.hasNextInt()) {
			pageNumber = in.nextInt();
		}

		String pageFile = filename + "page" + pageNumber;

		// search through metadata for this file
		for (mFile file : files) {
			if (file.getName().equals(filename)) {
				if (pageNumber > file.getNumberOfPages()) { // number logistics check
					return null;
				} else // finds the number
				{
					LinkedList<Page> pgs = file.getPage();
					p = pgs.get(pageNumber);
					System.out.println(p.toString());
					peer = chord.locateSuccessor(p.getGuid());
					iStream = peer.get(p.getGuid());
					return iStream;
				}
			}
		}

		return null;
	}

	public InputStream tail(Scanner in) throws Exception {
		String filename = "";
		InputStream iStream = null;
		System.out.print("Type in the file name: ");
		if (in.hasNext()) {
			filename = in.next();
		}

		Metadata md = readMetaData();
		LinkedList<mFile> files = md.getFiles();
		Page p = null;
		int lastpage = files.size() - 1;
		String pageFile = filename + "page" + lastpage;

		for (mFile file : files) {
			if (file.getName().equals(filename) && file != null) {
				p = file.getPage().getLast();
				System.out.println(p.toString());
				iStream = chord.get(p.getGuid());
				return iStream;
			}
		}

		return null;
	}

	public InputStream head(Scanner in) throws Exception {
		String filename = "";
		InputStream iStream = null;
		System.out.print("Type in the file name: ");
		if (in.hasNext()) {
			filename = in.next();
		}

		Metadata md = readMetaData();
		LinkedList<mFile> files = md.getFiles();
		Page p = null;
		String pageFile = filename + "page0";

		for (mFile file : files) {
			if (file.getName().equals(filename) && file != null) {
				p = file.getPage().getFirst();
				System.out.println(p.toString());
				iStream = chord.get(p.getGuid());
				return iStream;
			}
		}

		return null;
	}

	/**
	 * Takes in a file name, searches in the linked list and then returns that
	 * logical file
	 * 
	 * @param filename
	 * @return
	 */
	public mFile findLogicalFile(LinkedList<mFile> files, String filename) {
		for (mFile f : files) {
			if (f.getName().equals(filename)) {
				return f;
			}
		}
		return null;
	}

	public void runMapReduce() throws RemoteException {
		// Declare vars
		String fileName = "";
		String newFileName = "";
		LinkedList<mFile> files;
		mFile originalFile = new mFile();
		Boolean fileFoundFlag = false;
		ChordMessageInterface peer = null;
        
		// Take input from the user
		Scanner in = new Scanner(System.in);
		System.out.print("Which file will you like to count?");
		if (in.hasNext()) {
			fileName = in.nextLine();
		}

		// Search for the file linearly
		String reducedFileName = "";
		Metadata metaData = readMetaData();
		files = metaData.getFiles();
		for (mFile f : files) {
			if (f.getName().equals(fileName)) {
				System.out.print("File was found!");
				newFileName = fileName + "_reduce";
				dfs.touch(newFileName);
				fileFoundFlag = true;
				originalFile = f;
			}
		}

		// If file not found report to user
		if (!fileFoundFlag) {
			System.out.print("File was not found.");
			return;
		}

		// Go through every page in the file and map it
		Mapper mapper = new Mapper();
		for (Page p : originalFile.getPage()) {
			long pageGuid = p.getGuid();

			try {
				// Seperate the Mapper class again
				peer = chord.locateSuccessor(pageGuid);
				peer.mapContext(p.getGuid(), chord, mapper); /// in while
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Wait for the items to get mapped
		System.out.print("Waiting for Sync");
		try {
			while (!chord.isPhaseCompleted()) {
				//System.out.println("Negated isPhaseComplete()" + !chord.isPhaseCompleted());
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.print("Ready for the reduce Phase");

		// Begin to reduce
		chord.reduceContext(chord.getId(), chord, mapper, this);

		// Wait for reduce phase
		try {
			while (!chord.isPhaseCompleted()) {
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Create a new logical file (touch) with the pages as the output of reduce
	}
    
}
