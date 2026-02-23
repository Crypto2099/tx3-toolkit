package io.txpipe.tx3.intellij.lexer

import com.intellij.psi.tree.IElementType
import org.junit.Assert.*
import org.junit.Test

/**
 * Direct lexer tests — no IDE fixture needed.
 * Instantiates [Tx3LexerAdapter] and tokenizes strings.
 */
class Tx3LexerTest {

    private fun tokenize(input: String): List<Pair<IElementType, String>> {
        val lexer = Tx3LexerAdapter()
        lexer.start(input)
        val result = mutableListOf<Pair<IElementType, String>>()
        while (lexer.tokenType != null) {
            result.add(lexer.tokenType!! to lexer.tokenText)
            lexer.advance()
        }
        return result
    }

    private fun tokenTypes(input: String): List<IElementType> =
        tokenize(input).map { it.first }

    private fun assertSingleToken(input: String, expectedType: IElementType) {
        val tokens = tokenize(input).filter { it.first != com.intellij.psi.TokenType.WHITE_SPACE }
        assertEquals("Expected single token for '$input'", 1, tokens.size)
        assertEquals(expectedType, tokens[0].first)
        assertEquals(input, tokens[0].second)
    }

    // ── Declaration Keywords ──────────────────────────────────────────────────

    @Test
    fun testDeclarationKeywords() {
        val keywords = mapOf(
            "party" to Tx3TokenTypes.KW_PARTY,
            "policy" to Tx3TokenTypes.KW_POLICY,
            "record" to Tx3TokenTypes.KW_RECORD,
            "type" to Tx3TokenTypes.KW_TYPE,
            "tx" to Tx3TokenTypes.KW_TX,
            "env" to Tx3TokenTypes.KW_ENV,
            "asset" to Tx3TokenTypes.KW_ASSET,
        )
        for ((text, type) in keywords) {
            assertSingleToken(text, type)
        }
    }

    // ── Block Keywords ────────────────────────────────────────────────────────

    @Test
    fun testBlockKeywords() {
        val keywords = mapOf(
            "input" to Tx3TokenTypes.KW_INPUT,
            "output" to Tx3TokenTypes.KW_OUTPUT,
            "burn" to Tx3TokenTypes.KW_BURN,
            "mint" to Tx3TokenTypes.KW_MINT,
            "locals" to Tx3TokenTypes.KW_LOCALS,
            "collateral" to Tx3TokenTypes.KW_COLLATERAL,
            "reference" to Tx3TokenTypes.KW_REFERENCE,
            "signers" to Tx3TokenTypes.KW_SIGNERS,
            "validity" to Tx3TokenTypes.KW_VALIDITY,
            "metadata" to Tx3TokenTypes.KW_METADATA,
            "cardano" to Tx3TokenTypes.KW_CARDANO,
        )
        for ((text, type) in keywords) {
            assertSingleToken(text, type)
        }
    }

    // ── Field Keywords ────────────────────────────────────────────────────────

    @Test
    fun testFieldKeywords() {
        val keywords = mapOf(
            "from" to Tx3TokenTypes.KW_FROM,
            "to" to Tx3TokenTypes.KW_TO,
            "amount" to Tx3TokenTypes.KW_AMOUNT,
            "datum" to Tx3TokenTypes.KW_DATUM,
            "datum_is" to Tx3TokenTypes.KW_DATUM_IS,
            "redeemer" to Tx3TokenTypes.KW_REDEEMER,
            "min_amount" to Tx3TokenTypes.KW_MIN_AMOUNT,
            "ref" to Tx3TokenTypes.KW_REF,
            "script" to Tx3TokenTypes.KW_SCRIPT,
            "hash" to Tx3TokenTypes.KW_HASH,
            "since_slot" to Tx3TokenTypes.KW_SINCE_SLOT,
            "until_slot" to Tx3TokenTypes.KW_UNTIL_SLOT,
            "drep" to Tx3TokenTypes.KW_DREP,
            "stake" to Tx3TokenTypes.KW_STAKE,
            "version" to Tx3TokenTypes.KW_VERSION,
            "coin" to Tx3TokenTypes.KW_COIN,
        )
        for ((text, type) in keywords) {
            assertSingleToken(text, type)
        }
    }

