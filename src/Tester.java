import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Tester
{
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException
    {
//        String binary = "00010010100";
//        int num = Integer.parseInt(binary, 2);
//        System.out.println(binary);
//        System.out.println(Integer.toBinaryString(num));
        //System.out.println(FileCompressor.byteify(Integer.toBinaryString('\u0007')));

        PrintWriter writer = new PrintWriter("/Users/aidanbauer/Desktop/the-file-name.txt", "UTF-8");
        writer.write("The first line");
        writer.write("The second line");
        writer.close();
    }
}
