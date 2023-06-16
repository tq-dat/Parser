import java.util.ArrayList;

public class ParsingTable {
    public String[] terminal;
    public ArrayList<NonTerminal> nonTerminalsArray;

    public ParsingTable(String[] terminal, ArrayList<NonTerminal> nonTerminalsArray){
        this.terminal = terminal;
        this.nonTerminalsArray = nonTerminalsArray;
    }

    @Override
    public String toString() {
        String s ="";
        for (int i = 0; i < terminal.length; i++){
            s += terminal[i] + " ";
        }
        s += "\n";
        for (int i = 0; i < nonTerminalsArray.size(); i++){
            s += nonTerminalsArray.get(i).toString() + "\n";
        }

        return s;
    }
}
