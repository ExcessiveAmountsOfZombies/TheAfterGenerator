package me.thonk.aftergenerator.commands;

import me.thonk.aftergenerator.AfterGenerator;
import me.thonk.aftergenerator.generation.StructureRunnable;
import me.thonk.aftergenerator.objects.StructuresEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AfterGeneratorCommands implements CommandExecutor, TabCompleter {

    private HashMap<String, AfterGenCommand> commandHashMap = new HashMap<>();
    private HashMap<String, Method> commandList = new HashMap<>();

    private AfterGenerator generator;

    public AfterGeneratorCommands(AfterGenerator generator) {
        this.generator = generator;
        for (Method method : this.getClass().getMethods()) {
            if (method.isAnnotationPresent(AfterGenCommand.class)) {
                AfterGenCommand annotation = method.getAnnotation(AfterGenCommand.class);
                commandHashMap.put(annotation.name().toLowerCase(), annotation);
                commandList.put(annotation.name().toLowerCase(), method);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        String commandEntered = args[0].toLowerCase();

        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Commands can only be entered in game.");
            return false;
        }

        if (args.length > 0) {
            try {
                Method method = commandList.getOrDefault(commandEntered, getClass().getMethod("help", CommandSender.class, String[].class));
                if (method.isAnnotationPresent(AfterGenCommand.class)) {
                    return (Boolean) method.invoke(this, commandSender, args);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @AfterGenCommand(name = "generate", args = {"[Type]"})
    public boolean generate(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        try {
            boolean generated = generator.getManipulator().spawnStructureInWorld(player.getWorld(), player.getLocation(),
                    args[1]);

            if (generated) {
                player.sendMessage("Structure generated.");
            } else {
                player.sendMessage("Structure may not have generated completely.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            player.sendMessage(ChatColor.RED + "The command was not entered properly.");
            String commandArgs = "";
            for (String singleArg : commandHashMap.get("generate").args() ) {
                commandArgs += singleArg + " ";
            }
            player.sendMessage(ChatColor.RED + "/ag generate " + commandArgs);
        }

        return true;
    }

    @AfterGenCommand(name = "process", args = {"minBlockX", "minBlockZ", "maxBlockX", "maxBlockZ"})
    public boolean process(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        try {
            int minX = Integer.parseInt(args[1]);
            int minZ = Integer.parseInt(args[2]);
            int maxX = Integer.parseInt(args[3]);
            int maxZ = Integer.parseInt(args[4]);
            StructureRunnable runnable = new StructureRunnable(generator, generator.getManipulator(), player.getWorld(), minX, minZ, maxX, maxZ);
            BukkitTask taskID = Bukkit.getScheduler().runTaskTimer(generator, runnable, 0L, 4L);
            runnable.setTaskID(taskID);
        } catch (ArrayIndexOutOfBoundsException e) {
            player.sendMessage(ChatColor.RED + "The command was not entered properly.");
            String commandArgs = "";
            for (String singleArg : commandHashMap.get("process").args() ) {
                commandArgs += singleArg + " ";
            }
            player.sendMessage(ChatColor.RED + "/ag process " + commandArgs);
        }

        return true;
    }

    // todo make this write out command list
    @AfterGenCommand(name = "help")
    public boolean help(CommandSender sender, String[] args) {
        sender.sendMessage("Command:");
        for (Map.Entry<String, AfterGenCommand> entry : commandHashMap.entrySet()) {
            String arguments = "";
            for (String singleArg : entry.getValue().args()) {
                arguments += singleArg + " ";
            }

            sender.sendMessage(ChatColor.GRAY + "/" + entry.getKey() + " " + arguments);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> completedList = new ArrayList<>();

        if (args.length == 1) {
            String commandPiece = args[0];
            StringUtil.copyPartialMatches(commandPiece, commandList.keySet(), completedList);

        }
        if (args.length > 1) {
            ArrayList<String> commandArgs = new ArrayList<>(Arrays.asList(getCommandArgs(args[0])));


            // argLength is the actual arguments of a command.
            // it subtracts the /<command> and the command being used.
            // /aftergen (1) generate (2) <real args> (x - 2)
            // this is for the args in the annotation
            int argLength = args.length - 2;

            String[] argumentList = getCommandArgs(args[0].toLowerCase());


            if (argLength < argumentList.length) {
                String oneTypedArgument = getCommandArgs(args[0].toLowerCase())[argLength];



                commandArgs.removeIf(oneCommandArgument -> !oneTypedArgument.equalsIgnoreCase(oneCommandArgument));

                switch (oneTypedArgument) {
                    case "[Type]":
                        for (StructuresEnum type : StructuresEnum.values()) {
                            commandArgs.add(type.name());
                        }
                }

                StringUtil.copyPartialMatches(args[argLength + 1], commandArgs, completedList);
            }
        }
        return completedList;
    }

    private String[] getCommandArgs(String command) {
        return commandHashMap.get(command).args();
    }
}
