package finalproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyMini {
	static int scale = 2;
	
	 private static void write(String string)
	 {
	  System.out.print(string + "\n");

	 }
	
	 private static String gradeString(int score, int maxScore, String comment)
	 {
	  return comment + "Score: " + Integer.toString(score * scale) + "/" + Integer.toString(maxScore * scale);
	 }
	 
	 private static int testGetResults0(int testIdx)  //Test the final result. Uses testAcyclic.xml
	 {
	  String comment = "[" + testIdx + "]: Test whether the search result are correct.\n";
	  int maxScore = 5;
	  int grade = 0;
	  try {
	   SearchEngine searchEngine = new SearchEngine("testAcyclic.xml");
	   searchEngine.crawlAndIndex("siteA");

	   searchEngine.assignPageRanks(0.01);
	   ArrayList<String> results = searchEngine.getResults("8258010535"); //try a word in the graph

	   if(results != null && results.get(0).equals("siteE")==true){ //Make sure the most relevent url is correct
	    grade=5;
	   }
	   else{
	    comment = comment + "Error: Did not return correct url.\n";
	          grade = 0;
	   }
	   write(gradeString(grade, maxScore, comment));
	  }
	  catch (Exception e) {
	   comment = comment + "Exception Found: " + e.toString() + "\n";
	   e.printStackTrace();
	   write(gradeString(0, maxScore, comment));
	  }
	  return grade;
	 }


	 
	 public static void main(String args[]) {

		 testGetResults0(5);
	 }
	
	

}
