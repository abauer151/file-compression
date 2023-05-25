import java.util.*;

/**
 * Class representing a Huffman Tree used for Huffman Encoding
 * @author Aidan Bauer
 */
public class HuffmanTree
{
    //Root of the tree
    private InternalNode root;

    /**
     * Builds a Huffman Tree using a map of character frequency
     * @param counts
     */
    public HuffmanTree(Map<Character, Integer> counts)
    {
        buildHuffmanTree(counts);
    }

    /**
     * Comparator of Huffman Tree Nodes by frequency and tiebreaker is lexicographical order
     */
    public class HuffmanCmp implements Comparator<Node>
    {
        @Override
        public int compare(Node o1, Node o2)
        {
            if(o1.data != o2.data)
                return ((Integer)o1.data).compareTo(o2.data);
            LeafNode ln1 = o1.getLeftMostChild();
            LeafNode ln2 = o2.getLeftMostChild();

            return ln1.character.compareTo(ln2.character);
        }
    }

    /**
     * Using a map of character frequencies, builds a Huffman Tree
     * @param counts
     */
    public void buildHuffmanTree(Map<Character, Integer> counts)
    {
        Set<Map.Entry<Character, Integer>> set = counts.entrySet();
        PriorityQueue<Node> minPQ = new PriorityQueue<>(new HuffmanCmp());
        Iterator<Map.Entry<Character, Integer>> it = set.iterator();

        //Adds characters into priority queue
        while(it.hasNext())
        {
            var next = it.next();
            minPQ.add(new LeafNode(next.getKey(), next.getValue()));
        }

        //Empties priority queue to build tree
        while(!minPQ.isEmpty())
        {
            //New internal node with children as the next two items in the PQ
            InternalNode parent = new InternalNode(minPQ.remove(), minPQ.remove());

            //Connect to root of Huffman Tree and finish
            if(minPQ.size() == 0)
            {
                root = parent;
                return;
            }

            //Add new node back into PQ
            minPQ.add(parent);
        }
    }

    /**
     * Generates the DOT encoding of this graph as string, which can be
     * pasted into http://www.webgraphviz.com to produce a visualization.
     */
    public String generateDot()
    {
        StringBuilder dot = new StringBuilder("digraph d {\n");
        traverseDown(root, dot);

        return dot.toString() + "}";
    }

    /**
     * Recursive helper method for generateDot()
     * Traverses down and adds entries to dot string
     * @param node
     * @param dot
     */
    private void traverseDown(Node node, StringBuilder dot)
    {
        if(node.leftChild != null)
        {
            dot.append("\t\"" + node.toString() + "\" -> \"" + node.leftChild.toString() + "\"\n");
            traverseDown(node.leftChild, dot);
        }
        if(node.rightChild != null)
        {
            dot.append("\t\"" + node.toString() + "\" -> \"" + node.rightChild.toString() + "\"\n");
            traverseDown(node.rightChild, dot);
        }

    }

    /**
     *
     * @return binary encodings
     */
    public Map<Character, String> mapEncodings()
    {
        Map<Character, String> encodings = new HashMap<>();
        addEncodings(root, new StringBuilder(), encodings);
        return encodings;
    }

    /**
     * Recursive helper method for mapEncodings()
     *
     * @param start
     * @param soFar
     * @param encodings
     */
    private void addEncodings(Node start, StringBuilder soFar, Map<Character, String> encodings)
    {
        //If leafNode, add encoding and exit
        if(start instanceof LeafNode)
        {
            encodings.put(((LeafNode) start).character, soFar.toString());
            return;
        }

        //Traverse left, add a '0', traverse right, add a '1'
        StringBuilder left = new StringBuilder(soFar + "0");
        StringBuilder right = new StringBuilder(soFar + "1");
        addEncodings(start.leftChild, left, encodings);
        addEncodings(start.rightChild, right, encodings);
    }

    /**
     * Custom node class
     */
    public class Node
    {
        protected int data;
        protected Node leftChild;
        protected Node rightChild;

        public Node(int data)
        {
            this.data = data;
        }

        public Node(Node leftChild, Node rightChild, int data)
        {
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.data = data;
        }

        public LeafNode getLeftMostChild()
        {
            Node leftMost = this;
            while(leftMost.leftChild != null)
                leftMost = leftMost.leftChild;
            return (LeafNode)leftMost;
        }

        public String toString()
        {
            return "" + data;
        }
    }

    /**
     * Custom node class for nodes carrying character frequency information
     */
    public class LeafNode extends Node
    {
        private InternalNode parent;
        private Character character;

        public LeafNode(Character character, int frequency)
        {
            super(frequency);
            this.character = character;
        }

        public String toString()
        {
            if(character.equals('"'))
                return "'' " + data;
            if(character.equals('\n'))
                return "<line break> " + data;

            return character + " " +  data;
        }
    }

    /**
     * Custom node class for internal nodes carrying the data for the sum of the
     * data in its children
     */
    public class InternalNode extends Node
    {
        public InternalNode(Node leftChild, Node rightChild)
        {
            super(leftChild, rightChild, leftChild.data + rightChild.data);
        }
    }
}



