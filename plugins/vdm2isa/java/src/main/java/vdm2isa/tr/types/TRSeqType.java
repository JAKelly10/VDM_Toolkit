package vdm2isa.tr.types;

import com.fujitsu.vdmj.tc.types.TCSeqType;
import com.fujitsu.vdmj.tc.types.TCType;
import com.fujitsu.vdmj.lex.LexLocation;
import com.fujitsu.vdmj.tc.types.TCSeq1Type;

import vdm2isa.lex.IsaToken;
import vdm2isa.tr.definitions.TRDefinitionList;
import vdm2isa.tr.types.visitors.TRTypeVisitor;

public class TRSeqType extends TRType
{
	private static final long serialVersionUID = 1L;
    public final TRType seqof;
    public final boolean seq1;

    /**
     * Constructor needed for TRUnionType
     * @param location
     * @param seqof
     * @param seq1
     */
    public TRSeqType(TCType vdmType, TRDefinitionList definitions, TRType seqof, boolean seq1)
    {
        super(vdmType, definitions);
        this.seqof = seqof;
        this.seq1 = seq1;
    }

    public TRSeqType(TCSeqType owner, TRDefinitionList definitions, TRType seqof)    
    {
        this(owner, definitions, seqof, false);
    }

    public TRSeqType(TCSeq1Type owner, TRDefinitionList definitions, TRType seqof)    
    {
        this(owner, definitions, seqof, true);
    }

    @Override
    public IsaToken isaToken() {
        return seq1 ? IsaToken.SEQ1 : IsaToken.SEQ;
    }

    @Override
    public String translate() {
        return seqof.translate() + " " + isaToken().toString();
    }

    @Override
    public String invTranslate(String varName) {
        return IsaToken.parenthesise(IsaToken.INV.toString() + isaToken().toString() + 
            "' " + seqof.invTranslate(null) + (varName != null ? " " + varName : ""));
    }

	@Override
	public <R, S> R apply(TRTypeVisitor<R, S> visitor, S arg)
	{
		return visitor.caseSeqType(this, arg);
	}
}
