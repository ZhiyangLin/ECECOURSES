import org.antlr.v4.runtime.*;
public class Ircode {
	public String opcode;
	public Irnode oprand1;
	public Irnode oprand2;
	public Irnode result;
	public Ircode(String _opcode, Irnode _oprand1, Irnode _oprand2, Irnode _result){
		opcode = _opcode;
		oprand1 = _oprand1;
		oprand2 = _oprand2;
		result = _result;
	}
	public void printCode(){
		if(oprand1.value.equals("none") && oprand2.value.equals("none")){
			System.out.printf(";%s %s \n",opcode, result.value);
		}
		else if(oprand2.value.equals("none")){
			System.out.printf(";%s %s %s\n",opcode, oprand1.value, result.value);	
		}
		else{
			System.out.printf(";%s %s %s %s\n",opcode, oprand1.value, oprand2.value, result.value);
		}
	}

}