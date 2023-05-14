import java.io.*;
import java.util.Map;


public class Main
{
    public static void main(String[] args) throws IOException
    {
        Map<Character, Integer> counts = Encode.count(new File("input.txt"));
        System.out.println(counts);
        HuffmanTree huffmanTree = new HuffmanTree(counts);
        System.out.println(huffmanTree.generateDot());
//
//        FileInputStream fis = new FileInputStream(new File("input.txt"));
//        System.out.println((char) fis.read());
    }
}
