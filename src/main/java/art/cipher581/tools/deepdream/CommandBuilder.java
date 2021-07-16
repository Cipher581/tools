package art.cipher581.tools.deepdream;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommandBuilder {

	public static List<String> build(Map<Pattern, Supplier<String>> argReplacers, String[] commandTemplate) {
    	List<String> arguments = Arrays.stream(commandTemplate).map(arg -> {
            if (arg == null) {
                return "";
            }

            // System.out.println("arg: " + arg);
            for (Pattern argPattern : argReplacers.keySet()) {
                String value = argReplacers.get(argPattern).get();
                System.out.println("\tvalue: " + value);

                if (value == null) {
                    value = "";
                }

                // System.out.println("\tpattern: " + argPattern.pattern());
                arg = argPattern.matcher(arg).replaceAll(value);
                // System.out.println("\targ: " + arg);
            }

            return arg;
        }).collect(Collectors.toList());

        System.out.println("Arguments: " + arguments.stream().collect(Collectors.joining(", ")));

        return arguments;
    }

}
