import org.antlr.v4.runtime.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;

public class Scope {
        String name;
        Scope parent;
        HashMap<String, Symbol> table = new HashMap<String, Symbol>();
        ArrayList<String> keys = new ArrayList<String>();
        //Stack<Irnode> irnode = new Stack<Irnode>();
        public Scope(String _name)
        {
            name = _name;
        } 
        public Scope(String _name, Scope _parent)
        {
            name = _name;
            parent = _parent;
        }

}
