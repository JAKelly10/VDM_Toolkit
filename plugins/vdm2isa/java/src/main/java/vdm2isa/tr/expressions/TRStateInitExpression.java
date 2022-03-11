package vdm2isa.tr.expressions;

import com.fujitsu.vdmj.lex.LexLocation;
import com.fujitsu.vdmj.tc.expressions.TCExpression;
import com.fujitsu.vdmj.tc.expressions.TCStateInitExpression;

import vdm2isa.lex.IsaToken;
import vdm2isa.tr.TRNode;
import vdm2isa.tr.definitions.TRExplicitFunctionDefinition;
import vdm2isa.tr.expressions.visitors.TRExpressionVisitor;
import vdm2isa.tr.definitions.TRStateDefinition;
import vdm2isa.tr.patterns.TRPattern;
import vdm2isa.tr.types.TRType;


public class TRStateInitExpression extends TRExpression {

    //This has no access to information as in VDMJ it is purely used as a type checker and nothing else. 
    //All of the logic is in StateDefinition
    public TRStateInitExpression(TCStateInitExpression tc, TRType exptype) {
        super(tc != null && tc.location != null ? tc.location : LexLocation.ANY, (TCExpression)tc, exptype);
    }

    @Override
    public void setup()
    {
        super.setup();
        // anything specific to check?
        // * look into TRTypeDefinition for implicitly creating init expression if empty
        // * need to worry about state invariant implicit check see TRTypeDefinition for it  
        // * arguably you could perhaps think of extending TRTypeDefinition 

        //TRNode.setup(initExpression,initPattern, initdef); //initPattern, initExpression
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
        return "init ";
    }

}