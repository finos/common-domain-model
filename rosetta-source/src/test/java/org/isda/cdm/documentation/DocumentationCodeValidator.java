package org.isda.cdm.documentation;

import com.google.common.collect.Streams;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
	
    private final String synonymRegex = "\\[synonym [^\\]]*\\]";
    private final String definitionRegex = "<\"[\\s\\S]*?\">";
    private final String lineCommentRegex = "[^:]\\/\\/.*$";
    private final String whitespaceRegex = "\\s+";
    private final Pattern illegalSyntaxRegex = Pattern.compile(synonymRegex+"|"+definitionRegex+"|"+lineCommentRegex, Pattern.MULTILINE);
	private static final PatternStreamer codeBlockRegex = new PatternStreamer("(\\.\\. code-block:: .*\\s+$)((\\n[ \\t]+.*|\\s)+)");

	private String modelPath;
	private String docPath;
	private String snippetPath;


	public DocumentationCodeValidator(String docPath, String snippetPath, String modelPath) {
		this.docPath = docPath;
		this.snippetPath = snippetPath;
		this.modelPath = modelPath;
		// TODO Auto-generated constructor stub
	}
	
	void validate() throws IOException {
		String model = getModel();

        List<String> codeBlocks = getCodeBlocks().collect(Collectors.toList());
        Stream<String> illegalCodeBlocks = extractInvalidCode(codeBlocks, "code-block")
                .peek(System.out::println);

        List<String> snippets = getSnippets().collect(Collectors.toList());
        Stream<String> illegalSnippets = extractInvalidCode(snippets, "code-snippets")
                .peek(System.out::println);

        if (Streams.concat(illegalCodeBlocks,illegalSnippets).count() > 0) {
            System.err.println("ERROR found illegal syntax in code block/snippet. Run this script with --fix-up flag to sanitise.");
            System.exit(1);
        }

        long invalidCodeBlocks = validate(codeBlocks, model);
        long invalidSnippets = validate(snippets, model);

        if (invalidCodeBlocks > 0) {
            System.err.println("Found "+invalidCodeBlocks+" code-blocks that don't match model text.");
        }
        if (invalidSnippets > 0) {
            System.err.println("Found ["+invalidSnippets+"] code-snippets that don't match model text.");
        }
        if (invalidCodeBlocks + invalidSnippets != 0) {
        	System.exit(1);
        }
	}
	
	long validate(List<String> code, String model) {
		 Stream<String> invalidCode = code.stream()
					 .filter ( _code -> !_code.contains(".. code-block:: sourcecode") )
					 .filter ( _code -> !_code.contains(".. code-block:: MD") )
					 .filter ( _code -> !_code.contains(".. code-block:: Javascript") )
					 .filter ( _code -> !_code.contains(".. code-block:: Java") )
				 .filter ( _code -> !_code.contains(".. code-block:: JSON") )
				 .filter ( _code -> {
	                    String cleaned = _code
	                            .replaceAll(".*\\.\\. code-block.*", "")
	                            .replaceAll(whitespaceRegex, "");
	                    return !model.contains(cleaned);
	                })
	                .peek(System.out::println);

	        return invalidCode.count();
	}

	private Stream<String> getCodeBlocks() throws IOException {
        Stream<String> docs = loadFiles(docPath, ".rst").stream().map(f->f.content);

		Stream<String> codeBlocks = docs.flatMap (codeBlockRegex::results)
                .map(MatchResult::group);
                //.ifEmpty { throw IllegalStateException("No code blocks found in documentation file [$docPath]. Doesn't sound right! Go check.") }

        return codeBlocks;
    }
	
	private Stream<String> getSnippets() throws IOException {
        Stream<String> snippets = loadFiles(snippetPath, ".snippet").stream().map(f->f.content);
        return extractInvalidCode(snippets.collect(Collectors.toList()), "code-snippet");
    }
	
	private String getModel() throws IOException {
        String s = loadFiles(modelPath, ".rosetta").stream().map(f->f.toString()).collect(Collectors.joining());
		s = illegalSyntaxRegex.matcher(s).replaceAll("");
		s = s.replaceAll(whitespaceRegex, "");
       return s;
    }
	
	private Collection<MyFile> loadFiles(String dir, String ext) throws IOException  {
		BiPredicate<Path, BasicFileAttributes> docFileFilter = (path, attr) -> attr.isRegularFile() && path.getFileName().toString().endsWith(ext);
		try (Stream<Path> paths = Files.find(Paths.get(dir), 1, docFileFilter)) {
			return paths.map(path -> new MyFile(path, readString(path))).collect(Collectors.toList());
		}
    }
	
	private Stream<String> extractInvalidCode(List<String> code, String type) {
        return code.stream().filter(s->illegalSyntaxRegex.matcher(s).matches());
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
        files.forEach( file -> {
            String sanitised = illegalSyntaxRegex.matcher(file.content).replaceAll(""); 
            System.out.println("Sanitising [${file.path}]");
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
		 
		 String docPath = cmd.getOptionValue("doc-path","documentation/source");
		 String snippetPath = cmd.getOptionValue("snippet-path","documentation/source/code-snippets");
		 String modelPath = cmd.getOptionValue("model-path","rosetta-source/src/main/rosetta");
		 boolean fixUp = cmd.hasOption("fix-up");
		 
		 DocumentationCodeValidator validator = new DocumentationCodeValidator(docPath, snippetPath, modelPath);

	    if (fixUp) {
	        System.out.println("WARNING Running in fix-up mode, this will sanitise documentation .rst and .snippets files in-place.");
	        validator.fixUp();
	    }

		 validator.validate();
	}

}
