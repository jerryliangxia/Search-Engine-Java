package finalproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry; // You may need it to implement fastSort

public class Sorting{ //<K extends Comparable<K>>

	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) <0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);					
				}
			}
        }
        return sortedUrls;                    
    }
    
    
	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    
    // mergesort implementation. create an arraylist, fill it up, then put it all into a normal K[] array
    // modified from <K,V extends Comparable> to <K extends Comparable<K>, V> for mergesort methods
    @SuppressWarnings("unchecked")
	public static <K extends Comparable<K>, V> ArrayList<K> fastSort(HashMap<K, V> results) {	// takes in a hashmap called results
    	ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls
        K[] sorted = (K[]) new Comparable[results.size()];
        int i = 0;
        for(K url : sortedUrls) {
        	sorted[i] = (K) url;
        	i++;
        }
        
        sorted = mergeSort(sorted);
        
        //put into an array list
        ArrayList<K> finalReturn = new ArrayList<K>();
        for(K url : sorted) {
        	finalReturn.add(url);
        }
        
    	// ADD YOUR CODE HERE
    	return finalReturn;
    }

    
    /* made with modifications from source: 
	 * https://www.youtube.com/watch?v=yv6svAfoYik&list=LL&index=1&ab_channel=JohnathonKwisses
	 */
	@SuppressWarnings("unchecked")
	private static <K extends Comparable<K>> K[] mergeSort(K[] list) {
    	if(list.length <= 1) return list;
    	
		int mid = (list.length)/2;
		
		
		K[] list1 = (K[]) new Comparable[mid];
		K[] list2;
		
		if(list.length % 2  == 0) {
			list2 = (K[]) new Comparable[mid];
		} else {
			list2 = (K[]) new Comparable[mid+1];
		}
		
		list1 = getElements1(list, list1, mid);
		list2 = getElements2(list, list2, mid);
		
		list1 = mergeSort(list1);
		list2 = mergeSort(list2);
		return merge(list1,list2);
    		
    }
	
	private static <K extends Comparable<K>> K[] getElements1(K[] list, K[] list1, int mid) {
		for(int i = 0; i < mid; i++) {	// creates arraylists
			list1[i] = list[i];
		}
		return list1;
	}
	
	private static <K extends Comparable<K>> K[] getElements2(K[] list, K[] list1, int start) {
		for(int i = 0; i < list1.length; i++) {	// creates arraylists
			list1[i] = list[start+i];
		}
		return list1;
	}
    
    private static <K extends Comparable<K>> K[] merge(K[] list1, K[] list2) {
    	@SuppressWarnings("unchecked")
		K[] list = (K[]) new Comparable[list1.length + list2.length];
    	int lptr = 0, rptr = 0, resptr = 0;
    	while(lptr <  list1.length || rptr < list2.length) {	// while one is not through yet
    		if(lptr <  list1.length && rptr < list2.length) {	// only while both still have pointers
				// TODO: compares on hashcodes, need to change because it sorts on decreasing order
    			if(list1[lptr].compareTo(list2[rptr]) >= 0) { // sorted on decreasing order, so this applies
    				list[resptr++] = list1[lptr++];
    			}
    			else {
    				list[resptr++] = list2[rptr++];
    			}
    		}
    		else if(lptr < list1.length) {
    			list[resptr++] = list1[lptr++];
    		}
    		
    		else if(rptr < list2.length) {
    			list[resptr++]=list2[rptr++];
    		}
    	}
    	return list;
    }
    

}