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
        public boolean equal(Irnode n){
	        if(!gtype.equals(n.gtype)){
	            return false;
	        }
	        else if(!dtype.equals(n.dtype)){
	            return false;
	        }
	        else if(!value.equals(n.value)){
	            return false;
	        }
	        else
	        {
	            return true;
	        }
         
    }
}
