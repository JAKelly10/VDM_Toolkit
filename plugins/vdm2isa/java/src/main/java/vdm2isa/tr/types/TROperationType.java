package vdm2isa.tr.types;

import com.fujitsu.vdmj.tc.types.TCType;
import com.fujitsu.vdmj.tc.types.TCOperationType;
import com.fujitsu.vdmj.tc.types.TCFunctionType;
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

public class TROperationType extends TRFunctionType{

    private static final long serialVersionUID = 1L;
	// public TRTypeList parameters;
	public boolean pure;

	public TROperationType(TCOperationType vdmType, TRDefinitionList definitions, TRTypeList parameters, TRType result, boolean pure)
	{
		super(vdmType, definitions, parameters, false, result, false);
		// this.parameters = parameters;
		this.pure = pure;
	}

	private void StateAddedParameters()
	{
		TRType paramType = TRStateDefinition.state.recordType.copy(false);
		parameters.add(paramType);
	}

    @Override
	public void setup()
	{
		StateAddedParameters();
		if(getInnerType() instanceof TRVoidType)
		{
			TRType paramType = TRStateDefinition.state.recordType.copy(false);
			setResult(paramType);
		} 
		super.setup();
		setFormattingSeparator("\n\t");
		parameters.setCurried(true);

	}

    @Override
	public <R, S> R apply(TRTypeVisitor<R, S> visitor, S arg)
	{
		return visitor.caseOperationType(this, arg);
	}

	@Override
	public TRType getResultType()
	{
		return getInnerType();//result;//getInnerType();
	}

	@Override
	public TCType getVDMFunctionType()
	{
		return (TCOperationType)getVDMType();
	}

	@Override
	public TCFunctionType getVDMFunctionPreType()
	{
		return ((TCFunctionType)getVDMType()).getPreType();
	}

	@Override
	public TCFunctionType getVDMFunctionPostType()
	{
		return ((TCFunctionType)getVDMType()).getPostType();
	}

	@Override
	public TCFunctionType getVDMFunctionCurriedPreType(boolean isCurried)
	{
		return getVDMFunctionPreType();
	}

	@Override
	public TCFunctionType getVDMFunctionCurriedPostType(boolean isCurried)
	{
		return getVDMFunctionPostType();
	}

    @Override
	public TCFunctionType getVDMFunctionMeasureType(boolean isCurried, TCType actual)
	{
		return null;
	}

}
