/*
* File: LexicalAnalyzer.java
* Author: John Kucera
* Date: April 6, 2020
* Purpose: This Java class is meant to accompany Project1.java. It is a 
* lexical analyzer which takes the contents of the input file and tokenizes
* them, to be returned to the parser. tokenType method looks at the first
* character in a lexeme in the file and returns the according enumerated type
* that it must be.
*/

// import of necessary java classes
import java.io.*;

public class LexicalAnalyzer {
    
    // Initialize StreamTokenizer
    private StreamTokenizer tokenize;
    
    // Lexical Analyzer Constructor
    public LexicalAnalyzer(String file) throws FileNotFoundException {
        BufferedReader input = new BufferedReader(new FileReader(file));
        tokenize = new StreamTokenizer(input);
        tokenize.quoteChar('"');
        tokenize.ordinaryChar('.');
    } // end of method
    
    // Method that returns the appropriate enumerated-type token (to the parser)
    public EnumTokens getNextToken() throws IOException, FileSyntaxError {
        try {
            int token = tokenize.nextToken();
            switch (token) {
                case StreamTokenizer.TT_WORD: // returning full lexeme word
                    if (EnumTokens.NOT_FOUND == tokenType(this.getLexeme())) {
                        throw new FileSyntaxError(tokenize.toString());
                    }
                    else {
                        return tokenType(this.getLexeme());
                    }
                case StreamTokenizer.TT_NUMBER:
                    return EnumTokens.NUMBER;
                case '(':
                    return EnumTokens.L_PAREN;
                case ')':
                    return EnumTokens.R_PAREN;
                case '.':
                    return EnumTokens.PERIOD;
                case ',':
                    return EnumTokens.COMMA;
                case ':':
                    return EnumTokens.COLON;
                case ';':
                    return EnumTokens.SEMICOLON;
                case '"':
                    return EnumTokens.STRING;
                case StreamTokenizer.TT_EOF: // end of file
                    return EnumTokens.EOF;
            } // end of switch
        } // end of try
        catch (IOException ex) {
            System.out.println("IOException: File Did Not Open.");
        } // end of catch
        return EnumTokens.EOF;
    } // end of method
    
    // Getter method for returning a number as double 
    // (gets typecasted as integer in parser methods)
    public double getNumber() {
    	return tokenize.nval;
    } // end of method
    
    // Getter for returning lexeme word as a string
    public String getLexeme() {
    	return tokenize.sval;
    } // end of method
    
    // Method that takes the first character of a lexeme in the file and returns
    // according enumerated type
    private EnumTokens tokenType(String lexeme) {
        switch (lexeme.charAt(0)) {
            case 'W':
                if (lexeme.equals("Window")) {
                    return EnumTokens.WINDOW;
                }
            case 'E':
                if (lexeme.equals("End")) {
                    return EnumTokens.END;
                }
            case 'L': // lexeme that starts with L could be Layout or Label
                if (lexeme.equals("Layout")) {
                    return EnumTokens.LAYOUT;
                }
                else if (lexeme.equals("Label")) {
                    return EnumTokens.LABEL;
                }
            case 'F':
                if (lexeme.equals("Flow")) {
                    return EnumTokens.FLOW;
                }
            case 'G': // lexeme that starts with G could be Group or Grid
                if (lexeme.equals("Group")) {
                    return EnumTokens.GROUP;
                }
                else if (lexeme.equals("Grid")) {
                    return EnumTokens.GRID;
                }
            case 'B':
                if (lexeme.equals("Button")) {
                    return EnumTokens.BUTTON;
                }
            case 'R':
                if (lexeme.equals("Radio")) {
                    return EnumTokens.RADIO;
                }
            case 'P':
                if (lexeme.equals("Panel")) {
                    return EnumTokens.PANEL;
                }
            case 'T':
                if (lexeme.equals("Textfield")) {
                    return EnumTokens.TEXTFIELD;
                }
        } // end of switch
        return EnumTokens.NOT_FOUND;
    } // end of method
} // end of class
