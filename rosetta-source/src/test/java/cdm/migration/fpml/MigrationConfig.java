package cdm.migration.fpml;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class MigrationConfig {
    public final Path projectRoot;
    public final Path oldFpmlJar;
    public final Path newFpmlJar;
    public final String oldFpmlRootInJar;
    public final String newFpmlRootInJar;
    public final Path cdmRosettaDir;
    public final String fileMask;
    public final List<String> entryFunctions;
    public final Confidence confidence;
    public final FidelityMode fidelityMode;
    public final boolean dryRun;
    public final boolean apply;
    public final Path outputDir;

    public MigrationConfig(
            Path projectRoot,
            Path oldFpmlJar,
            Path newFpmlJar,
            String oldFpmlRootInJar,
            String newFpmlRootInJar,
            Path cdmRosettaDir,
            String fileMask,
            List<String> entryFunctions,
            Confidence confidence,
            FidelityMode fidelityMode,
            boolean dryRun,
            boolean apply,
            Path outputDir) {
        this.projectRoot = projectRoot;
        this.oldFpmlJar = oldFpmlJar;
        this.newFpmlJar = newFpmlJar;
        this.oldFpmlRootInJar = oldFpmlRootInJar;
        this.newFpmlRootInJar = newFpmlRootInJar;
        this.cdmRosettaDir = cdmRosettaDir;
        this.fileMask = fileMask;
        this.entryFunctions = entryFunctions;
        this.confidence = confidence;
        this.fidelityMode = fidelityMode;
        this.dryRun = dryRun;
        this.apply = apply;
        this.outputDir = outputDir;
    }

    public static MigrationConfig parse(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder().longOpt("project-root").hasArg().build());
        options.addOption(Option.builder().longOpt("old-fpml-jar").hasArg().build());
        options.addOption(Option.builder().longOpt("new-fpml-jar").hasArg().build());
        options.addOption(Option.builder().longOpt("old-fpml-root-in-jar").hasArg().build());
        options.addOption(Option.builder().longOpt("new-fpml-root-in-jar").hasArg().build());
        options.addOption(Option.builder().longOpt("cdm-rosetta-dir").hasArg().build());
        options.addOption(Option.builder().longOpt("file-mask").hasArg().build());
        options.addOption(Option.builder().longOpt("entry-function").hasArg().build());
        options.addOption(Option.builder().longOpt("confidence").hasArg().build());
        options.addOption(Option.builder().longOpt("fidelity").hasArg().build());
        options.addOption(Option.builder().longOpt("dry-run").build());
        options.addOption(Option.builder().longOpt("apply").build());
        options.addOption(Option.builder().longOpt("output-dir").hasArg().build());
        options.addOption(Option.builder("h").longOpt("help").build());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        if (cmd.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("FpmlFlatteningMigrationTool", options);
            System.exit(0);
        }

        Path projectRoot = Paths.get(cmd.getOptionValue("project-root", ".")).toAbsolutePath().normalize();
        String home = System.getProperty("user.home");
        Path defaultOldJar = Paths.get(home, ".m2", "repository", "com", "regnosys", "rune-fpml", "rosetta-source", "1.5.3",
                "rosetta-source-1.5.3.jar");
        Path defaultNewJar = Paths.get(home, ".m2", "repository", "com", "regnosys", "rune-fpml", "rosetta-source",
                "0.0.0.master-SNAPSHOT", "rosetta-source-0.0.0.master-SNAPSHOT.jar");
        Path oldFpmlJar = Paths.get(cmd.getOptionValue("old-fpml-jar", defaultOldJar.toString()));
        Path newFpmlJar = Paths.get(cmd.getOptionValue("new-fpml-jar", defaultNewJar.toString()));
        String oldFpmlRootInJar = cmd.getOptionValue("old-fpml-root-in-jar", "fpml/rosetta");
        String newFpmlRootInJar = cmd.getOptionValue("new-fpml-root-in-jar", "fpml/rosetta");
        Path cdmRosettaDir = projectRoot.resolve(cmd.getOptionValue("cdm-rosetta-dir", "rosetta-source/src/main/rosetta"));
        String fileMask = cmd.getOptionValue("file-mask", "ingest-*.rosetta");

        String[] entries = cmd.getOptionValues("entry-function");
        List<String> entryFunctions;
        if (entries != null && entries.length > 0) {
            entryFunctions = Arrays.asList(entries);
        } else {
            entryFunctions = new ArrayList<String>();
            entryFunctions.add("Ingest_FpmlConfirmationToTradeState");
            entryFunctions.add("Ingest_FpmlConfirmationToWorkflowStep");
        }

        Confidence confidence = Confidence.valueOf(cmd.getOptionValue("confidence", "MEDIUM").toUpperCase());
        FidelityMode fidelityMode = FidelityMode.valueOf(cmd.getOptionValue("fidelity", "MAXIMUM").toUpperCase());
        boolean apply = cmd.hasOption("apply");
        boolean dryRun = cmd.hasOption("dry-run") || !apply;
        Path outputDir = projectRoot.resolve(cmd.getOptionValue("output-dir", "target/fpml-migration"));

        if (confidence == Confidence.LOW) {
            System.err.println("Warning: LOW confidence mode applies low-confidence rewrites.");
        }

        return new MigrationConfig(
                projectRoot,
                oldFpmlJar,
                newFpmlJar,
                oldFpmlRootInJar,
                newFpmlRootInJar,
                cdmRosettaDir,
                fileMask,
                entryFunctions,
                confidence,
                fidelityMode,
                dryRun,
                apply,
                outputDir);
    }
}
