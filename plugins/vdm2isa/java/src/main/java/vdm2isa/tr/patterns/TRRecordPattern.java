package vdm2isa.tr.patterns;

import com.fujitsu.vdmj.lex.LexLocation;
import com.fujitsu.vdmj.tc.lex.TCNameToken;
import com.fujitsu.vdmj.tc.patterns.TCRecordPattern;

import vdm2isa.lex.IsaTemplates;
import vdm2isa.lex.IsaToken;
import vdm2isa.messages.IsaErrorMessage;
import vdm2isa.tr.TRNode;
import vdm2isa.tr.patterns.visitors.TRPatternVisitor;
import vdm2isa.tr.types.TRFieldList;
import vdm2isa.tr.types.TRRecordType;
import vdm2isa.tr.types.TRType;

public class TRRecordPattern extends TRPattern {
    
    private final TCNameToken typename;
    private final TRPatternList plist;
    private final TRType type;
    
    public TRRecordPattern(TCRecordPattern p, LexLocation location, TCNameToken typename, TRPatternList plist, TRType type)
    {
        super(p, location);
        this.typename = typename;
        this.plist = plist;
        this.type = type;
    }
    
    @Override 
    public void setup()
    {
        super.setup();
        if (plist == null || plist.size() == 0)
        report(IsaErrorMessage.ISA_VDM_EMPTYRECORD_PATTERN_1P, String.valueOf(typename));
        setSemanticSeparator(IsaToken.SEMICOLON.toString() + " ");
        TRNode.setup(plist, type);
        //System.out.println(toString());
    }

    @Override 
    public String getPattern()
    {
        return typeAware(IsaToken.bracketit(IsaToken.LRECORD, String.valueOf(plist), IsaToken.RRECORD));
    }

    @Override
    public String toString()
    {
        return super.toString() + 
            "\n\t tname = " + String.valueOf(typename) + 
            "\n\t plist = " + String.valueOf(plist) +  
            "\n\t type  = " + String.valueOf(type); 
    }

    @Override
    public IsaToken isaToken() {
        return IsaToken.RECORD;
    }

    /**
     * Record patterns translate taking a let into context to allow for user names to be available
     * That means, we are effectively exchanging the pattern for a let, given you can't have record
     * patterns in Isabelle.
     */
    // @Override
    // public String translate() {
    //     return getPatternList().recordPatternTranslate();
    // }
     /**
      * Record patterns translate taking a let into context to allow for user names to be available
      * That means, we are effectively exchanging the pattern for a let, given you can't have record
      * patterns in Isabelle.
     */
     @Override
    public String translate() {
         return IsaToken.dummyVarNames(1, location);
    }
 
    protected String fieldNameTranslate(int index, String dummyName)
    {
        assert index >= 0 && index < plist.size();
        StringBuilder sb = new StringBuilder();

        TRPattern p = plist.get(index);
        String patternName;
        String fieldName;
        if (p instanceof TRBasicPattern)
        {
            // for basic pattern, use pattern = (field_RECORD dummyName)
            patternName = p.invTranslate();
        }
        // for record pattern within another, use inner dummies
        else if (p instanceof TRRecordPattern)
        {
            // for record pattern, use inner dummy name to project the other record parts in the field name
            patternName = dummyName + Integer.valueOf(index);
        }
        //else if (p instanceof TRStructuredPattern)
        else //if (p instanceof TRPatternBind)        = error
        {
            patternName = p.invTranslate();
        }
        sb.append(patternName);
        sb.append(IsaToken.SPACE.toString());
        sb.append(IsaToken.EQUALS.toString());
        sb.append(IsaToken.SPACE.toString());

        if (p instanceof TRBasicPattern)
        {
            // for basic pattern, use p = (field_RECORD dummyName)
            TRBasicPattern bp = (TRBasicPattern)p;
            TRFieldList flist = TRRecordType.fieldsOf(this.typename);
            if (flist != null)
            {
                assert index < flist.size();
                fieldName = flist.get(index).getTagName();
            }
            else
            {
                // this can generate an error when the pattern and field names differ
                // A :: a: int inv mk_A(v) == v > 10; fieldName = v_A! instead of a_A!
                fieldName = bp.invTranslate();
                report(IsaErrorMessage.ISA_FIELDEXPR_RECORDNAME_2P, this.typename, type.toString());
            }
        }
        // for record pattern within another, use inner dummies
        else if (p instanceof TRRecordPattern)
        {
            TRRecordPattern rp = (TRRecordPattern)p;
            fieldName = rp.recordPatternTranslate();
        }
        //else if (p instanceof TRStructuredPattern)
        else //if (p instanceof TRPatternBind)        = error
        {
            fieldName = p.invTranslate();
        }
        sb.append(IsaToken.parenthesise(IsaTemplates.isabelleRecordFieldName(typename.toString(), fieldName) + " " + dummyName));
        return sb.toString();
    }

    @Override 
    public boolean hasRecordPatterns()
    {
        return true;
    }

    /**
     * On the actual record pattern, invTranslate its TRPatternList with SEMICOLONS. This sets up the 
     * local declaration context to unpick projected fields. The TRPatternList.recordPatternTranslate()
     * call will handle let-in and parenthesis.   
     * @return
     */
    @Override
    public String recordPatternTranslate()
    {
        StringBuilder sb = new StringBuilder();
        String dummyName = translate();
		if (!plist.isEmpty())
		{
            sb.append(fieldNameTranslate(0, dummyName));
            for (int i=1; i < plist.size(); i++)
			{
                sb.append(getSemanticSeparator());
                sb.append(fieldNameTranslate(i, dummyName));
			}
		}
		return sb.toString();
    }

    @Override
    public <R, S> R apply(TRPatternVisitor<R, S> visitor, S arg) {
        return visitor.caseRecordPattern(this, arg);
    }
}
