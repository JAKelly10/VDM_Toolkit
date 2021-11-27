/*******************************************************************************
 *	Copyright (c) 2020 Leo Freitas.
 ******************************************************************************/

package plugins;

import java.util.Iterator;

import com.fujitsu.vdmj.mapper.ClassMapper;
import com.fujitsu.vdmj.messages.Console;
import com.fujitsu.vdmj.messages.InternalException;
import com.fujitsu.vdmj.runtime.Interpreter;
import com.fujitsu.vdmj.runtime.ModuleInterpreter;
import com.fujitsu.vdmj.tc.modules.TCModule;
import com.fujitsu.vdmj.tc.modules.TCModuleList;

import vdm2isa.lex.IsaToken;
import vdm2isa.tr.TRNode;
import vdm2isa.tr.modules.TRModule;
import vdm2isa.tr.modules.TRModuleList;

public class Vdm2isaPlugin extends GeneralisaPlugin
{
	// determines whether to add "pre_f =>" on post condition definitions
	public static boolean linientPost;
	// determines whether to print VDM comments as Isabelle comments
	public static boolean printVDMComments;
	// determines whether to print Isabelle comments generated by translation
	public static boolean printIsaComments;
	// whether to run exu before starting
	public static boolean runExu;
	// whether to generate min/max defs from TRTypeDefinition
	public static boolean translateTypeDefMinMax;
	// whether to print location information per TLD
	public static boolean printLocations;

		/**
	 * Default=true; translate VDM values as Isabelle abbreviations.
	 */
	public static boolean valueAsAbbreviation; 


	private TRModuleList translatedModules;

	public Vdm2isaPlugin(Interpreter interpreter)
	{
		super(interpreter);
		this.translatedModules = new TRModuleList();
	}

	public TRModuleList getTranslatedModules()
	{
		return this.translatedModules;
	}

	@Override 
	protected void localReset()
	{
		super.localReset();
		// no need to clear before construction; already empty
		if (translatedModules != null)
			translatedModules.clear();
	}


    @Override
	//TODO add plugin property about using abbreviations or definitions for TRValueDefinition  
	//TODO add plugin option about raising warnings? or just raise them 
    public boolean isaRun(TCModuleList tclist, String[] argv) throws Exception 
	{
		boolean result = false;
		if (interpreter instanceof ModuleInterpreter)
		{
			Vdm2isaPlugin.setupProperties();
			Vdm2isaPlugin.reset();

			if (Vdm2isaPlugin.runExu)
			{
				ExuPlugin exu = new ExuPlugin(interpreter);
				// plugin run worked if exu's run works
				result = exu.run(argv);
			}
			else
			{
				// plugin run worked
				result = true;
			}

			Console.out.println("Starting Isabelle to VDM translation.");

			// VDM errors don't pass VDMJ; some VDM warnings have to be raised as errors to avoid translation issues
			//Vdm2isaPlugin.processVDMWarnings();

			try
			{
				TCModuleList tclist_filtered = new TCModuleList(); 
				tclist_filtered.addAll(tclist);
				Iterator<TCModule> mi = tclist_filtered.iterator();
				while (mi.hasNext())
				{
					if (mi.next().name.getName().equals(IsaToken.VDMTOOLKIT.toString()))
					{
						mi.remove();
						break;
					}
				} 

				// class map TC -> TR trees + set them up
				translatedModules = ClassMapper.getInstance(TRNode.MAPPINGS).init().convert(tclist_filtered);//tclist);
				translatedModules.setup();

				// be strict on translation output
				// strict => GeneralisaPlugin.getErrorCount() == 0
				if (!GeneralisaPlugin.strict || GeneralisaPlugin.getErrorCount() == 0)
				{
					int mcount = 0;
					for (TRModule module : translatedModules)
					{
						outputModule(module.getLocation(), module.name.toString(), module.translate());  
						mcount++;
					}
					// only successful output calls
					addLocalModules(mcount);//translatedModules.size());
				}
			}
			catch (InternalException e)
			{
				Console.out.println(e.toString());
			}
			catch (Throwable t)
			{
				Console.out.println("Uncaught exception: " + t.toString());
				t.printStackTrace();
				addLocalErrors(1);
			}
		}
		return result;
	}

	@Override
	public String help()
	{
		return "vdm2isa - translate all loaded VDM modules to Isabelle/HOL (v. " + Vdm2isaPlugin.isaVersion + ")";
	}

	public static void setupProperties()
	{
		GeneralisaPlugin.setupProperties();
		Vdm2isaPlugin.linientPost 	= false;
		Vdm2isaPlugin.printVDMComments = true;
		Vdm2isaPlugin.printIsaComments = true;
		Vdm2isaPlugin.runExu			= true;
		Vdm2isaPlugin.valueAsAbbreviation = true;
		Vdm2isaPlugin.translateTypeDefMinMax = true;
		Vdm2isaPlugin.printLocations = true;
	}
}
