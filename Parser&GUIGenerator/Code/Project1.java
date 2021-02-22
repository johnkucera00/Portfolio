/*
* File: Project1.java
* Author: John Kucera
* Date: April 6, 2020
* Purpose: This Java program uses recursive descent to parse a GUI definition
* language defined in an input file. After parsing, it generates the GUI that
* the language defines. When parsing, ensures that the language in the input
* file is properly formatted (looking at tokens returned from
* LexicalAnalyzer.java), then applies the information to the GUI as components.
* This file is meant to be accompanied by EnumTokens.Java, FileSyntaxError.java,
* and LexicalAnalyzer.java.
*/

// import of necessary java classes
import java.io.*;
import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class Project1 {

    // Initializing variables
    private LexicalAnalyzer lexicalA;
    private static EnumTokens layer, token;
    private int heightGUI, widthGUI, numColumns, numRows, horizontalGap,
        verticalGap, widthTextfield = 0;

    // GUI components
    private JLabel label;
    private JFrame frame;
    private JPanel panel;
    private JButton button;
    private JTextField textfield;
    private ButtonGroup buttonGroup;
    private JRadioButton radioButton;

    // Method that parses through the GUI components, including frame title and
    // frame dimensions
    private boolean GUIparser() throws IOException, FileSyntaxError {

        // Window, setting as current layer
        if (token == EnumTokens.WINDOW) {
            frame = new JFrame();
            layer = EnumTokens.WINDOW;
            token = lexicalA.getNextToken();

            // String (title), setting window title
            if (token == EnumTokens.STRING) {
                frame.setTitle(lexicalA.getLexeme());
                token = lexicalA.getNextToken();

                // Left Parentheses
                if (token == EnumTokens.L_PAREN) {
                    token = lexicalA.getNextToken();

                    // Number (width), typecasting width as integer
                    if (token == EnumTokens.NUMBER) {
                        widthGUI = (int) lexicalA.getNumber();
                        token = lexicalA.getNextToken();

                        // Comma
                        if (token == EnumTokens.COMMA) {
                            token = lexicalA.getNextToken();

                            // Number (height), typecasting height as integer,
                            // setting frame size
                            if (token == EnumTokens.NUMBER) {
                                heightGUI = (int) lexicalA.getNumber();
                                frame.setSize(widthGUI, heightGUI);
                                token = lexicalA.getNextToken();

                                // Right parentheses
                                if (token == EnumTokens.R_PAREN) {
                                    token = lexicalA.getNextToken();

                                    // Layout type
                                    if (this.layoutParser()) {
                                        // Widgets
                                        if (this.getWidgets()) {
                                            // End
                                            if (token == EnumTokens.END) {
                                                token = lexicalA.getNextToken();

                                                // Period (end of file)
                                                if (token == EnumTokens.PERIOD) {
                                                    frame.setVisible(true);
                                                    frame.setLocationRelativeTo(null);
                                                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                                    frame.setAlwaysOnTop(true);
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } // end of outermost if
        return false;
    } // end of method

    // Method that parses through Layout components, including layout type
    // and components of a Grid layout (if Grid is selected)
    private boolean layoutParser() throws IOException, FileSyntaxError {

        // Layout
        if (token == EnumTokens.LAYOUT) {
            token = lexicalA.getNextToken();

            // Grid layout type
            if (token == EnumTokens.GRID) {
                token = lexicalA.getNextToken();

                // Left parentheses
                if (token == EnumTokens.L_PAREN) {
                    token = lexicalA.getNextToken();

                    // Number (rows), typecasting # of rows as integer
                    if (token == EnumTokens.NUMBER) {
                        numRows = (int) lexicalA.getNumber();
                        token = lexicalA.getNextToken();

                        // Comma
                        if (token == EnumTokens.COMMA) {
                            token = lexicalA.getNextToken();

                            // Number (columns), typecasting # of columns as integer
                            if (token == EnumTokens.NUMBER) {
                                numColumns = (int) lexicalA.getNumber();
                                token = lexicalA.getNextToken();

                                // Right parentheses (no gap information)
                                if (token == EnumTokens.R_PAREN) {
                                    token = lexicalA.getNextToken();

                                    // Colon
                                    if (token == EnumTokens.COLON) {
                                        // Setting grid layout rows and columns
                                        if (layer == EnumTokens.WINDOW) {
                                            frame.setLayout(new GridLayout(numRows,
                                                numColumns));
                                        }
                                        else if (layer == EnumTokens.PANEL) {
                                            panel.setLayout(new GridLayout(numRows,
                                                numColumns));
                                        }
                                        token = lexicalA.getNextToken();
                                        return true;
                                    }
                                } // end of if block for NOT HAVING gap information

                                // Comma (gap information to follow)
                                else if (token == EnumTokens.COMMA) {
                                    token = lexicalA.getNextToken();

                                    // Number (horizontal gap), typecasting as integer
                                    if (token == EnumTokens.NUMBER) {
					horizontalGap = (int) lexicalA.getNumber();
					token = lexicalA.getNextToken();

                                        // Comma
					if (token == EnumTokens.COMMA) {
                                            token = lexicalA.getNextToken();

                                            // Number (vertical gap), typecasting as integer
                                            if (token == EnumTokens.NUMBER) {
						verticalGap = (int) lexicalA.getNumber();
						token = lexicalA.getNextToken();

                                                // Right parentheses
						if (token == EnumTokens.R_PAREN) {
                                                    token = lexicalA.getNextToken();

                                                    // Colon
                                                    if (token == EnumTokens.COLON) {
                                                        // Setting grid layour rows,
                                                        // columns, gaps
							if (layer == EnumTokens.WINDOW) {
                                                            frame.setLayout(new
                                                                GridLayout(numRows,
                                                                    numColumns,
                                                                    horizontalGap,
                                                                    verticalGap));
							}
                                                        else if (layer == EnumTokens.PANEL) {
                                                            panel.setLayout(new
                                                                GridLayout(numRows,
                                                                    numColumns,
                                                                    horizontalGap,
                                                                    verticalGap));
							}
							token = lexicalA.getNextToken();
							return true;
                                                    }
						}
                                            }
                                        }
                                    }
                                } // end of else if block for HAVING gap information
                            }
                        }
                    }
                }
            } // end of if block for choosing GRID layout

            // Flow layout type
            else if (token == EnumTokens.FLOW) {
                token = lexicalA.getNextToken();

                // Colon
                if (token == EnumTokens.COLON) {
                    // Applying flow layout
                    if (layer == EnumTokens.WINDOW) {
                        frame.setLayout(new FlowLayout());
                    }
                    else if (layer == EnumTokens.PANEL) {
                        panel.setLayout(new FlowLayout());
                    }
                    token = lexicalA.getNextToken();
                    return true;
                }
            } // end of else if block for choosing FLOW layout
        } // end of outermost if
        return false;
    } // end of method

    // Method that parses through Widgets components, including buttons,
    // labels, and textfield
    private boolean widgetsParser() throws IOException, FileSyntaxError {

        // Button
        if (token == EnumTokens.BUTTON) {
            token = lexicalA.getNextToken();

            // String (button name), set button name
            if (token == EnumTokens.STRING) {
		button = new JButton(lexicalA.getLexeme());
		token = lexicalA.getNextToken();

                // Semicolon
		if (token == EnumTokens.SEMICOLON) {
                    // Adding button to current layer
                    if (layer == EnumTokens.WINDOW) {
			frame.add(button);
                    }
                    else if (layer == EnumTokens.PANEL) {
			panel.add(button);
                    }
                    token = lexicalA.getNextToken();
                    return true;
		}
            }
        } // end of outermost if - BUTTON

        // Group
        else if (token == EnumTokens.GROUP) {
            buttonGroup = new ButtonGroup();
            token = lexicalA.getNextToken();

            // Radio buttons
            if (this.getRadioButtons()) {

                // End
		if (token == EnumTokens.END) {
                    token = lexicalA.getNextToken();
                    // Semicolon
                    if (token == EnumTokens.SEMICOLON) {
			token = lexicalA.getNextToken();
			return true;
                    }
		}
            }
        } // end of outermost else if - GROUP

        // Label
        else if (token == EnumTokens.LABEL) {
            token = lexicalA.getNextToken();

            // String (label text), set label text
            if (token == EnumTokens.STRING) {
		String labelText = lexicalA.getLexeme();
                label = new JLabel(labelText);
		label.setHorizontalAlignment(SwingConstants.CENTER);
                token = lexicalA.getNextToken();

                // Semicolon
		if (token == EnumTokens.SEMICOLON) {
                    // Adding label to current layer
                    if (layer == EnumTokens.WINDOW) {
			frame.add(label);
                    }
                    else if (layer == EnumTokens.PANEL) {
			panel.add(label);
                    }
                    token = lexicalA.getNextToken();
                    return true;
		}
            }
        } // end of outermost else if - LABEL

        // Panel
        else if (token == EnumTokens.PANEL) {
            // Adding Panel to current layer, switching layer to new panel
            if (layer == EnumTokens.WINDOW) {
                frame.add(panel = new JPanel());
            }
            else if (layer == EnumTokens.PANEL) {
		panel.add(panel = new JPanel());
            }
            layer = EnumTokens.PANEL;
            token = lexicalA.getNextToken();

            // Adding following Layout and Widget components to Panel
            if (this.layoutParser()) {
		if (this.getWidgets()) {
                    // End
                    if (token == EnumTokens.END) {
			token = lexicalA.getNextToken();
                        // Semicolon
			if (token == EnumTokens.SEMICOLON) {
                            token = lexicalA.getNextToken();
                            return true;
			}
                    }
		}
            }
        } // end of outermost else if - PANEL

        // Textfield
        else if (token == EnumTokens.TEXTFIELD) {
            token = lexicalA.getNextToken();

            // Number (width)
            if (token == EnumTokens.NUMBER) {
                widthTextfield = (int) lexicalA.getNumber();
                textfield = new JTextField(widthTextfield);
                token = lexicalA.getNextToken();

                // Semicolon
                if (token == EnumTokens.SEMICOLON) {
                    // Add Textfield to current layer
                    if (layer == EnumTokens.WINDOW) {
			frame.add(textfield);
                    }
                    else if (layer == EnumTokens.PANEL) {
			panel.add(textfield);
                    }
                    token = lexicalA.getNextToken();
                    return true;
		}
            }
        } // end of outermost else if - TEXTFIELD
        return false;
    } // end of method

    // Recursive method to help widgets parser
    private boolean getWidgets() throws IOException, FileSyntaxError {
        if (this.widgetsParser()) {
            if (this.getWidgets()) {
                return true;
            }
            return true;
        }
        else {
            return false;
        }
    } // end of method

    // Method that parses through Radio buttons labels
    private boolean radioButtonsParser() throws IOException, FileSyntaxError {

        // Radio
        if (token == EnumTokens.RADIO) {
            token = lexicalA.getNextToken();

            // String (radio button label), add label to button, add button
            // to group
            if (token == EnumTokens.STRING) {
                radioButton = new JRadioButton(lexicalA.getLexeme());
                buttonGroup.add(radioButton);
                token = lexicalA.getNextToken();

                // Semicolon
                if (token == EnumTokens.SEMICOLON) {
                    // Add radio button to current layer
                    if (layer == EnumTokens.WINDOW) {
			frame.add(radioButton);
                    }
                    else if (layer == EnumTokens.PANEL) {
			panel.add(radioButton);
                    }
                    token = lexicalA.getNextToken();
                    return true;
                }
            }
        } // end of outermost if
        return false;
    } // end of method

    // Recursive method to help radio buttons parser
    private boolean getRadioButtons() throws IOException, FileSyntaxError {
        if (this.radioButtonsParser()) {
            if (this.getRadioButtons()) {
                return true;
            }
            return true;
        }
        else {
            return false;
        }
    } // end of method

    // Constructor
    public Project1(LexicalAnalyzer lexicalA) {
        try {
            this.lexicalA = lexicalA;
            token = lexicalA.getNextToken();
            this.GUIparser();
        }
        catch (IOException ex) {
            System.out.println("IOException: Your file has failed to open. Please try again.");
        }
        catch (FileSyntaxError ex) {
            System.out.println("Your input file has a syntax error. Please fix it and try again.\n");
        }
    } // end of constructor method

    // main method
    public static void main(String[] args) throws IOException {
        // Reading file in
        String file = "";
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the name of your file: ");
        file = bReader.readLine();
        // Running file through lexical analyzer and parser, then constructing GUI
        LexicalAnalyzer lexicalA = new LexicalAnalyzer(file);
        new Project1(lexicalA);
    } // end of main method
} // end of class
