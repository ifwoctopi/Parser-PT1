import java.util.*;

public class Parser {

    private List<String> idents = new ArrayList<>();
    private List<String> keywords = new ArrayList<>();
    private List<String> loops = new ArrayList<>();
    private List<String> expressions = new ArrayList<>();
    private String input;
    private int pos;



    public Parser(String text) {
        this.input = text;
        this.pos = 0;

    }

    public boolean isIdent(String word) {
        pos = 0;
        boolean result = false;
        if (word.isEmpty()) return false; // Empty input is invalid

        if (Character.isLowerCase(word.charAt(pos))) { // Check first letter
            pos++;


            while (pos < word.length()){
                if (Character.isLowerCase(input.charAt(pos))) {
                    pos++;

                }
                else {

                    return false;}
            }
            result = true; // Successfully parsed

        }

        else {return false;}

        return result; // Didn't match
    }
    public boolean isKeyword(String word){
        pos = 0;
        String keyword1 = "float";
        String keyword2 = "while";
        StringBuilder tempString = new StringBuilder();
        if(word.isEmpty()) return false;
        if (Character.isWhitespace(word.charAt(pos))) return false;
        if (Character.isUpperCase(word.charAt(pos))) return false;

        while (pos < word.length() && pos < keyword1.length() && pos < keyword2.length()){
            tempString.append(word.charAt(pos));
            pos++;
        }
        if(tempString.isEmpty()) return false;

        if(tempString.toString().equals(keyword1) | tempString.toString().equals(keyword2)) return true;

        return false;
    }

    public boolean isLoop(){
        pos = 0;
        boolean result = false;
        for(String keyword : keywords) {
            for(String ident : idents) {

                if (input.equals(keyword + " ( " + ident + " >= 10 )")) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public boolean isExpr(){
        pos = 0;
        boolean result = false;
        for(String ident : idents) {
            for (String ident2 : idents) {
                if (input.equals(ident + " / " + ident2) || input.equals(ident + " * " + ident2)) {
                    result = true;

                    break;
                } else {
                    for (String expr : expressions) {
                        if (input.equals(ident + " * " + expr) || input.equals(ident + " / " + expr)) {
                            result = true;
                            break;
                        }
                    }

                        }
                    }

                }
                return result;

        }



    public static void main(String[] args) {
        Dictionary<String, String[]> dict = new Hashtable<String, String[]>();
        String[] bnf_Keywords = {"float", "while"};
        dict.put("<Keyword>", bnf_Keywords);

        Parser parser = new Parser("n * m");
        String[] line = parser.input.split(" ");
        System.out.println(Arrays.toString(line));
        for (String s : line) {
            if (parser.isKeyword(s)) {
                System.out.println(s + " is keyword");
                parser.keywords.add(s);
            } else if (parser.isIdent(s)) {
                System.out.println(s + " is ident");
                parser.idents.add(s);
                // System.out.println(parser.ident.toString());
            }
            else {
                System.out.println(s + " is none");
            }

        }
        if(parser.isLoop()){ parser.loops.add(parser.input); }
        if(parser.isExpr()){
            parser.expressions.add(parser.input);
            System.out.println(parser.input + " is an expression"); }
        }


    }
