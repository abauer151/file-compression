import java.io.*;
import java.util.Map;


public class Main
{
    public static void main(String[] args) throws IOException
    {
        Map<Character, Integer> counts = FileCompression.count(new File("input.txt"));
        System.out.println(counts);
        HuffmanTree huffmanTree = new HuffmanTree(counts);
        Map<Character, String> map = huffmanTree.mapEncodings();
        FileCompression.writeEncodings(map, new File("input.txt"), new File("binary.dat"));
        System.out.println(map.toString());
        System.out.println(huffmanTree.generateDot());
        FileCompression.decode(map, new File("binary.dat"), new File("output.txt"));

//
//        FileInputStream fis = new FileInputStream(new File("input.txt"));
//        System.out.println((char) fis.read());
    }
}
