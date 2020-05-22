package org.isda.cdm.documentation

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.util.function.BiPredicate
import java.util.stream.Collectors

class CodeBlockValidator(
        private val docPath: String,
        private val modelPath: String,
        private val fixUp: Boolean) {

    private val synonymRegex = "\\[synonym [^\\]]*\\]"
    private val definitionRegex = "<\".*\">"
    private val lineCommentRegex = "[^:]\\/\\/.*\$"
    private val whitespaceRegex = "\\s+"
    private val illegalSyntaxRegex = "$synonymRegex|$definitionRegex|$lineCommentRegex"
    private val codeBlockRegex = Regex("(\\.\\. code-block:: .*\\s+$)((\\n +.*|\\s)+)", RegexOption.MULTILINE)

    fun validate() {
        val codeBlocks = getCodeBlocks()
        val model = getModel()
        val invalidBlocks = codeBlocks
                .filter { codeBlock -> !model.contains(codeBlock) }
                .onEach { println(it) }

        val blocks = invalidBlocks.count()
        if (blocks > 0) {
            throw java.lang.IllegalStateException("Found [$blocks] code blocks that don't match model text")
        }
    }

    private fun getCodeBlocks(): Sequence<String> {
        val doc = Path.of(docPath).toUri().toURL().readText()

        val codeBlocks = codeBlockRegex.findAll(doc)
                .map(MatchResult::value)
                .ifEmpty { throw IllegalStateException("No code blocks found in documentation file [$docPath]. Doesn't sound right! Go check.") }

        val illegalCodeBlocks = codeBlocks
                .filter { it.contains(Regex(illegalSyntaxRegex, RegexOption.MULTILINE)) }
                .onEach(::println)

        val illegalBlocks = illegalCodeBlocks.count()

        return if (illegalBlocks > 0) {
            if (fixUp) {
                println("WARNING in fix-up mode, removing illegal syntax from [$docPath]")
                val sanitised = doc.replace(Regex(illegalSyntaxRegex, RegexOption.MULTILINE), "")
                Files.writeString(Paths.get(docPath), sanitised)
                getCodeBlocks()
            } else {
                throw java.lang.IllegalStateException("Found [$illegalBlocks] invalid code blocks in documentation file [$docPath]. Line comments, definitions, and synonyms are not allowed in code snippets")
            }
        } else {
            codeBlocks
        }
    }

    private fun getModel(): String {
        val rosettaFileFilter = BiPredicate<Path, BasicFileAttributes> { path, attr -> attr.isRegularFile && path.fileName.toString().endsWith(".rosetta") }

        return Files.find(Path.of(modelPath), 1, rosettaFileFilter)
                .map { path -> Files.readString(path, Charset.defaultCharset()) }.collect(Collectors.joining())
                .replace(Regex(illegalSyntaxRegex, RegexOption.MULTILINE), "")
                .replace(Regex(whitespaceRegex), "")
    }
}

fun main(args: Array<String>) {
    val options = Options()
            .addOption("u", "fix-up", false, "Remove illegal code syntax from document file in-place.")
            .addOption("d", "doc-path", true, "Relative path to the document.")
            .addOption("m", "model-path", true, "Relative path to the model files directory.")

    val cmd = DefaultParser().parse(options, args)

    val docPath = cmd.getOptionValue("doc-path") ?: "documentation/source/documentation.rst"
    val modelPath = cmd.getOptionValue("model-path") ?: "src/main/rosetta"
    val fixUp = cmd.hasOption("fix-up")

    CodeBlockValidator(docPath, modelPath, fixUp).validate()
}