    // ── Control Keywords ──────────────────────────────────────────────────────

    @Test
    fun testControlKeywords() {
        val keywords = mapOf(
            "import" to Tx3TokenTypes.KW_IMPORT,
            "let" to Tx3TokenTypes.KW_LET,
            "if" to Tx3TokenTypes.KW_IF,
            "else" to Tx3TokenTypes.KW_ELSE,
            "true" to Tx3TokenTypes.KW_TRUE,
            "false" to Tx3TokenTypes.KW_FALSE,
        )
        for ((text, type) in keywords) {
            assertSingleToken(text, type)
        }
    }

    // ── Built-in Types ────────────────────────────────────────────────────────

    @Test
    fun testBuiltinTypes() {
        val types = mapOf(
            "Int" to Tx3TokenTypes.TYPE_INT,
            "Bytes" to Tx3TokenTypes.TYPE_BYTES,
            "Bool" to Tx3TokenTypes.TYPE_BOOL,
            "Unit" to Tx3TokenTypes.TYPE_UNIT,
            "UtxoRef" to Tx3TokenTypes.TYPE_UTXO_REF,
            "Address" to Tx3TokenTypes.TYPE_ADDRESS,
            "Value" to Tx3TokenTypes.TYPE_VALUE,
            "List" to Tx3TokenTypes.TYPE_LIST,
            "Map" to Tx3TokenTypes.TYPE_MAP,
        )
        for ((text, type) in types) {
            assertSingleToken(text, type)
        }
    }

    // ── Built-in Symbols ──────────────────────────────────────────────────────

    @Test
    fun testBuiltinSymbols() {
        assertSingleToken("Ada", Tx3TokenTypes.BUILTIN_ADA)
        assertSingleToken("fees", Tx3TokenTypes.BUILTIN_FEES)
    }

    // ── Operators ─────────────────────────────────────────────────────────────

    @Test
    fun testOperators() {
        val operators = mapOf(
            "+" to Tx3TokenTypes.OP_PLUS,
            "-" to Tx3TokenTypes.OP_MINUS,
            "*" to Tx3TokenTypes.OP_MUL,
            "/" to Tx3TokenTypes.OP_DIV,
            "==" to Tx3TokenTypes.OP_EQ,
            "!=" to Tx3TokenTypes.OP_NEQ,
            "<" to Tx3TokenTypes.OP_LT,
            "<=" to Tx3TokenTypes.OP_LE,
            ">" to Tx3TokenTypes.OP_GT,
            ">=" to Tx3TokenTypes.OP_GE,
            "&&" to Tx3TokenTypes.OP_AND,
            "||" to Tx3TokenTypes.OP_OR,
            "!" to Tx3TokenTypes.OP_NOT,
            "=" to Tx3TokenTypes.OP_ASSIGN,
            "::" to Tx3TokenTypes.OP_DOUBLE_COLON,
            "..." to Tx3TokenTypes.OP_SPREAD,
        )
        for ((text, type) in operators) {
            assertSingleToken(text, type)
        }
    }

    // ── Punctuation ───────────────────────────────────────────────────────────

    @Test
    fun testPunctuation() {
        val punctuation = mapOf(
            "{" to Tx3TokenTypes.LBRACE,
            "}" to Tx3TokenTypes.RBRACE,
            "(" to Tx3TokenTypes.LPAREN,
            ")" to Tx3TokenTypes.RPAREN,
            "[" to Tx3TokenTypes.LBRACKET,
            "]" to Tx3TokenTypes.RBRACKET,
            "," to Tx3TokenTypes.COMMA,
            ";" to Tx3TokenTypes.SEMICOLON,
            ":" to Tx3TokenTypes.COLON,
            "." to Tx3TokenTypes.DOT,
        )
        for ((text, type) in punctuation) {
            assertSingleToken(text, type)
        }
    }

