package com.example;

import com.example.command.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CodeupRequestApplication {
    static {
        System.setProperty("logging.level.root", "WARN");
        System.setProperty("logging.level", "WARN");
    }

    public static void main(String[] args) throws Exception {
        if (args == null || args.length == 0) {
            log.error("args is null");
            return;
        }
        Options options = new Options();
        options.addOption("h", false, "print this message");

        List<Command> commands = new ArrayList<>();
        commands.add(new BranchCommand());
        commands.add(new RepositoryCommand());
        commands.add(new AllRepositoryCommand());
        commands.add(new MergeRequestCommand());

        for (Command command : commands) {
            options.addOption(command.option(), false, command.helpMessage());
        }

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(180, "java -jar aliyun-codeup-api-tools-0.0.1-SNAPSHOT.jar", "Options:", options, "", true);
            return;
        }

        for (Command command : commands) {
            if (cmd.hasOption(command.option())) {
                command.execute(args);
                return;
            }
        }
    }
}
