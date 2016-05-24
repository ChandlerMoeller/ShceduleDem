package com.cs115.shceduledem;

import org.apache.commons.collections15.Factory;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

/**
 * Created by brtsai on 5/14/16.
 * Parts taken/adapted from jung::TestEdmondsKarpMaxFlow.html
 */
public class NetworkMaker {
    private ArrayList<ScheduleElement> schedList;
    private HashMap<String,Integer> volunteers;
    private HashMap<String,Integer> tables;
    private int volunteerCount = 0;
    private int tableCount = 0;
    private DirectedGraph<Number,Number> graph;
    private Factory<Number> edgeFactory;

    NetworkMaker(){
    }

    NetworkMaker(ArrayList<ScheduleElement> newList){
        schedList = newList;
        volunteers = new HashMap<>();
        tables = new HashMap<>();
        graph = new DirectedSparseMultigraph<Number,Number>();
        edgeFactory = new Factory<Number>() {
            int count = 0;
            public Number create() {
                return count++;
            }
        };

        /** Loads volunteers with volunteer names, and a volunteer ID
         *  based on the current volunteer count.
         *  This volunteer ID will serve as that person's node ID
         *  in the network graph.
         */
        for (ScheduleElement ele: schedList
             ) {
            ArrayList<String> listOfPeople = ele.getArrayListOfPeople();
            for (String name: listOfPeople
                 ) {
                if(!volunteers.containsKey(name)){
                    volunteers.put(name,volunteerCount++);
                }
            }
        }

        /** Loads tabling times with table names, and a table ID
         *  based on the current table count added to the total
         *  volunteer count.
         *  This table ID will serve as that table's node ID
         *  in the network graph.
         */
        for (ScheduleElement ele: schedList
             ) {
            String name = ele.month + "::" + ele.day + "::" + ele.time;
            if(!tables.containsKey(name)){
                tables.put(name,volunteerCount+tableCount++);
            }
        }

    }

    public int getNodeIDFromVolunteerName(String name){
        return volunteers.get(name);
    }

    public int getNodeIDFromTableName(String name){
        return tables.get(name);
    }

}
