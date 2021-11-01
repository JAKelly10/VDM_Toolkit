/*******************************************************************************
 *	Copyright (c) 2020 Leo Freitas.
 ******************************************************************************/

package vdm2isa.tr.expressions;

import com.fujitsu.vdmj.tc.expressions.TCMapletExpression;
import com.fujitsu.vdmj.tc.expressions.TCMapletExpressionList;

import vdm2isa.lex.IsaToken;
import vdm2isa.tr.TRMappedList;

public class TRMapletExpressionList extends TRMappedList<TCMapletExpression, TRMapletExpression>
{
	private static final long serialVersionUID = 1L;
	
	protected TRMapletExpressionList()
	{
		super();
		setSemanticSeparator(IsaToken.COMMA.toString());
	}

	public TRMapletExpressionList(TRMapletExpressionList from)
	{
		this();
		addAll(from);
	}

	public TRMapletExpressionList(TCMapletExpressionList list) throws Exception
	{
		super(list);
		setSemanticSeparator(IsaToken.COMMA.toString());
	}
}
