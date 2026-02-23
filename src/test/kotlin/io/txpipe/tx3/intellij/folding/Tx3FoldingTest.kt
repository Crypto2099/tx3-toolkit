package io.txpipe.tx3.intellij.folding

import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Tests for code folding regions.
 * Uses `<fold text='...'>` markers in fixture files.
 */
class Tx3FoldingTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String = "src/test/testData"

    fun testTxFolding() {
        myFixture.testFolding("$testDataPath/folding/TxFolding.tx3")
    }

    fun testRecordFolding() {
        myFixture.testFolding("$testDataPath/folding/RecordFolding.tx3")
    }

    fun testTypeFolding() {
        myFixture.testFolding("$testDataPath/folding/TypeFolding.tx3")
    }

    fun testEnvFolding() {
        myFixture.testFolding("$testDataPath/folding/EnvFolding.tx3")
    }

    fun testLocalsFolding() {
        myFixture.testFolding("$testDataPath/folding/LocalsFolding.tx3")
    }

    fun testPolicyImportFolding() {
        myFixture.testFolding("$testDataPath/folding/PolicyImportFolding.tx3")
    }

    fun testBlockCommentFolding() {
        myFixture.testFolding("$testDataPath/folding/BlockCommentFolding.tx3")
    }

    fun testLineCommentRunFolding() {
        myFixture.testFolding("$testDataPath/folding/LineCommentRunFolding.tx3")
    }

    fun testRecordLiteralFolding() {
        myFixture.testFolding("$testDataPath/folding/RecordLiteralFolding.tx3")
    }

    fun testVariantExprFolding() {
        myFixture.testFolding("$testDataPath/folding/VariantExprFolding.tx3")
    }
}
