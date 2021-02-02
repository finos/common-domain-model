package org.isda.cdm.documentation

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.util.function.BiPredicate
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.system.exitProcess

class DocumentationCodeValidator(
        private val docPath: String,
        private val snippetPath: String,
        private val modelPath: String) {

    private val synonymRegex = "\\[synonym [^\\]]*\\]"
    private val definitionRegex = "<\".*\">"
    private val lineCommentRegex = "[^:]\\/\\/.*\$"
    private val whitespaceRegex = "\\s+"
    private val illegalSyntaxRegex = "$synonymRegex|$definitionRegex|$lineCommentRegex"
    private val codeBlockRegex = Regex("(\\.\\. code-block:: .*\\s+$)((\\n[ \\t]+.*|\\s)+)", RegexOption.MULTILINE)

    fun validate() {
        fun validate(code: Sequence<String>, model: String): Int {
            val invalidCode = code
                    .filter { _code -> !_code.contains(".. code-block:: Javascript") }
                    .filter { _code -> !_code.contains(".. code-block:: Java") }
                    .filter { _code ->
                        val cleaned = _code
                                .replace(Regex(".*\\.\\. code-block.*"), "")
                                .replace(Regex(whitespaceRegex), "")
                        !model.contains(cleaned, ignoreCase = false)
                    }
                    .onEach(::println)

            return invalidCode.count()
        }

        val model = getModel()

        val codeBlocks = getCodeBlocks()
        val illegalCodeBlocks = extractInvalidCode(codeBlocks, "code-block")
                .onEach(::println)

        val snippets = getSnippets()
        val illegalSnippets = extractInvalidCode(snippets, "code-snippets")
                .onEach(::println)

        if ((illegalCodeBlocks + illegalSnippets).count() > 0) {
            System.err.println("ERROR found illegal syntax in code block/snippet. Run this script with --fix-up flag to sanitise.")
            exitProcess(1)
        }

        val invalidCodeBlocks = validate(codeBlocks, model)
        val invalidSnippets = validate(snippets, model)

        if (invalidCodeBlocks > 0) {
            System.err.println("Found [$invalidCodeBlocks] code-blocks that don't match model text.")
        }
        if (invalidSnippets > 0) {
            System.err.println("Found [$invalidSnippets] code-snippets that don't match model text.")
        }
        if (invalidCodeBlocks + invalidSnippets != 0) {
            exitProcess(1)
        }
    }

    private fun getCodeBlocks(): Sequence<String> {
        val docs = loadFiles(docPath, ".rst").map(File::content).asSequence()

        val codeBlocks = docs.flatMap { doc -> codeBlockRegex.findAll(doc) }
                .map(MatchResult::value)
                .ifEmpty { throw IllegalStateException("No code blocks found in documentation file [$docPath]. Doesn't sound right! Go check.") }

        return codeBlocks;
    }

    private fun getSnippets(): Sequence<String> {
        val snippets = loadFiles(snippetPath, ".snippet").map(File::content).asSequence()
        return extractInvalidCode(snippets, "code-snippet")
    }

    private fun getModel(): String {
        return loadFiles(modelPath, ".rosetta").joinToString("")
                .replace(Regex(illegalSyntaxRegex, RegexOption.MULTILINE), "")
                .replace(Regex(whitespaceRegex), "")
    }

    private fun loadFiles(dir: String, ext: String): Collection<File> {
        val docFileFilter = BiPredicate<Path, BasicFileAttributes> { path, attr -> attr.isRegularFile && path.fileName.toString().endsWith(ext) }

        return Files.find(Path.of(dir), 1, docFileFilter).use { paths: Stream<Path> ->
            paths.map { path -> File(path, Files.readString(path)) }.collect(Collectors.toList())
        }
    }

    private fun extractInvalidCode(code: Sequence<String>, type: String): Sequence<String> {
        return code.filter { it.contains(Regex(illegalSyntaxRegex, RegexOption.MULTILINE)) }
    }

    fun fixUp() {
        val files = loadFiles(docPath, ".rst") + loadFiles(snippetPath, ".snippet")
        files.forEach { file ->
            val sanitised = file.content.replace(Regex(illegalSyntaxRegex, RegexOption.MULTILINE), "")
            println("Sanitising [${file.path}]")
            Files.writeString(file.path, sanitised)
        }
    }
}

data class File(val path: Path, val content: String)

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

    val validator = DocumentationCodeValidator(docPath, snippetPath, modelPath)

    if (fixUp) {
        println("WARNING Running in fix-up mode, this will sanitise documentation .rst and .snippets files in-place.")
        validator.fixUp()
    }

    validator.validate()
}