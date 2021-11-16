package vdm2isa.tr.patterns;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.fujitsu.vdmj.ast.lex.LexToken;
import com.fujitsu.vdmj.messages.InternalException;
import com.fujitsu.vdmj.tc.expressions.TCBooleanLiteralExpression;
import com.fujitsu.vdmj.tc.patterns.TCMultipleBind;
import com.fujitsu.vdmj.tc.patterns.TCMultipleBindList;

import vdm2isa.lex.IsaToken;
import vdm2isa.messages.IsaErrorMessage;
import vdm2isa.tr.TRMappedList;
import vdm2isa.tr.TRNode;
import vdm2isa.tr.expressions.TRBinaryExpression;
import vdm2isa.tr.expressions.TRExpression;
import vdm2isa.tr.expressions.TRLiteralExpression;
import vdm2isa.tr.expressions.TRNotYetSpecifiedExpression;
import vdm2isa.tr.expressions.TRVariableExpression;
import vdm2isa.tr.types.TRBasicType;
import vdm2isa.tr.types.TRSeqType;
import vdm2isa.tr.types.TRSetType;
import vdm2isa.tr.types.TRType;

public class TRMultipleBindList extends TRMappedList<TCMultipleBind, TRMultipleBind> implements TRRecordContext
{
    private static final long serialVersionUID = 1L;

	private Map<TRMultipleBindKind, SortedSet<Integer>> bindSpread = null;
	private boolean parenthesise;
    
    public TRMultipleBindList()
    {
        super();
    }    

	public TRMultipleBindList(TRMultipleBindList from)  
	{
		this();
		addAll(from);
	}

    public TRMultipleBindList(TCMultipleBind bind) throws Exception
    {
        this(bind.getMultipleBindList()); 
    }

    public TRMultipleBindList(TCMultipleBindList from) throws Exception
	{
		super(from);
	}

	@Override
	protected void setup()
	{
		super.setup();
		setParenthesise(true);
		// multiple type binds are space (not comma) separated
        setSemanticSeparator(" ");
        setFormattingSeparator(" ");
		//Multiple bind list translation must take into consideration the kind of bind within it; for type binds that involves adding inv_T dummy; others just empty
        setInvTranslateSeparator(getFormattingSeparator() + IsaToken.AND.toString() + getFormattingSeparator());
	}

	public boolean getParenthesise()
	{
		return parenthesise;
	}

    public boolean setParenthesise(boolean p) {
		boolean old = p;
		this.parenthesise = p;
		for(TRMultipleBind b : this)
		{
			b.setParenthesise(p);
		}
		return old;
	}

	public String compTranslate(boolean vdmPatternsOnly)
    {
        StringBuilder sb = new StringBuilder();
		if (!isEmpty())
		{
			sb.append(get(0).compTranslate(vdmPatternsOnly));

			for (int i=1; i<size(); i++)
			{
				sb.append(getSemanticSeparator());
				sb.append(getFormattingSeparator());
				sb.append(get(i).compTranslate(vdmPatternsOnly));
			}
		}
		return sb.toString();
    } 

	/**
	 * Returns a map of how many binds in the list have what kind where range contains the indexes of what kind 
	 * @return
	 */
	public Map<TRMultipleBindKind, SortedSet<Integer>> figureBindsOut()
	{
		if (bindSpread == null)
		{
			bindSpread = new HashMap<TRMultipleBindKind, SortedSet<Integer>>();
			// add the zero values as well so that other checks count them up
			bindSpread.put(TRMultipleBindKind.SET, new TreeSet<Integer>());
			bindSpread.put(TRMultipleBindKind.SEQ, new TreeSet<Integer>());
			bindSpread.put(TRMultipleBindKind.TYPE, new TreeSet<Integer>());
			for(int i = 0; i < size(); i++)
			{
				TRMultipleBind b = get(i);
				if (b instanceof TRMultipleSetBind)
					bindSpread.get(TRMultipleBindKind.SET).add(i);
				else if (b instanceof TRMultipleSeqBind)
					bindSpread.get(TRMultipleBindKind.SEQ).add(i);
				else if (b instanceof TRMultipleTypeBind)
					bindSpread.get(TRMultipleBindKind.TYPE).add(i);
				else 
					throw new InternalException(IsaErrorMessage.PLUGIN_NYI_2P.number, "Invalid type bind kind " + b.getClass().getSimpleName());
			}
			assert size() == bindSpread.get(TRMultipleBindKind.SET).size() + 
							 bindSpread.get(TRMultipleBindKind.SEQ).size() + 
							 bindSpread.get(TRMultipleBindKind.TYPE).size();
		}	
		// everyone is accounted for, including zero cases
		assert bindSpread != null && !bindSpread.isEmpty() && 
			   bindSpread.containsKey(TRMultipleBindKind.SET) && 
			   bindSpread.containsKey(TRMultipleBindKind.SEQ) && 
			   bindSpread.containsKey(TRMultipleBindKind.TYPE);
		return Collections.unmodifiableMap(bindSpread);
	}

