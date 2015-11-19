import org.antlr.v4.runtime.*;
public class Acode {
	public String opcode = null;
	public String oprand1 = null;
	public String oprand2 = null;
	public Acode(String _opcode){
		opcode = _opcode;
	}
	public Acode(String _opcode, String _oprand1){
		opcode = _opcode;
		oprand1 = _oprand1;
	}
	public Acode(String _opcode, String _oprand1, String _oprand2){
		opcode = _opcode;
		oprand1 = _oprand1;
		oprand2 = _oprand2;
	}
	public void printCode(){
		if(oprand1 == null && oprand2 == null){
			System.out.printf("%s \n", opcode);
		}
		else if(oprand2 == null){
			System.out.printf("%s %s \n",opcode, oprand1);
		}
		else{
			System.out.printf("%s %s %s \n",opcode, oprand1, oprand2);
		}
	}
}