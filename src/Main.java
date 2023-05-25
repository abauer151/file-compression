import javax.swing.filechooser.FileSystemView;
import java.io.*;


public class Main
{
    public static void main(String[] args) throws IOException
    {
        //System.out.println((char)Integer.parseInt("hhh", 2));
//        byte b = (byte) 01100011;
//        char c = (char)b;
//        byte back = (byte)c;
        //System.out.println(Integer.toBinaryString('c'));
        File home = FileSystemView.getFileSystemView().getHomeDirectory();
        System.out.println(home.getAbsolutePath());

       // File in = new File("out.txt");
      //  FileCompressor.encode(in, "out");
//        File bin = new File("binary.txt");
//        File out = new File("output.txt");
//        Map<Character, Integer> counts = FileCompression.count(in);
//        HuffmanTree huffmanTree = new HuffmanTree(counts);
//        System.out.println(huffmanTree.generateDot());
//        Map<Character, String> map = huffmanTree.mapEncodings();
//        FileCompression.writeEncodings(map, in, bin);
//        FileCompression.decode(map, bin, out);



    }

}
