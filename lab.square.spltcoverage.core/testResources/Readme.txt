TestInput:
	SplCoverage:
		It has coverages of the invariant pl.
		Product1~4 coverages are varying, but Product5 is not.
		Because: Product5 has feature C uniquely, but feature C doesn't affect any code.
		Use this properties to test.
	
	FeatureSetGroup:
		Newer and Older depends on FeatureIDE versions.
	
TestOutput:
	These are 'for-test'.
	Don't use these for an input to another tests.	