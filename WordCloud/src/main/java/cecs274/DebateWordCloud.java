package cecs274;

/**
 * 
 * Analyzes a text file containing the transcript of a presidential debate and
 * creating an ArrayList of WordFrequency objects, that is words spoken by a specified speaker 
 * and their frequency identifies the 40 most frequently used words by that speaker, 
 * and uses that information to call the HTMLWordCloudPrinter methods provided to create the word clouds.
 * 
 * 
 * @author Gideon Essel gideon.essel@student.csulb.edu
 * @author Christian Lam christian.lam@student.csulb.edu
 */

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
//<a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Collections.html</a>
//Found a class to sort and shuffle ArrayLists
import java.util.Collections;

public class DebateWordCloud {
   
   // TODO: Declare constants, instance or class variables
   private ArrayList<String> words; //All the words from the debate file.

   private ArrayList<String> stopWords; //All the stop words

   private static ArrayList<String> onlyWords;// The words of specific speakers

   private static ArrayList<String> keyWords;//Speakers words with the speaker,stop words, and punctuations stripped out

   private static ArrayList<WordFrequency> frequencyWord; // Stores words and their frequency

   private static ArrayList<WordFrequency> arrayListWords; //The top 40 words from frequencyWord.

   public static int MINFREQINDEX = 0; // index of the lowest frequency

   public static int MAXFREQINDEX = 39; // index of the highest frequency

   private static boolean skipsFirstLine;

   private static boolean replaceTop;

   /**
    * A method to create a word cloud from the top 40 words most used frequently by a certain speaker 
    * during a debate. 
    * @param args Creates a wordCloud with the arguments of (Debate File, Speaker Name, StopWords File)
    * @throws FileNotFoundException Throws an exception if the file is not found
    */
   
   public static void main(String args[]) throws FileNotFoundException {
      DebateWordCloud debate = new DebateWordCloud();

      if(args.length < 3 || args.length > 4) //Checks to make sure exactly three args are passed into args
      {

         System.out.println("Please specify these three paramters: debate file, speaker, and stopwords file");
      }

      else

      {
      debate.parseFile(args[0],args[1]); //Debate file and speaker args is passed into parseFile method.

      debate.removePunc(); // Removes the punctuations 

      debate.stopWordsFile(args[2]); //We pass stop words file args into stopWordsFile.

      debate.increaseFrequency(); // increases each words frequency 

      arrayListWords = new ArrayList<>(); // creates ArrayList to hold 40 word

      arrayListWords = getTopWords(frequencyWord); // sets top 40 words into a new ArrayList

      //Constructor is created with the title, minimum frequency and maximum are passed in as parameters
      HtmlWordCloudPrinter cloud = new HtmlWordCloudPrinter(args[1],arrayListWords.get(MINFREQINDEX).getFrequency(),arrayListWords.get(MAXFREQINDEX).getFrequency());
      
      Collections.shuffle(arrayListWords); //Shuffles a given list using the user provided source of randomness.

      for(int i = 0; i < arrayListWords.size(); i++) //Iterates through top 40 words
      {
         cloud.addCloud(arrayListWords.get(i).getWord(),arrayListWords.get(i).getFrequency()); //Passes string and frequency into method
      }

      cloud.print(args[1].toLowerCase()  + "WordCloud.html"); //Creates name of output file

      }
   }   
   /**
    * A method that is given the debate text file and the speaker name.
    * With the given parameters the method will read the debate file and find all the words
    * said by the speaker and store them.
    * @param debateText is the debate text file to be used
    * @param speaker is the speaker name to be used
    */
   public void parseFile(String debateText, String speaker) throws FileNotFoundException {
      File debateFile = new File(debateText); //Constructs new file for the debate file.

      Scanner debateScanner = new Scanner(debateFile); //A new scanner is created for the debate file.

      words = new ArrayList<>(); //Constructs an arrayList to hold debate file words.
      boolean isSpeaking = false;

      // sets Speakername to the inputed speaker with colon
      String speakerName = speaker.toUpperCase() + ":";
   
      // Loops until there is no line to read
      while (debateScanner.hasNextLine()) {

         String word = debateScanner.nextLine(); //We store each line from the file as a String
         
         //Line is checked if it contains specific elements we return the boolean as false
         if (word.contains(":")
               && word.substring(0, word.indexOf(":")).toUpperCase().equals(word.substring(0, word.indexOf(":")))
               && !(word.substring(0, word.indexOf(":")).equals(speaker))) {
            isSpeaking = false;
            
         }
         //If line contains speaker then we set boolean to truth
         if (word.contains(speaker)) {
            isSpeaking = true;
         }

        
         if (isSpeaking) {
            words.add(word.replace(speakerName, "")); //Instances of speaker are replace with blank space
         }
         
      }
      debateScanner.close();
   }

