public class Tester
{
    public static void main(String[] args)
    {
        System.out.println((char)Integer.parseInt("0000111", 2));
        System.out.println(FileCompressor.byteify(Integer.toBinaryString('\u0007')));

    }
}
