package vdm2isa.tr.expressions;

import vdm2isa.lex.IsaToken;
import vdm2isa.lex.TRIsaVDMCommentList;
import vdm2isa.messages.IsaErrorMessage;
import vdm2isa.messages.IsaInfoMessage;
import vdm2isa.messages.IsaWarningMessage;
import vdm2isa.tr.TRNode;
import vdm2isa.tr.expressions.TRStatement;
import vdm2isa.tr.expressions.TRStatementList;
import vdm2isa.tr.definitions.TRDefinitionList;
import vdm2isa.tr.expressions.visitors.TRExpressionVisitor;
import vdm2isa.tr.patterns.TRMultipleBindList;
import vdm2isa.tr.patterns.TRPattern;
import vdm2isa.tr.patterns.TRPatternListList;
import vdm2isa.tr.patterns.TRPatternContext;
import vdm2isa.tr.types.TRType;

import com.fujitsu.vdmj.lex.LexLocation;
import com.fujitsu.vdmj.tc.statements.TCBlockStatement;
import com.fujitsu.vdmj.tc.statements.TCStatement;

public class TRBlockStatement extends TRSimpleBlockStatement
{
    public TRDefinitionList assignmentDefs;

    public TRBlockStatement(LexLocation location, TRIsaVDMCommentList comments,TCBlockStatement exp, TRType stmttype, TRStatementList statements, TRDefinitionList assignmentDefs)
    {
        super(location, comments, exp, stmttype, statements);
        this.assignmentDefs = assignmentDefs;
    }

    @Override
    public void setup(){
        super.setup();
        TRNode.setup(assignmentDefs);
    }
    
}