   /**
    * A method that takes the words spoken by the speaker that were recently
    * stored and compares it to a file of words that removes those words in the
    * speakers words. Then the key words that remain are stored.
    * @param stopText the file used to remove all the stop words.
    */
   public void stopWordsFile(String stopText) throws FileNotFoundException {
      File stopFile = new File(stopText);

      Scanner stopScanner = new Scanner(stopFile);

      stopWords = new ArrayList<>();

      keyWords = new ArrayList<>();

      while (stopScanner.hasNextLine()) {
         // Adds each stop word in new ArrayList
         String stopLine = stopScanner.nextLine();

         stopWords.add(stopLine);
      }
      // Loops through ArrayList of line with removed punc
      for (int i = 0; i < onlyWords.size(); i++) {
         // Loops sets new string to each word split
         for (String j : onlyWords.get(i).split(" ")) {
            // Loop sets new string to each stop word
            for (String k : stopWords) {
               // If word = stop word
               if (j.equalsIgnoreCase(k)) {
                  // removed word
                  j = "";
               }
            }
            // Adds to new ArrayList with stopWords removed
            keyWords.add(j + " ");
         }
      }
      stopScanner.close();
   }
   /**
    * This method takes the key words that were stored and removes all the punctuations
    * and also all the noises by the audience.
    */
   public void removePunc() {
      // Creates new ArrayList to store words with punc removed
      onlyWords = new ArrayList<>();
      // Sets new string to ArrayList with speakers words
      for (String i : words) {
         // replaces any punctuation and other crowd
         i = i.replaceAll("[^\\s\\w]", "").replaceAll("APPLAUSE", "").replaceAll("LAUGHTER", "").replaceAll("[0-9]","").stripLeading().trim().toLowerCase();
         // Adds only the words into ArrayList
         onlyWords.add(i);
      }

   }
   /**
    * This method takes the stored key words and counts
    * how many times the word was frequently said.
    *
    *CITATION: Engineering Tutors ~ Joshua K and Joshua L
    *Helped me to brainstorm ways in which increaseFrequency
    *could be implemented 
    */
   public void increaseFrequency()
   {

   frequencyWord = new ArrayList<WordFrequency>();

   boolean isIdentical = false;

   for(int i = 0; i < keyWords.size(); i++) 
   {
         for (int j = 0; j < frequencyWord.size(); j++)
         {
         //Gets element in keyWord arrayList and checks whether the
         //values are the same contained in frequencyWord
         if(keyWords.get(i).equals(frequencyWord.get(j).getWord()))
            {
               frequencyWord.get(j).incrementFrequency(); /*Increments the object*/
               isIdentical = true; 
               break;
             }
      }

      if(isIdentical == false) 
      {
          WordFrequency word = new WordFrequency(keyWords.get(i), 1); //WordFrequency object is created with String and current occurence
          
          frequencyWord.add(word); //Adds word objects to an arrayList of similar objects 
        
      }

      else 
      {
       isIdentical = false;
      }

      
   }  
     
   }
   /**
    * This method takes given list contain the key words and its frequencies
    * and creates a top 40 mosted used word list. The list will then contain
    * the 40 most used words and its frequencies.
    * @param frequencyWord the list used containing both the key words and its frequencies
    * @return an ArrayList contain the top 40 words and its frequencies
    * CITATION: CALEB LEE
    * Caleb helped explain to us that we must create a new ArrayList of object
    * WordFrequency to hold the top 40 words and frequencies. He also told us to create
    * an object of WordFrequency to get both the word and frequency from the ArrayList before.
    * He also told us to separate the word and frequencies and told us to add those into
    * the new ArrayList and make sure to skip the first line since we had a space frequency.
    */
   public static ArrayList<WordFrequency> getTopWords(ArrayList<WordFrequency> frequencyWord) {
      // Creates a new ArrayList to hold the top words 
      ArrayList<WordFrequency> topWords = new ArrayList<WordFrequency>();
      // Creates a boolean variable to skip the very first word which is a space
      skipsFirstLine = true;
      // Creates a boolean to stop loop once another wordFrequency is greater
      replaceTop = false;
      // Loops through the ArrayList containing keyWords and the frequencies
      for (int i = 0; i < frequencyWord.size(); i++) {
         // Creates a new object for WordFrequency to hold the keyWords and frequencies
         WordFrequency wordAndFreq = frequencyWord.get(i);
         // Creates a string to hold the words of the new object
         String word = frequencyWord.get(i).getWord();
         // Creates a integer variable to hold the frequencies of the new object
         Integer wordFreq = frequencyWord.get(i).getFrequency();
         //  If the firstLine is not skipped and the size of the ArrayList holding top 40 words is less than 40 and the word isnt a space
         if(skipsFirstLine == false && topWords.size() < 40 && word != " ") {
            // Adds the word and the frequency of the current index to ArrayList
            topWords.add(frequencyWord.get(i));
         }


         // else if the word isnt a space and the firstLine is not skipped
         else if (word != " " && skipsFirstLine == false){
            // Loops until we replace a top 40 word
            while ( replaceTop )
            {
            // Loops through the arrayList of top 40 words
            for(int j = 0; j < topWords.size(); j++) {
               // If the frequency of the current index in the arrayList holding keyWords is larger than the frequency of the current index in top 40 words
               if(wordFreq > topWords.get(j).getFrequency()) {
                  // We replace the current index with the word from the arrayList holding the keyWords
                  topWords.set(j, wordAndFreq);
                  replaceTop = true;
               }
            }
            }
         }
         //  This makes it not skip the very first time.
         skipsFirstLine = false;
      }
      // A Class that sorts an arraylist in increasing order of frequencies
      Collections.sort(topWords);
      // Returns an array list of the top 40 words sorted
      return topWords;
   }
}

   
    

     
         
   
