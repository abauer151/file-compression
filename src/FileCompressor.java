import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Static class that contains methods used in file compression and decompression
 * @author Aidan Bauer
 */
public class FileCompressor
{
    /**
     * Encodes the input text in the input file
     * @param input
     * @param encryptedFileName
     * @throws IOException
     */
    public static void encode(File input, String encryptedFileName) throws IOException
    {
        //Count the occurrences of each character in text file
        Map<Character, Integer> counts = FileCompressor.count(input);

        //Build Huffman Tree from counts
        HuffmanTree huffmanTree = new HuffmanTree(counts);

        //Map each character to an encoding
        Map<Character, String> encodings = huffmanTree.mapEncodings();

        //Use Huffman Tree encodings to turn text file into binary
        File encrypted = new File("Desktop/" + encryptedFileName + ".txt");
        encrypted.createNewFile();
        writeEncodings(encodings, input, encrypted);

    }

    /**
     * Private helper method that reads a File and maps the each character to the number
     * of times it occurred in the file
     * @param input
     * @return
     * @throws IOException
     */
    private static Map<Character, Integer> count(File input) throws IOException
    {
        //Count occurrences of each character in file
        Map<Character, Integer> counts = new HashMap<>();

        Scanner scanner = new Scanner(input);
        scanner.useDelimiter("");

        //Scans entire file, character by character
        while(scanner.hasNext() || scanner.hasNextLine())
        {
            Character next = scanner.next().charAt(0);

            //Maps characters
            if(counts.containsKey(next))
                counts.put(next, counts.get(next) + 1);
            else
            {
                counts.put(next, 1);
            }
        }
        return counts;
    }

    /**
     * Write the text file into encoding in the given outpur file
     * @param encodings
     * @param input
     * @param output
     * @throws FileNotFoundException
     */
    public static void writeEncodings(Map<Character, String> encodings, File input, File output) throws FileNotFoundException
    {
        Scanner scanner = new Scanner(input);
        PrintWriter out = new PrintWriter(output);
        StringBuilder binary = new StringBuilder();
        scanner.useDelimiter("");

        //Read original file
        while(scanner.hasNext() || scanner.hasNextLine())
        {
            Character next = scanner.next().charAt(0);
            binary.append(encodings.get(next));
        }

        //Take 8 bits of binary at a time, that we encoded from the original file, and write to the
        // output file as the character representation to save space
        for(int i = 8; i < binary.length(); i += 8)
        {
            out.write((char)Integer.parseInt(binary.substring(i - 8, i), 2));
        }

        //Write remainder of binary string as regular binary
        out.write(binary.substring(binary.length() - (binary.length() % 8), binary.length() - 1));

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
            String binary = FileCompressor.byteify(Integer.toBinaryString(next));
            str.append(binary);
        }
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
        while(code.length() != 8)
            code = "0" + code;
        return code;
    }
}
