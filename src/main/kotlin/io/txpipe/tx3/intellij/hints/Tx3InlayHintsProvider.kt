package io.txpipe.tx3.intellij.hints

import com.intellij.codeInsight.hints.*
import com.intellij.codeInsight.hints.presentation.InlayPresentation
import com.intellij.codeInsight.hints.presentation.PresentationFactory
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import io.txpipe.tx3.intellij.psi.Tx3File
import io.txpipe.tx3.intellij.psi.impl.*
import javax.swing.JPanel

class Tx3InlayHintsProvider : InlayHintsProvider<NoSettings> {

    override val key: SettingsKey<NoSettings> = SettingsKey("tx3.inlay.hints")
    override val name: String = "Tx3 Inlay Hints"
    override val previewText: String = """
        party Sender;
        record State { lock_until: Int, owner: Bytes }
        tx transfer(quantity: Int) {
          input gas { from: Sender, min_amount: Ada(quantity) }
          output { to: Sender, amount: gas - Ada(quantity) - fees }
        }
    """.trimIndent()

    override fun createSettings(): NoSettings = NoSettings()

    override fun getCollectorFor(
        file: PsiFile,
        editor: Editor,
        settings: NoSettings,
        sink: InlayHintsSink
    ): InlayHintsCollector? {
        if (file !is Tx3File) return null
        return Tx3InlayHintsCollector(editor)
    }

    override fun createConfigurable(settings: NoSettings): ImmediateConfigurable = object : ImmediateConfigurable {
        override fun createComponent(listener: ChangeListener) = JPanel()
    }
}

@Suppress("UnstableApiUsage")
private class Tx3InlayHintsCollector(
    editor: Editor,
) : InlayHintsCollector {

    private val factory = PresentationFactory(editor)

    override fun collect(element: PsiElement, editor: Editor, sink: InlayHintsSink): Boolean {
        when (element) {
            is Tx3TxParamImpl    -> addTypeHint(element, element.paramType()?.typeName(), sink)
            is Tx3RecordFieldImpl -> addTypeHint(element, element.fieldType()?.typeName(), sink)
        }
        return true
    }

    private fun addTypeHint(element: Tx3NamedElementBase, typeName: String?, sink: InlayHintsSink) {
        typeName ?: return
        val nameIdent = element.nameIdentifier ?: return

        // Skip if the type annotation is already written explicitly
        var sibling = nameIdent.nextSibling
        while (sibling != null) {
            val text = sibling.text.trim()
            if (text == ":") return
            if (text.isNotEmpty() && text != " ") break
            sibling = sibling.nextSibling
        }

        val offset = nameIdent.textRange.endOffset
        val presentation: InlayPresentation = factory.roundWithBackground(
            factory.smallText(": $typeName")
        )
        sink.addInlineElement(offset, relatesToPrecedingText = true, presentation = presentation, placeAtTheEndOfLine = false)
    }
}