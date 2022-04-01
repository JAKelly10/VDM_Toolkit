package vdm2isa.tr.definitions;

import com.fujitsu.vdmj.tc.definitions.TCAccessSpecifier;
import com.fujitsu.vdmj.tc.definitions.TCDefinition;
import com.fujitsu.vdmj.tc.definitions.TCExplicitOperationDefinition;
import com.fujitsu.vdmj.tc.lex.TCNameList;
import com.fujitsu.vdmj.tc.lex.TCNameToken;
import com.fujitsu.vdmj.typechecker.NameScope;
import com.fujitsu.vdmj.lex.LexLocation;

import vdm2isa.lex.IsaToken;
import vdm2isa.lex.TRIsaVDMCommentList;
import vdm2isa.messages.IsaErrorMessage;
import vdm2isa.messages.IsaInfoMessage;

import vdm2isa.tr.TRNode;

import vdm2isa.tr.annotations.TRAnnotationList;
import vdm2isa.tr.definitions.visitors.TRDefinitionVisitor;
import vdm2isa.tr.definitions.TRExplicitFunctionDefinition;
import vdm2isa.tr.definitions.TRStateDefinition;
import vdm2isa.tr.definitions.TRDefinitionList;
import vdm2isa.tr.definitions.TRDefinition;
import vdm2isa.tr.expressions.TRApplyExpression;
import vdm2isa.tr.expressions.TRExpression;
import vdm2isa.tr.expressions.TRExpressionList;
import vdm2isa.tr.patterns.TRBasicPattern;
import vdm2isa.tr.patterns.TRPattern;
import vdm2isa.tr.patterns.TRPatternList;
import vdm2isa.tr.patterns.TRPatternListList;
import vdm2isa.tr.patterns.TRUnionContext;
import vdm2isa.tr.types.TRFunctionType;
import vdm2isa.tr.types.TRType;
import vdm2isa.tr.types.TRTypeList;
import vdm2isa.tr.types.TRTypeSet;
import vdm2isa.tr.types.TROperationType;

import vdm2isa.tr.definitions.visitors.TRDefinitionVisitor;


public class TRExplicitOperationDefinition extends TRDefinition {

    private static final long serialVersionUID = 1L;
	public TROperationType type;
	public final TRTypeList unresolved;
	public final TRPatternList parameterPatterns;
	public final TRExpression precondition;
	public final TRExpression postcondition;
	//public final TRStatement body;

	public TRExplicitFunctionDefinition predef;
	public TRExplicitFunctionDefinition postdef;
	public TRDefinitionList paramDefinitions;
	public TRStateDefinition state;

	private TRType actualResult = null;
	public boolean isConstructor = false;
	public TRTypeSet possibleExceptions = null;

    public TRExplicitOperationDefinition(
        TCExplicitOperationDefinition definition,
        TRIsaVDMCommentList comments,
        TRAnnotationList annotations,
        TCNameToken name,
        NameScope nameScope, 
        boolean used, 
        boolean excluded,

        // For this class
        TRPatternList parameterPatterns,
        TRDefinitionList paramDefinitions,
        TROperationType type,
        TRTypeList unresolved,
        // TRStatement body,
        TRExpression precondition,
        TRExpression postcondition, 
        TRExplicitFunctionDefinition predef,
        TRExplicitFunctionDefinition postdef,
        TRStateDefinition state,
        boolean isConstructor
    ) {
        super(definition, name != null ? name.getLocation() : LexLocation.ANY, comments, annotations, name, nameScope, used, excluded);
        this.parameterPatterns = parameterPatterns;
        this.paramDefinitions = paramDefinitions;
        this.type = type;
		this.unresolved = unresolved;

        this.precondition = precondition;
        this.postcondition = postcondition;
        this.predef = predef;
        this.postdef = postdef;
        this.state = state;
        this.isConstructor = isConstructor;

    }

    @Override
    public void setup(){
        super.setup();
        setFormattingSeparator("\n\t");
        TRNode.setup(predef, postdef, precondition, postcondition, type, state, paramDefinitions, parameterPatterns, unresolved);
    }

    @Override
    public IsaToken isaToken() {
        return IsaToken.EOF;
    }

    @Override
    public <R, S> R apply(TRDefinitionVisitor<R, S> visitor, S arg) {
        return visitor.caseExplicitOperationDefinition(this, arg);
    }

    @Override
    public String translate() {
        StringBuilder sb = new StringBuilder();
        // translate the precondition
		if (predef != null) 
		{
			sb.append(predef.translate());
			sb.append("\n");
		} else {
            TRFunctionType invType = TRFunctionType.getInvariantType(type);
            TRPatternListList parameters = TRPatternListList.newPatternListList(TRBasicPattern.dummyPattern(location, false));
            
            predef = TRExplicitFunctionDefinition.createUndeclaredSpecification(
                name, nameScope, used, excluded, null, invType, false , parameters, 
                new TRDefinitionListList(), TRSpecificationKind.PRE
            );

            sb.append(predef.translate());
			sb.append("\n");
        }

		// translate the postcondition
		if (postdef != null)
		{
			sb.append(postdef.translate());
			sb.append("\n");
		}

        sb.append(super.translate());
        sb.append(parameterPatterns.translate());
        sb.append(type.translate());
        
        return sb.toString();
    }
    
    @Override 
    public String toString()
    {
        return super.toString();
    }

}