    // ── Literals ──────────────────────────────────────────────────────────────

    @Test
    fun testIntLiteral() {
        assertSingleToken("42", Tx3TokenTypes.INT_LITERAL)
        assertSingleToken("0", Tx3TokenTypes.INT_LITERAL)
        assertSingleToken("1000000", Tx3TokenTypes.INT_LITERAL)
    }

    @Test
    fun testHexLiteral() {
        assertSingleToken("0xabcdef01", Tx3TokenTypes.HEX_LITERAL)
        assertSingleToken("0x00", Tx3TokenTypes.HEX_LITERAL)
    }

    @Test
    fun testStringLiteral() {
        assertSingleToken("\"hello world\"", Tx3TokenTypes.STRING_LITERAL)
        assertSingleToken("\"\"", Tx3TokenTypes.STRING_LITERAL)
    }

    @Test
    fun testBytesLiteral() {
        assertSingleToken("#deadbeef", Tx3TokenTypes.BYTES_LITERAL)
    }

    @Test
    fun testUtxoRefLiteral() {
        // A UTXO reference: 0xHEX#DIGIT
        assertSingleToken("0xabcdef0123456789abcdef0123456789abcdef0123456789abcdef0123456789#0", Tx3TokenTypes.UTXO_REF_LITERAL)
    }

    @Test
    fun testAssetLiteral() {
        // Asset literal: 0xHEX."string"
        assertSingleToken("0xabcdef01.\"TOKEN\"", Tx3TokenTypes.ASSET_LITERAL)
    }

    // ── Compound Literal Precedence ───────────────────────────────────────────

    @Test
    fun testUtxoRefTokenizesAsOneToken() {
        // Ensure a UTXO ref literal is a single token, not split into hex + # + int
        val tokens = tokenize("0xabcdef0123456789abcdef0123456789abcdef0123456789abcdef0123456789#0")
            .filter { it.first != com.intellij.psi.TokenType.WHITE_SPACE }
        assertEquals("UTXO ref should be a single token", 1, tokens.size)
        assertEquals(Tx3TokenTypes.UTXO_REF_LITERAL, tokens[0].first)
    }

    // ── Comments ──────────────────────────────────────────────────────────────

    @Test
    fun testLineComment() {
        assertSingleToken("// this is a comment", Tx3TokenTypes.LINE_COMMENT)
    }

    @Test
    fun testBlockComment() {
        assertSingleToken("/* block comment */", Tx3TokenTypes.BLOCK_COMMENT)
        assertSingleToken("/* multi\nline\ncomment */", Tx3TokenTypes.BLOCK_COMMENT)
    }

    // ── Identifiers ───────────────────────────────────────────────────────────

    @Test
    fun testIdentifier() {
        assertSingleToken("myVar", Tx3TokenTypes.IDENTIFIER)
        assertSingleToken("camelCase123", Tx3TokenTypes.IDENTIFIER)
        assertSingleToken("MyType", Tx3TokenTypes.IDENTIFIER)
    }

    // ── Bad Characters ────────────────────────────────────────────────────────

    @Test
    fun testBadCharacter() {
        val tokens = tokenize("@")
            .filter { it.first != com.intellij.psi.TokenType.WHITE_SPACE }
        assertEquals(1, tokens.size)
        assertEquals(com.intellij.psi.TokenType.BAD_CHARACTER, tokens[0].first)
    }

    // ── Multi-token Sequence ──────────────────────────────────────────────────

    @Test
    fun testMultiTokenSequence() {
        val tokens = tokenize("party Sender;")
            .filter { it.first != com.intellij.psi.TokenType.WHITE_SPACE }
        assertEquals(3, tokens.size)
        assertEquals(Tx3TokenTypes.KW_PARTY, tokens[0].first)
        assertEquals(Tx3TokenTypes.IDENTIFIER, tokens[1].first)
        assertEquals("Sender", tokens[1].second)
        assertEquals(Tx3TokenTypes.SEMICOLON, tokens[2].first)
    }

