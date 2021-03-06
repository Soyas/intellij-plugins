package com.intellij.flex.refactoring;

import com.intellij.flex.util.FlexTestUtils;
import com.intellij.javascript.flex.css.FlexStylesIndexableSetContributor;
import com.intellij.javascript.flex.mxml.schema.FlexSchemaHandler;
import com.intellij.lang.javascript.JSBundle;
import com.intellij.lang.javascript.JSTestOption;
import com.intellij.lang.javascript.JSTestOptions;
import com.intellij.lang.javascript.flex.FlexModuleType;
import com.intellij.lang.javascript.refactoring.JSInlineVarOrFunctionTestBase;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;

import static com.intellij.openapi.vfs.VfsUtilCore.convertFromUrl;
import static com.intellij.openapi.vfs.VfsUtilCore.urlToPath;

public class FlexInlineFunctionTest extends JSInlineVarOrFunctionTestBase {

  @Override
  protected String getTestDataPath() {
    return FlexTestUtils.getTestDataPath("as_refactoring/inlineFunction/");
  }

  @Override
  protected ModuleType getModuleType() {
    return FlexModuleType.getInstance();
  }

  @Override
  protected void setUp() throws Exception {
    VfsRootAccess.allowRootAccess(getTestRootDisposable(),
                                  urlToPath(convertFromUrl(FlexSchemaHandler.class.getResource("z.xsd"))),
                                  urlToPath(convertFromUrl(FlexStylesIndexableSetContributor.class.getResource("FlexStyles.as"))));
    super.setUp();
    FlexTestUtils.setupFlexSdk(myModule, getTestName(false), getClass());
  }

  private void defaultTest() throws Exception {
    doTest(getTestName(false), "js2");
  }

  public void testDefaultParams() throws Exception {
    defaultTest();
  }

  public void testJustStatements2() throws Exception {
    defaultTest();
  }

  public void testJustStatements2_2() throws Exception {
    defaultTest();
  }

  public void testJustStatements2_3() throws Exception {
    defaultTest();
  }

  public void testJustStatements2_4() {
    doTestFailure(getTestName(false), "js2", JSBundle.message("javascript.refactoring.cannot.inline.function.with.multiple.returns"));
  }

  public void testJustStatements2_5() throws Exception {
    defaultTest();
  }

  public void testJustStatements2_6() throws Exception {
    defaultTest();
  }

  public void testJustOneCall() throws Exception {
    doTest(new String[]{getTestName(false) + ".js2"}, true);
  }

  public void testJustStatementsInMxml() throws Exception {
    doTest(getTestName(false), "mxml");
  }

  @JSTestOptions({JSTestOption.WithFlexFacet, JSTestOption.WithJsSupportLoader})
  public void testInsideAttribute() throws Exception {
    doTest(getTestName(false), "mxml");
    doTest(getTestName(false) + "_2", "mxml");
    doTest(getTestName(false) + "_3", "mxml");
  }

  public void testReplacingThis() throws Exception {
    defaultTest();
  }

  public void testNoReplacingThis() throws Exception {
    defaultTest();
  }

  public void testQualifyStatics() throws Exception {
    defaultTest();
  }

  public void testStaticCall() throws Exception {
    defaultTest();
  }


  public void testHasRestParams() {
    doTestFailure(getTestName(false), "js2", 
                  JSBundle.message("javascript.refactoring.cannot.inline.function.referencing.rest.parameter"));
  }

  public void testConstructor() {
    shouldFail("Can not inline constructor");
  }

  public void testConstructor2() {
    shouldFail("Can not inline constructor");
  }

  private void shouldFail(String reason) {
    doTestFailure(getTestName(false), "js2", reason);
  }

  public void testMethodInHierarchy() {
    String reason = "Can not inline method that participates in hierarchy";
    doTestFailure(getTestName(false) + 1, "js2", reason);
    doTestFailure(getTestName(false) + 2, "js2", reason);
    doTestFailure(getTestName(false) + 3, "js2", reason);
  }

