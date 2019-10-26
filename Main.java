import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Character.isLetter;

public class Main {

    private static String output = "";
    private static String[] types;
    private static ArrayList<String> names = new ArrayList<>(5);
    private static String structDec = "struct dynarc_TYPE {\n\tTYPE* array;\n\tint len;\n\tint size;\n};\n";
    private static String arrayDec = "NAME.array = (TYPE *)malloc(50 * sizeof(TYPE));\nNAME.len = 0;\nNAME.size = 50;\n";
    private static String initStatement = "NAME[iterator] = VALUE;\n";
    private static String _1ArgFunction = "FUNCTION_CALL(ARG1);\n";
    private static String _2ArgFunction = "FUNCTION_CALL(ARG1, ARG2);\n";
    private static String _3ArgFunction = "FUNCTION_CALL(ARG1, ARG2, ARG3);\n";
    private static String _4ArgFunction = "FUNCTION_CALL(ARG1, ARG2, ARG3, ARG4);\n";

    public static boolean isDeclaredType(String type) {
        for(int i = 0; i < types.length; i++) {
            if(type.equals(types[i])) return true;
        }
        return false;
    }

    public static boolean isDeclaredDynarc(String name) {
        for(int i = 0; i < names.size(); i++) {
            if(name.equals(names.get(i))) return true;
        }
        return false;
    }

    public static void writeOutput(String stringToWrite, String end) {
        output = output + stringToWrite + end;
    }

    public static String updateDeclaration(String line) {
        String out = "";
        int indexUse = line.indexOf("use");
        int indexBopen = line.indexOf('[', indexUse) + 1;
        int indexBclose = line.indexOf(']', indexBopen);
        types = line.substring(indexBopen, indexBclose).split(",");
        for(int i = 0; i < types.length; i++) {
            types[i] = types[i].trim();
            out = out + structDec.replaceAll("TYPE", types[i]);
        }
        return out;
    }

    public static String arrayDeclaration(String line) {
        String out;
        String arrayType = "";
        String arrayName = "";
        int i;
        int indexUscore = line.indexOf("dynarc_") + "dynarc_".length();
        for(i = indexUscore; isLetter(line.charAt(i)); i++) {
            arrayType = arrayType + line.charAt(i);
            indexUscore = indexUscore + 1;
        }
        if(!isDeclaredType(arrayType)) {
            System.err.println("Type: " + arrayType + " was not declared. Please fix !");
            System.exit(1);
        }
        out = "struct " + line + "\n";
        String temporaryString = line.substring(indexUscore + 1).trim();
        arrayName = temporaryString.substring(0, temporaryString.indexOf(";")).trim();
        names.add(arrayName);
        out = out + arrayDec.replaceAll("TYPE", arrayType).replaceAll("NAME", arrayName);
        System.out.println(names.get(0));

        return out;
    }

    public static String arrayInitialization(String line) {
        String out = "";
        String name = line.substring(0, line.indexOf("=")).trim();
        if(!isDeclaredDynarc(name)) {
            System.err.println("Oops. Looks like [" + name + "] was not declared !");
            System.exit(1);
        }
        String[] values = line.substring(line.indexOf("{") + 1, line.indexOf("}")).split(",");
        for(int i = 0; i < values.length; i++) {
            out = out + initStatement;
            out = out.replace("NAME", name);
            out = out.replace("iterator", "" + i);
            out = out.replace("VALUE", values[i]);
        }
        out = out + name + ".len = " + values.length + ";\n";
        return out;
    }

    public static String setSingleValue(String line) {
        String out;
        String name = line.substring(0, line.indexOf("[")).trim();
        if(!isDeclaredDynarc(name)) {
            System.err.println("Oops. Looks like [" + name + "] was not declared !");
            System.exit(1);
        }
        String value = line.substring(line.indexOf("=") + 1, line.indexOf(";")).trim();
        String index = line.substring(line.indexOf("[") + 1, line.indexOf("]")).trim();
        out = initStatement;
        out = out.replace("NAME", name).replace("iterator", index).replace("VALUE", value);
        out = out + name + ".len = " + name + ".len <= " + index + " ? " + index + " + 1 : " + name + ".len;\n";
        return out;
    }

    public static String _1ArgFunCall(String line, String action) {
        String name = line.substring(0, line.indexOf(".")).trim();
        return _1ArgFunction.replace("ARG1", name).replace("FUNCTION_CALL", action);
    }

    public static String _2ArgFunCall(String line, String action) {
        String name = line.substring(0, line.indexOf(".")).trim();
        String index = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
        return _2ArgFunction.replace("ARG1", name).replace("ARG2", index).replace("FUNCTION_CALL", action);
    }

    public static String _3ArgFunCall(String line, String action) {
        String name = line.substring(0, line.indexOf(".")).trim();
        String element = line.substring(line.indexOf("(") + 1, line.indexOf(",")).trim();
        String index = line.substring(line.indexOf(",") + 1, line.indexOf(")")).trim();
        return _3ArgFunction.replace("ARG1", name).replace("ARG2", element).replace("ARG3", index).replace("FUNCTION_CALL", action);
    }

    public static void main(String[] args) {
        String line;
        String filename = "D:\\Data\\CODE\\JavaCode\\Dynarc\\src\\code.c";
        Scanner sc = null;
        try {
            sc = new Scanner(new File(filename));

            while(sc.hasNext()) {
                line = sc.nextLine();
                line = line.trim();

                if(line.isEmpty()) {
                    writeOutput("", "\n");
                    continue;
                }

                if (line.contains("use"))
                    writeOutput(updateDeclaration(line), "");
                else if (line.contains("dynarc_"))
                    writeOutput(arrayDeclaration(line), "");
                else if (line.contains(".pop"))
                    writeOutput(_1ArgFunCall(line, "pop"), "");
                else if (line.contains(".push"))
                    writeOutput(_2ArgFunCall(line, "push"), "");
                else if (line.contains(".removeAll"))
                    writeOutput(_3ArgFunCall(line, "deleteAll"), "");
                else if (line.contains(".insert"))
                    writeOutput(_3ArgFunCall(line, "insert"), "");
                else if (line.contains(".remove"))
                    writeOutput(_2ArgFunCall(line, "remove"), "");
                else if (line.contains("=")) {
                    if(line.contains("{")) {
                        writeOutput(arrayInitialization(line), "");
                    }
                    else if(line.contains("[")) {
                        writeOutput(setSingleValue(line), "");
                    }
                    else {
                        writeOutput(line, "\n");
                    }
                }
                else
                    writeOutput(line, "\n");
            }

            for(int i = 0; i < names.size(); i++) {
                output = output.replaceAll(names.get(i) + "\\[", names.get(i) + ".array[");
            }

            System.out.println(output);
        } catch (FileNotFoundException e) {
            System.out.println(filename + " does not exist in the present directory.");
        }

        try {
            sc.close();
        } catch(NullPointerException e) {
            System.out.println("Oops... Something went wrong.");
        }
    }
}