    // ── Identifier Patterns ────────────────────────────────────────────────────

    @Test
    fun testIdentifierWithUnderscore() {
        assertSingleToken("my_var", Tx3TokenTypes.IDENTIFIER)
        assertSingleToken("some_long_name", Tx3TokenTypes.IDENTIFIER)
        assertSingleToken("x123_y", Tx3TokenTypes.IDENTIFIER)
    }

    @Test
    fun testIdentifierStartingWithUppercase() {
        assertSingleToken("MyType", Tx3TokenTypes.IDENTIFIER)
        assertSingleToken("TokenDatum", Tx3TokenTypes.IDENTIFIER)
        assertSingleToken("LendTokenDetails", Tx3TokenTypes.IDENTIFIER)
    }

    // ── Double Colon Operator ──────────────────────────────────────────────────

    @Test
    fun testDoubleColonInVariantConstruction() {
        val tokens = tokenize("Action::Increment")
            .filter { it.first != com.intellij.psi.TokenType.WHITE_SPACE }
        assertEquals(3, tokens.size)
        assertEquals(Tx3TokenTypes.IDENTIFIER, tokens[0].first)
        assertEquals("Action", tokens[0].second)
        assertEquals(Tx3TokenTypes.OP_DOUBLE_COLON, tokens[1].first)
        assertEquals(Tx3TokenTypes.IDENTIFIER, tokens[2].first)
        assertEquals("Increment", tokens[2].second)
    }

    @Test
    fun testCardanoDoubleColonSequence() {
        val tokens = tokenize("cardano::plutus_witness")
            .filter { it.first != com.intellij.psi.TokenType.WHITE_SPACE }
        assertEquals(3, tokens.size)
        assertEquals(Tx3TokenTypes.KW_CARDANO, tokens[0].first)
        assertEquals(Tx3TokenTypes.OP_DOUBLE_COLON, tokens[1].first)
        assertEquals(Tx3TokenTypes.IDENTIFIER, tokens[2].first)
        assertEquals("plutus_witness", tokens[2].second)
    }

    // ── Spread Operator ────────────────────────────────────────────────────────

    @Test
    fun testSpreadOperatorSequence() {
        val tokens = tokenize("...source")
            .filter { it.first != com.intellij.psi.TokenType.WHITE_SPACE }
        assertEquals(2, tokens.size)
        assertEquals(Tx3TokenTypes.OP_SPREAD, tokens[0].first)
        assertEquals("...", tokens[0].second)
        assertEquals(Tx3TokenTypes.IDENTIFIER, tokens[1].first)
    }

    // ── Logical Operators in Context ───────────────────────────────────────────

    @Test
    fun testLogicalOperatorSequence() {
        val tokens = tokenize("a && b || !c")
            .filter { it.first != com.intellij.psi.TokenType.WHITE_SPACE }
        assertEquals(6, tokens.size)
        assertEquals(Tx3TokenTypes.IDENTIFIER, tokens[0].first)
        assertEquals(Tx3TokenTypes.OP_AND, tokens[1].first)
        assertEquals(Tx3TokenTypes.IDENTIFIER, tokens[2].first)
        assertEquals(Tx3TokenTypes.OP_OR, tokens[3].first)
        assertEquals(Tx3TokenTypes.OP_NOT, tokens[4].first)
        assertEquals(Tx3TokenTypes.IDENTIFIER, tokens[5].first)
    }

    // ── Generic Type Sequence ──────────────────────────────────────────────────

    @Test
    fun testGenericTypeTokenization() {
        val tokens = tokenize("List<Int>")
            .filter { it.first != com.intellij.psi.TokenType.WHITE_SPACE }
        assertEquals(4, tokens.size)
        assertEquals(Tx3TokenTypes.TYPE_LIST, tokens[0].first)
        assertEquals(Tx3TokenTypes.OP_LT, tokens[1].first)
        assertEquals(Tx3TokenTypes.TYPE_INT, tokens[2].first)
        assertEquals(Tx3TokenTypes.OP_GT, tokens[3].first)
    }

