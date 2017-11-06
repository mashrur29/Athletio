package utility;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import general.Day;

/**
 * Created by kashob on 10/24/17.
 */

public class HashMapSort {
    Map<String , Integer> unsortedHashMap, sortedHashMap;
    public HashMapSort(HashMap<String , Integer> hashMap)
    {
        unsortedHashMap = hashMap;
    }
    public Map<String, Integer> doSort()
    {
        Map<String,Integer> sortedMap = new TreeMap<String, Integer>(new Comparator<String>()
                {

                    @Override
                    public int compare(String i1, String i2)
                    {
                        Day day1 = new Day(i1);
                        Day day2 = new Day(i2);
                        if(day1.year == day2.year)
                        {
                            if(day1.month == day2.month)
                            {
                                int bool= 0;
                                if(day1.day < day2.day)
                                    bool = 1;
                                else if(day1.day > day2.day)
                                    bool = -1;
                                return bool;
                            }
                            else
                            {
                                int bool= 0;
                                if(day1.month < day2.month)
                                    bool = 1;
                                else if(day1.month > day2.month)
                                    bool = -1;
                                return bool;
                            }
                        }
                        else
                        {
                            int bool= 0;
                            if(day1.year < day2.year)
                                bool = 1;
                            else if(day1.year > day2.year)
                                bool = -1;
                            return bool;
                        }
                       // return i1.compareTo(i2);
                    }
                }
                );
        sortedMap.putAll(unsortedHashMap);
        return sortedMap;
    }
}
