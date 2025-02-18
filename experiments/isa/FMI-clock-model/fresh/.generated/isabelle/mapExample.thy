(* VDM to Isabelle Translation @2022-02-22T14:45:23.577358Z
   Copyright 2021, Leo Freitas, leo.freitas@newcastle.ac.uk

in '/Users/nljsf/Local/reps/git/VDM_Toolkit/plugins/vdm2isa/examples/FMI-clock-model/fresh/mapExample.vdmsl' at line 1:8
files = [/Users/nljsf/Local/reps/git/VDM_Toolkit/plugins/vdm2isa/examples/FMI-clock-model/fresh/mapExample.vdmsl]
*)
theory mapExample
imports "VDMToolkit" 
begin


\<comment>\<open>VDM source: S = compose S of x:nat end\<close>
\<comment>\<open>in 'mapExample' (/Users/nljsf/Local/reps/git/VDM_Toolkit/plugins/vdm2isa/examples/FMI-clock-model/fresh/mapExample.vdmsl) at line 6:5\<close>
record S = 
	x\<^sub>S :: "VDMNat"
	

\<comment>\<open>VDM source: inv_S: (S +> bool)
	inv_S(dummy0) ==
null\<close>
\<comment>\<open>in 'mapExample' (/Users/nljsf/Local/reps/git/VDM_Toolkit/plugins/vdm2isa/examples/FMI-clock-model/fresh/mapExample.vdmsl) at line 6:5\<close>
definition
	inv_S :: "S \<Rightarrow> bool"
where
	"inv_S dummy0 \<equiv> 
		\<comment>\<open>Implicitly defined type invariant checks for undeclared `inv_S` specification.\<close>
		( (((inv_VDMNat (x\<^sub>S dummy0))) ))"
 
lemmas inv_S_defs = inv_S_def inv_VDMNat_def 


	
	
\<comment>\<open>VDM source: T = compose T of y:map (nat) to (S) end
	inv mk_T(y) == (forall x in set (dom y) & ((y(x).x) > 10))\<close>
\<comment>\<open>in 'mapExample' (/Users/nljsf/Local/reps/git/VDM_Toolkit/plugins/vdm2isa/examples/FMI-clock-model/fresh/mapExample.vdmsl) at line 9:5\<close>
record T = 
	y\<^sub>T :: "(VDMNat \<rightharpoonup> S)"
	

\<comment>\<open>VDM source: inv_T: (T +> bool)
	inv_T(mk_T(y)) ==
(forall x in set (dom y) & ((y(x).x) > 10))\<close>
\<comment>\<open>in 'mapExample' (/Users/nljsf/Local/reps/git/VDM_Toolkit/plugins/vdm2isa/examples/FMI-clock-model/fresh/mapExample.vdmsl) at line 11:9\<close>
definition
	inv_T :: "T \<Rightarrow> bool"
where
	"inv_T dummy0 \<equiv> 
		\<comment>\<open>Implicitly defined type invariant checks for  `inv_T` specification.\<close>
		( (((inv_Map (inv_VDMNat) inv_S  (y\<^sub>T dummy0))) ))  \<and> 
		\<comment>\<open>Implicit pattern context conversion\<close>
		(let y = (y\<^sub>T dummy0) in 
		\<comment>\<open>User defined body of inv_T.\<close>
		(\<forall> x \<in> (dom y)  . ((x\<^sub>S (the((y x)))) > (10::VDMNat1))))"
 
lemmas inv_T_defs = inv_Map_def inv_Map_defs inv_S_def inv_T_def inv_VDMNat_def 


	
	
\<comment>\<open>VDM source: f: (T * S -> nat)
	f(m, s) ==
((m.y)(((m.y)((s.x)).x)).x)\<close>
\<comment>\<open>in 'mapExample' (/Users/nljsf/Local/reps/git/VDM_Toolkit/plugins/vdm2isa/examples/FMI-clock-model/fresh/mapExample.vdmsl) at line 16:5\<close>

