    // Generated from Micro.g4 by ANTLR 4.5.1

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;
import java.util.List;
import java.lang.Integer;
/**
 * This class provides an empty implementation of {@link MicroListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
public class CustomListener extends MicroBaseListener {
    private int printIr = 0;
    private int tempNo;
    private int rn = 0;
    private Stack<Scope> scopes;
    private Stack<Scope> p_scopes;
    private Stack<Irnode> irnode;
    private ArrayList<Ircode> ircode;
    private Stack<Irnode> incr_irnode;
    private Stack<Irnode> tnode;
    private ArrayList<Acode> acode; 
    private HashMap<String, String> rtable;
    private HashMap<String, Symbol> globalTable;
    private HashMap<String, Function> funcTable;
    private Stack<String> labels;
    private Irnode none = new Irnode("none", "none", "none");
    private Irnode done = new Irnode("done", "done", "done");
    private int labelnum = 1;
    public boolean in_incr = false;
    public boolean enterfor = false;
    public boolean enterif = false;
    public Function curFun;
    public int call_expr = 0;
    public String callname;
    public String rettype;
    //Leaders of basic blocks
    public ArrayList<Ircode> leaders;
    
    public CustomListener() {
        scopes = new Stack<Scope>();
        p_scopes = new Stack<Scope>();
        ircode = new ArrayList<Ircode>();
        irnode = new Stack<Irnode>();
        tnode = new Stack<Irnode>();
        acode = new ArrayList<Acode>();
        rtable = new HashMap<String, String>();
        labels = new Stack<String>();
        incr_irnode = new Stack<Irnode>();
        globalTable = new HashMap<String, Symbol>();
        funcTable = new HashMap<String, Function>();
        leaders = new ArrayList<Ircode>();
    }
    @Override public void enterProgram(MicroParser.ProgramContext ctx){
        Scope scope = new Scope("GLOBAL");
        scopes.push(scope);
        p_scopes.push(scope);
    }

    @Override public void exitProgram(MicroParser.ProgramContext ctx) {
        BasicBlocks();

        //CreateCtrGraph();

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
        /*
        for (Scope scope : p_scopes)
        {
            if(scope.type.equals("FUNCTION")){
                System.out.println(scope.type + " " + scope.name);
            }
            else{
                System.out.println(scope.type);
            }
            for(String key :scope.table.keySet()){
                System.out.println(key + ": " + scope.table.get(key).value);
            }

        }*/
        if(printIr == 1){
            for(Irnode ir : irnode){
                System.out.println(ir.gtype + " " + ir.dtype +" "+ ir.value);
            }
        }
        
        if(printIr == 0){
            System.out.println(";IR code");
            for(Ircode c: ircode){
                c.printCode();
            }
            
            System.out.println(";tiny code");
            for(Acode ac: acode){
                ac.printCode();
            }
            System.out.println("end");
        }
    }
    @Override public void enterString_decl(MicroParser.String_declContext ctx) {
        String lv;
        String varType = "STRING";
        String varName =ctx.id().getText();
        String varValue = ctx.str().getText();
        Symbol var = new Symbol(varType, varValue);
        Scope top = scopes.peek();
        checkDeclError(top.table, varName);
        //top.table.put(varName, var);
        //top.keys.add(varName);
        
        if(top.type.equals("GLOBAL")){
            Acode c = new Acode("str", varName, varValue);
            acode.add(c);
            globalTable.put(varName,var);
        }
        else{
            lv = String.format("$L%d", curFun.ln);
            var = new Symbol(varType, lv);
            curFun.localTable.put(varName, var);
            curFun.ln++;    
        }
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
            String lv;
            Symbol var = new Symbol(varType, varValue);
            checkDeclError(top.table, name);
            top.table.put(name, var);
            
            if(top.type.equals("GLOBAL")){
                //System.out.println(top.type);
                Acode c = new Acode("var", name);
                acode.add(c);
                globalTable.put(name,var);
            }
            else{
                lv = String.format("$L%d", curFun.ln);
                curFun.ln++;
                var = new Symbol(varType, lv);
                curFun.localTable.put(name, var);
            }
        }
    }
    @Override public void enterFunc_decl(MicroParser.Func_declContext ctx){
        String varName = ctx.id().getText();
        //funcTable.put(varName, ctx.any_type().getText());
        Function f = new Function(varName, ctx.any_type().getText());
        funcTable.put(varName,f);
        Scope scope = new Scope("FUNCTION", varName);
        if(printIr == 0){
            tempNo = 1;
            Irnode ir = new Irnode("FUNCTION","none",varName);
            Ircode fnode = new Ircode("LABEL", none, none, ir);
            ircode.add(fnode);
            fnode = new Ircode("LINK", none, none, none);
            ircode.add(fnode);
        }
        else{
            Irnode temp = new Irnode("FUNCTION","none",varName);
            irnode.push(temp);
        }
        scopes.push(scope);
        p_scopes.push(scope);
        curFun = f;
    }

    @Override public void exitFunc_decl(MicroParser.Func_declContext ctx){
        Ircode c;
        if(printIr == 0)
        {
            /*Irnode left = irnode.pop();
            Irnode result = new Irnode("return", left.dtype, "$R");
            if(left.dtype.equals("INT")){
                c = new Ircode("STOREI", left, none, result);
                ircode.add(c);
            }
            else{
                c = new Ircode("STOREF", left, none, result);   
                ircode.add(c);
            }*/
            if(!ircode.get(ircode.size() - 1).opcode.equals("RET")){
                c =  new Ircode("RET", none, none, none);
                ircode.add(c);
            }
            c = new Ircode("END", none, none, none);
            ircode.add(c);
        }
        else{
            Irnode temp = new Irnode("exitFunc", "none", ctx.id().getText());
            irnode.push(temp);
        }
        
    }

    @Override public void enterParam_decl(MicroParser.Param_declContext ctx){
        String varType = ctx.var_type().getText();
        String varName = ctx.id().getText();
        String varValue = "";
        String param;
        Symbol var = new Symbol(varType, varValue);
        Scope scope = scopes.peek();
        //System.out.println(scope.type +"  " + varName);
        checkDeclError(scope.table, varName);
        scope.table.put(varName, var);
        param = String.format("$P%d", curFun.pn);
        curFun.pn++;
        //System.out.println(param);
        var = new Symbol(varType, param);
        curFun.paramTable.put(varName, var);

        //System.out.println(scope.type);
        //scope.keys.add(varName);
        //Acode c = new Acode("var", varName);
        //acode.add(c);
        

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
        if(printIr == 0){
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
        if(printIr == 0){
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
        else{
            Irnode temp = new Irnode("exitFor", "none", "none");
            irnode.push(temp);
        }
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
        Irnode in;
        Symbol var;
        if(ctx.id() != null)
        {
            if(globalTable.get(ctx.id().getText()) != null){
                var = globalTable.get(ctx.id().getText());
                in = new Irnode("id", var.type, ctx.id().getText());
                irnode.push(in);
            }
            else if(curFun.paramTable.get(ctx.id().getText())!= null){
                var = curFun.paramTable.get(ctx.id().getText());
                in = new Irnode("param", var.type, var.value);
                irnode.push(in);
            }
            else{
                var = curFun.localTable.get(ctx.id().getText());
                in = new Irnode("local", var.type, var.value);
                irnode.push(in);
            }
        }
        if(ctx.INTLITERAL() != null)
        {
            in = new Irnode("INTLITERAL", "INT", ctx.INTLITERAL().getText());
            irnode.push(in);
        }
        if(ctx.FLOATLITERAL() != null)
        {
            in = new Irnode("FLOATLITERAL", "FLOAT", ctx.FLOATLITERAL().getText());
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
        if(printIr == 1){
            Irnode ir = new Irnode("enterass", "none", "none");
            irnode.push(ir);
        }
        //look up type from the hashtable\
        Irnode in;
        Scope global;
        Symbol var;
        if(ctx.id() != null)
        {
            if(globalTable.get(ctx.id().getText()) != null){
                var = globalTable.get(ctx.id().getText());
                in = new Irnode("id", var.type, ctx.id().getText());
                irnode.push(in);
            }
            else if(curFun.paramTable.get(ctx.id().getText()) != null){
                var = curFun.paramTable.get(ctx.id().getText());
                in = new Irnode("param", var.type, var.value);
                irnode.push(in);
            }
            else{
                var = curFun.localTable.get(ctx.id().getText());
                in = new Irnode("local", var.type, var.value);
                irnode.push(in);
            }
        }
        //System.out.printf("id = %s\n",in.value);
        Irnode ineq = new Irnode(":=", "none", ":=");
        irnode.push(ineq);
    }
    
    @Override public void exitAssign_expr(MicroParser.Assign_exprContext ctx) {
        int numOfop = 0;
        int numOftn = 0;
        if(in_incr == true){
            incr_irnode.push(done);
            while(!irnode.empty()){
                incr_irnode.push(irnode.pop());
            }
        }
        if(printIr == 0){
            if(call_expr == 1){
                int fpush = 0;
                String type;
                Irnode temp;
                //Stack<Irnode> ri = new Stack<Irnode>();
                while(!irnode.empty() && !irnode.peek().gtype.equals("enterpush")){
                    temp = irnode.pop();
                    //System.out.println(temp.value);            
                    if(temp.gtype.equals("op")){
                        numOfop++;
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
                        numOftn++;

                    //System.out.println("push");
                    }
                }
                Ircode c = new Ircode("PUSH",none,none,none);
                ircode.add(c);
                if(numOfop > 0){
                    while(numOfop > 0){
                        temp = tnode.peek();
                        fpush ++;
                        c = new Ircode("PUSH",none, none, temp);
                        ircode.add(c);
                        //System.out.println("tnode " + temp.gtype+ " " + temp.value);
                        numOfop--;
                    }
                }
                else{
                    while(numOftn > 0){
                        temp = tnode.pop();
                        fpush ++;
                        c = new Ircode("PUSH",none, none, temp);
                        ircode.add(c);
                        //System.out.println("tnode " + temp.gtype+ " " + temp.value);
                        numOftn--;
                    }
                }
                temp = new Irnode("FUNCTION", "none", callname);
                //System.out.println(callname);
                c = new Ircode("JSR",none, none, temp);
                ircode.add(c);
                if(fpush > 0){
                    irnode.pop();
                    while(fpush > 0){
                        fpush--;
                        c = new Ircode("POP",none, none, none);
                        ircode.add(c);
                    }
                    String tempr = String.format("$T%d", tempNo);

                    Irnode r = new Irnode("temp", funcTable.get(callname).retType, tempr);
                    tnode.push(r);
                    tempNo++;
                    c = new Ircode("POP",none, none, r);
                    ircode.add(c);
                }
                call_expr = 0;
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
        }
        else{
            Irnode temp = new Irnode("exitass", "none" ,"none");
            irnode.push(temp);
        }
        
    }
    @Override public void enterWrite_stmt(MicroParser.Write_stmtContext ctx) {
        String op;
    	String[] ids = ctx.id_list().getText().split(",");
        String name;
        String gtype;
        //System.out.println("I am writing");
        //Scope top = scopes.peek();
    	for(String id: ids){
            Symbol var = globalTable.get(id);
            if(var != null){
                name = id;
                gtype = "id";
            }
            else if(curFun.paramTable.get(id)!= null){
                var = curFun.paramTable.get(id);
                name = var.value;
                gtype = "param";
            }
            else{
                var = curFun.localTable.get(id);
                name = var.value;
                gtype = "local";
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
            Irnode iD = new Irnode(gtype, "none", name); 
    		Ircode n = new Ircode(op, none, none, iD);
    		ircode.add(n);
    	}

    }
	
	@Override public void exitWrite_stmt(MicroParser.Write_stmtContext ctx) { }



    @Override public void enterRead_stmt(MicroParser.Read_stmtContext ctx) {
        String op;
        String name;
        String gtype;
        String[] ids = ctx.id_list().getText().split(",");
        //System.out.println("I am writing");
        //Scope top = scopes.peek();
        for(String id: ids){
            Symbol var = globalTable.get(id);
            if(var != null){
                name = id;
                gtype = "id";
            }
            else if(curFun.paramTable.get(id)!= null){
                var = curFun.paramTable.get(id);
                name = var.value;
                gtype = "param";
            }
            else{
                var = curFun.localTable.get(id);
                name = var.value;
                gtype = "local";
            }
            if(var.type.equals("INT")){
                op = "READI";
            }
            else{
                op = "READF";
            }
            Irnode iD = new Irnode(gtype, var.type, name);
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



    @Override public void enterCall_expr(MicroParser.Call_exprContext ctx) { 
        callname = ctx.id().getText();
        rettype = ctx.id().getText();
        Irnode ir = new Irnode("enterpush","none","none");
        irnode.push(ir);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitCall_expr(MicroParser.Call_exprContext ctx) {
        if(printIr == 0){
            call_expr = 1;
            /*
            int fpush = 0;
            String type;
            Irnode temp;
            Stack<Irnode> ri = new Stack<Irnode>();
            Ircode c = new Ircode("PUSH",none,none,none);
            ircode.add(c);
            while(!irnode.empty() && !irnode.peek().gtype.equals("enterpush")){
                ri.push(irnode.pop());
                //System.out.println(temp.value);
                fpush++;
            }
            while(!ri.empty()){
                temp = ri.pop();
                c = new Ircode("PUSH",none, none, temp);
                ircode.add(c);
            }
            if(fpush > 0){
                irnode.pop();
                while(fpush > 0){
                    fpush--;
                    c = new Ircode("POP",none, none, none);
                    ircode.add(c);
                }
                String tempr = String.format("$T%d", tempNo);
                Irnode r = new Irnode("temp", "INT", tempr);
                tempNo++;
                c = new Ircode("POP",none, none, r);
                ircode.add(c);
            }
            temp = new Irnode("FUNCTION", "none", ctx.id().getText());
            c = new Ircode("JSR",none, none, temp);
            ircode.add(c);*/
        }
        else{
            Irnode temp = new Irnode("exitCall","none","none");
            irnode.push(temp);
        }
    }

    @Override public void enterReturn_stmt(MicroParser.Return_stmtContext ctx) { 
        if(printIr == 1){
            Irnode ret = new Irnode("enterret","none", "none");
            irnode.push(ret);
        }
        
    }
    @Override public void exitReturn_stmt(MicroParser.Return_stmtContext ctx) { 
        if(printIr == 1){
            Irnode ret = new Irnode("exitret","none", "none");
            irnode.push(ret);
        }
        if(printIr == 0)
        {
            Ircode c;
            Irnode r;
            while(!irnode.empty()){
                Irnode temp = irnode.pop();
                
                //System.out.println(temp.value);            
                if(temp.gtype.equals("op")){
                    Irnode left = tnode.pop();
                    Irnode right = tnode.pop();
                    Generate3AC(temp, left, right);
                }
                else{
                    tnode.push(temp);
                }
            }
            //System.out.println(left.gtype + " " + left.dtype + " " + left.value);
            Irnode ret = tnode.pop();
            String dtype = ret.dtype;
            r = ret;
            if(ret.gtype.equals("INTLITERAL")){
                String tempr = String.format("$T%d", tempNo);
                r = new Irnode("temp", "INT", tempr);
                tempNo++;
                Irnode opr1 = new Irnode("INTLITERAL","INT",ret.value);
                c = new Ircode("STOREI", opr1, none, r);
                ircode.add(c);
            }
            else if(ret.gtype.equals("FLOATLITERAL")){
                String tempr = String.format("$T%d", tempNo);
                r = new Irnode("temp", "INT", tempr);
                tempNo++;
                Irnode opr1 = new Irnode("INTLITERAL","INT",ret.value);
                c = new Ircode("STOREI", opr1, none, r);
                ircode.add(c);
            }
            Irnode result = new Irnode("return", dtype, "$R");
            if(dtype.equals("INT")){
                c = new Ircode("STOREI", r, none, result);
                ircode.add(c);
            }
            else{
                c = new Ircode("STOREF", r, none, result);   
                ircode.add(c);
            }
            c = new Ircode("RET", none, none, none);
            ircode.add(c);
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */

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
        String tempr;
        //Scope top = scopes.peek();
        if(opcode.gtype.equals(":=")){
            if(left.gtype.equals("INTLITERAL")){
                tempr = String.format("$T%d", tempNo);
                Irnode r = new Irnode("temp", "INT", tempr);
                tempNo++;
                op = "STOREI";
                opr1 = new Irnode("INTLITERAL","INT",left.value);
                Ircode c = new Ircode(op, opr1, none, r);
                ircode.add(c);
                //tnode.push(r);
                oprand1 = r;
            }
            else if(left.gtype.equals("FLOATLITERAL")){
                tempr = String.format("$T%d", tempNo);
                Irnode r = new Irnode("temp", "FLOAT", tempr);
                tempNo++;
                op = "STOREF";
                opr1 = new Irnode("FLOATLITERAL","FLOAT",left.value);
                Ircode c = new Ircode(op, opr1, none, r);
                ircode.add(c);
                //tnode.push(r);
                oprand1 = r;
            }
            else{// if(left.gtype.equals("id") || left.gtype.equals("param") || left.gtype.equals("local")){
                oprand1 = left;
                //System.out.println(left.gtype);
            }/*
            else{
                oprand1 = new Irnode("temp", left.dtype, left.value);

            }*/

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
                tempr = String.format("$T%d", tempNo);
                Irnode r = new Irnode("temp", "INT", tempr);
                tempNo++;
                op = "STOREI";
                opr1 = new Irnode("INTLITERAL", "INT", left.value);
                Ircode c = new Ircode(op, opr1, none, r);
                ircode.add(c);
                oprand1 = r;
            }
            else if(left.gtype.equals("FLOATLITERAL")){
                tempr = String.format("$T%d", tempNo);
                Irnode r = new Irnode("temp", "FLOAT", tempr);
                tempNo++;
                op = "STOREF";
                opr1 = new Irnode("FLOATLITERAL", "FLOAT", left.value);
                Ircode c = new Ircode(op, opr1, none, r);
                ircode.add(c);
                oprand1 = r; 
            }
            else{//if(left.gtype.equals("id") || left.gtype.equals("param") || left.gtype.equals("local")){
                oprand1 = left;
                
            }/*
            else{
                oprand1 = new Irnode("temp", left.dtype, left.value);
                System.out.println(left.gtype);
            }*/

            if(right.gtype.equals("INTLITERAL")){
                //System.out.println(right.value);
                tempr = String.format("$T%d", tempNo);
                Irnode r = new Irnode("temp", "INT", tempr);
                tempNo++;
                op = "STOREI";
                opr1 = new Irnode("INTLITERAL", "INT", right.value);
                Ircode c = new Ircode(op, opr1, none, r);
                //c.printCode();
                ircode.add(c);
                oprand2 = r;
                //System.out.println(oprand2);
            }
            else if(right.gtype.equals("FLOATLITERAL")){
                tempr = String.format("$T%d", tempNo);
                Irnode r = new Irnode("temp", "FLOAT", tempr);
                tempNo++;
                op = "STOREF";
                opr1 = new Irnode("FLOATLITERAL", "FLOAT", right.value);
                Ircode c = new Ircode(op, opr1, none, r);
                ircode.add(c);
                oprand2 = r;
            }
            else{// if(right.gtype.equals("id")){
                oprand2 = right;//new Irnode("id", right.dtype, right.value);
            }
            /*else{
                oprand2 = new Irnode("temp", right.dtype, right.value);
            }*/
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
            
            tempr = String.format("$T%d", tempNo);
            tempNo++;
            if(left.dtype.equals("INT")){
                Irnode r = new Irnode("temp", "INT", tempr);
                tnode.push(r);
                Ircode c = new Ircode(op, oprand1, oprand2, r);
                ircode.add(c);    
            } 
            else{
                Irnode r = new Irnode("temp", "FLOAT", tempr);
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
        String tempr;
        //System.out.println(opcode.value);
        
        if(left.gtype.equals("INTLITERAL")){
            tempr = String.format("$T%d", tempNo);
            Irnode r = new Irnode("temp", "INT", tempr);
            tempNo++;
            op = "STOREI";
            opr1 = new Irnode("INTLITERAL", "INT", left.value);
            Ircode c = new Ircode(op, opr1, none, r);
            ircode.add(c);
            oprand1 = r;
        }
        else if(left.gtype.equals("FLOATLITERAL")){
            tempr = String.format("$T%d", tempNo);
            Irnode r = new Irnode("temp", "FLOAT", tempr);
            tempNo++;
            op = "STOREF";
            opr1 = new Irnode("FLOATLITERAL", "FLOAT", left.value);
            Ircode c = new Ircode(op, opr1, none, r);
            ircode.add(c);
            oprand1 = r; 
        }
        else {
            oprand1 = left;
        }

        if(right.gtype.equals("INTLITERAL")){
            //System.out.println(right.value);
            tempr = String.format("$T%d", tempNo);
            Irnode r = new Irnode("temp", "INT", tempr);
            tempNo++;
            op = "STOREI";
            opr1 = new Irnode("INTLITERAL", "INT", right.value);
            Ircode c = new Ircode(op, opr1, none, r);
            //c.printCode();
            ircode.add(c);
            oprand2 = r;
            //System.out.println(oprand2);
        }
        else if(right.gtype.equals("FLOATLITERAL")){
            tempr = String.format("$T%d", tempNo);
            Irnode r = new Irnode("temp", "FLOAT", tempr);
            tempNo++;
            op = "STOREF";
            opr1 = new Irnode("FLOATLITERAL", "FLOAT", right.value);
            Ircode c = new Ircode(op, opr1, none, r);
            ircode.add(c);
            oprand2 = r;
        }
        else {
            oprand2 = right;
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
    /******************************************************
    * Code associated with translate from 3AC to assembly
    ******************************************************/ 
    public void GenerateTiny(){
        String op;
        String reg;
        String o1;
        String o2;
        Acode ac;
        int i;
        //call main
        ac = new Acode("push");
        acode.add(ac);
        for(i = 0; i <= 3; i++){
            o1 = String.format("r%d", i);
            ac = new Acode("push", o1);
            acode.add(ac);
        }
        ac = new Acode("jsr", "main");
        acode.add(ac);
        ac = new Acode("sys", "halt");
        acode.add(ac);
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
                if(funcTable.get(c.result.value) != null){
                    curFun = funcTable.get(c.result.value);
                }
            }
            else if(c.opcode.equals("LINK")){
                op = "link";
                o1 = String.format("%d", curFun.ln - 1);
                ac = new Acode(op, o1);
                acode.add(ac);
            }
            else if(c.opcode.equals("RET")){
                ac = new Acode("unlnk");
                acode.add(ac);
                ac = new Acode("ret");
                acode.add(ac);
            }
            else if(c.opcode.equals("WRITEI")||c.opcode.equals("WRITEF")||c.opcode.equals("WRITES")){
                writeOp(c);
            }
            else if(c.opcode.equals("READI")||c.opcode.equals("READF")){
                readOp(c);
            }
            
            else if(c.opcode.equals("PUSH")){
                if(c.result.value == "none"){
                    ac = new Acode("push");
                    acode.add(ac);
                }
                else{
                    if(c.result.gtype.equals("temp")){
                        ac = new Acode("push", rtable.get(c.result.value));  
                        acode.add(ac); 
                    }
                    else{
                        ac = new Acode("push", aoprand(c.result));
                        acode.add(ac);
                    }
                }
            }
            else if(c.opcode.equals("POP")){
                if(c.result.value == "none"){
                    ac = new Acode("pop");
                    acode.add(ac);
                }
                else{
                    o1 = String.format("r%d", rn);
                    rn++;
                    ac = new Acode("pop", o1);
                    acode.add(ac);
                    rtable.put(c.result.value,o1);
                }
            }
            else if(c.opcode.equals("JSR")){
                for(i = 0; i <= 3; i++){
                    o1 = String.format("r%d", i);
                    ac = new Acode("push", o1);
                    acode.add(ac);
                }
                ac = new Acode("jsr", c.result.value);
                acode.add(ac);
                for(i = 3; i >= 0; i--){
                    o1 = String.format("r%d", i);
                    ac = new Acode("pop", o1);
                    acode.add(ac);
                }
            }
            else if(c.opcode.equals("END")){

            }
            else{
                
                aComp(c);
                //System.out.println(c.oprand1.value + " " + c.oprand2.value);
            }
        }
    }
    
    public void aOp(Ircode c, String op){
        String opr1;
        String opr2;
        String reg;
        String o1;
        String o2;
        String gtype;
        String value;
        Acode ac;
        if(c.oprand1.gtype.equals("temp") && c.oprand2.gtype.equals("temp")){
            opr1 = rtable.get(c.oprand1.value);
            opr2 = rtable.get(c.oprand2.value);
            ac = new Acode(op, opr1, opr2);
            acode.add(ac);
            rtable.put(c.result.value, opr2);
        }
        else if(c.oprand1.gtype.equals("temp") && !c.oprand2.gtype.equals("temp")){
            o1 = aoprand(c.oprand2);
            opr1 = String.format("r%d", rn);
            rn++;
            ac = new Acode("move",o1, opr1);
            acode.add(ac);           
            opr2 = rtable.get(c.oprand1.value);
            ac = new Acode(op, opr1, opr2);
            acode.add(ac);
            rtable.put(c.result.value, opr1);
        }
        else if(!c.oprand1.gtype.equals("temp") && c.oprand2.gtype.equals("temp")){
            o1 = aoprand(c.oprand1);
            opr1 = String.format("r%d", rn);
            rn++;
            ac = new Acode("move",o1, opr1);
            acode.add(ac);           
            opr2 = rtable.get(c.oprand2.value);
            ac = new Acode(op, opr2, opr1);
            acode.add(ac);
            rtable.put(c.result.value, opr1);
        }
        else{
            o1 = aoprand(c.oprand1);
            opr1 = String.format("r%d", rn);
            rn++;
            ac = new Acode("move",o1, opr1);
            acode.add(ac);           
            opr2 = aoprand(c.oprand2);
            ac = new Acode(op, opr2, opr1);
            acode.add(ac);
            rtable.put(c.result.value, opr1);
        }
    }

    public void aComp(Ircode c){
        String op;
        Acode ac;
        String reg;
        String OP = c.opcode;
        if(c.oprand2.gtype.equals("id")||c.oprand2.gtype.equals("param")|| c.oprand2.gtype.equals("local") ){
            reg = String.format("r%d",rn);
            rn++;
            ac = new Acode("move", aoprand(c.oprand2), reg);
            acode.add(ac);
        }
        if(c.oprand1.dtype.equals("INT")){
            op = "cmpi";
        }
        else{
            op = "cmpr";
        }
        reg = String.format("r%d", rn-1);
        ac = new Acode(op, aoprand(c.oprand1), reg);
        acode.add(ac);
        if(OP == "EQ"){
            op = "jeq";
        }
        else if(OP.equals("NE")){
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
        else if(OP.equals("LE")){
            op = "jle";
        }
        ac = new Acode(op,c.result.value);
        acode.add(ac);
    }


    public void aStoreOp(Ircode c){
        String gtype;
        String value;
        String opr1;
        String opr2;
        String reg;
        String o1;
        String o2;
        String op = "move";
        Acode ac;
        //reg = String.format("r%d ir", regNo);
        if(c.oprand1.gtype.equals("INTLITERAL") || c.oprand1.gtype.equals("FLOATLITERAL")){
            opr1 = c.oprand1.value;
            opr2 = String.format("r%d", rn);
            rn++;
            ac = new Acode(op, opr1, opr2);
            acode.add(ac);
            rtable.put(c.result.value, opr2);
            //System.out.println(opr2);
        }
        else{
            if(c.oprand1.gtype.equals("temp") && c.result.gtype.equals("temp")){
                opr1 = rtable.get(c.oprand1.value);
                opr2 = rtable.get(c.result.value);
                ac = new Acode(op, opr1, opr2);
                acode.add(ac);
                rtable.put(c.result.value, opr2);
            }
            else if(c.oprand1.gtype.equals("temp") && !c.result.gtype.equals("temp")){          
                opr1 = rtable.get(c.oprand1.value);
                opr2 = aoprand(c.result);
                ac = new Acode(op, opr1, opr2);
                acode.add(ac);
            }
            else if(!c.oprand1.gtype.equals("temp") && c.result.gtype.equals("temp")){
                opr1 =  aoprand(c.oprand1);
                opr2 = String.format("r%d", rn);
                rn++;
                ac = new Acode(op,opr1,opr2);
                acode.add(ac);
                rtable.put(c.result.value, opr2);
            }
            else{
                o1 =  aoprand(c.oprand1);
                opr1 = String.format("r%d", rn);
                rn++;
                ac = new Acode(op,o1,opr1);
                acode.add(ac);
                opr2 = aoprand(c.result);
                ac = new Acode(op,opr1,opr2);
                acode.add(ac);
                //rtable.put(c.result.value, opr2);
            }
                /*
                o1 = aoprand(c.oprand1);
                if(rtable.get(o1) == null){
                    o2 = String.format("r%d", rn);
                    rn++;
                    ac = new Acode(op, o1, o2);
                    acode.add(ac);               
                    opr1 = o2;
                }
                else{
                    opr1 = rtable.get(o1);
                }
                opr2 = aoprand(c.result);
                ac = new Acode(op, opr1, opr2);
                acode.add(ac);*/
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
        String opr1 = aoprand(c.result);
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
        String opr1 = aoprand(c.result);
        //System.out.println(c.result.gtype + " " + c.result.value);
        Acode ac = new Acode(op, opr1);
        //ac.printCode();
        acode.add(ac);
    }
    public String irPtoaP(String paramn){
        int p;
        String ap;
        ap = paramn.replaceAll("\\D+","");
        p = 6 + curFun.pn -1 - Integer.parseInt(ap);
        //System.out.println(curFun.pn + " " +p);
        ap = String.format("$%d", p);
        return ap;
    }
    public String irLtoaL(String local){
        int l;
        String al;
        al = local.replaceAll("\\D+","");
        l = - Integer.parseInt(al);
        al = String.format("$%d", l);
        return al;
    }
    public String irTtoaT(String temp){
        int t;
        String at;
        at = temp.replaceAll("\\D+","");
        t = - (curFun.ln -1 + Integer.parseInt(at));
        at = String.format("$%d", t);
        return at;
    }
    public String aoprand(Irnode ir){
        String gtype = ir.gtype;
        String value = ir.value;
        String aop;
        if(gtype.equals("id")){
            aop = value;
        }
        else if(gtype.equals("param")){
            aop = irPtoaP(value);
        }
        else if(gtype.equals("local")){
            aop = irLtoaL(value);
        }
        else if(gtype.equals("temp")){
            aop = irTtoaT(value);   
        }
        else{
            aop = String.format("$%d",6 + curFun.pn-1);
            //System.out.println("I missed something here");
        }
        return aop;
    }

    /******************************************************
    * Register Allocation
    ******************************************************/

    /******************************************************
    * 
    ******************************************************/
    
    public void BasicBlocks(){
        ArrayList<Integer> lderIndex = new ArrayList<Integer>();
        Integer index;
        for(Ircode c: ircode){
            if(ircode.indexOf(c) == 1){
                leaders.add(c);
                index = new Integer(1);
                lderIndex.add(index);
            }
            else if(c.opcode.equals("EQ")){
                leaders.add(c);
                index = new Integer(ircode.indexOf(c));
                lderIndex.add(index);
            }
            else if(c.opcode.equals("NE")){
                leaders.add(c);
                index = new Integer(ircode.indexOf(c));
                lderIndex.add(index);
            }
            else if(c.opcode.equals("GT")){
                leaders.add(c);
                index = new Integer(ircode.indexOf(c));
                lderIndex.add(index);
            }
            else if(c.opcode.equals("LT")){
                leaders.add(c);
                index = new Integer(ircode.indexOf(c));
                lderIndex.add(index);
            }
            else if(c.opcode.equals("GE")){
                leaders.add(c);
                index = new Integer(ircode.indexOf(c));
                lderIndex.add(index);
            }
            else if(c.opcode.equals("LE")){
                leaders.add(c);
                index = new Integer(ircode.indexOf(c));
                lderIndex.add(index);
            }
            else if(c.opcode.equals("JUMP")){
                leaders.add(c);
                index = new Integer(ircode.indexOf(c));
                lderIndex.add(index);
            }
            else if(c.opcode.equals("LABEL")){
                leaders.add(c);
                index = new Integer(ircode.indexOf(c));
                lderIndex.add(index);
            }
        }
        /*       
        for(Integer i: lderIndex){
            System.out.println(i.intValue());
        }*/
    }
    public void CtrFlowGraph(){
        for(Ircode c: ircode){
            for(Ircode c: ircode){
                if(ircode.indexOf(c) == 1){
                    leaders.add(c);
                    index = new Integer(1);
                    lderIndex.add(index);
                }
                else if(c.opcode.equals("EQ")){
                    index = new Integer(ircode.indexOf(c));
                    lderIndex.add(index);
                }
                else if(c.opcode.equals("NE")){
                    leaders.add(c);
                    index = new Integer(ircode.indexOf(c));
                    lderIndex.add(index);
                }
                else if(c.opcode.equals("GT")){
                    leaders.add(c);
                    index = new Integer(ircode.indexOf(c));
                    lderIndex.add(index);
                }
                else if(c.opcode.equals("LT")){
                    leaders.add(c);
                    index = new Integer(ircode.indexOf(c));
                    lderIndex.add(index);
                }
                else if(c.opcode.equals("GE")){
                    leaders.add(c);
                    index = new Integer(ircode.indexOf(c));
                    lderIndex.add(index);
                }
                else if(c.opcode.equals("LE")){
                    leaders.add(c);
                    index = new Integer(ircode.indexOf(c));
                    lderIndex.add(index);
                }
                else if(c.opcode.equals("JUMP")){
                    leaders.add(c);
                    index = new Integer(ircode.indexOf(c));
                    lderIndex.add(index);
                }
                else if(c.opcode.equals("LABEL")){
                    leaders.add(c);
                    index = new Integer(ircode.indexOf(c));
                    lderIndex.add(index);
                }
            }
        }
    }


}
