package vdm2isa.tr.expressions;

import com.fujitsu.vdmj.lex.LexLocation;
import com.fujitsu.vdmj.tc.expressions.TCExpression;
import com.fujitsu.vdmj.tc.definitions.TCStateDefinition;
import com.fujitsu.vdmj.tc.expressions.TCStateInitExpression;


import vdm2isa.lex.IsaToken;
import vdm2isa.tr.TRNode;
import vdm2isa.tr.definitions.TRDefinition;
import vdm2isa.tr.definitions.TRDefinitionList;
import vdm2isa.tr.definitions.TRExplicitFunctionDefinition;
import vdm2isa.tr.expressions.visitors.TRExpressionVisitor;
import vdm2isa.tr.definitions.TRStateDefinition;
import vdm2isa.tr.definitions.visitors.TRDefinitionVisitor;
import vdm2isa.tr.patterns.TRPattern;
import vdm2isa.tr.types.TRRecordType;
import vdm2isa.tr.types.TRType;

public class TRStateInitExpression extends TRExpression {

    private final TRPattern initPattern;
    private final TRExpression initExpression;
    private final TRExplicitFunctionDefinition initdef;

    public TRStateInitExpression(TCStateInitExpression tc, TRStateDefinition state) {
        super(state.initExpression.location, (TCExpression)tc, state.initExpression.getType());
        this.initPattern = state.initPattern;
        this.initExpression = state.initExpression;
        this.initdef = state.initdef;

    }

    @Override
    public void setup()
    {
        super.setup();
        // anything specific to check?
        // * look into TRTypeDefinition for implicitly creating init expression if empty
        // * need to worry about state invariant implicit check see TRTypeDefinition for it  
        // * arguably you could perhaps think of extending TRTypeDefinition 

        TRNode.setup(initExpression,initPattern, initdef); //initPattern, initExpression
    }


    @Override
    public <R, S> R apply(TRExpressionVisitor<R, S> visitor, S arg) {
        // TODO Auto-generated method stub
        return visitor.createStateInitExpression(this, arg);
    }

    @Override
    public IsaToken isaToken() {
        // TODO Auto-generated method stub
        return IsaToken.EOF;
    }

    @Override
    public String translate() {
        // TODO Auto-generated method stub
        return "init " + initPattern.translate() + "expr " + initExpression.translate();
    }

}