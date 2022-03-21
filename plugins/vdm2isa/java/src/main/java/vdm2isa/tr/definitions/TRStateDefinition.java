package vdm2isa.tr.definitions;

import com.fujitsu.vdmj.lex.LexLocation;
import com.fujitsu.vdmj.lex.Token;
import com.fujitsu.vdmj.tc.definitions.TCDefinition;
import com.fujitsu.vdmj.tc.definitions.TCStateDefinition;
import com.fujitsu.vdmj.tc.lex.TCNameToken;
import com.fujitsu.vdmj.typechecker.NameScope;

import vdm2isa.lex.IsaToken;
import vdm2isa.lex.TRIsaVDMCommentList;
import vdm2isa.messages.IsaErrorMessage;
import vdm2isa.messages.IsaInfoMessage;
import vdm2isa.tr.TRNode;
import vdm2isa.tr.annotations.TRAnnotationList;
import vdm2isa.tr.definitions.visitors.TRDefinitionVisitor;
import vdm2isa.tr.definitions.TRExplicitFunctionDefinition;
import vdm2isa.tr.definitions.TRSpecificationKind;
import vdm2isa.tr.expressions.TRBinaryExpression;
import vdm2isa.tr.expressions.TRExpression;
import vdm2isa.tr.expressions.TRStateInitExpression;
import vdm2isa.tr.patterns.TRPattern;
import vdm2isa.tr.types.TRType;
import vdm2isa.tr.types.TRRecordType;
import vdm2isa.tr.types.TRInvariantType;
import vdm2isa.tr.types.TRFunctionType;
import vdm2isa.tr.patterns.TRPatternListList;
import vdm2isa.tr.patterns.TRBasicPattern;

public class TRStateDefinition extends TRAbstractTypedDefinition {

    public final TRPattern invPattern;
	public final TRExpression invExpression;
    public final TRPattern initPattern;
    public final TRExpression initExpression;
    public final TRExplicitFunctionDefinition initdef;
    private final TRDefinitionList statedefs;
    private final boolean canBeExecuted;
    public final TRRecordType recordType;

    public TRStateDefinition(
        // all those belong to TRDefinition
        TCStateDefinition definition, 
        LexLocation location, 
        TRIsaVDMCommentList comments,
        TRAnnotationList annotations, 
        TCNameToken name, NameScope nameScope, boolean used, boolean excluded, 
        // this to abstract typed def; here is a record type instead
        TRRecordType recordType,

        // will keep it simple and rely on the TRRecordType structure for the TCStateDefinition 
        // correspondent that will work nicely, given the record translation
        TRPattern invPattern,
        TRExpression invExpression,
        TRPattern initPattern,
        TRExpression initExpression, 
        TRExplicitFunctionDefinition initdef, 
        TRDefinitionList statedefs, 
        boolean canBeExecuted  
        ) 
    {
        super(definition, location, comments, annotations, name, nameScope, used, excluded, recordType);
        this.invPattern = invPattern;
        this.invExpression = invExpression;
        this.initPattern = initPattern;
        this.initExpression = initExpression;
        this.initdef = initdef;
        this.statedefs = statedefs; 
        this.canBeExecuted = canBeExecuted; 
        // see similar exmaple in TRMapType etc. 
        this.recordType = recordType;   // super.type = this.recordType; needed for TR mapping 
    }

    @Override
    public void setup()
    {
        super.setup();
        // anything specific to check?
        // * look into TRTypeDefinition for implicitly creating init expression if empty
        // Do we have to have an init expression? If its empty is a valid translation not instead proving that there is at least one valid state
        

        // * arguably you could perhaps think of extending TRTypeDefinition 

        TRNode.setup(recordType, statedefs, initPattern, initExpression, initdef, invPattern, invExpression);

        if (!validInitExpression()){
            report(IsaErrorMessage.VDMSL_INVALID_STATE_INIT_1P, name);
        } 

        if(needsImplicitlyGeneratedUndeclaredSpecification()){
            System.out.println("Needs spec generated");
            TRType paramType = ((TRInvariantType)recordType).copy(false);
            TRFunctionType invType = TRFunctionType.getInvariantType(paramType);
            TRPatternListList parameters = TRPatternListList.newPatternListList(TRBasicPattern.dummyPattern(location, false));
            
            recordType.setInvariantDefinition(TRExplicitFunctionDefinition.createUndeclaredSpecification(
                name, nameScope, used, excluded, null, invType, false , parameters, 
                new TRDefinitionListList(), TRSpecificationKind.INV
            ));

        }

        TRNode.setup(recordType, initPattern, initExpression, initdef, statedefs);
    }

    private boolean validInitExpression()
    {
        return initExpression instanceof TRBinaryExpression && 
            IsaToken.from(((TRBinaryExpression)initExpression).op).equals(IsaToken.EQUALS);
    }

    protected boolean needsImplicitlyGeneratedUndeclaredSpecification()
	{
		return this.invPattern == null && this.invExpression == null;
	}

    @Override 
    public String toString()
    {
        return super.toString();
    }

    public TRBinaryExpression getInitExpression()
    {
        return (TRBinaryExpression)initExpression;
    }

    @Override
    public <R, S> R apply(TRDefinitionVisitor<R, S> visitor, S arg) {
        return visitor.createStateDefinition(this, arg);
    }

    @Override
    public IsaToken isaToken() {
        return IsaToken.EOF;
    }

    @Override 
    public String translate()
    {
        return super.translate() + recordType.translateTypeTLD() + 
        recordType.translateSpecTLD() + translateInit();
    }

    public String translateInit(){
        if(initExpression == null){
            return "";
        }
        if(initExpression instanceof TRBinaryExpression){
            StringBuilder sb = new StringBuilder();
            sb.append(initdef.translatePreamble());
            sb.append(IsaToken.DEFINITION.toString());
            sb.append("\n\t");
            sb.append("init_" + name);
            sb.append(IsaToken.SPACE.toString());
            sb.append(IsaToken.TYPEOF.toString());
            sb.append(IsaToken.SPACE.toString());
            sb.append(IsaToken.ISAQUOTE.toString());
            sb.append(name);
            sb.append(IsaToken.ISAQUOTE.toString());
            sb.append(IsaToken.SPACE.toString());
            sb.append(this.tldIsaComment());
            sb.append("\nwhere\n\t");
            sb.append("init_" + name);
            sb.append(IsaToken.SPACE.toString());
            sb.append(IsaToken.EQUALSEQUALS.toString());
            sb.append("\n\t\t");
            sb.append(IsaToken.comment(IsaInfoMessage.VDM_EXPLICIT_FUNCTION_USER_DEFINED_BODY_1P.format("init_"+name.toString()), getFormattingSeparator()));
            sb.append("\t");
            sb.append(getInitExpression().right.translate());
            sb.append("\n");
            return sb.toString();
        } else {
            return initdef.translate();
        }
    }
}
