package vdm2isa.tr.expressions;

import com.fujitsu.vdmj.lex.LexLocation;

import vdm2isa.tr.TRNode;
import vdm2isa.tr.definitions.TRDefinition;
import vdm2isa.tr.patterns.TRMultipleBindList;
import vdm2isa.tr.types.TRType;

public abstract class TRAbstractCompExpression extends TRExpression {

    protected final TRExpression first;
    protected final TRMultipleBindList bindings;
    protected final TRExpression predicate;
    protected final TRDefinition def;

    public TRAbstractCompExpression(LexLocation location, 
        TRExpression first, TRMultipleBindList bindings, 
        TRExpression predicate, TRDefinition def, TRType exptype) {
        super(location, exptype);
        this.first = first;
        this.bindings = bindings;
        this.predicate = predicate; 
        this.def = def;
    }

    @Override 
    public void setup()
    {
        super.setup();
        setFormattingSeparator(" ");
        TRNode.setup(first, bindings, predicate, def);
    }

    //TODO?!
    // @Override
    // public TRPatternListList getPatternListList() {
    //     TRPatternListList result = super.getPatternListList();
    //     result.addAll(first.getPatternListList());
    //     result.addAll(predicate.getPatternListList());
    //     bindings.
    //     return result;
    // }


    @Override
    public String toString()
    {
        return getClass().getSimpleName() + 
            "\n\t first = " + String.valueOf(first) + 
            "\n\t binds = " + String.valueOf(bindings) +
            "\n\t pred  = " + String.valueOf(predicate) +
            "\n\t def   = " + String.valueOf(def) +
            "\n\t loc   = " + String.valueOf(getLocation());
    }    
}
