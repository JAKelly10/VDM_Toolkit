(* VDM to Isabelle Translation @2021-11-18T14:45:38.365429Z
   Copyright 2021, Leo Freitas, leo.freitas@newcastle.ac.uk

in './src/test/resources/TestV2IExprsRecords.vdmsl' at line 1:8
files = [./src/test/resources/TestV2IExprsRecords.vdmsl]
*)
theory TestV2IExprsRecords
imports VDMToolkit
begin


\<comment>\<open>in 'TestV2IExprsRecords' (./src/test/resources/TestV2IExprsRecords.vdmsl) at line 7:5\<close>
record R = 
	x\<^sub>R :: "VDMNat"
		 y\<^sub>R :: "VDMNat"
	

\<comment>\<open>in 'TestV2IExprsRecords' (./src/test/resources/TestV2IExprsRecords.vdmsl) at line 10:9\<close>
definition
	inv_R :: "R \<Rightarrow> \<bool>"
where
	"inv_R r \<equiv> 
		\<comment>\<open>Implicitly defined type invariant checks for inv_R specification\<close>
		( (((inv_VDMNat (x\<^sub>R r))) \<and>
		 ((inv_VDMNat (y\<^sub>R r))) ))  \<and> 
		\<comment>\<open>User defined body of inv_R\<close>
		((x\<^sub>R (r)) < (y\<^sub>R (r)))"
 

	
\<comment>\<open>in 'TestV2IExprsRecords' (./src/test/resources/TestV2IExprsRecords.vdmsl) at line 17:5\<close>

\<comment>\<open>in 'TestV2IExprsRecords' (./src/test/resources/TestV2IExprsRecords.vdmsl) at line 17:5\<close>
definition
	pre_mkr :: "VDMNat \<Rightarrow> \<bool>"
where
	"pre_mkr n \<equiv> 
		\<comment>\<open>Implicitly defined type invariant checks for undeclared pre_mkr specification\<close>
		((inv_VDMNat n))"


\<comment>\<open>in 'TestV2IExprsRecords' (./src/test/resources/TestV2IExprsRecords.vdmsl) at line 17:5\<close>
definition
	post_mkr :: "VDMNat\<Rightarrow> R \<Rightarrow> \<bool>"
where
	"post_mkr n  RESULT \<equiv> 
		\<comment>\<open>Implicitly defined type invariant checks for undeclared post_mkr specification\<close>
		((inv_VDMNat n)  \<and>   (((inv_VDMNat (x\<^sub>R RESULT))) \<and>
		 ((inv_VDMNat (y\<^sub>R RESULT))) ))"

definition
	mkr :: "VDMNat \<Rightarrow> R"
where
	"mkr n \<equiv> 
	\<comment>\<open>User defined body of mkr\<close>
	\<lparr>x\<^sub>R = n, y\<^sub>R = (n+(1::VDMNat1))\<rparr>"

	
\<comment>\<open>in 'TestV2IExprsRecords' (./src/test/resources/TestV2IExprsRecords.vdmsl) at line 21:5\<close>

\<comment>\<open>in 'TestV2IExprsRecords' (./src/test/resources/TestV2IExprsRecords.vdmsl) at line 21:5\<close>
definition
	pre_recbind :: "R \<Rightarrow> \<bool>"
where
	"pre_recbind dummy0 \<equiv> 
		\<comment>\<open>Implicitly defined type invariant checks for undeclared pre_recbind specification\<close>
		( (((inv_VDMNat (x\<^sub>R dummy0))) \<and>
		 ((inv_VDMNat (y\<^sub>R dummy0))) ))"


\<comment>\<open>in 'TestV2IExprsRecords' (./src/test/resources/TestV2IExprsRecords.vdmsl) at line 21:5\<close>
definition
	post_recbind :: "R\<Rightarrow> VDMNat \<Rightarrow> \<bool>"
