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
        StringBuilder binary = new StringBuilder();
        scanner.useDelimiter("");

        while(scanner.hasNext() || scanner.hasNextLine())
        {
            Character next = scanner.next().charAt(0);
            binary.append(encodings.get(next));
        }
        System.out.println(binary);

        for(int i = 8; i < binary.length(); i += 8)
        {
            out.write((char)Integer.parseInt(binary.substring(i - 8, i), 2));
        }

        out.close();
    }

    public static void decode(Map<Character, String> encodings, File binaryEncoding, File output) throws FileNotFoundException
    {
        Map<String, Character> decodings = new HashMap<>();

        for(Map.Entry<Character, String> entry : encodings.entrySet())
            decodings.put(entry.getValue(), entry.getKey());

        Scanner scanner = new Scanner(binaryEncoding);
        StringBuilder str = new StringBuilder();
        PrintWriter out = new PrintWriter(output);

        scanner.useDelimiter("");

        while(scanner.hasNext() || scanner.hasNextLine())
        {
            Character next = scanner.next().charAt(0);
            String binary = FileCompression.byteify(Integer.toBinaryString(next));
            str.append(binary);
        }
        System.out.println(str);
        scanner.close();

        int start = 0;
        for(int i = 1; i < str.length(); i++)
        {
            if(decodings.containsKey(str.substring(start, i)))
            {
                out.write(decodings.get(str.substring(start, i)));
                start = i;
                i++;
            }
        }
        out.close();
    }

    public static String byteify(String code)
    {
        String ret = code;
        while (ret.length() != 8)
            ret = "0" + ret;
        return ret;
    }
}
