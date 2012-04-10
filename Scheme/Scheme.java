import Scheme.*;

import java.io.*;

class Scheme {
    public static void main(String[] args) {
        Interpreter i = new Interpreter();
        SExpression e = i.interpret(readEntireFile(args[0]));
        System.out.println(e.formattedString());
    }

    public static String readEntireFile(String filename) {
        try {
            FileInputStream f = new FileInputStream(filename);
            String result = new String();

            while(f.available() > 0) {
                result += (char)f.read();
            }
            return result;
        } catch (FileNotFoundException e) {
            System.err.println("File does not exist.");
            return null;
        } catch (IOException e) {
            System.err.println("IOException.");
            return null;
        }
    }
}