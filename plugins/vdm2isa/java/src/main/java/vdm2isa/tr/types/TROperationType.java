package vdm2isa.tr.types;

import com.fujitsu.vdmj.tc.types.TCType;
import com.fujitsu.vdmj.tc.types.TCOperationType;
import com.fujitsu.vdmj.tc.lex.TCNameList;
import com.fujitsu.vdmj.tc.lex.TCNameToken;
import com.fujitsu.vdmj.typechecker.NameScope;
import com.fujitsu.vdmj.lex.LexLocation;

import vdm2isa.lex.IsaToken;
import vdm2isa.lex.TRIsaVDMCommentList;
import vdm2isa.messages.IsaErrorMessage;
import vdm2isa.messages.IsaInfoMessage;

import vdm2isa.tr.TRNode;
import vdm2isa.tr.definitions.TRDefinitionList;
import vdm2isa.tr.types.TRType;
import vdm2isa.tr.types.TRAbstractInnerTypedType;
import vdm2isa.tr.types.TRTypeList;

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

    @Override
	public void setup()
	{
		super.setup();
		setFormattingSeparator("\n\t");
		// presume that all function types will be curried
		TRNode.setup(parameters); 
		parameters.setCurried(true);
		//System.out.println(toString());
	}

    @Override
	public <R, S> R apply(TRTypeVisitor<R, S> visitor, S arg)
	{
		return visitor.caseOperationType(this, arg);
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

    @Override
	public String invTranslate(String varName) {
        return "Operation inv translation";
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
