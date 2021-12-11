(* VDM to Isabelle Translation @2021-12-11T13:29:54.848659Z
   Copyright 2021, Leo Freitas, leo.freitas@newcastle.ac.uk

in './src/test/resources/TestV2IModules.vdmsl' at line 18:8
files = [./src/test/resources/TestV2IModules.vdmsl]
*)
theory B
imports "A" "VDMToolkit" 
begin

\<comment>\<open>unqualified VDM import from F: f; use Isabelle qualified naming directly.\<close>
	\<comment>\<open>VDM import from A: f renamed F.\<close>
	abbreviation "F \<equiv> f"
	
	
\<comment>\<open>VDM source: g: (nat -> nat)
	g(a) ==
((F(a) - 1) + v)\<close>
\<comment>\<open>in 'B' (./src/test/resources/TestV2IModules.vdmsl) at line 29:5\<close>

\<comment>\<open>VDM source: pre_g = ?\<close>
\<comment>\<open>in 'B' (./src/test/resources/TestV2IModules.vdmsl) at line 29:5\<close>
definition
	pre_g :: "VDMNat \<Rightarrow> bool"
where
	"pre_g a \<equiv> 
		\<comment>\<open>Implicitly defined type invariant checks for undeclared `pre_g` specification.\<close>
		((inv_VDMNat a))"


\<comment>\<open>VDM source: post_g = ?\<close>
\<comment>\<open>in 'B' (./src/test/resources/TestV2IModules.vdmsl) at line 29:5\<close>
definition
	post_g :: "VDMNat\<Rightarrow> VDMNat \<Rightarrow> bool"
where
	"post_g a  RESULT \<equiv> 
		\<comment>\<open>Implicitly defined type invariant checks for undeclared `post_g` specification.\<close>
		((inv_VDMNat a)  \<and>  (inv_VDMNat RESULT))"

definition
	g :: "VDMNat \<Rightarrow> VDMNat"
where
	"g a \<equiv> 
	\<comment>\<open>User defined body of g.\<close>
	(((F a) - (1::VDMNat1)) + A.v)"

end