where
	"post_recbind dummy0  RESULT \<equiv> 
		\<comment>\<open>Implicitly defined type invariant checks for undeclared post_recbind specification\<close>
		( (((inv_VDMNat (x\<^sub>R dummy0))) \<and>
		 ((inv_VDMNat (y\<^sub>R dummy0))) )  \<and>  (inv_VDMNat RESULT))"

definition
	recbind :: "R \<Rightarrow> VDMNat"
where
	"recbind dummy0 \<equiv> 
	\<comment>\<open>Implicit record pattern projection conversion\<close>
	(let x = (x\<^sub>R dummy0); y = (y\<^sub>R dummy0) in 
			\<comment>\<open>User defined body of recbind\<close>
			(x + y))"

	
\<comment>\<open>in 'TestV2IExprsRecords' (./src/test/resources/TestV2IExprsRecords.vdmsl) at line 25:5\<close>

\<comment>\<open>in 'TestV2IExprsRecords' (./src/test/resources/TestV2IExprsRecords.vdmsl) at line 28:11\<close>
definition
	pre_recbindSpec :: "R \<Rightarrow> \<bool>"
where
	"pre_recbindSpec dummy0 \<equiv> 
		\<comment>\<open>Implicitly defined type invariant checks for pre_recbindSpec specification\<close>
		( (((inv_VDMNat (x\<^sub>R dummy0))) \<and>
		 ((inv_VDMNat (y\<^sub>R dummy0))) ))  \<and> 
		\<comment>\<open>Implicit record pattern projection conversion\<close>
		(let x = (x\<^sub>R dummy0); y = (y\<^sub>R dummy0) in 
			\<comment>\<open>User defined body of pre_recbindSpec\<close>
			(x < y))"


\<comment>\<open>in 'TestV2IExprsRecords' (./src/test/resources/TestV2IExprsRecords.vdmsl) at line 29:21\<close>
definition
	post_recbindSpec :: "R\<Rightarrow> VDMNat \<Rightarrow> \<bool>"
where
	"post_recbindSpec dummy0 RESULT \<equiv> 
		\<comment>\<open>Implicitly defined type invariant checks for post_recbindSpec specification\<close>
		( (((inv_VDMNat (x\<^sub>R dummy0))) \<and>
		 ((inv_VDMNat (y\<^sub>R dummy0))) )  \<and>  (inv_VDMNat RESULT))  \<and> 
		\<comment>\<open>Implicit record pattern projection conversion\<close>
		(let x = (x\<^sub>R dummy0); y = (y\<^sub>R dummy0) in 
			\<comment>\<open>User defined body of post_recbindSpec\<close>
			((x < RESULT) \<and> (y < RESULT)))"

definition
	recbindSpec :: "R \<Rightarrow> VDMNat"
where
	"recbindSpec dummy0 \<equiv> 
	\<comment>\<open>Implicit record pattern projection conversion\<close>
	(let x = (x\<^sub>R dummy0); y = (y\<^sub>R dummy0) in 
			\<comment>\<open>User defined body of recbindSpec\<close>
			(x + y))"

	
\<comment>\<open>in 'TestV2IExprsRecords' (./src/test/resources/TestV2IExprsRecords.vdmsl) at line 32:5\<close>

\<comment>\<open>in 'TestV2IExprsRecords' (./src/test/resources/TestV2IExprsRecords.vdmsl) at line 32:5\<close>
definition
	pre_letbest :: "\<bool>"
where
	"pre_letbest  \<equiv> True"


\<comment>\<open>in 'TestV2IExprsRecords' (./src/test/resources/TestV2IExprsRecords.vdmsl) at line 32:5\<close>
definition
	post_letbest :: "VDMNat \<Rightarrow> \<bool>"
where
	"post_letbest RESULT \<equiv> 
		\<comment>\<open>Implicitly defined type invariant checks for undeclared post_letbest specification\<close>
		((inv_VDMNat RESULT))"

definition
	letbest :: "VDMNat"
