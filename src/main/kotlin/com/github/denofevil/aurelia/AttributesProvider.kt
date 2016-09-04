package com.github.denofevil.aurelia

import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ArrayUtil
import com.intellij.xml.XmlAttributeDescriptor
import com.intellij.xml.XmlAttributeDescriptorsProvider
import com.intellij.xml.impl.BasicXmlAttributeDescriptor

/**
 * @author Dennis.Ushakov
 */
class AttributesProvider : XmlAttributeDescriptorsProvider {
    override fun getAttributeDescriptors(xmlTag: XmlTag): Array<XmlAttributeDescriptor> = arrayOf(
            AttributeDescriptor(Aurelia.REPEAT_FOR),
            AttributeDescriptor(Aurelia.AURELIA_APP)
    )

    override fun getAttributeDescriptor(name: String, xmlTag: XmlTag): XmlAttributeDescriptor? {
        for (attr in Aurelia.INJECTABLE) {
            if (name.endsWith(".$attr")) {
                val descriptor = xmlTag.descriptor
                if (descriptor != null) {
                    val attrName = name.substring(0, name.length - attr.length - 1)
                    val attributeDescriptor = descriptor.getAttributeDescriptor(attrName, xmlTag)
                    return attributeDescriptor ?: descriptor.getAttributeDescriptor("on" + attrName, xmlTag)
                }
            }
        }
        return if (Aurelia.REPEAT_FOR == name || Aurelia.AURELIA_APP == name) AttributeDescriptor(name) else null
    }

    private class AttributeDescriptor(private val name: String) : BasicXmlAttributeDescriptor() {

        override fun init(psiElement: PsiElement) {
        }

        override fun isRequired(): Boolean = false
        override fun hasIdType(): Boolean = false
        override fun hasIdRefType(): Boolean = false
        override fun isEnumerated(): Boolean = false
        override fun getDeclaration(): PsiElement? = null
        override fun getName(): String = name
        override fun getDependences(): Array<Any> = ArrayUtil.EMPTY_OBJECT_ARRAY
        override fun isFixed(): Boolean = false
        override fun getDefaultValue(): String? = null
        override fun getEnumeratedValues(): Array<String>? = ArrayUtil.EMPTY_STRING_ARRAY
    }
}