	/**
	 * Checks whether there are binds of the given kind in the list
	 * @param kind
	 * @return
	 */	
    public boolean foundBinds(TRMultipleBindKind kind) {
		return !figureBindsOut().get(kind).isEmpty();
    }

	public boolean areBindsUniform(TRMultipleBindKind kind) {
		return figureBindsOut().get(kind).size() == size();
	}

	/**
	 * Checks whether all binds are of the same kind in the list 
	 * @param kind
	 * @return
	 */	
	public boolean areBindsUniform() {
		return areBindsUniform(TRMultipleBindKind.SET) || areBindsUniform(TRMultipleBindKind.SEQ) || areBindsUniform(TRMultipleBindKind.TYPE);   
    }

	@Override
	public boolean hasRecordPatterns() {
		boolean result = false;
		for(int i = 0; i < size() && !result; i++)
		{
			result = get(i).hasRecordPatterns();
		}
		return result;
	}

	@Override
    public String recordPatternTranslate() {
		StringBuilder sb = new StringBuilder();
		if (!isEmpty())
		{
			String recTranslate = get(0).recordPatternTranslate();
			sb.append(recTranslate);
			for(int i = 1; i < size(); i++)
			{
				if (!recTranslate.isEmpty())
				{
					// no need for semantic separator since the PatternList keeps all the context in control up to "in" part
					//sb.append(getSemanticSeparator());
					sb.append(getFormattingSeparator());					
				}
				recTranslate = get(i).recordPatternTranslate();
				sb.append(recTranslate);
			}
		}
		return sb.toString();
    }

	private TRExpression patternExpression(TRPattern p, LexToken op, TRExpression rhs)
	{
		assert TRExpression.VALID_BINARY_OPS.contains(IsaToken.from(op)) && IsaToken.from(op).equals(IsaToken.INSET);
		return null;
		//TRBinaryExpression.newBooleanChain(op, args)
		// return new TRBinaryExpression(
		// 			TRVariableExpression.newVariableExpr(p.location, name, original, exptype), op, rhs, 
		// 			TRBasicType.boolType(p.getLocation()));
	}

	private TRExpression patternListExpression(TRPatternList plist, TRExpression rhs)
	{
		TRExpression result;
		return null;
	}

	private TRExpression bindingExpression(int index)
	{
		assert index >= 0 && index < size() && !(get(index) instanceof TRMultipleTypeBind);
		TRMultipleBind b = get(index);
		TRType btype;
		if (b instanceof TRMultipleSetBind)
		{
			TRMultipleSetBind bset = (TRMultipleSetBind)b;
			TRExpression rhs = (TRExpression)bset.getRHS();
			TRSetType stype = (TRSetType)rhs.getType();
			btype = stype.setof;
			assert !bset.plist.isEmpty();
		}
		else if (b instanceof TRMultipleSeqBind)
		{
			TRMultipleSeqBind bseq = (TRMultipleSeqBind)b;
			TRExpression rhs = (TRExpression)bseq.getRHS();
			TRSeqType stype = (TRSeqType)rhs.getType();
			btype = stype.seqof;
			assert !bseq.plist.isEmpty();
		}
		TRExpression result = null;
		return result;				
	}

	public TRExpression getBindingsExpression()
	{
		TRExpression result = TRLiteralExpression.newBooleanLiteralExpression(getLocation(), true);
		if (!isEmpty())
		{
			Map<TRMultipleBindKind, SortedSet<Integer>> structure = figureBindsOut();			
			SortedSet<Integer> binding_indices_of_interest = new TreeSet<Integer>();
			for(int i = 0; i < size(); i++) { binding_indices_of_interest.add(i); }
			binding_indices_of_interest.removeAll(structure.get(TRMultipleBindKind.TYPE));

			if (!binding_indices_of_interest.isEmpty())
			{
				Iterator<Integer> indices = binding_indices_of_interest.iterator();
				
				while (indices.hasNext())
				{
					indices.next();
				}
			}
		}
		else
		{
			// if called on empty, then give up! 
			//result = new TRNotYetSpecifiedExpression(getLocation(), TRExpression.unknownType(getLocation()));
			// Perhaps just say "true"? 
		}
		return result;
	}
	
	public boolean setSetSeqForMultiBindList()
	{
		boolean result = size() == 1 && get(0) instanceof TRMultipleSetBind;
		if (result)
		{
			((TRMultipleSetBind)get(0)).seqBind = true;
		}	
		return result; 
	}

	public boolean isSetSeqBind() {
		return size() == 1 && 
			get(0) instanceof TRMultipleSetBind && 
			((TRMultipleSetBind)get(0)).seqBind;
	}

	/**
	 * Returns the first bind RHS or null for empty
	 * @return
	 */
	public TRNode getRHS()
	{
		return isEmpty() ? null : get(0).getRHS();
	}

	public static String translate(TRMultipleBind... args)
	{
		TRMultipleBindList list = new TRMultipleBindList();
		list.addAll(Arrays.asList(args));
		return list.translate();	
	}
}
