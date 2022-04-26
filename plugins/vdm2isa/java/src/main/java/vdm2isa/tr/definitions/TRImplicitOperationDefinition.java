package vdm2isa.tr.definitions;

import com.fujitsu.vdmj.tc.definitions.TCAccessSpecifier;
import com.fujitsu.vdmj.tc.definitions.TCDefinition;
import com.fujitsu.vdmj.tc.definitions.TCImplicitOperationDefinition;
import com.fujitsu.vdmj.tc.lex.TCNameList;
import com.fujitsu.vdmj.tc.lex.TCNameToken;
import com.fujitsu.vdmj.typechecker.NameScope;
import com.fujitsu.vdmj.lex.LexLocation;

import vdm2isa.lex.IsaToken;
import vdm2isa.lex.TRIsaVDMCommentList;
import vdm2isa.lex.IsaTemplates;
import vdm2isa.lex.IsaItem;
import vdm2isa.messages.IsaErrorMessage;
import vdm2isa.messages.IsaInfoMessage;

import vdm2isa.tr.TRNode;

import vdm2isa.tr.annotations.TRAnnotationList;
import vdm2isa.tr.definitions.visitors.TRDefinitionVisitor;
import vdm2isa.tr.definitions.TRExplicitFunctionDefinition;
import vdm2isa.tr.definitions.TRStateDefinition;
import vdm2isa.tr.definitions.TRDefinitionList;
import vdm2isa.tr.definitions.TRDefinition;
import vdm2isa.tr.definitions.TRExplicitOperationDefinition;
import vdm2isa.tr.expressions.TRApplyExpression;
import vdm2isa.tr.expressions.TRExpression;
import vdm2isa.tr.expressions.TRExpressionList;
import vdm2isa.tr.expressions.TRStatement;
import vdm2isa.tr.patterns.TRBasicPattern;
import vdm2isa.tr.patterns.TRPattern;
import vdm2isa.tr.patterns.TRPatternList;
import vdm2isa.tr.patterns.TRPatternListList;
import vdm2isa.tr.patterns.TRUnionContext;
import vdm2isa.tr.types.TRPatternListTypePairList;
import vdm2isa.tr.types.TRFunctionType;
import vdm2isa.tr.types.TRType;
import vdm2isa.tr.types.TRTypeList;
import vdm2isa.tr.types.TRTypeSet;
import vdm2isa.tr.types.TROperationType;
import vdm2isa.tr.types.TRVoidType;

import vdm2isa.tr.definitions.visitors.TRDefinitionVisitor;


public class TRImplicitOperationDefinition extends TRExplicitOperationDefinition {


    private TRPatternListTypePairList parameterPatterns;

    public TRImplicitOperationDefinition(
        TCImplicitOperationDefinition definition,
        TRIsaVDMCommentList comments,
        TRAnnotationList annotations,
        TCNameToken name,
        NameScope nameScope, 
        boolean used, 
        boolean excluded,

        // For this class
        TRPatternListTypePairList parameterPatterns,
        TROperationType type,
        TRTypeList unresolved,
        TRStatement body,
        TRExpression precondition,
        TRExpression postcondition, 
        TRExplicitFunctionDefinition predef,
        TRExplicitFunctionDefinition postdef,
        TRStateDefinition state
    ) {
        super(
            (TCDefinition) definition,
            comments,
            annotations,
            name,
            nameScope, 
            used, 
            excluded,

            // For this class
            parameterPatterns != null ? parameterPatterns.getParamPatternListList().getFlatPatternList() : null,
            new TRDefinitionList(),
            type,
            unresolved,
            body,
            precondition,
            postcondition, 
            predef,
            postdef,
            state,
            false,
            false
        );

        this.parameterPatterns = parameterPatterns;
    }

    @Override
    public void setup(){
        super.setup();
        setFormattingSeparator("\n\t");
        TRNode.setup(this.parameterPatterns);
    }

    @Override
    public <R, S> R apply(TRDefinitionVisitor<R, S> visitor, S arg) {
        return visitor.caseImplicitOperationDefinition(this, arg);
    } 
    
    @Override 
	public boolean isImplicitFunction()
	{
		return true;
	}
}
