package vdm2isa.tr.types;

import com.fujitsu.vdmj.tc.types.TCType;
import com.fujitsu.vdmj.tc.types.TCOperationType;
import com.fujitsu.vdmj.tc.lex.TCNameList;
import com.fujitsu.vdmj.tc.lex.TCNameToken;
import com.fujitsu.vdmj.typechecker.NameScope;

import javax.xml.transform.Result;

import com.fujitsu.vdmj.lex.LexLocation;

import vdm2isa.lex.IsaToken;
import vdm2isa.lex.TRIsaVDMCommentList;
import vdm2isa.lex.IsaTemplates;
import vdm2isa.messages.IsaErrorMessage;
import vdm2isa.messages.IsaInfoMessage;

import vdm2isa.tr.TRNode;
import vdm2isa.tr.definitions.TRDefinitionList;
import vdm2isa.tr.definitions.TRStateDefinition;
import vdm2isa.tr.types.TRType;
import vdm2isa.tr.types.TRAbstractInnerTypedType;
import vdm2isa.tr.types.TRTypeList;
import vdm2isa.tr.types.TRVoidType;

import vdm2isa.tr.types.visitors.TRTypeVisitor;

public class TROperationType extends TRAbstractInnerTypedType{

    private static final long serialVersionUID = 1L;
	public TRTypeList parameters;
	public boolean pure;

	public TROperationType(TCOperationType vdmType, TRDefinitionList definitions, TRTypeList parameters, TRType result, boolean pure)
	{
		super(vdmType, definitions, result);
		this.parameters = parameters;
		this.pure = pure;
	}

	static private TRType NoVoidReturn(TRType result)
	{
		if(result instanceof TRVoidType)
		{
			TRType paramType = TRStateDefinition.state.recordType.copy(false);
			return paramType;
		} 
		return result;
	}

	private void StateAddedParameters()
	{
		TRType paramType = TRStateDefinition.state.recordType.copy(false);
		parameters.add(paramType);
	}

    @Override
	public void setup()
	{
		super.setup();
		setFormattingSeparator("\n\t");
		// presume that all function types will be curried
		//this.parameters = StateAddedParameters(param);
		StateAddedParameters();
		TRNode.setup(parameters);
		if(getInnerType() instanceof TRVoidType)
		{
			TRType paramType = TRStateDefinition.state.recordType.copy(false);
			setInnerType(paramType);
		} 
		TRNode.setup(getInnerType());
		parameters.setCurried(true);
		//System.out.println(toString());
	}

    @Override
	public <R, S> R apply(TRTypeVisitor<R, S> visitor, S arg)
	{
		return visitor.caseOperationType(this, arg);
	}

	public TRType getResultType()
	{
		return getInnerType();//result;//getInnerType();
	}

    
    @Override
    public String translate(){
        StringBuilder sb = new StringBuilder();
		sb.append(parameters.translate());
		sb.append(IsaToken.SPACE.toString());
		sb.append(isaToken().toString());
		sb.append(IsaToken.SPACE.toString());
		sb.append(getInnerType().translate());
		return sb.toString();

    }

	protected String paramInvTranslate(int index)
	{
		assert index >= 0 && index < parameters.size();
        StringBuilder sb = new StringBuilder();
		//if (isParametricInvariantType())
		//{
			// only one parameter
			//assert index == 0 && parameters.get(index) instanceof TRParameterType;
			//sb.append(parameters.get(index).invTranslate(null));
		//}
        if (!isParametricInvariantType())
		{
			sb.append(getInvTypeString());
			sb.append(IsaToken.SPACE.toString());
		}
		sb.append(parameters.get(index).invTranslate(null));
		return sb.toString();
	}

    @Override
	protected String getInvTypeString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(IsaToken.INV.toString());
		// transform "lambda" => "Lambda" for inv_Lambda call
		int i = sb.length();
		sb.append(IsaToken.LAMBDA.vdmToken().toString());
		sb.setCharAt(i, Character.toUpperCase(sb.charAt(i)));
		return sb.toString();
    }

	@Override
	public String invTranslate(String varName) {
        StringBuilder sb = new StringBuilder();
		sb.append(paramInvTranslate(0));
		for(int i = 1; i < parameters.size(); i++)
		{
			sb.append(IsaToken.SPACE.toString());
			sb.append(IsaToken.LPAREN.toString());
			sb.append(paramInvTranslate(i));
		}
		sb.append(IsaToken.SPACE.toString());
		sb.append(getResultType().invTranslate(null));
		if (parameters.size() > 1)
		{
			sb.append(IsaTemplates.replicate(IsaToken.RPAREN.toString(), parameters.size()-1));
		}
		sb.append(varName != null ? varName : "");
		return IsaToken.parenthesise(sb.toString());
		// // function type invariants are implicit? e.g. v = (lambda x: nat, y: nat & x + y)
		// // we can't really check inv_VDMNat1 of x or y; that's the LambdaExpression's job
		// // we must, however, check the type invariant of the result!
		// // that also means, the declaring party must take that into account in the inv_XXX def!
		// // e.g. inv_v x y == "inv_VDMNat (v x y)"
		// String rVarName = varName != null ? dummyVarNames(varName) : varName;
		// StringBuilder sb = new StringBuilder();
		// sb.append(getFormattingSeparator());

		// //sb.append(IsaToken.comment(IsaInfoMessage.VDM_LAMBDA_INVARIANT.toString()));		
		// sb.append(getFormattingSeparator());
		// sb.append(IsaToken.parenthesise(
		// 	IsaToken.INV.toString() + IsaToken.LAMBDA.toString() + IsaToken.SPACE.toString() +
		// 	getInnerType().invTranslate(null) + IsaToken.SPACE.toString() + getResultType().invTranslate(null) +
		// 	(rVarName == null ? "" : IsaToken.SPACE.toString() + rVarName))
		// );
		// return sb.toString();
	}

	/**
	 * Type parametric invariant types inv_T for generic type @T have function type (@T => bool). 
	 * @return
	 */
	public boolean isParametricInvariantType()
	{
		return parameters.size() == 1 && parameters.get(0) instanceof TRParameterType && getResultType().isBooleanType();
	}


    @Override
    public TRType copy(boolean atTLD){
        //TODO need to actually implement this deep copy
        return this;
    }

    @Override
    public IsaToken isaToken() {
        return IsaToken.TFUN;
    }
 


}
