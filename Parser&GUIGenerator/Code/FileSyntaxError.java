/*
* File: FileSyntaxError.java
* Author: John Kucera
* Date: April 6, 2020
* Purpose: This Java file is meant to accompany Project1.java. It is a user
* defined checked exception that handles situations where there is a syntax
* error in the GUI definition language defined in the input file. This 
* custom exception provides the location of the syntax error as output for the
* user to see.
*/

// Constructor
public class FileSyntaxError extends Exception {
    public FileSyntaxError (String error) {
        System.out.println("\nFileSyntaxError: File contains a syntax error.");
        System.out.println("ERROR LOCATION: " + error + "\n");
    }
} // end of class
