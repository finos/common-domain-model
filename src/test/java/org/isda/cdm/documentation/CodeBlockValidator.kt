package org.isda.cdm.documentation

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.util.function.BiPredicate
import java.util.stream.Collectors

class CodeBlockValidator {

    private val docFile = "/documentation-test.txt"
    private val modelDir = "cdm/rosetta"

    private val rosettaFileFilter = BiPredicate<Path, BasicFileAttributes> { path, attr -> attr.isRegularFile && path.fileName.toString().endsWith(".rosetta") }

    private val synonymRegex = "\\[synonym [^\\]]*\\]"
    private val definitionRegex = "<\".*\">"
    private val lineCommentRegex = "\\/\\/.*"
    private val whitespaceRegex = "\\s+"
    private val illegalSyntaxRegex = "$synonymRegex|$definitionRegex|$lineCommentRegex"

    private val codeBlockRegex = Regex("(\\.\\. code-block:: .*\\s+$)((\\n +.*|\\s)+)", RegexOption.MULTILINE)

    fun validate() {
        val codeBlocks = getCodeBlocks()
        val model = getModel()
        val invalidBlocks = codeBlocks
                .filter { codeBlock -> !model.contains(codeBlock) }
                .onEach { println(it) }
                .toSet()

        if (invalidBlocks.isNotEmpty()) {
            throw java.lang.IllegalStateException("Found code blocks that don't match model text")
        }
    }

    private fun getCodeBlocks(): Sequence<String> {
        val doc = this::class.java.getResource(docFile).readText()

        val codeBlocks = codeBlockRegex.findAll(doc)
                .map(MatchResult::value)
                .ifEmpty { throw IllegalStateException("No code blocks found in documentation file [$docFile]. Doesn't sound right! Go check.") }

        val illegalCodeBlocks = codeBlocks
                .filter { it.contains(Regex(illegalSyntaxRegex, RegexOption.MULTILINE)) }
                .onEach(::println)

        if (illegalCodeBlocks.count() > 0) {
            throw java.lang.IllegalStateException("Found illegal code syntax in documentation file [$docFile]. Line comments, definitions, and synonyms are not allowed in code snippets")
        } else {
            return codeBlocks
        }
    }

    private fun getModel(): String {
        val rosettaFileFilter = rosettaFileFilter
        val modelDirectory = this::class.java.classLoader.getResource(modelDir) ?: throw IllegalStateException("No model code found in classpath: $modelDir")
        return Files.find(Paths.get(modelDirectory.toURI()), 1, rosettaFileFilter)
                .map { path -> Files.readString(path, Charset.defaultCharset()) }.collect(Collectors.joining())
                .replace(Regex(illegalSyntaxRegex, RegexOption.MULTILINE), "")
                .replace(Regex(whitespaceRegex), "")
    }
}

fun main(args: Array<String>) {
    CodeBlockValidator().validate()
}