\<comment>\<open>VDM source: pre_f: (T * S +> bool)
	pre_f(m, s) ==
null\<close>
\<comment>\<open>in 'mapExample' (/Users/nljsf/Local/reps/git/VDM_Toolkit/plugins/vdm2isa/examples/FMI-clock-model/fresh/mapExample.vdmsl) at line 16:5\<close>
definition
	pre_f :: "T \<Rightarrow> S \<Rightarrow> bool"
where
	"pre_f m   s \<equiv> 
		\<comment>\<open>Implicitly defined type invariant checks for undeclared `pre_f` specification.\<close>
		(inv_T m  \<and>  inv_S s)"


\<comment>\<open>VDM source: post_f: (T * S * nat +> bool)
	post_f(m, s, RESULT) ==
null\<close>
\<comment>\<open>in 'mapExample' (/Users/nljsf/Local/reps/git/VDM_Toolkit/plugins/vdm2isa/examples/FMI-clock-model/fresh/mapExample.vdmsl) at line 16:5\<close>
definition
	post_f :: "T \<Rightarrow> S \<Rightarrow> VDMNat \<Rightarrow> bool"
where
	"post_f m   s   RESULT \<equiv> 
		\<comment>\<open>Implicitly defined type invariant checks for undeclared `post_f` specification.\<close>
		(inv_T m  \<and>  inv_S s  \<and>  (inv_VDMNat RESULT))"

definition
	f :: "T \<Rightarrow> S \<Rightarrow> VDMNat"
where
	"f m   s \<equiv> 
	\<comment>\<open>User defined body of f.\<close>
	(x\<^sub>S (the(((y\<^sub>T m) (x\<^sub>S (the(((y\<^sub>T m) (x\<^sub>S s)))))))))"

	
	
\<comment>\<open>VDM source: g: (T * S -> nat)
	g(m, s) ==
f(m, (m.y)(((m.y)((s.x)).x)))\<close>
\<comment>\<open>in 'mapExample' (/Users/nljsf/Local/reps/git/VDM_Toolkit/plugins/vdm2isa/examples/FMI-clock-model/fresh/mapExample.vdmsl) at line 21:5\<close>

\<comment>\<open>VDM source: pre_g: (T * S +> bool)
	pre_g(m, s) ==
null\<close>
\<comment>\<open>in 'mapExample' (/Users/nljsf/Local/reps/git/VDM_Toolkit/plugins/vdm2isa/examples/FMI-clock-model/fresh/mapExample.vdmsl) at line 21:5\<close>
definition
	pre_g :: "T \<Rightarrow> S \<Rightarrow> bool"
where
	"pre_g m   s \<equiv> 
		\<comment>\<open>Implicitly defined type invariant checks for undeclared `pre_g` specification.\<close>
		(inv_T m  \<and>  inv_S s)"


\<comment>\<open>VDM source: post_g: (T * S * nat +> bool)
	post_g(m, s, RESULT) ==
null\<close>
\<comment>\<open>in 'mapExample' (/Users/nljsf/Local/reps/git/VDM_Toolkit/plugins/vdm2isa/examples/FMI-clock-model/fresh/mapExample.vdmsl) at line 21:5\<close>
definition
	post_g :: "T \<Rightarrow> S \<Rightarrow> VDMNat \<Rightarrow> bool"
where
	"post_g m   s   RESULT \<equiv> 
		\<comment>\<open>Implicitly defined type invariant checks for undeclared `post_g` specification.\<close>
		(inv_T m  \<and>  inv_S s  \<and>  (inv_VDMNat RESULT))"

definition
	g :: "T \<Rightarrow> S \<Rightarrow> VDMNat"
where
	"g m   s \<equiv> 
	\<comment>\<open>User defined body of g.\<close>
	(f m   ((y\<^sub>T m) (x\<^sub>S (the(((y\<^sub>T m) (x\<^sub>S s)))))))"

end