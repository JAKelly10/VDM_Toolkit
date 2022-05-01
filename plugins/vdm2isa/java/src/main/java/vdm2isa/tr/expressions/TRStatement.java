package vdm2isa.tr.expressions;

import vdm2isa.lex.IsaToken;
import vdm2isa.lex.TRIsaVDMCommentList;
import vdm2isa.messages.IsaErrorMessage;
import vdm2isa.messages.IsaInfoMessage;
import vdm2isa.messages.IsaWarningMessage;
import vdm2isa.tr.TRNode;
import vdm2isa.tr.definitions.TRExplicitFunctionDefinition;
import vdm2isa.tr.definitions.TRStateDefinition;
import vdm2isa.tr.expressions.TRExpression;
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
import com.fujitsu.vdmj.tc.statements.TCStatement;
import com.fujitsu.vdmj.tc.expressions.visitors.TCFreeVariableExpressionVisitor;
import com.fujitsu.vdmj.tc.lex.TCNameList;
import com.fujitsu.vdmj.tc.lex.TCNameSet;
import com.fujitsu.vdmj.typechecker.Environment;
import com.fujitsu.vdmj.typechecker.FlatEnvironment;

// @JK
// Is it ok that we are fudging it into an expression?
public abstract class TRStatement extends TRExpression {

    TRIsaVDMCommentList comments;

    TCStatement expS;

    public TRStatement(LexLocation location, TRIsaVDMCommentList comments, TCStatement exp, TRType stmttype)
	{
		super(location, null, stmttype);
        this.expS = exp;
        this.comments = comments;
	}

    public <R, S> R apply(TRExpressionVisitor<R, S> visitor, S arg) 
    {
        return visitor.caseTRStatement(this, arg);
    }

    public String translate(){
        return comments.translate();// + TRStateDefinition.state.recordType.getInvDef().getParameters().patternContextTranslate(null) + "\n\t";
    }

    public IsaToken isaToken() {
        return IsaToken.EOF;
    }

    
    // public TRExpression(LexLocation location, TCExpression exp, TRType exptype)
	// {
	// 	super(location);
    //     this.exp = exp;
    //     this.exptype = exptype;
    //     this.hasWarnedAboutUnknownType = false;
    //     this.hasWarnedAboutNullType = false;
    //     this.exprFVV = new TCFreeVariableExpressionVisitor(new TCGetFreeVariablesVisitorSet());
    //     //this.fvvENV = new EnvTriple(new FlatEnvironment(new TCDefinitionList()), new FlatEnvironment(new TCDefinitionList()), new AtomicBoolean(false)); 
    //     this.fvvENV = new FlatEnvironment(new TCDefinitionList());
	// }

}