where
	"letbest  \<equiv> 
	\<comment>\<open>User defined body of letbest\<close>
	(
		SOME (dummy0::VDMNat) .(dummy0 \<in> { (let x = (x\<^sub>R dummy0); y = (y\<^sub>R dummy0) in (x + y)) | (dummy0 :: R) .  \<comment>\<open>Type bound set compression will generate a (possibly spurious, i.e. inv_VDMSet') difficult set finiteness proof!!!\<close>  (( (((inv_VDMNat (x\<^sub>R dummy0))) \<and>
		 ((inv_VDMNat (y\<^sub>R dummy0))) )))  \<and> (let x = (x\<^sub>R dummy0); y = (y\<^sub>R dummy0) in (x > y)) }))"

	
abbreviation
	v64 :: "(VDMNat1\<times> VDMNat1\<times> VDMNat1\<times> VDMNat1)"
where
	"v64 \<equiv> ((1::VDMNat1), (2::VDMNat1), (3::VDMNat1), (4::VDMNat1))"

	definition
	inv_v64 :: "\<bool>"
where
	"inv_v64  \<equiv> 
		((inv_VDMNat1 (fst v64))\<and>
		 (inv_VDMNat1 (fst (snd v64)))\<and>
		 (inv_VDMNat1 (fst (snd (snd v64))))\<and>
		 (inv_VDMNat1 (snd (snd (snd v64))))
		)"

	
	
abbreviation
	v65 :: "R"
where
	"v65 \<equiv> \<lparr>x\<^sub>R = (1::VDMNat1), y\<^sub>R = (2::VDMNat1)\<rparr>"

	definition
	inv_v65 :: "\<bool>"
where
	"inv_v65  \<equiv> inv_R v65"

	
	
abbreviation
	v651 :: "VDMNat"
where
	"v651 \<equiv> (x\<^sub>R (\<lparr>x\<^sub>R = (1::VDMNat1), y\<^sub>R = (2::VDMNat1)\<rparr>))"

	definition
	inv_v651 :: "\<bool>"
where
	"inv_v651  \<equiv> (inv_VDMNat v651)"

	
	
abbreviation
	v652 :: "VDMNat"
where
	"v652 \<equiv> (x\<^sub>R (v65))"

	definition
	inv_v652 :: "\<bool>"
where
	"inv_v652  \<equiv> (inv_VDMNat v652)"

	
	
abbreviation
	v653 :: "VDMNat"
where
	"v653 \<equiv> (x\<^sub>R ((mkr (1::VDMNat1))))"

	definition
	inv_v653 :: "\<bool>"
where
	"inv_v653  \<equiv> (inv_VDMNat v653)"

	
	
abbreviation
	v67 :: "R"
where
	"v67 \<equiv> (v65)\<lparr>x\<^sub>R := (10::VDMNat1), y\<^sub>R := (20::VDMNat1)\<rparr>"

	definition
	inv_v67 :: "\<bool>"
where
	"inv_v67  \<equiv> inv_R v67"

	
	
abbreviation
	v731 :: "R"
where
	"v731 \<equiv> (THE dummy0. (((dummy0 \<in>{v65})) \<and> (let x = (x\<^sub>R dummy0); y = (y\<^sub>R dummy0) in (x < y))))"

	definition
	inv_v731 :: "\<bool>"
where
	"inv_v731  \<equiv> inv_R v731"

	
	
abbreviation
	v741 :: "R \<Rightarrow> VDMNat"
where
	"v741 \<equiv> (
	\<lambda> (dummy0 :: R) .let x = (x\<^sub>R dummy0); y = (y\<^sub>R dummy0) in 
		(if (( (((inv_VDMNat (x\<^sub>R dummy0))) \<and>
		 ((inv_VDMNat (y\<^sub>R dummy0))) ))) \<and> (inv_VDMNat (x + y)) then
		(x + y)
	 else
		undefined
	)
	)"

	definition
	inv_v741 :: "R \<Rightarrow> \<bool>"
where
	"inv_v741 dummy0 \<equiv> 
	\<comment>\<open>function type invariant depends on its lambda definition dummy names used being equal.\<close>
	(inv_VDMNat (v741 dummy0))"

	
	
