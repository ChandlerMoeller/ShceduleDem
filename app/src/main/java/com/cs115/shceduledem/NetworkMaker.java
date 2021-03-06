package com.cs115.shceduledem;

import android.util.Log;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.MapTransformer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.uci.ics.jung.algorithms.flows.EdmondsKarpMaxFlow;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * Created by brtsai on 5/14/16.
 * Some parts taken/adapted from jung::TestEdmondsKarpMaxFlow.html
 */
public class NetworkMaker {
    private ArrayList<ScheduleElement> schedList;
    private ArrayList<ScheduleElement> solutionList;
    private HashMap<String,Number> volunteers;
    private HashMap<Number,String> volunteersYellowBook;
    private HashMap<String,Number> tables;
    private HashMap<Number,String> tablesYellowBook;
    private int volunteerCount = 0;
    private int tableCount = 0;
    private DirectedGraph<Number,Number> graph;
    private Factory<Number> edgeFactory;
    private Map<Number, Number> edgeCapacityMap;
    private Map<Number, Number> edgeFlowMap;
    private Number source;
    private Number sink;
    private int volunteerQuota = 5;
    private DirectedGraph<Number,Number> flowGraph;
    private int maxFlow;

    NetworkMaker(){
    }

    NetworkMaker(ArrayList<ScheduleElement> newList, int givenquota){
        volunteerQuota = givenquota;
        schedList = newList;
        solutionList = new ArrayList<ScheduleElement>();
        volunteers = new HashMap<>();
        volunteersYellowBook = new HashMap<>();
        tables = new HashMap<>();
        tablesYellowBook = new HashMap<>();
        graph = new DirectedSparseMultigraph<Number,Number>();
        edgeFactory = new Factory<Number>() {
            int count = 0;
            public Number create() {
                return count++;
            }
        };
        graph = new DirectedSparseMultigraph<Number, Number>();
        edgeCapacityMap = new HashMap<Number, Number>();
        edgeFlowMap = new HashMap<Number, Number>();

        /** Loads volunteers with volunteer names, and a volunteer ID
         *  based on the current volunteer count.
         *  This volunteer ID will serve as that person's node ID
         *  in the network graph.
         *  A corresponding volunteer node is added to the graph
         */
        for (ScheduleElement ele: schedList
             ) {
            ArrayList<String> listOfPeople = ele.getArrayListOfPeople();
            for (String name: listOfPeople
                 ) {
                if(!volunteers.containsKey(name)){
                    int ID = volunteerCount++;
                    volunteers.put(name,ID);
                    volunteersYellowBook.put(ID,name);
                    graph.addVertex(ID);
                }
            }
        }

        /** Loads tabling times with table names, and a table ID
         *  based on the current table count added to the total
         *  volunteer count.
         *  This table ID will serve as that table's node ID
         *  in the network graph.
         *  A corresponding table node is added to the graph
         */
        for (ScheduleElement ele: schedList
             ) {
            String name = ele.getName();
            if(!tables.containsKey(name)){
                int ID = volunteerCount+tableCount++;
                tables.put(name,ID);
                tablesYellowBook.put(ID,name);
                graph.addVertex(ID);
                Log.d("AddingVertex", ele.toString() + " aka: " + name + "," + ID);
            }
        }

        source = new Integer(getSourceNodeID());
        sink = new Integer(getSinkNodeID());
        graph.addVertex(source);
        graph.addVertex(sink);

        /** Links the source to each volunteer node with capacity equal to
         *  the volunteerQuota
         */
        for (Map.Entry<String,Number> entry: volunteers.entrySet()
             ) {
            Number edge = edgeFactory.create();
            graph.addEdge(edge,source,entry.getValue(), EdgeType.DIRECTED);
            edgeCapacityMap.put(edge, volunteerQuota);
        }

        /** Links each table node to the sink with capacity equal to
         *  number of volunteers.
         */
        for (Map.Entry<String,Number> entry: tables.entrySet()
             ) {
            Number edge = edgeFactory.create();
            graph.addEdge(edge, entry.getValue(),sink,EdgeType.DIRECTED);
            edgeCapacityMap.put(edge,volunteerCount);
        }

        /** Links each volunteer node to each corresponding table node
         *  that they are able to table for with a pipe of capacity 1
         */
        for (ScheduleElement ele: schedList
             ) {
            for (String name: ele.getCanTableList()
                 ) {
                Number edge = edgeFactory.create();
                graph.addEdge(edge, volunteers.get(name), tables.get(ele.getName()),EdgeType.DIRECTED);
                edgeCapacityMap.put(edge,1);
            }
        }

        org.apache.commons.collections15.Transformer<Number,Number> ultron =
                MapTransformer.<Number,Number>getInstance(edgeCapacityMap);

        EdmondsKarpMaxFlow<Number,Number> ek =
                new EdmondsKarpMaxFlow<Number, Number>(
                        graph,
                        source,
                        sink,
                        ultron,
                        //MapTransformer.<Number, Number>getInstance(edgeCapacityMap),
                        edgeFlowMap,
                        edgeFactory);
        ek.evaluate();

        flowGraph = ek.getFlowGraph();
        maxFlow = ek.getMaxFlow();

        Log.d("maxFlow", new String(""+maxFlow));


        Collection<Number> myEdges = flowGraph.getEdges();

        /**
         *  Prints debug information about the max flow graph
         */

        /**
        for (Number e: myEdges
             ) {
            Collection<Number> incidents = flowGraph.getIncidentVertices(e);
            boolean flip = false;
            String connectors = "";
            for (Number v: incidents
                 ) {
                if(!flip){
                    connectors += nodeNameLookUpFromID(v);
                    flip = true;
                    if(edgeFlowMap.get(e).intValue()==1 && getVolunteerNameFromNodeID(v)!= null){
                        Log.d("findingFlows", "YADAAA");
                    }
                }else{
                    connectors += " to " + nodeNameLookUpFromID(v);
                }
            }
            Log.d("edge", "ID:"+e.toString() + " connects " + connectors + " with flow " + edgeFlowMap.get(e).intValue());
        }
        */




        /**
        Collection<Number> myVertices = flowGraph.getVertices();

        for (Number v: myVertices
             ) {

            Log.d("vertex", "ID: "+v.toString() + ", Name: " + nodeNameLookUpFromID(v.intValue()));

        }
         */


        /** Gets the assignment of volunteers to tables in the form
         *  of ScheduleElements
         */

        for (Map.Entry<String, Number> edge: tables.entrySet()
                ) {
            Number e = edge.getValue();
            ArrayList<String> assignedVolunteers = new ArrayList<String>();
            Log.d("TableNameTest", getTableNameFromNodeID(e));

            Collection<Number> incidentEdges = flowGraph.getIncidentEdges(e);

            boolean flip = false;
            for (Number dirEdge: incidentEdges
                 ) {
                // For some reason there are 6 incident edges, 5 linking person to table,
                // and 1 from table to sink. So flip/break is necessary
                if(flip)break;

                for (Number vertex: flowGraph.getIncidentVertices(dirEdge)
                     ) {
                    if(getVolunteerNameFromNodeID(vertex)!=null && edgeFlowMap.get(e).intValue() != 0){
                        //Log.d("test","SUPERYADAA!!! Assigned " +getVolunteerNameFromNodeID(vertex) + " to " + edge.getKey() + "with flow: " + edgeFlowMap.get(e).intValue());
                        assignedVolunteers.add(getVolunteerNameFromNodeID(vertex));

                        flip = true;
                    }
                }

            }

            ScheduleElement se = new ScheduleElement(getTableNameFromNodeID(e),assignedVolunteers);
            solutionList.add(se);
        }

        /**
        for (Map.Entry<String, Number> e: tables.entrySet()
             ) {


            Collection<Number> incidents = flowGraph.getIncidentEdges(e.getValue());
            ArrayList<String> assignedVolunteers = new ArrayList<String>();
            boolean flip = false;
            String connectors = "";
            for (Number v: incidents
                    ) {
                Log.d("NodeID:", v.toString());
                if(v.intValue() != sink.intValue()){
                    Log.d("MakingSolution", "pre-yay");
                    if(edgeFlowMap.get(v).intValue()!=0){
                        assignedVolunteers.add(getVolunteerNameFromNodeID(v));
                        Log.d("MakingSolution", "yay");
                    }
                }
                if(!flip){
                    connectors += nodeNameLookUpFromID(v);
                    flip = true;
                }else{
                    connectors += " to " + nodeNameLookUpFromID(v);
                }
            }
            Log.d("edgeTest", "ID:"+e.getValue().toString() + " connects " + connectors + " with flow " + edgeFlowMap.get(e.getValue()).intValue());

            ScheduleElement ele = new ScheduleElement(getVolunteerNameFromNodeID(e.getValue()), assignedVolunteers);
            solutionList.add(ele);

        }
        */
        for (ScheduleElement ele: solutionList
             ) {
            Log.d("Solution:", ele.toString());
        }

    }

    public ArrayList<ScheduleElement> getSolutionList(){
        return solutionList;
    }

    public String nodeNameLookUpFromID(Number id){

        if(getVolunteerNameFromNodeID(id) != null){
            return getVolunteerNameFromNodeID(id);
        }
        if(getTableNameFromNodeID(id) != null){
            return getTableNameFromNodeID(id);
        }
        if(id.intValue() == source.intValue()){
            return "source";
        }
        if(id.intValue() == sink.intValue()){
            return "sink";
        }

        return null;
    }

    public Number getNodeIDFromVolunteerName(String name){
        return volunteers.get(name);
    }

    public String getVolunteerNameFromNodeID(Number node){
        return volunteersYellowBook.get(node);
    }

    public Number getNodeIDFromTableName(String name){
        return tables.get(name);
    }

    public String getTableNameFromNodeID(Number node){
        return tablesYellowBook.get(node);
    }



    /** Precondition: The constructor has already read in
     *  volunteers and tables from the schedEles arraylist
     *  The schedEles was not empty (at least 1 volunteer
     *  and at least 1 table)
     */
    private int getSourceNodeID(){
        return volunteerCount+tableCount;
    }

    private int getSinkNodeID(){
        return getSourceNodeID()+1;
    }

}
