import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileCompression
{
    public static byte[] encode(File input) throws IOException
    {
        //Count the occurrences of each character in text file
        Map<Character, Integer> counts = FileCompression.count(input);

        //Build Huffman Tree from counts
        HuffmanTree huffmanTree = new HuffmanTree(counts);

        //Map each character to an encoding
        Map<Character, String> encodings = huffmanTree.mapEncodings();

        //Use Huffman Tree encodings to turn text file into binary


        return null;
    }

    public static Map<Character, Integer> count(File input) throws IOException
    {
        //count occurrences of each character in file
        Map<Character, Integer> counts = new HashMap<>();

        Scanner scanner = new Scanner(input);
        scanner.useDelimiter("");
        while(scanner.hasNext() || scanner.hasNextLine())
        {
            Character next = scanner.next().charAt(0);
            if(counts.containsKey(next))
                counts.put(next, counts.get(next) + 1);
            else
            {
                counts.put(next, 1);
            }
        }
        return counts;
    }

    public static void writeEncodings(Map<Character, String> encodings, File input, File output) throws FileNotFoundException
    {
        Scanner scanner = new Scanner(input);
        PrintWriter out = new PrintWriter(output);
        scanner.useDelimiter("");

        while(scanner.hasNext() || scanner.hasNextLine())
        {
            Character next = scanner.next().charAt(0);
            out.write(encodings.get(next));
        }
        out.close();
    }

    public static void decode(Map<Character, String> encodings, File binaryEncoding, File output) throws IOException
    {
        Map<String, Character> decodings = new HashMap<>();

        for(Map.Entry<Character, String> entry : encodings.entrySet())
            decodings.put(entry.getValue(), entry.getKey());

        Scanner scanner = new Scanner(binaryEncoding);
        StringBuilder str = new StringBuilder();
        ByteOutputStream out = new ByteOutputStream(output);

        scanner.useDelimiter("");

        while(scanner.hasNext() || scanner.hasNextLine())
        {
            Character next = scanner.next().charAt(0);
            str.append(next);
            if(decodings.containsKey(str.toString()))
            {
                out.write(decodings.get(str.toString()));
                str = new StringBuilder();
            }
        }
        scanner.close();
        out.close();
    }
}
