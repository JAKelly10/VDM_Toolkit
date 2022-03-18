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
import vdm2isa.tr.TRNode;
import vdm2isa.tr.annotations.TRAnnotationList;
import vdm2isa.tr.definitions.visitors.TRDefinitionVisitor;
import vdm2isa.tr.expressions.TRBinaryExpression;
import vdm2isa.tr.expressions.TRExpression;
import vdm2isa.tr.expressions.TRStateInitExpression;
import vdm2isa.tr.patterns.TRPattern;
import vdm2isa.tr.types.TRRecordType;

public class TRStateDefinition extends TRAbstractTypedDefinition {

    // did we forget to bring across inv information?
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
        

        // * need to worry about state invariant implicit check see TRTypeDefinition for it  
        // Can be handled in the actual translation as we need the implicit checks anyway

        // * arguably you could perhaps think of extending TRTypeDefinition 

        TRNode.setup(recordType, statedefs, initPattern, initExpression, initdef, invPattern, invExpression);

        if (!validInitExpression()){
            report(IsaErrorMessage.VDMSL_INVALID_STATE_INIT_1P, name);
        }
        TRNode.setup(recordType, initPattern, initExpression, initdef, statedefs);
    }

    private boolean validInitExpression()
    {
        return initExpression instanceof TRBinaryExpression && 
            IsaToken.from(((TRBinaryExpression)initExpression).op).equals(IsaToken.EQUALS);
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

    // public String translateInv(){
    //     StringBuilder sb = new StringBuilder();
    //     sb.append("definition\n\tinv_State :: \"State \\<Rightarrow> bool\"\nwhere\n\tinv_State "+ name +" \\<equiv>\n\t\t");
    //     // lots of brackets on this one and for some reason it seems to put the final one on a new line by its self
    //     sb.append(recordType.invTranslate());

    //     if(invExpression != null){
    //         sb.append(IsaToken.SPACE.toString());
    //         sb.append(IsaToken.AND.toString());

    //         sb.append("\n\t\t");
    //         // Think this needs to be processed so that it has the record with name of the variable
    //         sb.append(invExpression.translate());
    //     }
    //     sb.append("\n");
    //     return sb.toString();
    // }

    public String translateInit(){
        if(initExpression instanceof TRBinaryExpression){
            // annoyingly initdefs actually returns the comments shown below which gives it a incorrect definition of the init function.
            StringBuilder sb = new StringBuilder();
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
            //sb.append(IsaToken.WHERE.toString());
            sb.append("\nwhere\n\t");
            sb.append("init_" + name);
            sb.append(IsaToken.SPACE.toString());
            sb.append(IsaToken.EQUALSEQUALS.toString());
            sb.append("\n\t\t");
            // need to work out how to get the right hand side only of this expression as this will then always work as long as the expression can be translated
            // Hacky way to get RHS of expression probably should change to a better way using the parse tree.1
            sb.append(getInitExpression().right.translate());
            sb.append("\n");
            return sb.toString();
        } else {
            return initdef.translate();
        }
    }
}
