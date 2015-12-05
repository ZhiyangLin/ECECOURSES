import org.antlr.v4.runtime.*;
import java.util.*;

public class Ircode {
	public String opcode;
	public Irnode oprand1;
	public Irnode oprand2;
	public Irnode result;
	public boolean endofF = false;
	public int debug = 0;
	public ArrayList<Ircode> successors = new ArrayList<Ircode>();
	public ArrayList<Ircode> predecessors = new ArrayList<Ircode>();
	public LinkedHashSet<String> in = new LinkedHashSet<String>();
	public LinkedHashSet<String> out = new LinkedHashSet<String>();
	public LinkedHashSet<String> kill = new LinkedHashSet<String>();
	public LinkedHashSet<String> gen = new LinkedHashSet<String>();

	public Ircode(String _opcode, Irnode _oprand1, Irnode _oprand2, Irnode _result){
		opcode = _opcode;
		oprand1 = _oprand1;
		oprand2 = _oprand2;
		result = _result;
	}

	public void printCode(){
		if(oprand1.value.equals("none") && oprand2.value.equals("none") && result.value.equals("none")){
			System.out.println(";" + opcode);
		}
		else if(oprand1.value.equals("none") && oprand2.value.equals("none")){
			System.out.printf(";%s %s \n",opcode, result.value);
		}
		else if(oprand2.value.equals("none")){
			System.out.printf(";%s %s %s\n",opcode, oprand1.value, result.value);	
		}
		else{
			System.out.printf(";%s %s %s %s\n",opcode, oprand1.value, oprand2.value, result.value);
		}
		if(endofF == true){
			System.out.println();
		}
	}

	public void printGenKill(){
		if(oprand1.value.equals("none") && oprand2.value.equals("none") && result.value.equals("none")){
			System.out.println(";" + opcode);
		}
		else if(oprand1.value.equals("none") && oprand2.value.equals("none")){
			System.out.printf(";%s %s \n",opcode, result.value);
		}
		else if(oprand2.value.equals("none")){
			System.out.printf(";%s %s %s\n",opcode, oprand1.value, result.value);	
		}
		else{
			System.out.printf(";%s %s %s %s\n",opcode, oprand1.value, oprand2.value, result.value);
		}
		if(endofF == true){
			System.out.println();
		}
		System.out.printf("Gen: ");
		for(String s: gen){
			System.out.printf(s+" ");
		}
		System.out.println();
		System.out.printf("kill: ");
		for(String s: kill){
			System.out.printf(s+" ");
		}
		System.out.println();
	}

	public void deprintCode(){
		if(oprand1.value.equals("none") && oprand2.value.equals("none") && result.value.equals("none")){
			if(opcode.equals("END")){
				System.out.println("");
			}
			else{
				System.out.println(";" + opcode);
			}
		}
		else if(oprand1.value.equals("none") && oprand2.value.equals("none")){
			System.out.printf(";%s %s(%s) \n",opcode, result.value,result.gtype);
		}
		else if(oprand2.value.equals("none")){
			System.out.printf(";%s %s(%s) %s(%s)\n",opcode, oprand1.value,oprand1.gtype, result.value,result.gtype);	
		}
		else{
			System.out.printf(";%s %s(%s) %s(%s) %s(%s)\n",opcode, oprand1.value,oprand1.gtype, oprand2.value,oprand2.gtype, result.value,result.gtype);
		}
	}
	
}