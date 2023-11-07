package org.isda.cdm.documentation;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocumentationCodeValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentationCodeValidator.class);

    public static final List<String> ANNOTATIONS = Lists.newArrayList("docReference");
    private final String annotationRegex = "\\[(" + Joiner.on('|').join(ANNOTATIONS) + ") [^\\]]*\\]";
    private final String definitionRegex = "<\"[\\s\\S]*?\">";
    private final String lineCommentRegex = "[^:]\\/\\/.*$";
    private final String whitespaceRegex = "\\s+";
    private final Pattern illegalSyntaxRegex = Pattern.compile(annotationRegex + "|" + definitionRegex + "|" + lineCommentRegex, Pattern.MULTILINE);
    private static final PatternStreamer codeBlockRegex = new PatternStreamer("```.*?```");

    private String modelPath;
    private String docPath;
    private String snippetPath;


    public DocumentationCodeValidator(String docPath, String snippetPath, String modelPath) {
        this.docPath = docPath;
        this.snippetPath = snippetPath;
        this.modelPath = modelPath;
    }

    void validate() throws IOException {
        String model = getModel();

        List<String> codeBlocks = getCodeBlocks().collect(Collectors.toList());
        Stream<String> illegalCodeBlocks = extractInvalidCode(codeBlocks, "code-block")
                .peek(LOGGER::info);

        List<String> snippets = getSnippets().collect(Collectors.toList());
        Stream<String> illegalSnippets = extractInvalidCode(snippets, "code-snippets")
                .peek(LOGGER::info);

        if (Streams.concat(illegalCodeBlocks, illegalSnippets).count() > 0) {
            LOGGER.error("ERROR found illegal syntax in code block/snippet. Run this script with --fix-up flag to sanitise.");
            System.exit(1);
        }

        long invalidCodeBlocks = validate(codeBlocks, model);
        long invalidSnippets = validate(snippets, model);

        if (invalidCodeBlocks > 0) {
            LOGGER.error("Found " + invalidCodeBlocks + " code-blocks that don't match model text.");
        }
        if (invalidSnippets > 0) {
            LOGGER.error("Found [" + invalidSnippets + "] code-snippets that don't match model text.");
        }
        if (invalidCodeBlocks + invalidSnippets != 0) {
            System.exit(1);
        }
    }

    long validate(List<String> code, String model) {
        Stream<String> invalidCode = code.stream()
                .filter(_code -> !_code.contains("``` sourcecode"))
                .filter(_code -> !_code.contains("``` MD"))
                .filter(_code -> !_code.contains("``` Javascript"))
                .filter(_code -> !_code.contains("``` Java"))
                .filter(_code -> !_code.contains("``` JSON"))
                .filter(_code -> !_code.contains("``` xml"))
                .filter(_code -> {
                    String cleaned = _code
                            .replaceAll(".*```.*", "")
                            .replaceAll(whitespaceRegex, "");
                    return !model.contains(cleaned);
                })
                .peek(LOGGER::info);

        return invalidCode.count();
    }

    private Stream<String> getCodeBlocks() throws IOException {
        Stream<String> docs = loadFiles(docPath, ".md").stream().map(f -> f.content);

        Stream<String> codeBlocks = docs.flatMap(codeBlockRegex::results)
                .map(MatchResult::group);
        //.ifEmpty { throw IllegalStateException("No code blocks found in documentation file [$docPath]. Doesn't sound right! Go check.") }

        return codeBlocks;
    }

    private Stream<String> getSnippets() throws IOException {
        Stream<String> snippets = loadFiles(snippetPath, ".snippet").stream().map(f -> f.content);
        return extractInvalidCode(snippets.collect(Collectors.toList()), "code-snippet");
    }

    private String getModel() throws IOException {
        String s = loadFiles(modelPath, ".rosetta").stream().map(f -> f.toString()).collect(Collectors.joining());
        s = illegalSyntaxRegex.matcher(s).replaceAll("");
        s = s.replaceAll(whitespaceRegex, "");
        return s;
    }

    private Collection<MyFile> loadFiles(String dir, String ext) throws IOException {
        BiPredicate<Path, BasicFileAttributes> docFileFilter = (path, attr) -> attr.isRegularFile() && path.getFileName().toString().endsWith(ext);
        try (Stream<Path> paths = Files.find(Paths.get(dir), 1, docFileFilter)) {
            return paths.map(path -> new MyFile(path, readString(path))).collect(Collectors.toList());
        }
    }

    private Stream<String> extractInvalidCode(List<String> code, String type) {
        return code.stream().filter(s -> illegalSyntaxRegex.matcher(s).matches());
    }

    private String readString(Path p) {
        try {
            return new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void fixUp() throws IOException {
        Collection<MyFile> files = new ArrayList<>();
        files.addAll(loadFiles(docPath, ".rst"));
        files.addAll(loadFiles(snippetPath, ".snippet"));
        files.forEach(file -> {
            String sanitised = illegalSyntaxRegex.matcher(file.content).replaceAll("");
            LOGGER.info("Sanitising [{}]", file);
            try {
                Files.write(file.path, sanitised.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    private static class MyFile {

        private Path path;
        private String content;

        public MyFile(Path path, String content) {
            this.path = path;
            this.content = content;
        }

        @Override
        public String toString() {
            return "MyFile(path=" + path + ", content=" + content + ")";
        }

    }

    public static void main(String[] args) throws ParseException, IOException {
        Options options = new Options()
                .addOption("d", "doc-path", true, "Relative path to the document.")
                .addOption("s", "snippet-path", true, "Relative path to code snippet files.")
                .addOption("m", "model-path", true, "Relative path to the model files directory.")
                .addOption("u", "fix-up", false, "Remove illegal code syntax from document file in-place.");

        CommandLine cmd = new DefaultParser().parse(options, args);

        String docPath = cmd.getOptionValue("doc-path", "docs");
        String snippetPath = cmd.getOptionValue("snippet-path", "docs/code-snippets");
        String modelPath = cmd.getOptionValue("model-path", "rosetta-source/src/main/rosetta");
        boolean fixUp = cmd.hasOption("fix-up");

        DocumentationCodeValidator validator = new DocumentationCodeValidator(docPath, snippetPath, modelPath);

        if (fixUp) {
            LOGGER.warn("Running in fix-up mode, this will sanitise documentation .rst and .snippets files in-place.");
            validator.fixUp();
        }

        validator.validate();
    }
}
