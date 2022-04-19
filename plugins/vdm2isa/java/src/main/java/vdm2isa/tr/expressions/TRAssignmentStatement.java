package vdm2isa.tr.expressions;

import vdm2isa.lex.IsaToken;
import vdm2isa.lex.TRIsaVDMCommentList;
import vdm2isa.messages.IsaErrorMessage;
import vdm2isa.messages.IsaInfoMessage;
import vdm2isa.messages.IsaWarningMessage;
import vdm2isa.tr.TRNode;
import vdm2isa.tr.definitions.TRExplicitFunctionDefinition;
import vdm2isa.tr.expressions.TRExpression;
import vdm2isa.tr.expressions.TRBinaryExpression;
import vdm2isa.tr.expressions.visitors.TRExpressionVisitor;
import vdm2isa.tr.patterns.TRMultipleBindList;
import vdm2isa.tr.patterns.TRPattern;
import vdm2isa.tr.patterns.TRPatternListList;
import vdm2isa.tr.patterns.TRPatternContext;
import vdm2isa.tr.types.TRAbstractInnerTypedType;
import vdm2isa.tr.types.TRInvariantType;
import vdm2isa.tr.types.TRMapType;
import vdm2isa.tr.types.TROptionalType;
import vdm2isa.tr.types.TRRecordType;
import vdm2isa.tr.types.TRSeqType;
import vdm2isa.tr.types.TRSetType;
import vdm2isa.tr.types.TRType;
import vdm2isa.tr.types.TRUnknownType;

import com.fujitsu.vdmj.lex.LexLocation;
import com.fujitsu.vdmj.tc.definitions.TCDefinitionList;
import com.fujitsu.vdmj.tc.statements.TCAssignmentStatement;
import com.fujitsu.vdmj.tc.expressions.visitors.TCFreeVariableExpressionVisitor;
import com.fujitsu.vdmj.tc.lex.TCNameList;
import com.fujitsu.vdmj.tc.lex.TCNameSet;
import com.fujitsu.vdmj.typechecker.Environment;
import com.fujitsu.vdmj.typechecker.FlatEnvironment;


public class TRAssignmentStatement extends TRStatement {

    public TRExpression exp;
    public TRStateDesignator target;

    public TRAssignmentStatement(LexLocation location, TRIsaVDMCommentList comments, TCAssignmentStatement as, TRType stmttype, TRStateDesignator target, TRExpression exp)
	{
		super(location, comments, as, stmttype);
        this.exp = exp;
        this.target = target;
	}

    public <R, S> R apply(TRExpressionVisitor<R, S> visitor, S arg) 
    {
        // TODO: UPDATE THIS
        return visitor.caseTRStatement(this, arg);
    }

    public String translate(){
        if(exp instanceof TRBinaryExpression) {
            System.out.println(((TRBinaryExpression)exp).toString());
            System.out.println(((TRBinaryExpression)exp).left.toString());
            System.out.println(((TRBinaryExpression)exp).right.toString());
        }
        return target.translate() + " Assignemnt " + exp.translate();
    }

    public IsaToken isaToken() {
        return IsaToken.EOF;
    }

    
}