    @Test
    fun testMapTypeTokenization() {
        val tokens = tokenize("Map<Bytes, Int>")
            .filter { it.first != com.intellij.psi.TokenType.WHITE_SPACE }
        assertEquals(6, tokens.size)
        assertEquals(Tx3TokenTypes.TYPE_MAP, tokens[0].first)
        assertEquals(Tx3TokenTypes.OP_LT, tokens[1].first)
        assertEquals(Tx3TokenTypes.TYPE_BYTES, tokens[2].first)
        assertEquals(Tx3TokenTypes.COMMA, tokens[3].first)
        assertEquals(Tx3TokenTypes.TYPE_INT, tokens[4].first)
        assertEquals(Tx3TokenTypes.OP_GT, tokens[5].first)
    }

    // ── Bracket List Type Sequence ─────────────────────────────────────────────

    @Test
    fun testBracketListType() {
        val tokens = tokenize("[Int]")
            .filter { it.first != com.intellij.psi.TokenType.WHITE_SPACE }
        assertEquals(3, tokens.size)
        assertEquals(Tx3TokenTypes.LBRACKET, tokens[0].first)
        assertEquals(Tx3TokenTypes.TYPE_INT, tokens[1].first)
        assertEquals(Tx3TokenTypes.RBRACKET, tokens[2].first)
    }

    // ── Unit Literal ───────────────────────────────────────────────────────────

    @Test
    fun testUnitLiteralTokenization() {
        val tokens = tokenize("()")
            .filter { it.first != com.intellij.psi.TokenType.WHITE_SPACE }
        assertEquals(2, tokens.size)
        assertEquals(Tx3TokenTypes.LPAREN, tokens[0].first)
        assertEquals(Tx3TokenTypes.RPAREN, tokens[1].first)
    }

    // ── Asset Literal Variants ─────────────────────────────────────────────────

    // ── Question and Pipe Operators ──────────────────────────────────────────

    @Test
    fun testQuestionOperator() {
        assertSingleToken("?", Tx3TokenTypes.OP_QUESTION)
    }

    @Test
    fun testPipeOperator() {
        assertSingleToken("|", Tx3TokenTypes.OP_PIPE)
    }

    @Test
    fun testPipeDoesNotBreakLogicalOr() {
        // || should still tokenize as OP_OR, not two OP_PIPEs
        assertSingleToken("||", Tx3TokenTypes.OP_OR)
    }

    @Test
    fun testTernaryTokenSequence() {
        val tokens = tokenize("x ? 1 : 0")
            .filter { it.first != com.intellij.psi.TokenType.WHITE_SPACE }
        assertEquals(5, tokens.size)
        assertEquals(Tx3TokenTypes.IDENTIFIER, tokens[0].first)
        assertEquals(Tx3TokenTypes.OP_QUESTION, tokens[1].first)
        assertEquals(Tx3TokenTypes.INT_LITERAL, tokens[2].first)
        assertEquals(Tx3TokenTypes.COLON, tokens[3].first)
        assertEquals(Tx3TokenTypes.INT_LITERAL, tokens[4].first)
    }

    @Test
    fun testUnionTypeTokenSequence() {
        val tokens = tokenize("Bytes | Int")
            .filter { it.first != com.intellij.psi.TokenType.WHITE_SPACE }
        assertEquals(3, tokens.size)
        assertEquals(Tx3TokenTypes.TYPE_BYTES, tokens[0].first)
        assertEquals(Tx3TokenTypes.OP_PIPE, tokens[1].first)
        assertEquals(Tx3TokenTypes.TYPE_INT, tokens[2].first)
    }

    @Test
    fun testAssetLiteralLongHash() {
        assertSingleToken(
            "0xabcdef0123456789abcdef0123456789abcdef0123456789abcdef01.\"CONTROL\"",
            Tx3TokenTypes.ASSET_LITERAL
        )
    }
}
