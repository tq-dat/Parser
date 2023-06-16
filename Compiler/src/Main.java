import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    public static void makeTree (Tree p, ArrayList<Tree> list) {

        for (int i = 0; i < list.size();i++){
            if (list.get(i).nodeChild.size() == 0) {

                return;
            }

            if (!list.get(i).root.string.equals("0")){
                p.root = list.get(i).root;
                p.nodeChild = list.get(i).nodeChild;
                list.get(i).root.string = "0";
                for (int j = 0; j < p.nodeChild.size(); j++) {
                    makeTree(p.nodeChild.get(j),list);
                }
                break;
            }
        }


    }

    public static void printTree(Tree tree){
        System.out.println(tree.root.string);
        for (int i = 0;i < tree.nodeChild.size(); i++) {
            printTree(tree.nodeChild.get(i));
        }

    }


    public static ParsingTable readParsingTable(String url) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(url);
        Scanner scanner = new Scanner(fileInputStream);
        String s = scanner.nextLine();
        String x = "";
        String[] terminal = new String[40];
        int count = 0;
        for (int i = 0; i < s.length() + 1; i++){
            if (i == s.length()){
                terminal[count] = x;
                break;
            }
            if (s.charAt(i) != ';'){
                x += s.charAt(i);
            }else {
                terminal[count] = x;
                count += 1;
                x = "";
            }

        }

        //for (int i = 0 ; i< terminal.length; i++){
        //    System.out.println(terminal[i]);
        //}

        s = "";
        x = "";
        count = 0;
        ArrayList<NonTerminal> nonTerminalsArray = new ArrayList<>();
        while (scanner.hasNextLine()) {
            s = scanner.nextLine();
            String[] states = new String[40];
            for (int i = 0; i < s.length() + 1; i++){
                if (i == s.length()){
                    states[count] = x;
                    x = "";
                    count = 0;
                    break;
                }
                if (s.charAt(i) != ';'){
                    x += s.charAt(i);
                }else {
                    states[count] = x;
                    count += 1;
                    x = "";
                }
            }
            NonTerminal nonTerminal = new NonTerminal(states);
            nonTerminalsArray.add(nonTerminal);
        }

        //for (int i = 0; i < nonTerminalsArray.size(); i ++ ) {
        //    System.out.println(nonTerminalsArray.get(i).toString() + '\n');
        //}

        ParsingTable parsingTable = new ParsingTable(terminal,nonTerminalsArray);
        //System.out.println(parsingTable.toString());
        scanner.close();
        return parsingTable;
    }
    public static ArrayList<Token> scanCode (String url) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(url);
        Scanner scanner = new Scanner(fileInputStream);
        ArrayList<Token> tokens = new ArrayList<Token>();
        String data = "";
        while (scanner.hasNextLine()){
            data += scanner.nextLine();
        }

        Lexer lexer = new Lexer(data);
        tokens = lexer.run(lexer);
        scanner.close();
        return tokens;


    }


    public static void main(String args[]) throws FileNotFoundException {

        String url_parsing_table = "D:\\CompiletBTL\\Compiler\\Data\\parsingTable_4.txt";
        String url_lexer = "D:\\CompiletBTL\\Compiler\\Data\\sample_pscomp.vc";
        ParsingTable parsingTable = readParsingTable(url_parsing_table);
        ArrayList<Token> tokensList = scanCode(url_lexer);


        Parse parse = new Parse(parsingTable, tokensList);
        Output output = parse.run(parse);
        //ArrayList<Tree> treeArrayList = output.treeArrayList;

        //Tree tree = new Tree(treeArrayList.get(0).root,treeArrayList.get(0).nodeChild);
        //makeTree(tree,treeArrayList);
        //printTree(tree);


        for (int i = 0;i<output.treeArrayList.size();i++){
            if (output.nodeArrayList.get(i).string.equals(output.treeArrayList.get(i).root.string)) System.out.println(output.nodeArrayList.get(i));
            else System.out.println(-1);
        }





    }
}