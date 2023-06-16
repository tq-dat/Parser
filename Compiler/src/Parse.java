import java.util.ArrayList;
import java.util.Stack;

public class Parse {
    public ParsingTable parsingTable;
    public ArrayList<Token> tokensList;

    public Parse(ParsingTable parsingTable, ArrayList<Token> tokensList) {
        this.parsingTable = parsingTable;
        this.tokensList = tokensList;
    }

    public Output run(Parse parse) {
        ArrayList<Tree> treeArrayList = new ArrayList<Tree>();
        Stack<Node> stack = new Stack<Node>();
        ArrayList<Node> nodeArrayList = new ArrayList<Node>();
        Output output = new Output(treeArrayList,nodeArrayList);

        stack.push(new Node("$"));
        stack.push(new Node("PROGRAM"));
        int count = 0;
        //tokensList.add(new Token("$","$"));

        while (!stack.isEmpty()){

            if (count == tokensList.size()){
                return output;
            }
            Node X = stack.peek();
            Token a = tokensList.get(count);


            if (isTerminal(X)) {

                if (X.string.equals(convert_a(a)) || X.string.equals("$")) {
                    treeArrayList.add(new Tree(X,new ArrayList<>()));
                    nodeArrayList.add(stack.pop());
                    count += 1;
                }else {
                    treeArrayList.add(new Tree(new Node("ERROR"),new ArrayList<Tree>()));
                    nodeArrayList.add(new Node("ERROR"));
                    return output;
                }
            }else {
                if (X.string.equals("ε")) {
                    treeArrayList.add(new Tree(X,new ArrayList<Tree>()));
                    nodeArrayList.add(stack.pop());
                    continue;
                }
                int index = takeIndex(a);
                if (Table_X_a_nonblank(X,index)){
                    ArrayList<Node> nodes = takeNode(X,index);
                    Tree tree = new Tree(X,new ArrayList<Tree>());
                    for (int i = 0 ; i < nodes.size(); i++){
                        tree.nodeChild.add(new Tree(nodes.get(i),new ArrayList<Tree>()));
                    }
                    treeArrayList.add(tree);
                    nodeArrayList.add(stack.pop());
                    for (int i = nodes.size() - 1; i >= 0; i--) {
                        stack.push(nodes.get(i));
                    }
                }else {
                    treeArrayList.add(new Tree(new Node("ERROR"),new ArrayList<Tree>()));
                    nodeArrayList.add(new Node("ERROR"));
                    return output;
                }
            }


        }
        return output;

    }

    public ArrayList<Node> takeNode (Node X, int index) {
        ArrayList<Node> nodes = new ArrayList<Node>();

        for (int i = 0; i < this.parsingTable.nonTerminalsArray.size(); i++){
            if (X.string.equals(this.parsingTable.nonTerminalsArray.get(i).states[0])) {
                String[] states = this.parsingTable.nonTerminalsArray.get(i).states;
                String s = states[index];
                String valNode = "";
                for (int j = 0; j < s.length() + 1; j++){
                    if (j == s.length()){
                        nodes.add(new Node(valNode));
                        valNode = "";
                        break;
                    }
                    if (s.charAt(j) == ' '){
                        nodes.add(new Node(valNode));
                        valNode = "";
                    }else {
                        valNode += s.charAt(j);
                    }

                }
                break;
            }
        }


        return nodes;
    }
    public boolean Table_X_a_nonblank(Node X, int index) {

        for (int i = 0; i < this.parsingTable.nonTerminalsArray.size(); i++){
            if (X.string.equals(this.parsingTable.nonTerminalsArray.get(i).states[0])) {
                String[] states = this.parsingTable.nonTerminalsArray.get(i).states;
                String s = states[index];
                if (s.equals("0")) return false;
                else return true;
            }
        }
        return false;
    }

    public String convert_a(Token a) {
        switch (a.type) {
            case "OP", "SE":
                String[] OS = {"+", "-", "*", "/", "<", "<=", ">", ">=", "==", "!=", "||", "&&", "!", "=", "{", "}", "(", ")", "[", "]", ";", ","};
                String[] convertOS = {"plus", "minus", "multiply", "divide", "less", "lessequal", "greater", "greaterequal", "same", "different"
                        , "or", "and", "not", "equal", "lbrackets", "rbrackets", "lparenthesis", "rparenthesis", "lsbrackets", "lsbrackets", "semicolon", "comma"};
                for (int i = 0; i < OS.length; i++) {
                    if (a.value.equals(OS[i])) {
                        return convertOS[i];
                    }
                }
                break;
            case "KW" :
                return a.value;
            case "id", "boolliteral", "stringliteral", "intliteral", "floatliteral" :
                return a.type;
        }
        return "error";

    }

    public boolean isTerminal(Node X) {
        //if (X.string.equals("ε")) return true;
        String[] terminal = this.parsingTable.terminal;
        for (int i = 0; i < terminal.length; i++){
            if (X.string.equals(terminal[i])){
                return true;
            }
        }

        return false;
    }

    public int takeIndex(Token a) {
        for (int i = 0; i < this.parsingTable.terminal.length; i++) {
            String s = convert_a(a);
            if (s.equals(this.parsingTable.terminal[i])){
                return i;
            }
        }
        return -1;
    }
}
