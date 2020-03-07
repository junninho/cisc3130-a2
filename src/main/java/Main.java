// import necessary java libraries
import java.io.*;
import java.util.*;

//import CSVReader and dependencies
import com.opencsv.CSVReader;
import java.nio.charset.StandardCharsets;

class Song {
  String track;
  Song next, previous;

  // Song constructor
  public Song(String s) {
    track = s;
    next = null;
    previous = null;
  }
}

class Playlist {
  private Song first;
  private Song last;


  public Playlist() {
    first = null;
    last = null;
  }

  public boolean isEmpty() {
    return (first == null);
  }

  public void addSong(String song) {
    Song newSong = new Song(song);
    if (isEmpty()) {
      first = newSong;
    } else {
      last.next = newSong;
      newSong.previous = last;
    }
    last = newSong;
  } 

  public void listenToSong() {
    Song current = first;
    System.out.println("Currently listening to " + current.track);
    first = current.next;
  }
}

class SongHistoryList {
  Song first;

  public SongHistoryList() {
    first = null;
  }

  public void addSong(String s) {
    Song newSong = new Song(s);
    newSong.next = first;
    first = newSong;
  }

  public void lastListened() {
    Song current = first;
    System.out.println(current.track);
    first = current.next;
  }
}

class WeekQueue {
  Queue<String> songs;
  public WeekQueue(String file) throws Exception{
    try (FileInputStream fis = new FileInputStream(file);
    InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
    CSVReader reader = new CSVReader(isr)) {
      String[] nextLine; // store line for processing
      songs = new LinkedList<>();

      // ignore first two lines
      reader.readNext();
      reader.readNext();

      // extract artists from file and add to list
      while ((nextLine = reader.readNext()) != null) {
        songs.add(nextLine[1] + " - " + nextLine[2]);
      }
    }
  }

  public Queue getSongs() {
    return songs;
  }
}

public class Main {
  public static void main(String[] args) throws Exception {
    File directory = new File("data/");

    ArrayList<String> files = new ArrayList<>();
    
    if (directory.isDirectory()) {
      File[] fileList = directory.listFiles();

      for(File file : fileList) {
        if(file.isFile()) {
          files.add(file.getName());
        }
      }
    } else {
      System.out.println("directory does not exist");
    }

    Playlist allWeeks = new Playlist();

    for (String file : files) {
      WeekQueue data = new WeekQueue("data/" + file);
      Queue<String> s = data.getSongs();
      for (String song : s) {
        allWeeks.addSong(song);
      }
    }

    // test
    allWeeks.listenToSong();
    allWeeks.listenToSong();
    allWeeks.listenToSong();
    allWeeks.listenToSong();

  }
}