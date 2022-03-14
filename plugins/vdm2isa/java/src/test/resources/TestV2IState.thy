(* VDM to Isabelle Translation @2022-03-14T14:33:11.723Z
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
record State =
\<comment>\<open>VDM source: x = nat\<close>
\<comment>\<open>in 'TestV2IState'  plugins/vdm2isa/java/src/test/resources/TestV2IState.vdmsl  at line 6:4\<close>
 x::VDMNat 
\<comment>\<open>VDM source: y = real\<close>
\<comment>\<open>in 'TestV2IState'  plugins/vdm2isa/java/src/test/resources/TestV2IState.vdmsl  at line 7:4\<close>
 y::VDMReal 

definition
	inv_State :: "State \<Rightarrow> bool"
where
	inv_State S \<equiv>
		
		(((inv_VDMNat (x\<^sub>S S))) \<and> 
		 ((inv_VDMReal (y\<^sub>S S)))
		) \<and>
		((x > (10::VDMNat1)) \<and> (y < (10::VDMNat1)))

definition
	init_State :: "State"
where
	init_State \<equiv>
		 \<lparr>x\<^sub>S = (20::VDMNat1), y\<^sub>S = (5::VDMNat1)\<rparr>



end