package vdm2isa.tr.expressions;

import vdm2isa.lex.IsaToken;
import vdm2isa.lex.TRIsaVDMCommentList;
import vdm2isa.messages.IsaErrorMessage;
import vdm2isa.messages.IsaInfoMessage;
import vdm2isa.messages.IsaWarningMessage;
import vdm2isa.tr.expressions.TRExpression;
import vdm2isa.tr.expressions.TRBinaryExpression;
import vdm2isa.tr.definitions.TRStateDefinition;
import vdm2isa.tr.expressions.visitors.TRExpressionVisitor;
import vdm2isa.tr.patterns.TRMultipleBindList;
import vdm2isa.tr.patterns.TRRecordPattern;
import vdm2isa.tr.patterns.TRPatternListList;
import vdm2isa.tr.types.TRRecordType;
import vdm2isa.tr.types.TRType;

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
    public TRType targetType;
	public TRType expType;

    public TRAssignmentStatement(LexLocation location, TRIsaVDMCommentList comments, TCAssignmentStatement as, TRType stmttype, TRStateDesignator target, TRExpression exp, TRType targetType, TRType expType)
	{
		super(location, comments, as, stmttype);
        this.exp = exp;
        this.target = target;
        this.expType = expType;
        this.targetType = targetType;
	}

    public <R, S> R apply(TRExpressionVisitor<R, S> visitor, S arg) 
    {
        // TODO: UPDATE THIS
        return visitor.caseTRStatement(this, arg);
    }

    public String translate()
    {
        TRPatternListList patternContextProjection = TRPatternListList.newPatternListList(TRRecordPattern.RecordPatternGenerator(TRStateDefinition.state.recordType,TRStateDefinition.state.recordType.location));
        // if(exp instanceof TRBinaryExpression) {
        //     System.out.println(((TRBinaryExpression)exp).toString());
        //     System.out.println(((TRBinaryExpression)exp).left.toString());
        //     System.out.println(((TRBinaryExpression)exp).right.toString());
        // }
        //System.out.println(TRStateDefinition.state.recordType.getInvDef().getParameters().patternContextTranslate(null));
        // Check Parattern context is the correct dummy?
        return super.translate() +  IsaToken.LET.toString() + IsaToken.SPACE.toString() + 
        TRStateDefinition.state.name.toString() + 
        IsaToken.SPACE.toString() + IsaToken.EQUALS.toString() + IsaToken.SPACE.toString() +
        TRStateDefinition.state.name.toString() + // Got to be same name as state param
        IsaToken.LPAREN.toString() + target.translate() + IsaToken.RECORD_MODIFIER.toString() + exp.translate() + IsaToken.RPAREN.toString() +
        IsaToken.SPACE.toString() + IsaToken.IN.toString() + getFormattingSeparator() +
        patternContextProjection.patternContextTranslate(null);
    }

    public IsaToken isaToken() {
        return IsaToken.EOF;
    }

    
}
