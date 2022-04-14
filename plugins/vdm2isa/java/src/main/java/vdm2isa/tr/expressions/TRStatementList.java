package vdm2isa.tr.expressions;

import java.util.Arrays;

import com.fujitsu.vdmj.tc.statements.TCStatement;
import com.fujitsu.vdmj.tc.statements.TCStatementList;

import vdm2isa.tr.TRMappedList;
import vdm2isa.tr.TRNode;
import vdm2isa.tr.expressions.TRStatement;
import vdm2isa.tr.patterns.TRMultipleBind;
import vdm2isa.tr.patterns.TRPatternContext;
import vdm2isa.tr.patterns.TRUnionContext;

public class TRStatementList extends TRMappedList<TCStatement, TRStatement> {
    
    private int currentUnionContext;

	/**
	 * Allow this top-level list to be defined empty
	 */
	public TRStatementList()
	{
		super();
		currentUnionContext = 0;
	}

    public TRStatementList(TRStatementList from)
	{
		this();
		addAll(from);
	}	

    public TRStatementList(TCStatementList list) throws Exception
	{
		super(list);
	}

    public TRStatementList copy()
	{
		TRStatementList result = new TRStatementList(this); 
		TRNode.setup(result);
		return result;
	}

    @Override
	public void setup()
	{
		super.setup();
		setFormattingSeparator("\n\t");
	}

}
