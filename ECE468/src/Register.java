import org.antlr.v4.runtime.*;

public class Register {
    public String name;
    public boolean isDirty;
   	public boolean isFree;
    public Irnode var;
    public Register(int _num){
    	isDirty = false; 
    	isFree = true;
    	name = String.format("r%d", _num);
    	System.out.println(name);
    }
}
