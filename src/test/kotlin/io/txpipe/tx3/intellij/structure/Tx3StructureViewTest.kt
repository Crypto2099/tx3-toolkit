package io.txpipe.tx3.intellij.structure

import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder
import com.intellij.lang.LanguageStructureViewBuilder
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Tests for the Structure View panel.
 */
class Tx3StructureViewTest : BasePlatformTestCase() {

    fun testStructureViewContainsDeclarations() {
        val psiFile = myFixture.configureByText("test.tx3", """
            party Sender;
            party Receiver;

            record State {
                owner: Address,
                value: Int,
            }

            tx transfer(qty: Int) {
                input source {
                    from: Sender,
                    min_amount: Ada(qty),
                }
                output {
                    to: Receiver,
                    amount: Ada(qty),
                }
            }
        """.trimIndent())

        val builder = LanguageStructureViewBuilder.INSTANCE.getStructureViewBuilder(psiFile)
        assertNotNull("Structure view builder should exist", builder)
        val model = (builder as TreeBasedStructureViewBuilder).createStructureViewModel(myFixture.editor)

        try {
            val root = model.root
            val children = root.children
            val texts = children.map { it.presentation.presentableText }

            assertTrue("Should contain party 'Sender'", texts.any { it?.contains("Sender") == true })
            assertTrue("Should contain party 'Receiver'", texts.any { it?.contains("Receiver") == true })
            assertTrue("Should contain record 'State'", texts.any { it?.contains("State") == true })
            assertTrue("Should contain tx 'transfer'", texts.any { it?.contains("transfer") == true })
        } finally {
            model.dispose()
        }
    }

    fun testTxNodeHasChildren() {
        val psiFile = myFixture.configureByText("test.tx3", """
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
            }
        """.trimIndent())

        val builder = LanguageStructureViewBuilder.INSTANCE.getStructureViewBuilder(psiFile)
        assertNotNull(builder)
        val model = (builder as TreeBasedStructureViewBuilder).createStructureViewModel(myFixture.editor)

        try {
            val root = model.root
            val txNode = root.children.find {
                it.presentation.presentableText?.contains("transfer") == true
            }
            assertNotNull("Should find tx 'transfer' node", txNode)

            val txChildren = txNode!!.children
            assertTrue("Tx node should have children (input/output blocks)", txChildren.isNotEmpty())
        } finally {
            model.dispose()
        }
    }
}
