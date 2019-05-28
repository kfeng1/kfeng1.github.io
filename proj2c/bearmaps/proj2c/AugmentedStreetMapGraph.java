package bearmaps.proj2c;

import bearmaps.proj2ab.PointSet;
import bearmaps.proj2ab.Point;
import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.WeirdPointSet;
import edu.princeton.cs.algs4.TST;


import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 * @author Alan Yao, Josh Hug, Kylin Feng
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    private Map<Point, Node> nodeMap;
    private List<Point> roadVertices;
    private TST<Node> namesTrie;
    private HashMap <String, List<Node>> stringMap;
    private List<String> emptyStrings;
    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        namesTrie = new TST();
        roadVertices = new ArrayList<Point>();
        nodeMap = new HashMap<>();
        stringMap = new HashMap<>();
        //emptyStrings = new ArrayList<String>();
        List<Node> nodes = this.getNodes();
        for (Node node : nodes) {
            long id = node.id();
            if (neighbors(id).size() > 0) {
                Point p = new Point(node.lon(), node.lat());
                roadVertices.add(new Point(node.lon(), node.lat()));
                nodeMap.put(p, node);
            }
            if(node.name()!=null){
                String name = node.name();
                String cleansed = cleanString(name);
                /*if(cleansed.length() == 0){
                    emptyStrings.add(name);
                }
                else{*/
                if(cleansed.length()!=0)
                    namesTrie.put(cleansed, node);
                    if(stringMap.get(cleansed) == null){
                        List<Node> list = new ArrayList<>();
                        list.add(node);
                        stringMap.put(cleansed, list);

                    }
                    else{
                        stringMap.get(cleansed).add(node);

                    }
                }

            }

        }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     *
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {

        //only consider those with a certain thing
        PointSet closestPoints = new WeirdPointSet(roadVertices);
        Point p = closestPoints.nearest(lon, lat);
        return nodeMap.get(p).id();
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     *
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        prefix = cleanString(prefix);
        List<String> listString = new ArrayList<String>();
        for(String string: namesTrie.keysWithPrefix(prefix)){
            List<Node> list = stringMap.get(string);
            for(Node node: list){
                listString.add(node.name());
            }
        }
        return listString;


    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     *
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */


    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<Node> locations = stringMap.get(cleanString(locationName));
        for(Node node: locations){
            Map<String, Object> map = new HashMap<>();
            map.put("lat", node.lat());
            map.put("lon", node.lon());
            map.put("name", node.name());
            map.put("id", node.id());
            mapList.add(map);
        }
        return mapList;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    public static void main(String []args){
        System.out.println(cleanString("one two"));
        System.out.println(cleanString("23")+ "1");
    }


}
