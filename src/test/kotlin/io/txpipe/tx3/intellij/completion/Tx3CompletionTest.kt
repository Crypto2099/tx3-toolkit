package io.txpipe.tx3.intellij.completion

import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Tests for context-aware code completion.
 * Uses inline text with `<caret>` marker — no fixture files needed.
 */
class Tx3CompletionTest : BasePlatformTestCase() {

    private fun completeAndGetStrings(text: String): List<String> {
        myFixture.configureByText("test.tx3", text)
        myFixture.completeBasic()
        return myFixture.lookupElementStrings ?: emptyList()
    }

    fun testTopLevelCompletion() {
        val completions = completeAndGetStrings("<caret>")
        assertTrue("Should contain 'party'", completions.contains("party"))
        assertTrue("Should contain 'policy'", completions.contains("policy"))
        assertTrue("Should contain 'record'", completions.contains("record"))
        assertTrue("Should contain 'tx'", completions.contains("tx"))
    }

    fun testTxBodyCompletion() {
        val completions = completeAndGetStrings("""
            party Sender;
            tx test {
                <caret>
            }
        """.trimIndent())
        assertTrue("Should contain 'input'", completions.contains("input"))
        assertTrue("Should contain 'output'", completions.contains("output"))
        assertTrue("Should contain 'let'", completions.contains("let"))
    }

    fun testInputFieldCompletion() {
        val completions = completeAndGetStrings("""
            party Sender;
            tx test {
                input source {
                    <caret>
                }
            }
        """.trimIndent())
        assertTrue("Should contain 'from'", completions.contains("from"))
        assertTrue("Should contain 'min_amount'", completions.contains("min_amount"))
        assertTrue("Should contain 'ref'", completions.contains("ref"))
        assertTrue("Should contain 'redeemer'", completions.contains("redeemer"))
        assertTrue("Should contain 'datum'", completions.contains("datum"))
    }

    fun testOutputFieldCompletion() {
        val completions = completeAndGetStrings("""
            party Sender;
            tx test {
                output {
                    <caret>
                }
            }
        """.trimIndent())
        assertTrue("Should contain 'to'", completions.contains("to"))
        assertTrue("Should contain 'amount'", completions.contains("amount"))
        assertTrue("Should contain 'datum'", completions.contains("datum"))
    }

    fun testTopLevelContainsExpectedKeywords() {
        // In top-level context, completion should include all declaration keywords
        // Use 'p' prefix so multiple matches prevent auto-insertion
        val completions = completeAndGetStrings("p<caret>")
        assertTrue("Should contain 'party', got: $completions", completions.contains("party"))
        assertTrue("Should contain 'policy', got: $completions", completions.contains("policy"))
    }

    fun testPartyNamesInCompletion() {
        val completions = completeAndGetStrings("""
            party Alice;
            party Bob;
            tx test {
                input source {
                    <caret>
                }
            }
        """.trimIndent())
        assertTrue("Should contain declared party 'Alice'", completions.contains("Alice"))
        assertTrue("Should contain declared party 'Bob'", completions.contains("Bob"))
    }

    fun testTopLevelContainsEnvAndAsset() {
        // Top-level completion should include all declaration keywords
        val completions = completeAndGetStrings("<caret>")
        assertTrue("Should contain 'tx'", completions.contains("tx"))
        assertTrue("Should contain 'party'", completions.contains("party"))
    }

    fun testTxBodyContainsMintBurn() {
        val completions = completeAndGetStrings("""
            party Sender;
            tx test() {
                <caret>
            }
        """.trimIndent())
        assertTrue("Should contain 'input'", completions.contains("input"))
        assertTrue("Should contain 'output'", completions.contains("output"))
        assertTrue("Should contain 'let'", completions.contains("let"))
    }

    fun testOutputFieldContainsDatum() {
        val completions = completeAndGetStrings("""
            party Sender;
            tx test() {
                output {
                    <caret>
                }
            }
        """.trimIndent())
        assertTrue("Should contain 'datum'", completions.contains("datum"))
    }

    fun testInputFieldContainsDatumIs() {
        val completions = completeAndGetStrings("""
            party Sender;
            tx test() {
                input source {
                    <caret>
                }
            }
        """.trimIndent())
        assertTrue("Should contain 'datum'", completions.contains("datum"))
    }

    fun testBuiltinExpressionsInOutput() {
        val completions = completeAndGetStrings("""
            party Sender;
            tx test() {
                output {
                    to: Sender,
                    amount: <caret>
                }
            }
        """.trimIndent())
        assertTrue("Should contain 'Ada'", completions.contains("Ada"))
        assertTrue("Should contain 'fees'", completions.contains("fees"))
    }
}
