import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Lexer {
    public String source;

    public Lexer(String source) {
        this.source = source;
    }

    public ArrayList<Token> run(Lexer lexer){
        ArrayList<Token> tokens = new ArrayList<>();
        String token_now = "";
        String text = lexer.source;
        String[] OP = {"+", "-", "*", "/", "<", "<=", ">", ">=", "==", "!=", "||", "&&", "!", "="};
        String[] SE = {"{", "}", "(", ")", "[", "]", ";", ","};
        String[] KW = {"boolean", "break", "continue", "else", "for", "float" ,"if", "int", "return", "void","while"};
        Character[] end = {'{', '}', '(', ')', '[', ']', ';', ',','+', '-', '*', '/', '<', '>',  '|', '&', '!', '=', ' ', '\n', '\t'};


        Pattern pattern1 = Pattern.compile("\\w*");
        Pattern pattern2 = Pattern.compile("\\d*");

        int i = 0;

        while (i < text.length()){
            Character ch = text.charAt(i);

            if (token_now == ""){
                if (ch == ' ' || ch == '\t' || ch == '\n'){
                    i += 1;
                    continue;
                }
                if (ch == '\"'){
                    while (text.charAt(i+1) != '\"'){
                        token_now += text.charAt(i);
                        i += 1;
                    }
                    i += 1;
                    token_now += text.charAt(i);
                    tokens.add(new Token("stringliteral", token_now));
                    token_now = "";
                    i += 1;
                    continue;
                }
                token_now += ch;
                Matcher matcher1 = pattern1.matcher(token_now);
                if (!matcher1.matches()){

                    for (int x = 0; x < SE.length; x++){
                        if (SE[x].equals(token_now)){
                            tokens.add(new Token("SE", token_now));
                            i += 1;
                            token_now = "";
                            break;
                        }
                    }
                    for (int x = 0; x < OP.length; x++){
                        if (OP[x].equals(token_now)){
                            switch (token_now){
                                case "+", "-", "*": tokens.add(new Token("OP", token_now));  i += 1; break;
                                case "/":
                                    if (text.charAt(i+1) == '/') {
                                        while (text.charAt(i) != '\n') i += 1;
                                        break;
                                    }
                                    else if (text.charAt(i+1) == '*') {
                                        while (text.charAt(i) != '*' && text.charAt(i+1) != '/') i += 1;
                                        i += 2;
                                        break;
                                    }else tokens.add(new Token("OP", token_now)); break;
                                case "<",">","=","!":
                                    if (text.charAt(i+1) == '='){
                                        token_now += text.charAt(i+1);
                                        tokens.add(new Token("OP", token_now));
                                        i += 2;
                                        break;
                                    }else {
                                        tokens.add(new Token("OP", token_now));
                                        i += 1;
                                        break;
                                    }
                                case "|":
                                    if (text.charAt(i+1) == '|'){
                                        token_now += text.charAt(i+1);
                                        tokens.add(new Token("OP", token_now));
                                        i += 2;
                                        break;
                                    }else {
                                        tokens.add(new Token("OP", token_now));
                                        i += 1;
                                        break;
                                    }
                                case "&":
                                    if (text.charAt(i+1) == '&'){
                                        token_now += text.charAt(i+1);
                                        tokens.add(new Token("OP", token_now));
                                        i += 2;
                                        break;
                                    }else {
                                        tokens.add(new Token("OP", token_now));
                                        i += 1;
                                        break;
                                    }
                                case "_":
                                    i += 1;
                                    break;

                            }
                            token_now = "";
                            break;
                        }
                    }

                }else {
                    Matcher matcher2 = pattern2.matcher(token_now);
                    if (matcher2.matches()){
                        boolean done = false;
                        while (!done){
                            for (int x = 0; x < end.length; x++){
                                if (text.charAt(i+1) == end[x]){
                                    boolean haveDot = false;
                                    for (int n = 0; n < token_now.length(); n++){
                                        if (token_now.charAt(n) == '.'){
                                            tokens.add(new Token("floatliteral",token_now));
                                            haveDot =true;
                                            break;
                                        }
                                    }
                                    if (!haveDot) tokens.add(new Token("intliteral",token_now));
                                    done = true;
                                    break;
                                }
                            }
                            if (done){
                                i += 1;
                                token_now = "";
                                continue;
                            }else {
                                i+=1;
                                token_now += text.charAt(i);
                            }
                        }
                    }else {
                        Matcher matcher3 = pattern1.matcher(String.valueOf(text.charAt(i+1)));
                        if (!matcher3.matches()){
                            tokens.add(new Token("id" ,token_now));
                            token_now = "";
                            i += 1;
                        }else i += 1;

                    }
                }
            }else {
                token_now += ch;
                boolean done = false;
                for (int x = 0; x < KW.length; x++){
                    if (KW[x].equals(token_now)){
                        done = true;
                        Matcher matcher4 = pattern1.matcher(String.valueOf(text.charAt(i+1)));
                        if (!matcher4.matches()){
                            if (text.charAt(i+1) == '_'){
                                i += 1;
                            }else {
                                tokens.add(new Token("KW" ,token_now));
                                token_now = "";
                                i += 1;
                            }

                        }else i += 1;
                    }
                }
                if (!done){
                    Matcher matcher4 = pattern1.matcher(String.valueOf(text.charAt(i+1)));
                    if (!matcher4.matches()){
                        if (text.charAt(i+1) == '_'){
                            i += 1;
                        }else {
                            if (token_now.equals("true") || token_now.equals("false")){
                                tokens.add(new Token("boolliteral" ,token_now));
                            }else tokens.add(new Token("id" ,token_now));

                            token_now = "";
                            i += 1;
                        }
                    }else i += 1;
                }
            }

        }


        return tokens;
    }
}









