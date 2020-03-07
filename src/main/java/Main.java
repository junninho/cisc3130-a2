// import necessary java libraries
import java.io.*;
import java.util.*;

//import CSVReader and dependencies
import com.opencsv.CSVReader;
import java.nio.charset.StandardCharsets;

// Song node for queue
class Song {
  String track; // initialize track
  Song next, previous; // initialize next and previous

  // Song constructor
  public Song(String s) {
    track = s;
    next = null;
    previous = null;
  }
}

// Playlist Queue
class Playlist {
  private Song first;
  private Song last;

  // Playlist constructor
  public Playlist() {
    first = null;
    last = null;
  }

  // isEmpty method to check if queue is empty
  public boolean isEmpty() {
    return (first == null);
  }

  // method to add new song to queue
  public void addSong(String song) {
    Song newSong = new Song(song); // create new song node using parameter input
    if (isEmpty()) { // check if queue is empty
      first = newSong; // set first to equal new node
    } else {
      last.next = newSong; // set last's next to new node
      newSong.previous = last; // set new node's previous to last
    }
    last = newSong; // set last to equal new node
  } 

  // reads one song from the queue and removes it
  public String listenToSong() {
    Song current = first;
    first = current.next;
    return(current.track);
  }
}

// Song History List stack
class SongHistoryList {
  Song first;

  // SongHistoryList constructor
  public SongHistoryList() {
    first = null;
  }

  // method to add song to list
  public void addSong(String s) {
    Song newSong = new Song(s);
    newSong.next = first;
    first = newSong;
  }

  // method to return the last song listened to/first in the stack
  public String lastListened() {
    Song current = first;
    first = current.next;
    return(current.track);
  }
}

// queue that reads in songs from each file
class WeekQueue {
  Queue<String> songs; //queue to store songs using built-in queue
  public WeekQueue(String file) throws Exception{
    // import file
    try (FileInputStream fis = new FileInputStream(file);
    InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
    CSVReader reader = new CSVReader(isr)) {
      String[] nextLine; // store line for processing
      songs = new LinkedList<>();

      // ignore first two lines
      reader.readNext();
      reader.readNext();

      // extract songs from file and add title and artist to queue
      while ((nextLine = reader.readNext()) != null) {
        songs.add(nextLine[1] + " - " + nextLine[2]);
      }
    }
  }

  // method to get queue of songs
  public Queue getSongs() {
    return songs;
  }
}

// main method
public class Main {
  public static void main(String[] args) throws Exception {
    File directory = new File("data/"); // directory that holds data

    ArrayList<String> files = new ArrayList<>(); // array list to store file names
    
    // get file names from directory
    if (directory.isDirectory()) { // check if path is a directory
      File[] fileList = directory.listFiles(); // get file names from directory and store in array

      // add file name of files to arraylist
      for(File file : fileList) {
        if(file.isFile()) {
          files.add(file.getName());
        }
      }
    } else {
      System.out.println("directory does not exist");
    }

    Playlist allWeeks = new Playlist(); //playlist of all weeks

    // read in all files in arraylist and add to playlist
    for (String file : files) {
      WeekQueue data = new WeekQueue("data/" + file);
      Queue<String> s = data.getSongs();
      for (String song : s) {
        allWeeks.addSong(song);
      }
    }

    SongHistoryList songsPlayed = new SongHistoryList(); // list of songs played

    // listen to songs and add to songPlayed
    for(int i = 0; i < 15; i++) {
      String currentTrack = allWeeks.listenToSong();
      System.out.println("Currently playing: " + currentTrack);
      songsPlayed.addSong(currentTrack);
    }

    // print last listened
    System.out.println("--\nLast listened: " + songsPlayed.lastListened());

  }
}