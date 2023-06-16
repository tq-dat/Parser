public class NonTerminal {
    public String[] states;

    public NonTerminal( String[] states) {
        this.states = states;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0 ; i < 40; i++){
            s += states[i] + " ";
        }
        return  s;
    }
}
