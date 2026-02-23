package io.txpipe.tx3.intellij.highlighting

import com.intellij.psi.tree.IElementType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import io.txpipe.tx3.intellij.lexer.Tx3TokenTypes
import org.junit.Assert

/**
 * Tests for syntax highlighting and annotator-based validation.
 */
class Tx3HighlightingTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String = "src/test/testData"

    // ── Programmatic: every token type has a highlight mapping ─────────────────

    fun testAllTokenTypesHaveHighlights() {
        val highlighter = Tx3SyntaxHighlighter
        val allTokens = mutableListOf<IElementType>()

        // Collect all defined token types from Tx3TokenTypes
        allTokens.addAll(Tx3TokenTypes.TOP_LEVEL_KEYWORDS)
        allTokens.addAll(Tx3TokenTypes.BLOCK_KEYWORDS)
        allTokens.addAll(Tx3TokenTypes.FIELD_KEYWORDS)
        allTokens.addAll(Tx3TokenTypes.CONTROL_KEYWORDS)
        allTokens.addAll(Tx3TokenTypes.BUILTIN_TYPES)
        allTokens.addAll(Tx3TokenTypes.BUILTIN_SYMBOLS)
        allTokens.addAll(Tx3TokenTypes.OPERATORS)
        allTokens.addAll(Tx3TokenTypes.LITERALS)
        allTokens.add(Tx3TokenTypes.IDENTIFIER)
        allTokens.add(Tx3TokenTypes.LINE_COMMENT)
        allTokens.add(Tx3TokenTypes.BLOCK_COMMENT)
        allTokens.add(Tx3TokenTypes.LBRACE)
        allTokens.add(Tx3TokenTypes.RBRACE)
        allTokens.add(Tx3TokenTypes.LPAREN)
        allTokens.add(Tx3TokenTypes.RPAREN)
        allTokens.add(Tx3TokenTypes.LBRACKET)
        allTokens.add(Tx3TokenTypes.RBRACKET)
        allTokens.add(Tx3TokenTypes.COMMA)
        allTokens.add(Tx3TokenTypes.SEMICOLON)
        allTokens.add(Tx3TokenTypes.COLON)
        allTokens.add(Tx3TokenTypes.DOT)

        for (token in allTokens) {
            val highlights = highlighter.getTokenHighlights(token)
            Assert.assertTrue(
                "Token $token should have at least one highlight attribute",
                highlights.isNotEmpty()
            )
        }
    }

    // ── Annotator: output block missing fields → WARNING ──────────────────────

    fun testOutputMissingFields() {
        myFixture.configureByFile("highlighting/OutputMissingFields.tx3")
        myFixture.checkHighlighting(true, false, false)
    }

    // ── Annotator: input block missing from/ref → WARNING ─────────────────────

    fun testInputMissingFrom() {
        myFixture.configureByFile("highlighting/InputMissingFrom.tx3")
        myFixture.checkHighlighting(true, false, false)
    }

    // ── Annotator: unresolved reference → WEAK_WARNING ────────────────────────

    fun testUnresolvedRef() {
        myFixture.configureByFile("highlighting/UnresolvedRef.tx3")
        myFixture.checkHighlighting(true, false, false)
    }

    // ── Annotator: output block missing just 'to' → WARNING ─────────────────

    fun testOutputMissingTo() {
        myFixture.configureByFile("highlighting/OutputMissingTo.tx3")
        myFixture.checkHighlighting(true, false, false)
    }

    // ── Annotator: output block missing just 'amount' → WARNING ───────────

    fun testOutputMissingAmount() {
        myFixture.configureByFile("highlighting/OutputMissingAmount.tx3")
        myFixture.checkHighlighting(true, false, false)
    }

    // ── Annotator: input block with 'ref' → no warning ────────────────────

    fun testInputWithRef() {
        myFixture.configureByFile("highlighting/InputWithRef.tx3")
        myFixture.checkHighlighting(true, false, false)
    }

    // ── Annotator: env field names resolve → no warning ───────────────────

    fun testEnvFieldResolves() {
        myFixture.configureByFile("highlighting/EnvFieldResolves.tx3")
        myFixture.checkHighlighting(true, false, false)
    }

    // ── Annotator: missing trailing comma → ERROR ─────────────────────────

    fun testMissingTrailingComma() {
        myFixture.configureByFile("highlighting/MissingTrailingComma.tx3")
        myFixture.checkHighlighting(true, false, false)
    }

    // ── Annotator: cardano block — no warning ──────────────────────────────────

    fun testCardanoBlockNoWarning() {
        myFixture.configureByFile("highlighting/CardanoBlockValid.tx3")
        myFixture.checkHighlighting(true, false, false)
    }

    // ── Valid file — no warnings ──────────────────────────────────────────────

    fun testValidTransfer() {
        myFixture.configureByFile("highlighting/ValidTransfer.tx3")
        myFixture.checkHighlighting(true, false, false)
    }
}
