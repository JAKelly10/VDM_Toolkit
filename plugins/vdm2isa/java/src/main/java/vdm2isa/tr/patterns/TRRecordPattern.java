package vdm2isa.tr.patterns;

import java.util.Arrays;

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

public class TRRecordPattern extends TRAbstractContextualPattern {
    
    private final TCNameToken typename;
    private final TRType type;
    
    public TRRecordPattern(TCRecordPattern p, LexLocation location, TCNameToken typename, TRPatternList plist, TRType type)
    {
        super(p, location, plist);
        this.typename = typename;
        this.type = type;
    }
    
    @Override 
    public void setup()
    {
        super.setup();
        setSemanticSeparator(IsaToken.SEMICOLON.toString() + IsaToken.SPACE.toString());
        TRNode.setup(type);
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

    @Override
    protected  String indexedPatternExpression(int index, String dummyName)
    {
        assert index >= 0 && index < plist.size();
        String fieldName;
        // getting the field name from the record type
        // this is important given pattern names might not be the same as field name    
        // A :: a: int inv mk_A(v) == v > 10; fieldName = v_A! instead of a_A!
        TRFieldList flist = TRRecordType.fieldsOf(this.typename);
        if (flist != null)
        {
            assert index >= 0 && index < flist.size();
            fieldName = flist.get(index).getTagName();
        }
        else
        {
            // if can't find the fields, then fall back and report error
            fieldName = plist.get(index).invTranslate();
            report(IsaErrorMessage.ISA_FIELDEXPR_RECORDNAME_2P, this.typename, type.toString());
        }

        // project the field out so it's available
        return IsaToken.parenthesise(IsaTemplates.isabelleRecordFieldName(typename.toString(), fieldName) + IsaToken.SPACE.toString() + dummyName);
    }
 
    @Override 
    public boolean hasRecordPattern()
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
    public String recordPatternTranslate(String varName)
    {
        StringBuilder sb = new StringBuilder();
        String dummyName = varName == null ? translate() : varName;
		if (!plist.isEmpty())
		{
            sb.append(indexedPatternTranslate(0, dummyName));
            for (int i=1; i < plist.size(); i++)
			{
                sb.append(getSemanticSeparator());
                sb.append(indexedPatternTranslate(i, dummyName));
			}
		}
		return sb.toString();
    }

    @Override
    public <R, S> R apply(TRPatternVisitor<R, S> visitor, S arg) {
        return visitor.caseRecordPattern(this, arg);
    }

    @Override
    protected String getInvalidPatternMessage() {
        return "VDM record pattern for record type " + typename.toString();
    }
}
