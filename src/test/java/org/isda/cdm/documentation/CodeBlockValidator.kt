package org.isda.cdm.documentation

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.util.function.BiPredicate
import kotlin.streams.asSequence
import kotlin.streams.toList
import kotlin.system.exitProcess

class CodeBlockValidator(
        private val docPath: String,
        private val snippetPath: String,
        private val modelPath: String,
        private val fixUp: Boolean) {

    private val synonymRegex = "\\[synonym [^\\]]*\\]"
    private val definitionRegex = "<\".*\">"
    private val lineCommentRegex = "[^:]\\/\\/.*\$"
    private val whitespaceRegex = "\\s+"
    private val illegalSyntaxRegex = "$synonymRegex|$definitionRegex|$lineCommentRegex"
    private val codeBlockRegex = Regex("(\\.\\. code-block:: .*\\s+$)((\\n +.*|\\s)+)", RegexOption.MULTILINE)

    fun validate() {
        fun validate(code: Sequence<String>, model: String): Int {
            val invalidCode = code
                    .filter { _code -> !_code.contains(Regex(".*\\.\\. code-block:: Java")) }
                    .filter { _code ->
                        val cleaned = _code
                                .replace(Regex(".*\\.\\. code-block.*"), "")
                                .replace(Regex(whitespaceRegex), "")
                        !model.contains(cleaned)
                    }
                    .onEach(::println)

            return invalidCode.count()
        }

        val model = getModel()
        val invalidCodeBlocks = validate(getCodeBlocks(), model)
        val invalidSnippets = validate(getSnippets(), model)

        if (invalidCodeBlocks > 0) { System.err.println("Found [$invalidCodeBlocks] code-blocks that don't match model text.") }
        if (invalidSnippets > 0) { System.err.println("Found [$invalidSnippets] code-snippets that don't match model text.") }
        if (invalidCodeBlocks + invalidSnippets != 0) { exitProcess(1) }
    }

    private fun getCodeBlocks(): Sequence<String> {
        val docs = loadFiles(docPath, ".rst").asSequence()

        val codeBlocks = docs.flatMap { doc -> codeBlockRegex.findAll(doc) }
                .map(MatchResult::value)
                .ifEmpty { throw IllegalStateException("No code blocks found in documentation file [$docPath]. Doesn't sound right! Go check.") }

        return isValid(codeBlocks, "code-block")
    }

    private fun getSnippets(): Sequence<String> {
        val snippets = loadFiles(snippetPath, ".snippet").asSequence()
        return isValid(snippets, "code-snippet")
    }

    private fun getModel(): String {
        return loadFiles(modelPath, ".rosetta").joinToString("")
                .replace(Regex(illegalSyntaxRegex, RegexOption.MULTILINE), "")
                .replace(Regex(whitespaceRegex), "")
    }

    private fun loadFiles(dir: String, ext: String): Collection<String> {
        val docFileFilter = BiPredicate<Path, BasicFileAttributes> { path, attr -> attr.isRegularFile && path.fileName.toString().endsWith(ext) }

        return Files.find(Path.of(dir), 1, docFileFilter).use { paths ->
                    paths.map { path -> Files.readString(path, Charset.defaultCharset()) }.toList()
                }
    }

    private fun isValid(code: Sequence<String>, type: String): Sequence<String> {
        val illegalCode = code
                .filter { it.contains(Regex(illegalSyntaxRegex, RegexOption.MULTILINE)) }
                .onEach(::println)

        val illegalBlocks = illegalCode.count()
        return if (illegalBlocks > 0) {
            System.err.println("Found [$illegalBlocks] invalid `$type`s. Run this script with flag --fix-up to remove.")
            exitProcess(1)
        } else {
            code
        }
    }
}

fun main(args: Array<String>) {
    val options = Options()
            .addOption("d", "doc-path", true, "Relative path to the document.")
            .addOption("s", "snippet-path", true, "Relative path to code snippet files.")
            .addOption("m", "model-path", true, "Relative path to the model files directory.")
            .addOption("u", "fix-up", false, "Remove illegal code syntax from document file in-place.")

    val cmd = DefaultParser().parse(options, args)

    val docPath = cmd.getOptionValue("doc-path") ?: "documentation/source"
    val snippetPath = cmd.getOptionValue("snippet-path") ?: "documentation/source/code-snippets"
    val modelPath = cmd.getOptionValue("model-path") ?: "src/main/rosetta"
    val fixUp = cmd.hasOption("fix-up")

    CodeBlockValidator(docPath, snippetPath, modelPath, fixUp).validate()
}