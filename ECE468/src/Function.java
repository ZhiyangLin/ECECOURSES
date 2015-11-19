import org.antlr.v4.runtime.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;

public class Function {
    String name;
    String retType;
    int pn = 1;
    int ln = 1;
    HashMap<String, Symbol> paramTable = new HashMap<String, Symbol>();
    HashMap<String, Symbol> localTable = new HashMap<String, Symbol>();
    public Function(String _name ,String _retType)
    {
        name = _name;
        retType = _retType;
    }
}
