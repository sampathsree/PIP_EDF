package realtime;

//import com.google.common.collect.Maps;
//import com.google.common.collect.Ordering;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * Java program to demonstrate how to sort Map in Java on key and values.
 * Map can be sort on keys or values.
 *
 * @author Javin Paul
 */
public class MapSortingExample {

  
    public static void main(String args[]) {
  
        //creating Hashtable for sorting
        Map<String, Integer> olympic2012 = new HashMap<String, Integer>();
      
        olympic2012.put("England", 3123);
        olympic2012.put("USA", 34231);
        olympic2012.put("China", 2);
        olympic2012.put("Russia", 2);
        //olympic2012.put("Australia", 4); //adding duplicate value
      
        //printing hashtable without sorting
        System.out.println("Unsorted Map in Java : " + olympic2012);
      
        //sorting Map e.g. HashMap, Hashtable by keys in Java
//        Map<String, Integer> sorted = sortByKeys(olympic2012);
//        System.out.println("Sorted Map in Java by key: " + sorted);
      
      
        //sorting Map like Hashtable and HashMap by values in Java
        Map<String, Integer> sorted = sortByValues(olympic2012);
        System.out.println("Sorted Map in Java by values: " + sorted.get(1));
        for ( String key : sorted.keySet() ) {
            System.out.println( key );
        }
    	String[] task_lock = new String[2];
    	System.out.println(task_lock.length);
    	
    	for (int res_num = 0; res_num < 1; res_num++) {
    	System.out.println("res---"+res_num);
    	}
    	
    	
    }
  
    /*
     * Paramterized method to sort Map e.g. HashMap or Hashtable in Java
     * throw NullPointerException if Map contains null key
     */
    public static <K extends Comparable,V extends Comparable> Map<K,V> sortByKeys(Map<K,V> map){
        List<K> keys = new LinkedList<K>(map.keySet());
        Collections.sort(keys);
      
        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
        for(K key: keys){
            sortedMap.put(key, map.get(key));
        }
      
        return sortedMap;
    }
  
    /*
     * Java method to sort Map in Java by value e.g. HashMap or Hashtable
     * throw NullPointerException if Map contains null values
     * It also sort values even if they are duplicates
     */
    public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());
      
        Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

            @Override
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
      
        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
      
        for(Map.Entry<K,V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
      
        return sortedMap;
    }

}

