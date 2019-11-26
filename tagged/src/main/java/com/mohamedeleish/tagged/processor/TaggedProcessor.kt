package com.mohamedeleish.tagged.processor

import com.google.auto.service.AutoService
import com.mohamedeleish.tagged.annotation.Tagged
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(TaggedProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class TaggedProcessor : AbstractProcessor() {
    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun process(
        elementsSet: MutableSet<out TypeElement>,
        roundEnvironment: RoundEnvironment
    ): Boolean {

        val elements: List<String> = elementsSet.map { it.qualifiedName.toString() }
        val targetTagName = Tagged::class.java.canonicalName
        if (elements.contains(targetTagName)) {
            val generatedSourcesRoot: String =
                processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME].orEmpty()

            if (generatedSourcesRoot.isEmpty())
                return false

            val generatedDir = File(generatedSourcesRoot).apply { mkdir() }
            val fileSpecBuilder = FileSpec.builder("tagged.generated", "Tags")

            roundEnvironment.getElementsAnnotatedWith(Tagged::class.java)?.forEach {
                val tagName = getTagValNameFor(it)
                val tagValue = getTagFor(it)

                fileSpecBuilder
                    .addProperty(
                        PropertySpec.builder(
                            tagName,
                            String::class,
                            KModifier.CONST
                        ).initializer("\"$tagValue\"").build()
                    )
            }

            fileSpecBuilder
                .build()
                .writeTo(generatedDir)
        }

        return false
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Tagged::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    private fun getTagValNameFor(element: Element): String {
        val taggedAnnotation = element.getAnnotation(Tagged::class.java)
        val customValName = taggedAnnotation.customValName
        return if (customValName.isNotEmpty()) customValName
        else getSnakeCaseStringFrom(element.simpleName.toString()) + "_TAG"
    }

    private fun getTagFor(element: Element): String {
        val taggedAnnotation = element.getAnnotation(Tagged::class.java)
        val customTag = taggedAnnotation.customTag
        return if (customTag.isNotEmpty()) customTag
        else {
            val elementPackage = processingEnv.elementUtils.getPackageOf(element)
            val elementName = element.simpleName.toString()
            "${elementPackage}.${elementName}"
        }
    }

    private fun getSnakeCaseStringFrom(camelCase: String): String {
        val components = ArrayList<String>()
        for (charIndex in camelCase.indices) {
            val char = camelCase[charIndex]
            if (char.isUpperCase()) {
                var component = char.toString()
                for (subChar in camelCase.substring(charIndex + 1, camelCase.length)) {
                    if (subChar.isLowerCase())
                        component += subChar
                    else
                        break
                }

                components.add(component.toUpperCase())
            }
        }

        return components.joinToString("_")
    }
}