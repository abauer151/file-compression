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

        writeEncodings(counts, encodings, input, encryptedFileName);
    }

    /**
     * Private helper method that reads a File and maps the each character to the number
     * of times it occurred in the file
     * @param input
     * @return
     * @throws IOException
     */
    public static Map<Character, Integer> count(File input) throws IOException
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
     * Write the text file into encoding in the given output file
     * @param counts
     * @param encodings
     * @param input
     * @param filename name of new output file
     * @throws FileNotFoundException
     */
    private static void writeEncodings(Map<Character, Integer> counts, Map<Character, String> encodings, File input, String filename) throws FileNotFoundException, UnsupportedEncodingException
    {
        //Reads input file
        Scanner scanner = new Scanner(input);
        scanner.useDelimiter("");

        //Writes encodings to file
        PrintWriter out = new PrintWriter("/Users/aidanbauer/Desktop/" + filename + ".txt", "UTF-8");

        //String of binary encodings
        StringBuilder binary = new StringBuilder();

        //Read input file
        while(scanner.hasNext() || scanner.hasNextLine())
        {
            Character next = scanner.next().charAt(0);

            //Turn into binary using encodings
            binary.append(encodings.get(next));
        }

        //Writes character frequency information so the file can be decoded
        for(Map.Entry<Character, Integer> entry: counts.entrySet())
            out.write(entry.getKey() + "-x-" + entry.getValue() + "-x-");
        out.write("*I-x-");

        //Take 8 bits of binary at a time, that we encoded from the input file, and write to the
        // output file as the character representation of each byte to save space
        for(int i = 8; i <= binary.length(); i += 8)
            out.write((char)Integer.parseInt(binary.substring(i - 8, i), 2));

        //Write remainder of binary string as the last character
        out.write((char)Integer.parseInt("1" + binary.substring(binary.length() - (binary.length() % 8), binary.length()),2));

        out.close();
    }

    /**
     * Decodes a compressed file
     * @param binaryEncoding file to be decompressed
     * @param output location to decompress file
     * @throws FileNotFoundException
     */
    public static void decode(File binaryEncoding, File output) throws FileNotFoundException
    {
        //Reads compressed file
        Scanner scanner = new Scanner(binaryEncoding);
        scanner.useDelimiter("-x-");

        //Write uncompressed text to output
        PrintWriter out = new PrintWriter(output);

        //Uncompress into binary
        StringBuilder binary = new StringBuilder();

        //Read character frequency information
        Map<Character, Integer> counts = new HashMap<>();

        while(scanner.hasNext())
        {
            String next = scanner.next();

            //Two stars mark the end of frequency info
            if(next.equals("*I"))
                break;

            String nextnext = scanner.next();

            counts.put(next.charAt(0), Integer.parseInt(nextnext));
        }

        //Uses counts to generate encodings
        HuffmanTree huffmanTree = new HuffmanTree(counts);
        Map<Character, String> encodings = huffmanTree.mapEncodings();

        //Maps reverses encodings map
        Map<String, Character> decodings = new HashMap<>();
        for(Map.Entry<Character, String> entry : encodings.entrySet())
            decodings.put(entry.getValue(), entry.getKey());

        scanner.useDelimiter("");
        scanner.next();
        scanner.next();
        scanner.next();
        //Read compressed file
        while(scanner.hasNext() || scanner.hasNextLine())
        {
            //Read character by character
            Character next = scanner.next().charAt(0);

            //Last character
            if(!scanner.hasNext())
            {
                //Decompress last character in a different way
                binary.append(Integer.toBinaryString(next).substring(1));
                scanner.close();
                break;
            }

            //Uncompress character to binary
            String code = FileCompressor.byteify(Integer.toBinaryString(next));
            binary.append(code);
        }

        //Decode binary to original data
        for(int leftBound = 0, rightBound = 1; rightBound <= binary.length(); rightBound++)
        {
            if(decodings.containsKey(binary.substring(leftBound, rightBound)))
            {
                out.write(decodings.get(binary.substring(leftBound, rightBound)));
                leftBound = rightBound;
            }
        }
        out.close();
    }

    /**
     * Private helper that makes a byte of binary have a length of 8
     * if it does not already
     * @param code
     * @return
     */
    private static String byteify(String code)
    {
        while(code.length() != 8)
            code = "0" + code;
        return code;
    }
}
