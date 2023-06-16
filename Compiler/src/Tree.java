import java.util.ArrayList;

public class Tree {
    public Node root;
    public ArrayList<Tree> nodeChild;

    public Tree(Node root, ArrayList<Tree> nodeChild){
        this.root = root;
        this.nodeChild = nodeChild;
    }


}
