import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Main
{
    public static void main(String[] args) throws IOException
    {
        //System.out.println((char)Integer.parseInt("hhh", 2));
//        byte b = (byte) 01100011;
//        char c = (char)b;
//        byte back = (byte)c;
        //System.out.println(Integer.toBinaryString('c'));


        File in = new File("input.txt");
        File bin = new File("binary.txt");
        File out = new File("output.txt");
        Map<Character, Integer> counts = FileCompression.count(in);
        HuffmanTree huffmanTree = new HuffmanTree(counts);
        Map<Character, String> map = huffmanTree.mapEncodings();
        FileCompression.writeEncodings(map, in, bin);
        FileCompression.decode(map, bin, out);

    }

}
