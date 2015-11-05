    // Generated from Micro.g4 by ANTLR 4.5.1

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;
import java.util.List;
/**
 * This class provides an empty implementation of {@link MicroListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
public class CustomListener extends MicroBaseListener {
    private int regNo;
    private int rn = 0;
    private Stack<Scope> scopes;
    private Stack<Scope> p_scopes;
    private Stack<Irnode> irnode;
    private ArrayList<Ircode> ircode;
    private Stack<Irnode> incr_irnode;
    private Stack<Irnode> tnode;
    private ArrayList<Acode> acode;
    private HashMap<String, String> rtable;
    private Stack<String> labels;
    private Irnode none = new Irnode("none", "none", "none");
    private Irnode done = new Irnode("done", "done", "done");
    private int labelnum = 1;
    public boolean in_incr = false;
    public boolean enterfor = false;
    public boolean enterif = false;
    public CustomListener() {
        regNo = 1;
        scopes = new Stack<Scope>();
        p_scopes = new Stack<Scope>();
        ircode = new ArrayList<Ircode>();
        irnode = new Stack<Irnode>();
        tnode = new Stack<Irnode>();
        acode = new ArrayList<Acode>();
        rtable = new HashMap<String, String>();
        labels = new Stack<String>();
        incr_irnode = new Stack<Irnode>();
    }
    @Override public void enterProgram(MicroParser.ProgramContext ctx){
        Scope scope = new Scope("GLOBAL");
        scopes.push(scope);
        p_scopes.push(scope);
    }

    @Override public void exitProgram(MicroParser.ProgramContext ctx) {
        GenerateTiny();
        //scopes.pop();
        /*int num = 1;
        for (Scope scope : p_scopes)
        {
            if(scope.name.equals("BLOCK"))
            {
               System.out.println("Symbol table "+ scope.name+" "+num);
               num++;

            }
            else
        {
               System.out.println("Symbol table "+ scope.name);

            }

                for (String key : scope.keys) {
                        Symbol var = scope.table.get(key);
                    if (var.type.equals("STRING")) {
                        System.out.println("name " + key + " type " + var.type + " value " + var.value);
                    }
                    else{

                        System.out.println("name "+ key + " type " + var.type);
                        }
                }
                System.out.println("");

        }*/
        
            System.out.println(";IR code");
            for(Ircode c: ircode){
                c.printCode();
            }
            System.out.println(";tiny code");
            for(Acode ac: acode){
                ac.printCode();
            }
            System.out.println("sys halt");

        }
    @Override public void enterString_decl(MicroParser.String_declContext ctx) {
        String varType = "STRING";
        String varName =ctx.id().getText();
        String varValue = ctx.str().getText();
        Symbol var = new Symbol(varType, varValue);
        Scope top = scopes.peek();
        checkDeclError(top.table, varName);
        top.table.put(varName, var);
        top.keys.add(varName);
        Acode c = new Acode("str", varName, varValue);
        acode.add(c);
    }
    @Override public void exitString_decl(MicroParser.String_declContext ctx) {

    }

    @Override public void enterVar_decl(MicroParser.Var_declContext ctx) {

        String varNames= ctx.id_list().getText();
        String varType = ctx.var_type().getText();
        String varValue = "";
        String[] Names = varNames.split(",");
        Scope top = scopes.peek();
        int i;
        for (i=0;i<Names.length;i++){
            String name = Names[i];
            Symbol var = new Symbol(varType, varValue);
            checkDeclError(top.table, name);
            top.table.put(name, var);
            top.keys.add(name);
            Acode c = new Acode("var", name);
            acode.add(c);
        }
    }
    @Override public void enterFunc_decl(MicroParser.Func_declContext ctx) {
        String varName = ctx.id().getText();
        Scope scope = new Scope(varName);
        scopes.push(scope);
        p_scopes.push(scope);
    }

    @Override public void exitFunc_decl(MicroParser.Func_declContext ctx) {

    }

    @Override public void enterParam_decl(MicroParser.Param_declContext ctx){
        String varType = ctx.var_type().getText();
        String varName = ctx.id().getText();
        String varValue = "";
        Symbol var = new Symbol(varType, varValue);
        Scope scope = scopes.peek();
        checkDeclError(scope.table, varName);
        scope.table.put(varName, var);
        scope.keys.add(varName);
        Acode c = new Acode("var", varName);
        acode.add(c);

    }
    @Override public void exitParam_decl(MicroParser.Param_declContext ctx){

    }


    @Override public void enterIf_stmt(MicroParser.If_stmtContext ctx) {
        enterif = true;
        enterfor = false;

        Scope scope = new Scope("BLOCK");
        scopes.push(scope);
        p_scopes.push(scope);
        String label = "label" + labelnum;
        labels.push(label);
        labelnum++;
        
        label = "label" + labelnum;
        labels.push(label);
        labelnum++;
     }

    @Override public void exitIf_stmt(MicroParser.If_stmtContext ctx) {
        String label = labels.pop();
        Irnode in = new Irnode("none","none",label);
        Ircode c = new Ircode("LABEL",none,none,in);
        ircode.add(c);
    }
    @Override public void enterElse_part(MicroParser.Else_partContext ctx) {
        
        Scope scope = new Scope("BLOCK");
        //Irnode in = new Irnode("ELSE", "none", "ELSE");
        //irnode.push(in);
        scopes.push(scope);
        p_scopes.push(scope);
        String label1 = labels.pop();
        Irnode in1 = new Irnode("none","none",label1);
        Ircode c1 = new Ircode("JUMP",none,none,in1);
        ircode.add(c1);

        String label2 = labels.pop();
        Irnode in2 = new Irnode("none","none",label2);
        Ircode c2 = new Ircode("LABEL",none,none,in2);
        ircode.add(c2);
        labels.push(label1);
        
    }

    @Override public void exitElse_part(MicroParser.Else_partContext ctx) {
    }

    @Override public void enterCond(MicroParser.CondContext ctx){
        Irnode in = new Irnode("comp", "none", ctx.compop().getText());
        irnode.push(in);
    }
    @Override public void exitCond(MicroParser.CondContext ctx) { 
        while(!irnode.empty()){
            Irnode temp = irnode.pop();          
            if(temp.gtype.equals("op")){
                Irnode left = tnode.pop();
                Irnode right = tnode.pop();
                Generate3AC(temp, left, right);
            }
            else if(temp.gtype.equals("comp")){
                Irnode left = tnode.pop();
                //System.out.print(left.value);
                Irnode right = tnode.pop();
                genCond3ac(temp, left, right);
            }
            else{
                tnode.push(temp);
                //System.out.println("push");
            }
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterExpr(MicroParser.ExprContext ctx) { 
        //System.out.printf("(%s) ", ctx.getText());

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitExpr(MicroParser.ExprContext ctx) { }

    @Override public void enterCompop(MicroParser.CompopContext ctx) {
        //Irnode in = new Irnode("comp","none",ctx.getText());
        //irnode.push(in);
        //System.out.printf("(%s) ", ctx.getText());
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitCompop(MicroParser.CompopContext ctx) { }


    @Override public void enterFor_stmt(MicroParser.For_stmtContext ctx) {
        enterfor = true;
        enterif = false;
        Scope scope = new Scope("BLOCK");
        scopes.push(scope);
        p_scopes.push(scope);
        String label = "label" + labelnum;
        labels.push(label);
        labelnum++;
        

        label = "label" + labelnum;
        labels.push(label);
        labelnum++;
        

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitFor_stmt(MicroParser.For_stmtContext ctx) {
        String label1;
        String label2;
        while(!incr_irnode.empty() && incr_irnode.peek().value != "done"){
            irnode.push(incr_irnode.pop());
        }
        if(!incr_irnode.empty()){
            incr_irnode.pop();
        }
        while(!irnode.empty()){
            Irnode temp = irnode.pop();
            
            //System.out.println(temp.value);            
            if(temp.gtype.equals("op")){
                Irnode left = tnode.pop();
                Irnode right = tnode.pop();
                Generate3AC(temp, left, right);
            }
            else if(temp.gtype.equals(":=")){
                Irnode left = tnode.pop();
                //System.out.print(left.value);
                Irnode right = null;
                Generate3AC(temp, left, right);
            }
            else{
                tnode.push(temp);
            }
        }
        label1 = labels.pop();
        label2 = labels.pop();
        Irnode in = new Irnode("none","none",label2);
        Ircode c = new Ircode("JUMP", none, none, in);
        ircode.add(c);
        in = new Irnode("none","none",label1);
        c = new Ircode("LABEL", none, none, in);
        ircode.add(c);

    }
    @Override public void enterInit_stmt(MicroParser.Init_stmtContext ctx) {
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitInit_stmt(MicroParser.Init_stmtContext ctx) { 
        

        String label1;
        String label2;
        label1 = labels.pop();
        label2 = labels.pop();
        Irnode in = new Irnode("none","none",label2);
        Ircode c = new Ircode("LABEL", none, none, in);
        ircode.add(c);
        labels.push(label2);
        labels.push(label1);

    }
    @Override public void enterIncr_stmt(MicroParser.Incr_stmtContext ctx) { 
        in_incr = true;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitIncr_stmt(MicroParser.Incr_stmtContext ctx) { 
        in_incr = false;
    }


    @Override public void enterPrimary(MicroParser.PrimaryContext ctx) {
        if(ctx.id() != null)
        {
            Scope top = p_scopes.peek();
            Symbol var = top.table.get(ctx.id().getText());
            if(var == null){
                top = p_scopes.firstElement();
                var = top.table.get(ctx.id().getText());
            }

            Irnode in = new Irnode("id", var.type, ctx.id().getText());
            irnode.push(in);
        }
        if(ctx.INTLITERAL() != null)
        {
            Irnode in = new Irnode("INTLITERAL", "INT", ctx.INTLITERAL().getText());
            irnode.push(in);
        }
        if(ctx.FLOATLITERAL() != null)
        {
            Irnode in = new Irnode("FLOATLITERAL", "FLOAT", ctx.FLOATLITERAL().getText());
            irnode.push(in);
        }

    }
    @Override public void enterExpr_prefix(MicroParser.Expr_prefixContext ctx) {
        if(ctx.addop() != null){
            Irnode in = new Irnode("op", "none", ctx.addop().getText());
            irnode.push(in);
        }     
    }
    @Override public void exitExpr_prefix(MicroParser.Expr_prefixContext ctx) { 
    }
    
    @Override public void enterFactor_prefix(MicroParser.Factor_prefixContext ctx) {
        if(ctx.mulop() != null){
            Irnode in = new Irnode("op", "none", ctx.mulop().getText());
            irnode.push(in);
        } 
    }
    @Override public void exitFactor_prefix(MicroParser.Factor_prefixContext ctx) { }
    
    @Override public void enterAssign_expr(MicroParser.Assign_exprContext ctx) { 
        //look up type from the hashtable\
        Scope top = p_scopes.peek();
        Symbol var = top.table.get(ctx.id().getText());
        if(var == null){
            top = p_scopes.firstElement();
            var = top.table.get(ctx.id().getText());
        }   
        Irnode in = new Irnode("id", var.type, ctx.id().getText());
        irnode.push(in);
        //System.out.printf("id = %s\n",in.value);
        Irnode ineq = new Irnode(":=", "none", ":=");
        irnode.push(ineq);
    }
    
    @Override public void exitAssign_expr(MicroParser.Assign_exprContext ctx) {
        if(in_incr == true){
            incr_irnode.push(done);
            while(!irnode.empty()){
                incr_irnode.push(irnode.pop());
            }
        }
        while(!irnode.empty()){
            Irnode temp = irnode.pop();
            
            //System.out.println(temp.value);            
            if(temp.gtype.equals("op")){
                Irnode left = tnode.pop();
                Irnode right = tnode.pop();
                Generate3AC(temp, left, right);
            }
            else if(temp.gtype.equals(":=")){
                Irnode left = tnode.pop();
                //System.out.print(left.value);
                Irnode right = null;
                Generate3AC(temp, left, right);
            }
            else{
                tnode.push(temp);
                //System.out.println("push");
            }
        }
        
        

        //System.out.println(scopes.peek().name);
    }
    @Override public void enterWrite_stmt(MicroParser.Write_stmtContext ctx) {
        String op;
    	String[] ids = ctx.id_list().getText().split(",");
        //System.out.println("I am writing");
        //Scope top = scopes.peek();
    	for(String id: ids){
            Scope top = p_scopes.peek();
            Symbol var = top.table.get(id);
            if(var == null){
                top = p_scopes.firstElement();
                var = top.table.get(id);
            }
            if(var.type.equals("INT")){
                op = "WRITEI";
            }
            else if(var.type.equals("STRING")){
                op = "WRITES";
            }
            else{
                op = "WRITEF";
            }
            Irnode iD = new Irnode("none", "none", id); 
    		Ircode n = new Ircode(op, none, none, iD);
    		ircode.add(n);
    	}

    }
	
	@Override public void exitWrite_stmt(MicroParser.Write_stmtContext ctx) { }



    @Override public void enterRead_stmt(MicroParser.Read_stmtContext ctx) {
        String op;
        String[] ids = ctx.id_list().getText().split(",");
        //System.out.println("I am writing");
        //Scope top = scopes.peek();
        for(String id: ids){
            Scope top = p_scopes.peek();
            Symbol var = top.table.get(id);
            if(var == null){
                top = p_scopes.firstElement();
                var = top.table.get(id);
            }
            if(var.type.equals("INT")){
                op = "READI";
            }
            else{
                op = "READF";
            }
            Irnode iD = new Irnode("id", var.type, id); 
            Ircode n = new Ircode(op, none, none, iD);
            ircode.add(n);
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitRead_stmt(MicroParser.Read_stmtContext ctx) { }


    public void checkDeclError(HashMap symtable, String varName){
        if(symtable.get(varName) != null){
            System.out.println("DECLARATION ERROR " + varName);
            System.exit(1);
        }
    }

    public void Generate3AC(Irnode opcode, Irnode left, Irnode right){
        //System.out.println("gen3ac");
        String op;
        Irnode oprand1;
        Irnode opr1;
        Irnode oprand2;
        Irnode result;
        String reg;
        //Scope top = scopes.peek();
        if(opcode.gtype.equals(":=")){
            if(left.gtype.equals("INTLITERAL")){
                reg = String.format("$T%d", regNo);
                Irnode r = new Irnode("reg", "INT", reg);
                regNo++;
                op = "STOREI";
                opr1 = new Irnode("INTLITERAL","INT",left.value);
                Ircode c = new Ircode(op, opr1, none, r);
                ircode.add(c);
                //tnode.push(r);
                oprand1 = r;
            }
            else if(left.gtype.equals("FLOATLITERAL")){
                reg = String.format("$T%d", regNo);
                Irnode r = new Irnode("reg", "FLOAT", reg);
                regNo++;
                op = "STOREF";
                opr1 = new Irnode("FLOATLITERAL","FLOAT",left.value);
                Ircode c = new Ircode(op, opr1, none, r);
                ircode.add(c);
                //tnode.push(r);
                oprand1 = r;
            }
            else if(left.gtype.equals("id")){
                oprand1 = new Irnode("id", left.dtype, left.value);
            }
            else{
                oprand1 = new Irnode("reg", left.dtype, left.value);
            }

            if(left.dtype.equals("INT")){
                op = "STOREI";
            }
            else{
                op = "STOREF";
            }
            result = irnode.pop();
            //System.out.printf("%s, %s, %s\n", result.value, result.gtype, result.dtype);
            Ircode c = new Ircode(op, oprand1 , none, result);
            ircode.add(c);
        }
        else{
            if(left.gtype.equals("INTLITERAL")){
                reg = String.format("$T%d", regNo);
                Irnode r = new Irnode("reg", "INT", reg);
                regNo++;
                op = "STOREI";
                opr1 = new Irnode("INTLITERAL", "INT", left.value);
                Ircode c = new Ircode(op, opr1, none, r);
                ircode.add(c);
                oprand1 = r;
            }
            else if(left.gtype.equals("FLOATLITERAL")){
                reg = String.format("$T%d", regNo);
                Irnode r = new Irnode("reg", "FLOAT", reg);
                regNo++;
                op = "STOREF";
                opr1 = new Irnode("FLOATLITERAL", "FLOAT", left.value);
                Ircode c = new Ircode(op, opr1, none, r);
                ircode.add(c);
                oprand1 = r; 
            }
            else if(left.gtype.equals("id")){
                oprand1 = new Irnode("id", left.dtype, left.value);
            }
            else{
                oprand1 = new Irnode("reg", left.dtype, left.value);
            }

            if(right.gtype.equals("INTLITERAL")){
                //System.out.println(right.value);
                reg = String.format("$T%d", regNo);
                Irnode r = new Irnode("reg", "INT", reg);
                regNo++;
                op = "STOREI";
                opr1 = new Irnode("INTLITERAL", "INT", right.value);
                Ircode c = new Ircode(op, opr1, none, r);
                //c.printCode();
                ircode.add(c);
                oprand2 = r;
                //System.out.println(oprand2);
            }
            else if(right.gtype.equals("FLOATLITERAL")){
                reg = String.format("$T%d", regNo);
                Irnode r = new Irnode("reg", "FLOAT", reg);
                regNo++;
                op = "STOREF";
                opr1 = new Irnode("FLOATLITERAL", "FLOAT", right.value);
                Ircode c = new Ircode(op, opr1, none, r);
                ircode.add(c);
                oprand2 = r;
            }
            else if(right.gtype.equals("id")){
                oprand2 = new Irnode("id", right.dtype, right.value);
            }
            else{
                oprand2 = new Irnode("reg", right.dtype, right.value);
            }
            if(opcode.value.equals("+")){
                if(left.dtype.equals("INT")){
                    op = "ADDI";
                }
                else{
                    op = "ADDF";
                }
            }
            else if(opcode.value.equals("-")){
                if(left.dtype.equals("INT")){
                    op = "SUBI";
                }
                else{
                    op = "SUBF";
                }
            }
            else if(opcode.value.equals("*")){
                if(left.dtype.equals("INT")){
                    op = "MULTI";
                }
                else{
                    op = "MULTF";
                }
            }
            else{
                if(left.dtype.equals("INT")){
                    op = "DIVI";
                }
                else{
                    op = "DIVF";
                }
            }
            
            reg = String.format("$T%d", regNo);
            regNo++;
            if(left.dtype.equals("INT")){
                Irnode r = new Irnode("reg", "INT", reg);
                tnode.push(r);
                Ircode c = new Ircode(op, oprand1, oprand2, r);
                ircode.add(c);    
            } 
            else{
                Irnode r = new Irnode("reg", "FLOAT", reg);
                tnode.push(r);
                Ircode c = new Ircode(op, oprand1, oprand2, r);
                ircode.add(c);
            }    
        }
        
    }

    public void genCond3ac(Irnode opcode, Irnode left, Irnode right){
        //System.out.println("here");
        String op;
        Irnode oprand1;
        Irnode opr1;
        Irnode oprand2;
        Irnode result;
        String reg;
        //System.out.println(opcode.value);
        
        if(left.gtype.equals("INTLITERAL")){
                reg = String.format("$T%d", regNo);
                Irnode r = new Irnode("reg", "INT", reg);
                regNo++;
                op = "STOREI";
                opr1 = new Irnode("INTLITERAL", "INT", left.value);
                Ircode c = new Ircode(op, opr1, none, r);
                ircode.add(c);
                oprand1 = r;
            }
        else if(left.gtype.equals("FLOATLITERAL")){
            reg = String.format("$T%d", regNo);
            Irnode r = new Irnode("reg", "FLOAT", reg);
            regNo++;
            op = "STOREF";
            opr1 = new Irnode("FLOATLITERAL", "FLOAT", left.value);
            Ircode c = new Ircode(op, opr1, none, r);
            ircode.add(c);
            oprand1 = r; 
        }
        else if(left.gtype.equals("id")){
            oprand1 = new Irnode("id", left.dtype, left.value);
        }
        else{
            oprand1 = new Irnode("reg", left.dtype, left.value);
        }

        if(right.gtype.equals("INTLITERAL")){
            //System.out.println(right.value);
            reg = String.format("$T%d", regNo);
            Irnode r = new Irnode("reg", "INT", reg);
            regNo++;
            op = "STOREI";
            opr1 = new Irnode("INTLITERAL", "INT", right.value);
            Ircode c = new Ircode(op, opr1, none, r);
            //c.printCode();
            ircode.add(c);
            oprand2 = r;
            //System.out.println(oprand2);
        }
        else if(right.gtype.equals("FLOATLITERAL")){
            reg = String.format("$T%d", regNo);
            Irnode r = new Irnode("reg", "FLOAT", reg);
            regNo++;
            op = "STOREF";
            opr1 = new Irnode("FLOATLITERAL", "FLOAT", right.value);
            Ircode c = new Ircode(op, opr1, none, r);
            ircode.add(c);
            oprand2 = r;
        }
        else if(right.gtype.equals("id")){
            oprand2 = new Irnode("id", right.dtype, right.value);
        }
        else{
            oprand2 = new Irnode("reg", right.dtype, right.value);
        }
        if(opcode.value.equals("=")){
            op = "NE";
        }
        else if(opcode.value.equals("<")){
            op = "GE";
        }
        else if(opcode.value.equals(">")){
            op = "LE";
        }
        else if(opcode.value.equals(">=")){
            op = "LT";
        }
        else if(opcode.value.equals("<=")){
            op = "GT";
        }
        else{
            op = "EQ";
        }
        if(enterif == true){
            String label1 = labels.pop();
            String label2 = labels.pop();
            Irnode in = new Irnode("none","none",label2);
            Ircode c = new Ircode(op, oprand1, oprand2, in);
            labels.push(label2);
            labels.push(label1);
            ircode.add(c);
        }
        else{
            String label1 = labels.pop();
            Irnode in = new Irnode("none","none",label1);
            Ircode c = new Ircode(op, oprand1, oprand2, in);
            labels.push(label1);
            ircode.add(c);
        }
        
    }


    public void GenerateTiny(){
        String op;
        String opr1;
        String opr2;
        String reg;
        String o1;
        String o2;
        Acode ac;
        for(Ircode c : ircode){
            //c.printCode();
            if(c.opcode.equals("STOREI")||c.opcode.equals("STOREF")){
                aStoreOp(c);
            }
            else if(c.opcode.equals("ADDI")){
                op = "addi";
                aOp(c, op);
            }
            else if(c.opcode.equals("ADDF")){
                op = "addr";
                aOp(c, op);
            }
            else if(c.opcode.equals("MULTI")){
                op = "muli";
                aOp(c, op);
            }
            else if(c.opcode.equals("MULTF")){
                op = "mulr";
                aOp(c, op);
            }
            else if(c.opcode.equals("DIVI")){
                op = "divi";
                aOp(c, op);
            }
            else if(c.opcode.equals("DIVF")){
                op = "divr";
                aOp(c, op);
            }
            else if(c.opcode.equals("SUBI")){
                op = "subi";
                aOp(c, op);
            }
            else if(c.opcode.equals("SUBF")){
                op = "subr";
                aOp(c, op);
            }
            else if(c.opcode.equals("JUMP")){
                op = "jmp";
                ac = new Acode(op, c.result.value);
                acode.add(ac);
            }
            else if(c.opcode.equals("LABEL")){
                op = "label";
                ac = new Acode(op, c.result.value);
                acode.add(ac);
            }
            else if(c.opcode.equals("WRITEI")||c.opcode.equals("WRITEF")||c.opcode.equals("WRITES")){
                writeOp(c);
            }
            else if(c.opcode.equals("READI")||c.opcode.equals("READF")){
                readOp(c);
            }
            else{
                aComp(c);
            }
        }
    }

    public void aOp(Ircode c, String op){
        String opr1;
        String opr2;
        String reg;
        String o1;
        String o2;
        Acode ac;
        if(rtable.get(c.oprand1.value) == null || c.oprand1.gtype.equals("id")){
            o1 = c.oprand1.value;
            o2 = String.format("r%d", rn);
            rn++;
            ac = new Acode("move", o1, o2);
            acode.add(ac);
            //ac.printCode();
            opr2 = o2;
            if(c.oprand1.gtype.equals("id")){
                rtable.put(c.oprand1.value, o2);
                //System.out.printf(";Table:      %s:%s\n", c.oprand1.value, o2);
            }
        }
        else{
            opr2 = rtable.get(c.oprand1.value);  
        }

        if(c.oprand2.gtype.equals("id"))
        {
            opr1 = c.oprand2.value;
        }else{
            opr1 = rtable.get(c.oprand2.value);
        }
        ac = new Acode(op, opr1, opr2);
        //ac.printCode();
        acode.add(ac);
        rtable.put(c.result.value,opr2);
        //System.out.printf(";Table:      %s:%s\n", c.result.value, opr2);
    }

    public void aComp(Ircode c){
        String op;
        Acode ac;
        String reg;
        String OP = c.opcode;
        if(c.oprand2.gtype.equals("id")){
            reg = String.format("r%d",rn);
            rn++;
            ac = new Acode("move", c.oprand2.value, reg);
            acode.add(ac);
        }
        if(c.oprand1.dtype.equals("INT")){
            op = "cmpi";
        }
        else{
            op = "cmpr";
        }
        
        reg = String.format("r%d", rn-1);
        ac = new Acode(op, c.oprand1.value, reg);
        acode.add(ac);
        if(OP == "EQ"){
            op = "jeq";
        }
        else if(OP == "NE"){
            op = "jne";
        }
        else if(OP == "GT"){
            op = "jgt";
        }
        else if(OP == "LT"){
            op = "jlt";
        }
        else if(OP == "GE"){
            op = "jge";
        }
        else if(OP == "LE"){
            op = "jle";
        }
        ac = new Acode(op, c.result.value);
        acode.add(ac);
    }


    public void aStoreOp(Ircode c){
        String opr1;
        String opr2;
        String reg;
        String o1;
        String o2;
        String op;
        Acode ac;
        reg = String.format("r%d ir", regNo);
        op = "move";
        if(c.oprand1.gtype.equals("INTLITERAL") || c.oprand1.gtype.equals("FLOATLITERAL")){
            opr1 = c.oprand1.value;
            opr2 = String.format("r%d", rn);
            rn++;
            ac = new Acode(op, opr1, opr2);
            //ac.printCode();
            acode.add(ac);
            rtable.put(c.result.value, opr2);
            //System.out.printf(";Table:      %s:%s\n", c.result.value, opr2);
        }
        else if(c.oprand1.gtype.equals("id")){
            opr1 = c.oprand1.value;
            opr2 = String.format("r%d", rn);
            rn++;
            ac = new Acode(op, opr1, opr2);
            //ac.printCode();
            acode.add(ac);
            ac = new Acode(op, opr2, c.result.value);
            acode.add(ac);
            rtable.put(c.result.value, opr2);
        }
        else{
            if(rtable.get(c.oprand1.value)!= null){
                opr1 = rtable.get(c.oprand1.value);
            }
            else{
                opr1 = String.format("r%d", rn);
                rn++;
            }
            opr2 = c.result.value;
            ac = new Acode(op, opr1, opr2);
            //ac.printCode();
            acode.add(ac);
            rtable.put(c.result.value, opr1);
            //System.out.printf(";Table:      %s:%s\n", c.result.value, opr1);
            rtable.put(opr2, opr1);
        }
    }

    public void writeOp(Ircode c){
        String op;
        if(c.opcode.equals("WRITEI")){
            op = "sys writei";
        }
        else if(c.opcode.equals("WRITEF")){
            op = "sys writer";
        }
        else{
            op = "sys writes";
        }
        String opr1 = c.result.value;
        Acode ac = new Acode(op, opr1);
        //ac.printCode();
        acode.add(ac);
    }
    public void readOp(Ircode c){
        String op;
        if(c.result.dtype.equals("INT")){
            op = "sys readi";
        }
        else{
            op = "sys readr";
        }
        String opr1 = c.result.value;
        Acode ac = new Acode(op, opr1);
        //ac.printCode();
        acode.add(ac);
    }

}
