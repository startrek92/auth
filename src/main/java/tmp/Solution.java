package tmp;

import java.util.*;

class Event {
    String eventId;
    int productId;
    String timeStamp;

    public Event(String eId, int pId, String ts) {
        this.eventId = eId;
        this.productId = pId;
        this.timeStamp = ts;
    }
}

class Solution {
    public static void main(String[] args) {

        int n = 2;

        List<Event> eventList = new ArrayList<>();
        eventList.add(new Event("abc", 456, "2000"));
        eventList.add(new Event("def", 123, "2001"));
        eventList.add(new Event("ghi", 789, "2002"));
        eventList.add(new Event("jkl", 456, "2003"));
        eventList.add(new Event("mno", 123, "2004"));
        eventList.add(new Event("pqr", 456, "2005"));

        Map<Integer, Integer> map = new HashMap<>();

        for(Event event: eventList) {
            map.put(event.productId, map.getOrDefault(event.productId, 0) + 1);
        }

        PriorityQueue<List<Integer>> priorityQueue = new PriorityQueue<>();
        // count -> productId

        for(Integer key: map.keySet()) {
            List<Integer> ls = new ArrayList<>();
            ls.add(map.get(key));
            ls.add(key);

            priorityQueue.add(ls);
        }

        List<Integer> res = new ArrayList<>();

        while( n > 0) {
            if(priorityQueue.isEmpty()) {
                break;
            }

            List<Integer> top = priorityQueue.poll();
            res.add(top.get(1));
            n = n-1;
        }

        System.out.println(res);

    }
}