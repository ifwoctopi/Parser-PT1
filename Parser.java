import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Parser {

    private List<String> idents = new ArrayList<>();
    private List<String> keywords = new ArrayList<>(Arrays.asList("float", "while"));
    private String input;

    public Parser(String text) {
        this.input = text;
    }

    public Parser() {
        this.input = "";
    }

    // Adding on to the string that is to be parsed
    public String add(String word){
        if(this.input.isEmpty()) {
            this.input = this.input + word;
        }
        else{this.input = this.input + " " + word;}

        return this.input;
    }
    // check if the word matches the pattern of a valid identifier
    public boolean isIdent(String word) {
        return word.matches("[a-zA-Z_][a-zA-Z0-9_]*");
    }

    // check if the word is a keyword like 'float' or 'while'
    public boolean isKeyword(String word) {
        return word.contains("float") || word.contains("while");
    }

    // checks if the code has a valid while loop
    public boolean isLoop() {
        int whileIndex = input.indexOf("while");
        if (whileIndex == -1) return false; // no 'while' keyword found

        String whilePart = input.substring(whileIndex);
        int openParenIndex = whilePart.indexOf("(");
        int closeParenIndex = whilePart.indexOf(")");

        if (openParenIndex == -1 || closeParenIndex == -1) return false; // no parentheses found

        String condition = whilePart.substring(openParenIndex + 1, closeParenIndex).trim();
        String regex = "[a-zA-Z_][a-zA-Z0-9_]*\\s*[<>]=?\\s*[0-9]+";

        // check if the condition matches the expected pattern
        return condition.matches(regex);
    }

    // checks if the expression follows a valid identifier operator value format
    public boolean isExpr() {
        return input.matches("[a-zA-Z_][a-zA-Z0-9_]*\\s*[*/+-]\\s*[a-zA-Z0-9_]+");
    }

    // splits input into tokens and categorizes them
    public void lexicalAnalysis() {
        System.out.println("Lexical Analysis:");
        String[] tokens = input.split("\\s+|(?=[(){};,*=/])|(?<=[(){};,*=/])");
        for (String token : tokens) {
            if (token.isEmpty()) continue;
            if (isKeyword(token)){
                System.out.println(token + " -> KEYWORD");
                keywords.add(token);
            } else if (isIdent(token)) {
                System.out.println(token + " -> IDENTIFIER");
                idents.add(token);
            } else if (token.equals("(") || token.equals(")") || token.equals("{") || token.equals("}")) {
                System.out.println(token + " -> PARENTHESIS/BRACKET");
            } else if (token.equals(";")) {
                System.out.println(token + " -> SEMICOLON");
            } else if (token.equals(",")) {
                System.out.println(token + " -> COMMA");
            } else if (token.equals("=") || token.equals("*") || token.equals("/") || token.equals("+") || token.equals("-") || token.equals(">") || token.equals("<") || token.equals("=")) {
                System.out.println(token + " -> OPERATOR");
            } else {
                System.out.println(token + " -> UNKNOWN");
            }
        }
    }

    // performs the syntax checking using recursive descent parsing
    public boolean recursiveDescentParser() {
        // check if the function starts with 'float'
        if (!input.substring(0,7).contains("float ")) {
            System.out.println("Grammar Error: Function must start with 'float'.");
            return false;
        }

        String functionName = input.split("\\s+")[1];
        if (!isIdent(functionName)) {
            System.out.println("Grammar Error: Invalid function name.");
            return false;
        }

        // check for empty parentheses (no parameters allowed)
        if (input.contains("(") && input.contains(")") && !input.substring(input.indexOf("(") + 1, input.indexOf(")")).trim().isEmpty()) {
            System.out.println("Grammar Error: Function must not contain parameters.");
            return false;
        }

        // check for correct declaration syntax with 'float'
        if (!input.contains("float ")) {
            System.out.println("Grammar Error: Declarations must start with 'float'.");
            return false;
        }

        // validate the declaration syntax (expected 'float <identifier>;' format)
        if (!input.matches(".*float\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*;.*")) {
            System.out.println("Grammar Error: Invalid declaration syntax.");
            return false;
        }

        // check for assignment statements (must be in the form '<identifier> = <expr>;')
        if (!input.matches(".*[a-zA-Z_][a-zA-Z0-9_]*\\s*=\\s*.*\\s*;.*")) {
            System.out.println("Grammar Error: Invalid assignment statement.");
            return false;
        }

        // check if there's a while loop and validate its syntax
        if (input.contains("while")) {
            if (!isLoop()) {
                System.out.println("Grammar Error: Invalid loop syntax.");
                return false;
            }
        } else {
            System.out.println("Grammar Error: Missing while loop inside function.");
            return false;
        }

        // check that the function ends with a closing brace
        if (!input.endsWith("}")) {
            System.out.println("Grammar Error: Function must end with closing brace '}'");
            return false;
        }

        // if everything passes, the code is valid
        System.out.println("The Source Code is generated by the BNF grammar.");
        return true;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the filepath of the file you wish to access: ");
        String file_input = scanner.nextLine();

        //Removing quotes from the file path (in case user copies the file path directly)
        file_input = file_input.replace("\"", "");

        try {

            //Finding & Reading in the file
            File myObj = new File(file_input);
            Scanner printer = new Scanner(myObj);



            Parser parser = new Parser();
            System.out.println("You entered the following code:");
            String userInput;
            while(printer.hasNextLine()) {
                userInput = printer.nextLine();
                parser.add(userInput); //Adding the text to the string that is to be parsed
                System.out.println(userInput); //Printing the text in the file

            }

            parser.lexicalAnalysis();
            parser.recursiveDescentParser();
            printer.close();




            } catch(FileNotFoundException e){
                throw new RuntimeException(e);
            }
        }
    }

