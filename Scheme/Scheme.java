import Scheme.*;

import java.io.*;

class Scheme {
    public static void main(String[] args) {
		Interpreter i = new Interpreter();
		if(args.length > 0) {
        	SExpression e = i.interpret(readEntireFile(args[0]));
        	System.out.println(e.formattedString());
		}
		else {
			while(true) {
				System.out.print("scheme>> ");
				SExpression e = i.interpret(readSExpression());
				System.out.println(e.formattedString());
			}
		}
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

	public static String readSExpression() {
		try {
			String result = new String();
			result += (char)System.in.read();
			while(System.in.available() > 0) {
				result += (char)System.in.read();
			}
			return result;
		} catch (IOException e) {
			System.err.println("IOException.");
			return null;
		}
	}
}