abbreviation
	v781 :: "\<bool>"
where
	"v781 \<equiv> (\<forall> dummy0 \<in> {v65}  . (let x = (x\<^sub>R dummy0); y = (y\<^sub>R dummy0) in (y < x)))"

	definition
	inv_v781 :: "\<bool>"
where
	"inv_v781  \<equiv> (inv_bool v781)"

	
	
abbreviation
	v791 :: "VDMNat"
where
	"v791 \<equiv> (
		let 
(\<lparr>TRBasicPattern[identifier] = x 
		TRBasicPattern[identifier] = y [PL=2]\<rparr>::VDMNat) = v65;
(\<lparr>TRBasicPattern[identifier] = x 
		TRBasicPattern[identifier] = y [PL=2]\<rparr>::VDMNat) = v65;
		
(var1::VDMNat) = (10::VDMNat1)
		in
			let x = (x\<^sub>R dummy0); y = (y\<^sub>R dummy0) in 
		(if (inv_R dummy0) \<and> 
	((inv_VDMNat var1)) then
			((var1 + x) + y)
		 else
			undefined
		)
		)"

	definition
	inv_v791 :: "\<bool>"
where
	"inv_v791  \<equiv> (inv_VDMNat v791)"

	
	
abbreviation
	v801 :: "VDMNat"
where
	"v801 \<equiv> (
		SOME (dummy0::VDMNat) .(dummy0 \<in> { (let x = (x\<^sub>R dummy0); y = (y\<^sub>R dummy0) in (x + y)) | dummy0 .  ((dummy0 \<in>{v65}))  \<and> (let x = (x\<^sub>R dummy0); y = (y\<^sub>R dummy0) in (x < y)) }))"

	definition
	inv_v801 :: "\<bool>"
where
	"inv_v801  \<equiv> (inv_VDMNat v801)"

	
	
abbreviation
	v810 :: "VDMNat VDMSet"
where
	"v810 \<equiv> { (let x = (x\<^sub>R dummy0); y = (y\<^sub>R dummy0) in (x + y)) | dummy0 .  ((dummy0 \<in>{v65}))  \<and> (let x = (x\<^sub>R dummy0); y = (y\<^sub>R dummy0) in (y < x)) }"

	definition
	inv_v810 :: "\<bool>"
where
	"inv_v810  \<equiv> (inv_VDMSet' (inv_VDMNat) v810)"

	
	
abbreviation
	v830 :: "VDMNat VDMSeq"
where
	"v830 \<equiv> [ (let x = (x\<^sub>R dummy0); _ = (y\<^sub>R dummy0) in x) . dummy0 \<leftarrow> sorted_list_of_set ({v65}) , ((dummy0 \<in>{v65})) , (let x = (x\<^sub>R dummy0); _ = (y\<^sub>R dummy0) in ((0::VDMNat) < x)) ] \<comment>\<open>Set bind `(dummy0 \<in> {v65})` in sequence comprehension requires its Isabelle type to instantiate class linorder.  This can be a problem if the target type of @{term \<open>{v65}\<close>}  has a VDM ord_ predicate.\<close> "

	definition
	inv_v830 :: "\<bool>"
where
	"inv_v830  \<equiv> (inv_VDMSeq' (inv_VDMNat) v830)"

	
	
abbreviation
	v921 :: "VDMNat"
where
	"v921 \<equiv> (
		let 
(\<lparr>TRBasicPattern[identifier] = x 
		TRBasicPattern[identifier] = y [PL=2]\<rparr>::VDMNat) = v65;
(\<lparr>TRBasicPattern[identifier] = x 
		TRBasicPattern[identifier] = y [PL=2]\<rparr>::VDMNat) = v65
		in
			let x = (x\<^sub>R dummy0); y = (y\<^sub>R dummy0) in (if (inv_R dummy0) then
			(x + y)
		 else
			undefined
		)
		)"

	definition
	inv_v921 :: "\<bool>"
where
	"inv_v921  \<equiv> (inv_VDMNat v921)"

	
end