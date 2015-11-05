import org.antlr.v4.runtime.*;
public class Irnode {
        String value;
        String dtype;
        String gtype;
        public Irnode(String _gtype, String _dtype, String _value){
                gtype = _gtype;
                dtype = _dtype;
                value = _value;
        }
}
