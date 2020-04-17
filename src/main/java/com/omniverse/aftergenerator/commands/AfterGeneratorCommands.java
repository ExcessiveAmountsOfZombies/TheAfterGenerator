package com.omniverse.aftergenerator.commands;

import com.omniverse.aftergenerator.AfterGenerator;
import com.omniverse.aftergenerator.generation.StructureRunnable;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

    @AfterGenCommand(name = "generate", args = {"Type", "RandomNumber"})
    public boolean generate(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        boolean generated = generator.getManipulator().spawnStructureInWorld(player.getWorld(), player.getLocation(),
                args[1], Integer.parseInt(args[2]));
        if (generated) {
            player.sendMessage("Structure generated.");
        } else {
            player.sendMessage("Structure may not have generated completely.");
        }


        return generated;
    }

    @AfterGenCommand(name = "process", args = {"minBlockX", "minBlockZ", "maxBlockX", "maxBlockZ"})
    public boolean process(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        int minX = Integer.parseInt(args[1]);
        int minZ = Integer.parseInt(args[2]);
        int maxX = Integer.parseInt(args[3]);
        int maxZ = Integer.parseInt(args[4]);
        StructureRunnable runnable = new StructureRunnable(generator, generator.getManipulator(), player.getWorld(), minX, minZ, maxX, maxZ);
        BukkitTask taskID = Bukkit.getScheduler().runTaskTimer(generator, runnable, 0L, 4L);
        runnable.setTaskID(taskID);
        return true;
    }

    // todo make this write out command list
    @AfterGenCommand(name = "help")
    public boolean help(CommandSender sender, String[] args) {
        sender.sendMessage("no command entered");
        Player player = (Player) sender;
        player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().set(new NamespacedKey(generator, "steve"), PersistentDataType.STRING, "mygod");
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
            int argLength = args.length - 2;

            String[] argumentList = getCommandArgs(args[0].toLowerCase());


            if (argLength < argumentList.length) {
                String oneTypedArgument = getCommandArgs(args[0].toLowerCase())[argLength];

                commandArgs.removeIf(oneCommandArgument -> !oneTypedArgument.equalsIgnoreCase(oneCommandArgument));

                StringUtil.copyPartialMatches(oneTypedArgument, commandArgs, completedList);
            }
        }
        return completedList;
    }

    private String[] getCommandArgs(String command) {
        return commandHashMap.get(command).args();
    }
}
