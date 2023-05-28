import java.io.*;
import java.util.Map;


public class Main
{
    public static void main(String[] args) throws IOException
    {
        File input = new File("short.txt");
        File compressed = new File("/Users/aidanbauer/Desktop/compressed.txt");
        File output = new File("output.txt");

        Map<Character, Integer> counts = FileCompressor.count(input);
        System.out.println(counts.toString());

        HuffmanTree tree = new HuffmanTree(input);
        //System.out.println(tree.generateDot());

        FileCompressor.encode(input, "compressed");
        FileCompressor.decode(counts, new File("/Users/aidanbauer/Desktop/compressed.txt"), output);
    }

}
