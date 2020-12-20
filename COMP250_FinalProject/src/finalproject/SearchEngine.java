package finalproject;

import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String> > wordIndex;   // this will contain a set of pairs (String, LinkedList of Strings)	
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception{
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}
	
	/* 
	 * This does a graph traversal of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 * 
	 * 	This method will fit in about 30-50 lines (or less)
	 */
	public void crawlAndIndex(String url) throws Exception {
		// TODO : Add code here
		boolean added;
		added = internet.setVisited(url,true);
		if(added == false) {
			internet.addVertex(url);
			//wordIndex.put(url, parser.getContent(url));	// added (new). Every time a vertex is added, the word index is updated.
			internet.setVisited(url,true);
		}
		
		// 'do something' with it (add the vertex to the graph, add the words to wordIndex, add links as edges)
		ArrayList<String> urlLinks = parser.getLinks(url);
		ArrayList<String> words = parser.getContent(url);
		
		indexWords(words, url);
		indexEdges(urlLinks, url);
		
		// end of 'do something'
		
		for(String link : urlLinks) {
			if(!internet.getVisited(link)) {
				crawlAndIndex(link);
			}
		}
		
	}
	
	private void indexWords(ArrayList<String> words, String url) {
		for(String word : words) {
			word = word.toLowerCase();
			if(!wordIndex.containsKey(word)) {
				wordIndex.put(word,  new ArrayList<>());
				wordIndex.get(word).add(url);
			}
			else if(!wordIndex.get(word).contains(url)) wordIndex.get(word).add(url);
		}
	}
	
	private void indexEdges(ArrayList<String> urlLinks, String url) {
		for(String link : urlLinks) {
			if(internet.vertexList.get(link)==null)
			internet.addVertex(link);
			internet.addEdge(url, link);
		}
	}
	
		
	/* 
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex(). 
	 * To implement this method, refer to the algorithm described in the 
	 * assignment pdf. 
	 * 
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {
		// TODO : Add code here
		ArrayList<String> urls = new ArrayList<>();	// allocates all strings into an arraylist urls
		for(String url : internet.vertexList.keySet()) {	// TODO check if for:each works.
			urls.add(url);									// Puts all strings into urls arraylist based on current order
			internet.setPageRank(url, 1.0);					// Sets the page rank of each url in actual database internet to 1.0 (to start)
		}
		while(true) {	//while epsilon property not met
			ArrayList<Double> p2 = computeRanks(urls);	// create an arraylist of doubles returned from method computeRanks.
														// this will be used for testing the epsilon property
			int allDone = p2.size();
			int i = 0;	// for getting p2's values
			for(String url : internet.vertexList.keySet()) {	// for each url in the original internet database
				// check if the page rank is <= epsilon. If so, break from for:each and pretty much everything
				if(Math.abs(p2.get(i)-internet.getPageRank(url)) < epsilon) {	// in here "done" gets assigned its boolean value
					i++;
				}
				else break;	// to access p2's values
			}
			if(i/allDone==1) {
				int j = 0;	// for getting p2's values
				for(String url : internet.vertexList.keySet()) {	// TODO for:each loop (SHOULD LINE UP BUT CHECK)
					internet.setPageRank(url, p2.get(j));			// sets the page rank at a url to the vertex list
					j++;	// for p2's values
				}
				break;	// from everything
			
			}
			else {	// else, assign all of p2's into the original database
				int j = 0;	// for getting p2's values
				for(String url : internet.vertexList.keySet()) {	// TODO for:each loop (SHOULD LINE UP BUT CHECK)
					internet.setPageRank(url, p2.get(j));			// sets the page rank at a url to the vertex list
					j++;	// for p2's values
				}
			}
		}
		
	}

	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph 
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls. 
	 * Note that the double in the output list is matched to the url in the input list using 
	 * their position in the list.
	 */
	
	// this method gives back an arraylist with new page ranks
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		// TODO : Add code here
		ArrayList<Double> p2 = new ArrayList<>(vertices.size());	// creates the arraylist we intend to return with 0.0's

		int i = 0;	// for adding in p2's values
		for(String url : vertices) {	// first for loop goes through the whole arraylist of vertices
			double sum = 0.0;	// creating a sum equal to 0.0
			ArrayList<String> edgesInto = internet.getEdgesInto(url);
			for(String url1 : edgesInto) {	// second for loop
				sum += internet.getPageRank(url1)/internet.getOutDegree(url1)*0.5;
			}
			sum += 0.5;	// adds 0.5 to the sum
			p2.add(i,sum);	// adds to the arraylist of doubles, p2, we created
			i++;	// increments once added, then continues
		}
		return p2;	// p2 is returned

	}

	
	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 * 
	 * This method should take about 25 lines of code.
	 */
	public ArrayList<String> getResults(String query) {
		// TODO: Add code here
		ArrayList<Double> rankResults = new ArrayList<>();	// temp
		ArrayList<String> queryResults = new ArrayList<>();	// to be returned
		ArrayList<String> urls = wordIndex.get(query);		// to find words
		if(urls.size()==0) return urls;	// returns empty arraylist if nothing found
		else {
			// arraylist made to account for multiple values with the same key
			HashMap<Double, ArrayList<String>> results1 = new HashMap<>();
			
			for(String url : urls) {	// fills up results1 with all urls
				Double rank = internet.getPageRank(url);	// get the page rank of the url
				if(results1.containsKey(rank)) {	// if the key is contained in results1 already
					results1.get(rank).add(url);	// add the word to the arraylist
				}
				else {
					results1.put(rank, new ArrayList<String>());	// else create an arraylist with the string
					results1.get(rank).add(url);
				}
			}

			rankResults = Sorting.fastSort(results1);	// returns the ranks in order
			for(Double d : rankResults) {
				ArrayList<String> links = results1.get(d);
				for(String link : links) {
					queryResults.add(link);
				}
			}
		}
		return queryResults;
	}
}
