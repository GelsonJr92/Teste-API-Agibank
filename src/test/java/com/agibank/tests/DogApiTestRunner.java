package com.agibank.tests;

import org.junit.platform.suite.api.ExcludeClassNamePatterns;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Suite Completa - Dog API Tests")
@SelectPackages("com.agibank.tests")
@ExcludeClassNamePatterns(".*Runner.*")
//@IncludeTags("")
public class DogApiTestRunner {
}
