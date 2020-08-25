package cecs274;
/**
 * WordFrequency class used to represent a word and its current frequency. 
 * The class implements the Comparable interface
 * @author Gideon Essel gideon.essel@student.csulb.edu
 * @author Christian Lam christian.lam@student.csulb.edu
 */

public class WordFrequency implements Comparable <WordFrequency>  {

   private String wordString;
   private int wordFrequency; //Current word and its frequency

   /**
    * Initializes this WordFrequency with appropriate values provided in parameters
    * @param wordString string of words
    * @throws IllegalArgumentException if the intial frequency is a negative values or wordString is empty or null
    */
   public WordFrequency (String wordString)
   {
      if(wordString == null || wordString.isEmpty() || wordFrequency < 0)
      {
         throw new IllegalArgumentException();
      }

      this.wordString = wordString;
      
   }

   /**
    * Initializes this WordFrequency with appropriate values provided in parameters
    * @param wordString string of words
    * @param wordFrequency occurences of words
    * @throws IllegalArgumentException if the intial frequency is a negative values or wordString is empty or null
    */
   public WordFrequency (String wordString, int wordFrequency)
   {
      if(wordString == null || wordString.isEmpty() || wordFrequency < 0)
      {
         throw new IllegalArgumentException();
      }
      this.wordString = wordString;
      this.wordFrequency = wordFrequency;
   }
   

   /**
    * @return string value
    */
   public String getWord()
   {
      return wordString;
   }
   
   /**
    * @return frequency value
    */
   public int getFrequency()
   {
      return wordFrequency;
   }


   /**
    * @return incremented frequency value
    */
   public int incrementFrequency(){
     return wordFrequency +=1;
   }

   /**
    *@return String representation of WordFrequency object. Format: word : frequency
    */
   public String toString() {
      return(wordString + " : "+ Integer.toString(wordFrequency));
   }

   /**
    * @param other WordFrequency object
    * compareTo() compares frequencies objects by order then returns appropriate values
    * @return integer value based on conditions met
    *
    * CITATION: Big Java Early Objects page: 486 - 487
    * Source: <a href = "https://www.tutorialspoint.com/java/number_compareto.htm"> Tutorial </a>
    * The site gave me information on what information to return
    */
   @Override
   public int compareTo(WordFrequency other)
    {
      
      if(this.wordFrequency < other.wordFrequency)
      {
         return -1;
      }

      if(this.wordFrequency == other.wordFrequency)
      {
         return 0;
      }

      return 1;
   }

   /**
    * @param otherObject the title of the web page
    * equals() method is used to compare two strings
    * if the referenced string is equivalent to the object construction
    * it returns a truth value
    * CITATION: Big Java Early Objects page: 453
    */
   @Override
   public boolean equals(Object otherObject)
   {
      
      WordFrequency other = (WordFrequency) otherObject;
      if(this.equals(other))
      {
         return true;
      }

      else
      {
         return false;
      }
      
      
   }


   
}
