package io.txpipe.tx3.intellij

import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Tests for line/block comment toggling.
 */
class Tx3CommenterTest : BasePlatformTestCase() {

    fun testLineComment() {
        myFixture.configureByText("test.tx3", "party Sender<caret>;")
        myFixture.performEditorAction("CommentByLineComment")
        myFixture.checkResult("//party Sender;")
    }

    fun testBlockComment() {
        myFixture.configureByText("test.tx3", "<selection>party Sender;</selection>")
        myFixture.performEditorAction("CommentByBlockComment")
        myFixture.checkResult("/*\nparty Sender;*/\n")
    }
}
