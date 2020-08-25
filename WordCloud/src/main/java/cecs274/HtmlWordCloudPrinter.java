package cecs274;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * This class provides a way to create a simple web page representation
 * of a word cloud. This depends on the 
 * 
 * @author Alvaro Monge alvaro.monge@csulb.edu
 *
 */
public class HtmlWordCloudPrinter {

   /** Maximum font size */
   public static final int MAX_FONT_SIZE = 96;
   /** Minimum font size */
   public static final int MIN_FONT_SIZE = 14;

   private String pageTitle;
   private ArrayList<String> wordClouds;

   private final int minFrequency;
   private final int maxFrequency;

   /**
    * Initializes this HtmlWordCloudPrinter with appropriave values provided in parameters
    * @param pageTitle the title of the web page
    * @param minFrequency the minimum frequency of words in this HtmlWorldCloudPrinter
    * @param maxFrequency the maximum frequency of words in this HtmlWorldCloudPrinter
    * @throws IllegalArgumentException if the frequencies are negative values or pageTitle is empty or null
    */
   public HtmlWordCloudPrinter(String pageTitle, int minFrequency, int maxFrequency) throws IllegalArgumentException {
      if (pageTitle != null && pageTitle.length() > 0 && minFrequency > 0 && maxFrequency > minFrequency) {
         this.pageTitle = pageTitle;
         this.minFrequency = minFrequency;
         this.maxFrequency = maxFrequency;
         this.wordClouds = new ArrayList<>();
      } else {
         throw new IllegalArgumentException();
      }
   }

   /**
    * This method takes a word and its frequency and adds its HTML representation to the HtmlCloudPrinter
    * for printing later. The font size used in the word's HTML representation is proportional to its
    * frequency relative to the minimum and maximum frequencies possible.
    * @param word the word and frequency
    */
   public void addCloud(String word, int frequency) {
      float relativeRatio = (frequency - this.minFrequency) / (float) (this.maxFrequency - this.minFrequency);
      float fontSize = MAX_FONT_SIZE * relativeRatio + (1 - relativeRatio) * MIN_FONT_SIZE;

      String cloud = String.format("<span style=\"font-size:%.2fpx;\">%s</span>", fontSize, word);
      wordClouds.add(cloud);
   }


   /**
    * Prints the HTML representation of the word clouds to the named file.
    * 
    * @param fileName  is the name of the file to use (should end with .html)
    * @throws FileNotFoundException if the named file cannot be created
    */
   public void print(String fileName) throws FileNotFoundException {
      PrintWriter out = new PrintWriter(fileName);

      out.println("<!DOCTYPE html >");
      out.println("<html> <head>");
      out.println("<meta charset=\"UTF-8\">");
      out.printf("<title>%s</title>", this.pageTitle);
      out.println("</head>");

      out.println("<body>");
      out.printf("<h1>%s</h1>", this.pageTitle);
      out.println(startDiv());
      for (String wordCloud : this.wordClouds) {
         out.println(wordCloud);
      }
      out.println(endDiv());
      out.println("</body>");
      out.println("</html>");
      out.close();
   }

   /**
    * Creates and returns a String that starts an HTML div that is the container box of the word cloud
    * @return the start of a div box in HTML
    */
   private static String startDiv() {
      String divHeader = "<div style=\"width: 800px; background-color: rgb(250,250,250); ";
      divHeader += "border: 1px grey solid; text-align: center\">";

      return divHeader;
   }

   /**
    * Creates and returns a String that ends an HTML div that is the container box of the word cloud
    * @return the end of a div box in HTML
    */
   private static String endDiv() {
      return "</div>";
   }
}
