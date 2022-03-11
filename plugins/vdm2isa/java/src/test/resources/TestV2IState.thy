(* VDM to Isabelle Translation @2022-03-11T18:17:59.573Z
   Copyright 2021, Leo Freitas, leo.freitas@newcastle.ac.uk

in 'plugins/vdm2isa/java/src/test/resources/TestV2IState.vdmsl' at line 1:8
files = [plugins/vdm2isa/java/src/test/resources/TestV2IState.vdmsl]
*)
theory TestV2IState
imports "VDMToolkit" 
begin


\<comment>\<open>VDM source: state S of
x:nat
y:real
	inv mk_S(x, y) == ((x > 10) and (y < 10))
	init s == (s = mk_S(20, 5))
end\<close>
\<comment>\<open>in 'TestV2IState' (plugins/vdm2isa/java/src/test/resources/TestV2IState.vdmsl) at line 5:7\<close>
record S =
\<comment>\<open>VDM source: x = nat\<close>
\<comment>\<open>in 'TestV2IState'  plugins/vdm2isa/java/src/test/resources/TestV2IState.vdmsl  at line 6:4\<close>
 x :: VDMNat 
\<comment>\<open>VDM source: y = real\<close>
\<comment>\<open>in 'TestV2IState'  plugins/vdm2isa/java/src/test/resources/TestV2IState.vdmsl  at line 7:4\<close>
 y :: VDMReal 


\<comment>\<open>VDM source: init_S: (S +> bool)
	init_S(s) ==
init s == (s = mk_S(20, 5))\<close>
\<comment>\<open>in 'TestV2IState' (plugins/vdm2isa/java/src/test/resources/TestV2IState.vdmsl) at line 9:6\<close>
definition
	init_S :: "S \<Rightarrow> bool"
where
	"init_S s \<equiv> 
		\<comment>\<open>User defined body of init_S.\<close>
		s(s = \<lparr>x\<^sub>S = (20::VDMNat1), y\<^sub>S = (5::VDMNat1)\<rparr>)"



end