  @JSTestOptions({JSTestOption.WithFlexFacet})
  public void testMethodInHierarchyMxml() {
    doTestFailure(new String[]{getTestName(false) + ".mxml", getTestName(false) + ".js2"}, 
                  "Can not inline method that participates in hierarchy");
  }

  public void testInterfaceMethod() {
    shouldFail("Can not inline interface method");
  }

  public void testMethodFromExternalLibrary() {
    myAfterCommitRunnable =
      () -> FlexTestUtils.addLibrary(myModule, "library", getTestDataPath(), "ExternalLib.swc", "ExternalLib.zip", null);

    shouldFail("Can not inline function defined in external library");
  }

  public void testNonCallUsage() {
    shouldFail("Can not inline non call usage");
  }

  public void testConflicts1() {
    String[] conflicts = new String[]{
      "Field Foo.inter with internal visibility won't be accessible from method OtherPackage.otherPackageFunc()",
      "Field Foo.inter with internal visibility won't be accessible from method SubClass.subclassFunc()",
      "Field Foo.priv with private visibility won't be accessible from method Neighbour.neighbourFunc()",
      "Field Foo.priv with private visibility won't be accessible from method OtherPackage.otherPackageFunc()",
      "Field Foo.priv with private visibility won't be accessible from method SubClass.subclassFunc()",
      "Field Foo.prot with protected visibility won't be accessible from method Neighbour.neighbourFunc()",
      "Field Foo.prot with protected visibility won't be accessible from method OtherPackage.otherPackageFunc()",
      "Method Foo.fInter() with internal visibility won't be accessible from method OtherPackage.otherPackageFunc()",
      "Method Foo.fInter() with internal visibility won't be accessible from method SubClass.subclassFunc()",
      "Method Foo.fPriv() with private visibility won't be accessible from method Neighbour.neighbourFunc()",
      "Method Foo.fPriv() with private visibility won't be accessible from method OtherPackage.otherPackageFunc()",
      "Method Foo.fPriv() with private visibility won't be accessible from method SubClass.subclassFunc()",
      "Method Foo.fProt() with protected visibility won't be accessible from method Neighbour.neighbourFunc()",
      "Method Foo.fProt() with protected visibility won't be accessible from method OtherPackage.otherPackageFunc()"
    };
    doTestConflicts(getTestName(false), "js2", conflicts);
  }

  public void testConflicts2() {
    String[] conflicts = new String[]{
      "Field Foo.u with protected visibility won't be accessible from inner class Bar",
      "Field Foo.u with protected visibility won't be accessible from method Bar.ff()",
      "Field Foo.v with private visibility won't be accessible from inner class Bar",
      "Field Foo.v with private visibility won't be accessible from method Bar.ff()"
    };
    doTestConflicts(getTestName(false), "js2", conflicts);
  }

  public void testConflicts3() {
    String[] conflicts = new String[]{
      "Field Conflicts3_2.t with private visibility won't be accessible from constructor Foo.Foo(int)",
      "Method Conflicts3_2.fff() with private visibility won't be accessible from constructor Foo.Foo(int)"
    };
    doTestConflicts(new String[]{getTestName(false) + ".js2", getTestName(false) + "_2.mxml"}, conflicts);
  }

  @JSTestOptions(JSTestOption.WithFlexSdk)
  public void testConflicts4() {
    String[] conflicts = new String[]{
      "Field Foo.abc with private visibility won't be accessible from function &lt;anonymous&gt;(*) in class Conflicts4",
      "Method Foo.zz() with protected visibility won't be accessible from function &lt;anonymous&gt;(*) in class Conflicts4"
    };
    doTestConflicts(new String[]{getTestName(false) + ".mxml", getTestName(false) + "_2.js2"}, conflicts);
  }

  public void testConflicts5() {
    String[] conflicts = new String[]{
      "Field Conflicts5.p with private visibility won't be accessible from file Conflicts5.js2",
      "Field Conflicts5.p with private visibility won't be accessible from file Conflicts5_2.js2"};
    doTestConflicts(new String[]{getTestName(false) + ".js2", getTestName(false) + "_2.js2"}, conflicts);
  }
}
