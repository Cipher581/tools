package art.cipher581.tools.deepdream;


import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Call deep-dream by cmd
 */
public class DeepDreamCommand implements IDeepDream {

    public enum ExistingFileHandling {

        throwException, overwrite, skip

    }

    private static final Pattern ARG_VAR_FILE = Pattern.compile("(?i)%file%");

    private static final Pattern ARG_VAR_TARGET = Pattern.compile("(?i)%target%");

    private static final Pattern ARG_VAR_NETWORK = Pattern.compile("(?i)%network%");

    private static final Pattern ARG_VAR_BLEND = Pattern.compile("(?i)%blend%");

    private static final Pattern ARG_VAR_RESCALE_FACTOR = Pattern.compile("(?i)%rescaleFactor%");

    private static final Pattern ARG_VAR_ITERATIONS = Pattern.compile("(?i)%iterations%");

    private static final Pattern ARG_VAR_STEP_SIZE = Pattern.compile("(?i)%stepSize%");

    private static final Pattern ARG_VAR_TILE_SIZE = Pattern.compile("(?i)%tileSize%");

    private static final Pattern ARG_VAR_REPEATS = Pattern.compile("(?i)%repeats%");

    private static final Pattern ARG_VAR_LAYER = Pattern.compile("(?i)%layer%");

    private static final Pattern ARG_VAR_CHANNELS = Pattern.compile("(?i)%channels%");

    private String[] commandTemplate;

    /**
     * Optional working dir
     */
    public File workingDir;

    private ExistingFileHandling existingFileHandling = ExistingFileHandling.skip;


    public DeepDreamCommand(String[] commandTemplate) {
        super();

        if (commandTemplate == null) {
            throw new IllegalArgumentException("commandTemplate is null");
        }

        this.commandTemplate = commandTemplate;
    }


    @Override
    public void run(File file, File targetFile, DeepDreamSettings settings) throws DeepDreamException {
        if (!file.isFile()) {
            throw new DeepDreamException(file + " does not exist or is not a file");
        }

        if (targetFile.isFile()) {
            switch (existingFileHandling) {
                case skip:
                    System.out.println("skipping existing file " + targetFile);
                    return;
                case throwException:
                    throw new DeepDreamException(targetFile + " already exists");
                default:
                    break;
            }
        }

        System.out.println("creating " + targetFile);

        List<String> cmd = buildCommand(file, targetFile, settings);

        Process p = startProcess(cmd);

        waitForProcess(p);

        if (!targetFile.isFile()) {
            throw new DeepDreamException("file " + targetFile + " has not been created");
        }
    }


    private void waitForProcess(Process p) throws DeepDreamException {
        try {
            System.out.println("waiting for deep-dream to finish");

            int exitCode = p.waitFor();

            System.out.println("deep-dream terminated with code " + exitCode);

            if (exitCode != 0) {
                throw new DeepDreamException("deep-dream terminated with code " + exitCode);
            }
        } catch (InterruptedException e) {
            throw new DeepDreamException("deep-dream has been terminated abnormally", e);
        }
    }


    private Process startProcess(List<String> cmd) throws DeepDreamException {
        Process p;
        try {
            ProcessBuilder builder = new ProcessBuilder(cmd);

            if (workingDir != null) {
                builder.directory(workingDir);
            }

            builder.redirectErrorStream(true);
            builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            System.out.println("calling deep-dream");

            p = builder.start();
        } catch (IOException e) {
            throw new DeepDreamException("deep-dream could not be started", e);
        }

        return p;
    }


    private List<String> buildCommand(File file, File targetFile, DeepDreamSettings settings) {
        Map<Pattern, Supplier<String>> argReplacers = buildArgReplacers(file, targetFile, settings);

        return CommandBuilder.build(argReplacers, commandTemplate);
    }
    

    private Map<Pattern, Supplier<String>> buildArgReplacers(File file, File targetFile, DeepDreamSettings settings) {
        Map<Pattern, Supplier<String>> argReplacers = new HashMap<>();

        DecimalFormat floatFormat = new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.US));
        DecimalFormat intFormat = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.US));

        argReplacers.put(ARG_VAR_FILE, () -> getPath(file));
        argReplacers.put(ARG_VAR_TARGET, () -> getPath(targetFile));
        argReplacers.put(ARG_VAR_NETWORK, () -> settings.getNetwork());
        argReplacers.put(ARG_VAR_BLEND, () -> floatFormat.format(settings.getBlend()));
        argReplacers.put(ARG_VAR_RESCALE_FACTOR, () -> floatFormat.format(settings.getRescaleFactor()));
        argReplacers.put(ARG_VAR_ITERATIONS, () -> intFormat.format(settings.getIterations()));
        argReplacers.put(ARG_VAR_STEP_SIZE, () -> intFormat.format(settings.getStepSize()));
        argReplacers.put(ARG_VAR_TILE_SIZE, () -> intFormat.format(settings.getTileSize()));
        argReplacers.put(ARG_VAR_REPEATS, () -> intFormat.format(settings.getRepeats()));
        argReplacers.put(ARG_VAR_LAYER, () -> intFormat.format(settings.getLayer()));
        argReplacers.put(ARG_VAR_CHANNELS, () -> settings.getChannel() == null ? null : Arrays.stream(settings.getChannel().asArray()).mapToObj(v -> String.valueOf(v)).collect(Collectors.joining(",")));

        return argReplacers;
    }
    
    private static String getPath(File file) {
        String path = file.getAbsolutePath();
        
        path = path.replaceAll("\\\\", "/");
        
        System.out.println(path);
        return path;
    }


    public DeepDreamCommand withWorkingDir(File workingDir) {
        this.workingDir = workingDir;

        return this;
    }


    public DeepDreamCommand withExistingFileHandling(ExistingFileHandling existingFileHandling) {
        this.existingFileHandling = existingFileHandling;

        return this;
    }

}
