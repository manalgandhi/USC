
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author manal
 */
public class homework
{
    public static void main(String[] args)
    {
        homework obj = new homework();
        try
        {
            obj.run("input.txt", "output.txt");
        }
        catch (IOException ex)
        {
            Logger.getLogger(homework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run(String inputFile, String outputFile) throws IOException
    {
        Input input = readFile(inputFile);

        StringBuilder outputBuilder = new StringBuilder();

        switch (input.algo)
        {
            case "BFS":
            case "bfs":
                NodeST goalNodeBFS = bfs(input.startState, input.goalState, input.graph);
                Stack<NodeST> stackBFS = new Stack();
                NodeST nodeBFS = goalNodeBFS;

                while (nodeBFS != null)
                {
                    stackBFS.push(nodeBFS);
                    nodeBFS = nodeBFS.parentNode;
                }

                while (stackBFS.empty() == Boolean.FALSE)
                {
                    NodeST popedNode = stackBFS.pop();
                    outputBuilder.append(popedNode.nodeName);
                    outputBuilder.append(" ");
                    outputBuilder.append(popedNode.depth);

                    if (!stackBFS.isEmpty())
                    {
                        outputBuilder.append("\r\n");
                    }
                }
                break;

            case "dfs":
            case "DFS":
                NodeST goalNodeDFS = dfs(input.startState, input.goalState, input.graph);
                Stack<NodeST> stackDFS = new Stack();
                NodeST nodeDFS = goalNodeDFS;

                while (nodeDFS != null)
                {
                    stackDFS.push(nodeDFS);
                    nodeDFS = nodeDFS.parentNode;
                }

                while (stackDFS.empty() == Boolean.FALSE)
                {
                    NodeST popedNode = stackDFS.pop();
                    outputBuilder.append(popedNode.nodeName);
                    outputBuilder.append(" ");
                    outputBuilder.append(popedNode.depth);

                    if (!stackDFS.isEmpty())
                    {
                        outputBuilder.append("\r\n");
                    }
                }
                break;

            case "ucs":
            case "UCS":
                NodeUCS goalNodeUCS = ucs(input.startState, input.goalState, input.graph);
                Stack<NodeUCS> stackUCS = new Stack();
                NodeUCS nodeUCS = goalNodeUCS;

                while (nodeUCS != null)
                {
                    stackUCS.push(nodeUCS);
                    nodeUCS = nodeUCS.parentNode;
                }

                while (stackUCS.empty() == Boolean.FALSE)
                {
                    NodeUCS popedNode = stackUCS.pop();
                    outputBuilder.append(popedNode.nodeName);
                    outputBuilder.append(" ");
                    outputBuilder.append(popedNode.pathCost);

                    if (!stackUCS.isEmpty())
                    {
                        outputBuilder.append("\r\n");
                    }
                }
                break;

            case "A*":
            case "a*":
                NodeA goalNodeA = astar(input.startState, input.goalState, input.graph, input.sundayTraffic);
                Stack<NodeA> stackA = new Stack();
                NodeA nodeA = goalNodeA;

                while (nodeA != null)
                {
                    stackA.push(nodeA);
                    nodeA = nodeA.parentNode;
                }

                while (stackA.empty() == Boolean.FALSE)
                {
                    NodeA popedNode = stackA.pop();
                    outputBuilder.append(popedNode.nodeName);
                    outputBuilder.append(" ");
                    outputBuilder.append(popedNode.pathCost);

                    if (!stackA.isEmpty())
                    {
                        outputBuilder.append("\r\n");
                    }
                }
                break;
        }

        writeToFile(outputFile, outputBuilder.toString());
    }

    private NodeST bfs(String startState, String goalState, HashMap<String, List<Edge>> graph)
    {
        HashSet<String> visitedNodes = new HashSet<>();
        LinkedList<NodeST> queue = new LinkedList<>();

        NodeST startNode = new NodeST(startState, 0, 0, null);
        queue.add(startNode);
        visitedNodes.add(startState);

        do
        {
            //printList(queue);
            if (queue.isEmpty())
            {
                break;
            }

            NodeST currentNode = queue.remove();

            if (currentNode.nodeName.equals(goalState))
            {
                return currentNode;
            }

            List<Edge> outgoingEdgeArr = graph.get(currentNode.nodeName);

            if (outgoingEdgeArr != null)
            {
                for (Edge edge : outgoingEdgeArr)
                {
                    String childNodeName = edge.endNode;
                    long traversalCost = edge.weight;

                    if (visitedNodes.contains(childNodeName))
                    {
                        continue;
                    }
                    else
                    {
                        NodeST newNode = new NodeST(
                                childNodeName,
                                currentNode.pathCost + traversalCost,
                                currentNode.depth + 1,
                                currentNode);

                        queue.add(newNode);
                        visitedNodes.add(childNodeName);
                    }
                }
            }
        } while (queue.size() > 0);

        return null;
    }

    private NodeST dfs(String startState, String goalState, HashMap<String, List<Edge>> graph)
    {
        HashSet<String> visitedNodes = new HashSet<>();
        Stack<NodeST> stack = new Stack<>();

        NodeST startNode = new NodeST(startState, 0, 0, null);
        stack.push(startNode);
        visitedNodes.add(startState);

        do
        {
            //printVector(stack);
            if (stack.isEmpty())
            {
                break;
            }

            NodeST currentNode = stack.pop();

            if (currentNode.nodeName.equals(goalState))
            {
                return currentNode;
            }

            List<Edge> outgoingEdgeArr = graph.get(currentNode.nodeName);

            if (outgoingEdgeArr != null)
            {
                for (int i = outgoingEdgeArr.size() - 1; i >= 0; i--)
                {
                    Edge edge = outgoingEdgeArr.get(i);
                    String childNodeName = edge.endNode;
                    long traversalCost = edge.weight;

                    NodeST newNode = new NodeST(
                            childNodeName,
                            currentNode.pathCost + traversalCost,
                            currentNode.depth + 1,
                            currentNode);

                    if (stack.contains(childNodeName))
                    {
                        NodeST nodeInStack = stack.get(stack.indexOf(childNodeName));
                        if (newNode.depth < nodeInStack.depth)
                        {
                            stack.remove(childNodeName);
                            stack.push(newNode);
                            visitedNodes.add(childNodeName);
                        }
                    }
                    else if (visitedNodes.contains(childNodeName))
                    {
                        continue;
                    }
                    else
                    {
                        stack.push(newNode);
                        visitedNodes.add(childNodeName);
                    }
                }
            }
        } while (stack.size() > 0);

        return null;
    }

    private NodeUCS ucs(String startState, String goalState, HashMap<String, List<Edge>> graph)
    {
        int nodeCounter = 0;
        ArrayList<NodeUCS> closedNodeList = new ArrayList<NodeUCS>();
        LinkedList<NodeUCS> queue = new LinkedList<NodeUCS>();

        NodeUCS startNode = new NodeUCS(startState, -1, 0, 0, null);
        queue.add(startNode);
        closedNodeList.add(startNode);

        do
        {
            //printList(queue);
            if (queue.isEmpty())
            {
                break;
            }

            NodeUCS currentNode = queue.remove();

            if (currentNode.nodeName.equals(goalState))
            {
                return currentNode;
            }

            List<Edge> outgoingEdgeArr = graph.get(currentNode.nodeName);

            if (outgoingEdgeArr != null)
            {
                for (Edge edge : outgoingEdgeArr)
                {
                    String childNodeName = edge.endNode;
                    long traversalCost = edge.weight;

                    NodeUCS childNode = new NodeUCS(
                            childNodeName,
                            nodeCounter++,
                            currentNode.pathCost + traversalCost,
                            currentNode.depth + 1,
                            currentNode);

                    int childLocationInQueue = containsUCS(queue, childNode);
                    int childLocationInClosed = containsUCS(closedNodeList, childNode);
                    if (childLocationInQueue == -1 && childLocationInClosed == -1)
                    {
                        queue.add(childNode);
                    }
                    else if (childLocationInQueue != -1)
                    {
                        if (childNode.pathCost < queue.get(childLocationInQueue).pathCost)
                        {
                            queue.remove(childLocationInQueue);
                            queue.add(childNode);
                        }
                        else if (childNode.pathCost == queue.get(childLocationInQueue).pathCost
                                && childNode.id < queue.get(childLocationInQueue).id)
                        {
                            queue.remove(childLocationInQueue);
                            queue.add(childNode);
                        }
                    }
                    else if (childLocationInClosed != -1)
                    {
                        if (childNode.pathCost < closedNodeList.get(childLocationInClosed).pathCost)
                        {
                            closedNodeList.remove(childLocationInClosed);
                            queue.add(childNode);
                        }
                        else if (childNode.pathCost == closedNodeList.get(childLocationInClosed).pathCost
                                && childNode.id < closedNodeList.get(childLocationInClosed).id)
                        {
                            closedNodeList.remove(childLocationInClosed);
                            queue.add(childNode);
                        }
                    }
                }
            }

            closedNodeList.add(currentNode);
            Collections.sort(queue, new UCSQueueCompare());
        } while (queue.size() > 0);

        return null;
    }

    private NodeA astar(String startState,
            String goalState,
            HashMap<String, List<Edge>> graph,
            HashMap<String, Long> heuristic)
    {
        int nodeCounter = 0;
        ArrayList<NodeA> closedNodeList = new ArrayList<>();
        LinkedList<NodeA> queue = new LinkedList<>();

        NodeA startNode = new NodeA(startState, -1, 0, heuristic.get(startState), 0, null);
        queue.add(startNode);
        closedNodeList.add(startNode);

        do
        {
            //printList(queue);
            if (queue.isEmpty())
            {
                break;
            }

            NodeA currentNode = queue.remove();

            if (currentNode.nodeName.equals(goalState))
            {
                return currentNode;
            }

            //System.out.println("A* - CurrentNode:" + currentNode.nodeName);

            List<Edge> outgoingEdgeArr = graph.get(currentNode.nodeName);

            if (outgoingEdgeArr != null)
            {
                for (Edge edge : outgoingEdgeArr)
                {
                    String childNodeName = edge.endNode;
                    long traversalCost = edge.weight;

                    NodeA childNode = new NodeA(
                            childNodeName,
                            nodeCounter++,
                            currentNode.pathCost + traversalCost,
                            currentNode.pathCost + traversalCost + heuristic.get(childNodeName),
                            currentNode.depth + 1,
                            currentNode);

                    int childLocationInQueue = containsA(queue, childNode);
                    int childLocationInClosed = containsA(closedNodeList, childNode);
                    if (childLocationInQueue == -1 && childLocationInClosed == -1)
                    {
                        queue.add(childNode);
                    }
                    else if (childLocationInQueue != -1)
                    {
                        if (childNode.fCost < queue.get(childLocationInQueue).fCost)
                        {
                            queue.remove(childLocationInQueue);
                            queue.add(childNode);
                        }
                        else if (childNode.fCost == queue.get(childLocationInQueue).fCost
                                && childNode.id < queue.get(childLocationInQueue).id)
                        {
                            queue.remove(childLocationInQueue);
                            queue.add(childNode);
                        }
                    }
                    else if (childLocationInClosed != -1)
                    {
                        if (childNode.fCost < closedNodeList.get(childLocationInClosed).fCost)
                        {
                            closedNodeList.remove(childLocationInClosed);
                            queue.add(childNode);
                        }
                        else if (childNode.fCost == closedNodeList.get(childLocationInClosed).fCost
                                && childNode.id < closedNodeList.get(childLocationInClosed).id)
                        {
                            closedNodeList.remove(childLocationInClosed);
                            queue.add(childNode);
                        }
                    }
                }
            }

            closedNodeList.add(currentNode);
            Collections.sort(queue, new AQueueCompare());
        } while (queue.size() > 0);

        return null;
    }

    private Input readFile(String path) throws FileNotFoundException, IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));

        String algo = reader.readLine().trim();
        String startState = reader.readLine().trim();
        String goalState = reader.readLine().trim();

        Input input = new Input(algo, startState, goalState);

        //read all the traffic lines here
        String numOfLinesStr = reader.readLine().trim();
        Long numOfLines = Long.valueOf(numOfLinesStr);
        for (int i = 0; i < numOfLines; i++)
        {
            String line = reader.readLine().trim();

            String[] data = line.split("\\s+");
            input.addEdge(data[0], data[1], Long.valueOf(data[2]));
        }

        //read all the sunday traffic lines here
        String numOfSunLinesStr = reader.readLine();
        if (numOfSunLinesStr != null)
        {
            numOfSunLinesStr = numOfSunLinesStr.trim();
            Long numOfSunLines = Long.valueOf(numOfSunLinesStr);
            for (int i = 0; i < numOfSunLines; i++)
            {
                String line = reader.readLine().trim();

                String data[] = line.split("\\s+");
                input.addHeuristic(data[0], Long.valueOf(data[1]));
            }
        }

        String testLine = reader.readLine();
        if (testLine != null)
        {
            System.err.println("Data left unread at the end of input file. Data read: " + testLine);
        }

        return input;
    }

