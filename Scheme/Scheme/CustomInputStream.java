// Zeal Jagannatha

package Scheme;

// For Stack
import java.util.*;

// For InputStream
import java.io.*;

// Based loosly on C++ input streams
// Allows putback and peek, as well as readInt and readChar
public class CustomInputStream {

    // A Stack of the putback'd characters
    private Stack<Integer> savedChars = new Stack<Integer>();

    private InputStream iStream = null;

    public CustomInputStream() {
        iStream = System.in;
    }

    public CustomInputStream(InputStream initStream) {
        iStream = initStream;
    }

    public Integer readInteger() throws IOException {
        char c = readChar();
        String toParse = new String("");
        while(c >= '0' && c <= '9') {
            toParse += c;
        }
        putback(c);
        return Integer.valueOf(toParse);
    }

    public int readInt() throws IOException {
        char c = readChar();
        String toParse = new String("");
        while(c >= '0' && c <= '9') {
            toParse += c;
        }
        putback(c);
        return Integer.parseInt(toParse);
    }

    public char readChar() throws IOException {
        if(savedChars.empty()) {
            return (char)(int)iStream.read();
        }
        else
            return (char)(int)savedChars.pop();
    }

    public void putback(char c) {
        // Don't like doing this, but it's all I could figure out how to do...
        savedChars.push((int)c);
    }

    public Integer peek() throws IOException {
        if(savedChars.empty()) {
            savedChars.push(iStream.read());
        }
        return savedChars.peek();
    }
}