package cool.william.frontend;

import org.apache.commons.io.FileUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.gradle.api.tasks.options.OptionValues;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.*;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;

class SetupFrontendTask extends DefaultTask {
    private enum FrontendType {
        react, svelte
    }

    private Logger logger = getProject().getLogger();
    private String projectDirectory = System.getProperty("user.dir");
    private FrontendType frontendType;

    @Option(option = "frontend-type", description = "Configures the frontend type.")
    public void setFrontendType(FrontendType frontendType) {
        this.frontendType = frontendType;
    }

    @OptionValues("frontend-type")
    public List<FrontendType> getAvailableFrontendTypes() {
        return new ArrayList<FrontendType>(Arrays.asList(FrontendType.values()));
    }

    @Input
    public FrontendType getFrontendType() {
        return frontendType;
    }

    public SetupFrontendTask() {}

    @TaskAction
    void setupFrontend() {
        if(frontendType == FrontendType.react) {
            logger.info("Setting up a React frontend. https://reactjs.org/");
        } else if(frontendType == FrontendType.svelte) {
            logger.info("Setting up a Svelte frontend. https://svelte.dev/");
        }

        String rootName = frontendType.toString().toLowerCase();

        try {
            URI rootUri = Objects.requireNonNull(getClass().getClassLoader().getResource(rootName)).toURI();
            Path rootPath;
            if (rootUri.getScheme().equals("jar")) {
                FileSystem fileSystem;
                try {
                    fileSystem = FileSystems.getFileSystem(rootUri);
                } catch (FileSystemNotFoundException e) {
                    fileSystem = FileSystems.newFileSystem(rootUri, Collections.<String, Object>emptyMap());
                }
                rootPath = fileSystem.getPath(rootName).toAbsolutePath();
            } else {
                rootPath = Paths.get(rootUri);
            }
            Files.walk(rootPath)
                    .skip(1)
                    .forEach(path -> {
                        String destination = path.subpath(1, path.getNameCount()).toString();
                        if (Files.isDirectory(path)) {
                            makeDirectory(destination);
                        } else {
                            InputStream resourceAsStream = getClass().getResourceAsStream(path.toString());
                            Path currentDirectory = Paths.get(projectDirectory, destination).toAbsolutePath().normalize();
                            copyFromStream(resourceAsStream, currentDirectory);
                        }
                    });

            appendToGitIgnore();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void appendToGitIgnore() {
        Path gitIgnore = Paths.get(projectDirectory, ".gitignore");

        if(!Files.exists(gitIgnore)) {
            try {
                Files.createFile(gitIgnore);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            String gitIgnoreContent = FileUtils.readFileToString(gitIgnore.toFile(), UTF_8);

            if(!gitIgnoreContent.endsWith("\n")) {
                Files.write(gitIgnore, "\n".getBytes(), APPEND);
            }

            if(!gitIgnoreContent.contains("### Frontend Dependencies ###")) {
                String dependencies =
                        "\n### Frontend Dependencies ###\n" +
                        "node_modules/";
                logger.info("Writing Frontend Dependencies to .gitignore");
                Files.write(gitIgnore, dependencies.getBytes(), APPEND);
            }

            if(!gitIgnoreContent.contains("### Frontend Bundle ###")) {
                String bundle =
                        "\n\n### Frontend Bundle ###\n" +
                        "src/main/resources/static/*\n" +
                        "src/main/resources/templates/*\n";
                logger.info("Writing Frontend Bundle to .gitignore");
                Files.write(gitIgnore, bundle.getBytes(), APPEND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyFromStream(InputStream source, Path destination) {
        try {
            Files.copy(source, destination);
        } catch (FileAlreadyExistsException e) {
            logger.warn("File already exists at " + destination.toString() + " - skipping");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void makeDirectory(String directory) {
        Path path = Paths.get(directory);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
