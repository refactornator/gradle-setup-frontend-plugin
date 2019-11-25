package cool.william.frontend;

import com.github.psxpaul.task.ExecFork;
import com.moowork.gradle.node.npm.NpmTask;
import org.gradle.api.DefaultTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class SetupFrontendPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPluginManager()
                .apply("com.moowork.node");

        SetupFrontendTask setupFrontendTask = project.getTasks()
                .create("setupFrontend", SetupFrontendTask.class);

        Optional<Task> npmInstall = project.getTasksByName("npmInstall", true)
                .stream()
                .findFirst();
        npmInstall.ifPresent(setupFrontendTask::finalizedBy);

        project.getPluginManager()
                .apply("com.github.psxpaul.execfork");

        ExecFork frontendStart = project.getTasks()
                .create("frontendStart", ExecFork.class);
        frontendStart.setExecutable("npm");
        frontendStart.setArgs(Arrays.asList(new String[]{"run-script", "start"}));
        frontendStart.setWaitForOutput("ms");
        npmInstall.ifPresent(frontendStart::dependsOn);

        Optional<Task> bootRun = project.getTasksByName("bootRun", true)
                .stream()
                .findFirst();
        bootRun.ifPresent(frontendStart::setStopAfter);

        NpmTask frontendBuild = project.getTasks()
                .create("frontendBuild", NpmTask.class);
        frontendBuild.setArgs(Arrays.asList("run-script", "build"));
        npmInstall.ifPresent(frontendBuild::dependsOn);
    }
}
