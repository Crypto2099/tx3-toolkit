package io.txpipe.tx3.intellij.formatting

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Tests for the Tx3 code formatter.
 * Uses inline before/after strings — no fixture files.
 */
class Tx3FormattingTest : BasePlatformTestCase() {

    private fun doFormatTest(before: String, after: String) {
        myFixture.configureByText("test.tx3", before)
        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformat(myFixture.file)
        }
        myFixture.checkResult(after)
    }

    fun testBlockIndentation() {
        doFormatTest(
            """
tx test() {
output {
to: Sender,
amount: Ada(100),
}
}
            """.trimIndent(),
            """
tx test() {
    output {
        to: Sender,
        amount: Ada(100),
    }
}
            """.trimIndent()
        )
    }

    fun testSpaceAroundOperators() {
        doFormatTest(
            """
party Sender;

tx test(a: Int) {
    let x=a+1;
    output {
        to: Sender,
        amount: Ada(x),
    }
}
            """.trimIndent(),
            """
party Sender;

tx test(a: Int) {
    let x = a + 1;
    output {
        to: Sender,
        amount: Ada(x),
    }
}
            """.trimIndent()
        )
    }

    fun testColonSpacing() {
        doFormatTest(
            """
type State {
    owner:Address,
    value :Int,
}
            """.trimIndent(),
            """
type State {
    owner: Address,
    value: Int,
}
            """.trimIndent()
        )
    }

    fun testBraceSpacing() {
        doFormatTest(
            """
type State{
    owner: Address,
}
            """.trimIndent(),
            """
type State {
    owner: Address,
}
            """.trimIndent()
        )
    }

    fun testFormattingIdempotency() {
        val formatted = """
party Sender;
party Receiver;

tx transfer(qty: Int) {
    input source {
        from: Sender,
        min_amount: Ada(qty),
    }
    output {
        to: Receiver,
        amount: Ada(qty),
    }
    output {
        to: Sender,
        amount: source - Ada(qty) - fees,
    }
}
        """.trimIndent()
        // Formatting an already-formatted file should not change it
        doFormatTest(formatted, formatted)
    }
}
