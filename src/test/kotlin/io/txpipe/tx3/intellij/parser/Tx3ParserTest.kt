package io.txpipe.tx3.intellij.parser

import com.intellij.testFramework.ParsingTestCase

/**
 * PSI tree snapshot tests for the Tx3 parser.
 *
 * Each test maps to a `.tx3` input file in `src/test/testData/parser/`.
 * Expected PSI trees are in matching `.txt` files. To bootstrap `.txt` files,
 * run the test once — the framework will create the expected output from the
 * actual PSI dump. Verify the dump, then subsequent runs will compare against it.
 */
class Tx3ParserTest : ParsingTestCase("parser", "tx3", Tx3ParserDefinition()) {

    override fun getTestDataPath(): String = "src/test/testData"

    override fun skipSpaces(): Boolean = true

    override fun includeRanges(): Boolean = true

    // ── Top-level Declarations ────────────────────────────────────────────────

    fun testPartyDecl() = doTest(true)
    fun testPolicyImport() = doTest(true)
    fun testPolicyHex() = doTest(true)
    fun testPolicyBlock() = doTest(true)
    fun testRecordDecl() = doTest(true)
    fun testTypeRecord() = doTest(true)
    fun testTypeVariant() = doTest(true)
    fun testEnvDecl() = doTest(true)
    fun testAssetDecl() = doTest(true)

    // ── Tx Declarations ───────────────────────────────────────────────────────

    fun testTxSimple() = doTest(true)
    fun testTxWithParams() = doTest(true)
    fun testTxAllBlocks() = doTest(true)

    // ── Tx Body Blocks ────────────────────────────────────────────────────────

    fun testInputBlock() = doTest(true)
    fun testInputStarBlock() = doTest(true)
    fun testOutputBlock() = doTest(true)
    fun testBurnBlock() = doTest(true)
    fun testMintBlock() = doTest(true)
    fun testLocalsBlock() = doTest(true)
    fun testCollateralBlock() = doTest(true)
    fun testReferenceBlock() = doTest(true)
    fun testSignersBlock() = doTest(true)
    fun testValidityBlock() = doTest(true)
    fun testMetadataBlock() = doTest(true)
    fun testLetBinding() = doTest(true)

    // ── Expressions ───────────────────────────────────────────────────────────

    fun testBinaryExpressions() = doTest(true)
    fun testUnaryExpressions() = doTest(true)
    fun testCallExpression() = doTest(true)
    fun testRecordLiteral() = doTest(true)
    fun testFieldAccess() = doTest(true)
    fun testListLiteral() = doTest(true)
    fun testMapLiteral() = doTest(true)
    fun testSpreadExpr() = doTest(true)
    fun testVariantConstruction() = doTest(true)

    // ── Additional Construct Coverage ──────────────────────────────────────────

    fun testCardanoBlock() = doTest(true)
    fun testUnitLiteral() = doTest(true)
    fun testGenericTypeRef() = doTest(true)
    fun testBuiltinCalls() = doTest(true)
    fun testDatumIsField() = doTest(true)
    fun testBooleanExpressions() = doTest(true)
    fun testComparisonExpressions() = doTest(true)
    fun testListTypeRef() = doTest(true)
    fun testSpreadInRecord() = doTest(true)

    // ── New Language Constructs ──────────────────────────────────────────────

    fun testTypeAlias() = doTest(true)
    fun testUnionType() = doTest(true)
    fun testAnonymousRecord() = doTest(true)
    fun testVariantTuple() = doTest(true)
    fun testArrayType() = doTest(true)
    fun testIndexExpr() = doTest(true)
    fun testTernaryExpr() = doTest(true)
    fun testCombinedTypeFeatures() = doTest(true)
    fun testNestedTernary() = doTest(true)

    // ── Edge Cases ────────────────────────────────────────────────────────────

    fun testSoftKeywordsAsIdentifiers() = doTest(true)
    fun testTransferProtocol() = doTest(true)
    fun testVestingProtocol() = doTest(true)
}