    private void writeToFile(String outputPath, String output) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputPath)));
        writer.write(output);
        writer.flush();
        writer.close();
    }

    public class Input
    {
        //private int idCounter;
        private String algo;
        private String startState;
        private String goalState;

        private HashMap<String, List<Edge>> graph;
        private HashMap<String, Long> sundayTraffic;

        public Input(String algo, String startState, String goalState)
        {
            //idCounter = 0;
            this.algo = algo;
            this.startState = startState;
            this.goalState = goalState;

            graph = new HashMap<>();
            sundayTraffic = new HashMap<>();
        }

        public void addEdge(String startNode, String endNode, long distance)
        {
            //Edge edge = new Edge(idCounter, endNode, distance);
            //idCounter++;
            Edge edge = new Edge(endNode, distance);

            if (graph.containsKey(startNode))
            {
                graph.get(startNode).add(edge);
            }
            else
            {
                ArrayList<Edge> newList = new ArrayList<>();
                newList.add(edge);

                graph.put(startNode, newList);
            }
        }

        public void addHeuristic(String nodeName, Long value)
        {
            sundayTraffic.put(nodeName, value);
        }
    }

    public class Edge
    {
        //private int id;
        private String endNode;
        private long weight;

        /*public Edge(int id, String node, long weight)
        {
            //this.id = id;
            this.endNode = node;
            this.weight = weight;
        }
         */
        public Edge(String node, long weight)
        {
            this.endNode = node;
            this.weight = weight;
        }
    }

    public class NodeST
    {
        private String nodeName;
        private long pathCost;
        private int depth;

        private NodeST parentNode;

        public NodeST(String nodeName, long pathCost, int depth, NodeST parentNode)
        {
            this.nodeName = nodeName;
            this.pathCost = pathCost;
            this.depth = depth;
            this.parentNode = parentNode;
        }

        @Override
        public boolean equals(Object obj)
        {
            NodeST otherNode = (NodeST) obj;

            if (otherNode.nodeName.equals(this.nodeName))
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        @Override
        public String toString()
        {
            return nodeName;
        }
    }

    public class NodeUCS
    {
        private int id;
        private String nodeName;
        private long pathCost;
        private int depth;

        private NodeUCS parentNode;

        public NodeUCS(String nodeName, int id, long pathCost, int depth, NodeUCS parentNode)
        {
            this.id = id;
            this.nodeName = nodeName;
            this.pathCost = pathCost;
            this.depth = depth;
            this.parentNode = parentNode;
        }

        @Override
        public String toString()
        {
            return nodeName;
        }
    }

    public class NodeA
    {
        private int id;
        private String nodeName;
        private long pathCost;
        private long fCost;
        private int depth;

        private NodeA parentNode;

        public NodeA(String nodeName, int id, long pathCost, long fCost, int depth, NodeA parentNode)
        {
            this.id = id;
            this.nodeName = nodeName;
            this.pathCost = pathCost;
            this.fCost = fCost;
            this.depth = depth;
            this.parentNode = parentNode;
        }

        @Override
        public String toString()
        {
            return nodeName;
        }
    }

    private int containsUCS(List<NodeUCS> list, NodeUCS node)
    {
        for (int i = 0; i < list.size(); i++)
        {
            NodeUCS nodeFromList = list.get(i);

            if (node.nodeName.equals(nodeFromList.nodeName))
            {
                return i;
            }
        }

        return -1;
    }

    private int containsA(List<NodeA> list, NodeA node)
    {
        for (int i = 0; i < list.size(); i++)
        {
            NodeA nodeFromList = list.get(i);

            if (node.nodeName.equals(nodeFromList.nodeName))
            {
                return i;
            }
        }

        return -1;
    }

    class UCSQueueCompare implements Comparator<NodeUCS>
    {
        @Override
        public int compare(NodeUCS o1, NodeUCS o2)
        {
            if (o1.pathCost < o2.pathCost)
            {
                return -1;
            }
            else if (o1.pathCost == o2.pathCost)
            {
                if (o1.id < o2.id)
                {
                    return -1;
                }
                else
                {
                    return 1;
                }
            }
            else
            {
                return 1;
            }
        }
    }

    class AQueueCompare implements Comparator<NodeA>
    {
        @Override
        public int compare(NodeA o1, NodeA o2)
        {
            if (o1.fCost < o2.fCost)
            {
                return -1;
            }
            else if (o1.fCost == o2.fCost)
            {
                if (o1.id < o2.id)
                {
                    return -1;
                }
                else
                {
                    return 1;
                }
            }
            else
            {
                return 1;
            }
        }
    }

    public <T> void printVector(Vector<T> v)
    {
        Iterator<T> itr = v.iterator();

        System.out.println("Queuing function - " + v.size());
        while (itr.hasNext())
        {
            System.out.print(itr.next().toString());
        }
        System.out.println();
    }
    
    public <T> void printList(List<T> list)
    {
        Iterator<T> itr = list.iterator();

        System.out.print("List function: [" );
        while (itr.hasNext())
        {
            System.out.print(itr.next().toString()+", ");
        }
        System.out.println("]");
    }
}
