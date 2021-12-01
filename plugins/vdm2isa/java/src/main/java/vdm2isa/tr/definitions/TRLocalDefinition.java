package vdm2isa.tr.definitions;

import com.fujitsu.vdmj.lex.LexLocation;
import com.fujitsu.vdmj.tc.annotations.TCAnnotationList;
import com.fujitsu.vdmj.tc.definitions.TCDefinition;
import com.fujitsu.vdmj.tc.definitions.TCLocalDefinition;
import com.fujitsu.vdmj.tc.lex.TCNameToken;
import com.fujitsu.vdmj.typechecker.NameScope;

import vdm2isa.lex.IsaToken;
import vdm2isa.lex.TRIsaVDMCommentList;
import vdm2isa.tr.definitions.visitors.TRDefinitionVisitor;
import vdm2isa.tr.expressions.TRExpression;
import vdm2isa.tr.patterns.TRUnionContext;
import vdm2isa.tr.types.TRDataType;
import vdm2isa.tr.types.TRType;

/**
 * VDM local definitions are constructed by the type checker for certain kinds of TLDs (e.g. TRValueDefinition, some
 * let-defs, some lambdas, etc.). This is a useful class also to generalise aspects of TRValueDefinition, which are common.
*/
public class TRLocalDefinition extends TRAbstractTypedDefinition {
    
    private static final long serialVersionUID = 1L;

    //For exclusive use of TRDefinitionList test for all being LocalDefinition. OVerkill?
    protected TRLocalDefinition()
    {
        //this(LexLocation.ANY, new TCNameToken(LexLocation.ANY, ASTModule.defaultName(LexLocation.ANY).name, "default"), null);
        this(null, LexLocation.ANY, null, null, null, null, false, false, null);
    }

    protected TRLocalDefinition(
        TCDefinition definition, 
        LexLocation location, 
        TRIsaVDMCommentList comments, 
        TCAnnotationList annotations, 
        TCNameToken name, 
        NameScope nameScope, 
        boolean used, 
        boolean excluded,
        TRType type)
    {
        super(definition, location, comments, annotations, name, nameScope, used, excluded, type);
    }

    // name might be null in some cases. See TRLocalDefinition and TRValueDefinition
    public TRLocalDefinition(TCLocalDefinition definition,
        LexLocation location, 
        TRIsaVDMCommentList comments, 
        TCAnnotationList annotations, 
        TCNameToken name, 
        NameScope nameScope, 
        boolean used, 
        boolean excluded,
        TRType type)
    {
        // TRValueDefinition have patterns hence can pass name as null; it will have to change its own locality further
        this((TCDefinition)definition, location, comments, annotations, name, nameScope, used, excluded, type);
    }

    @Override
    public IsaToken isaToken()
	{
		return IsaToken.LOCAL;
	}

    @Override
	public String tldLocationTranslate()
    {
        // don't add local definitions locations!
        return ""; 
    }

    /**
     * Create internal translate to allow jumps within the hierarchy
     * @return
     */
    protected String translateNamedType()
    {
		// translate the "v: T"
		StringBuilder sb = new StringBuilder();

        // add any annotations or comments
		sb.append(translatePreamble());
		sb.append(IsaToken.parenthesise(getDeclaredName() + IsaToken.TYPEOF.toString() + getTypeString()));
        return sb.toString();
    }
    
    @Override
    public String translate()
    {
        return translateNamedType();
    }

	@Override
	public <R, S> R apply(TRDefinitionVisitor<R, S> visitor, S arg)
	{
		return visitor.caseLocalDefinition(this, arg);
	}

    /**
     * If the local definition (ultimate) type (i.e. chase renamed types) is a union, then needs union context
     */
    @Override 
	public boolean hasUnionTypes()
	{
        return this.type.ultimateType() instanceof TRDataType;
	}

    /**
     * Strategy for union types context is to use the inner param definition lists within explicit function definitions,
     * which ultimately leads to here.  
     */
	@Override
	public String unionTypesTranslate(TRExpression body, TRUnionContext innerContext)
	{
		StringBuilder sb = new StringBuilder();
        if (hasUnionTypes())
        {
            TRDataType dtype = (TRDataType)type.ultimateType();
            if (dtype.isEnumerated())
            {
                sb.append(body.translate());
            }
            else
            {
                sb.append(IsaToken.CASE.toString());
                sb.append(IsaToken.SPACE.toString());
                sb.append(this.name.toString());
                sb.append(IsaToken.SPACE.toString());
                sb.append(IsaToken.OF.toString());
                sb.append(getFormattingSeparator());
                sb.append(dtype.getDataTypeConstructors().unionTypesTranslate(this.name.toString(), body, innerContext));
            }
        }
        return IsaToken.parenthesise(sb.toString());
	}
}
