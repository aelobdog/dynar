/*
    Copyright (C) 2019, 2020 Ashwin Godbole
    
    This file is licensed under the GNU GPLv3. For more information on the terms
    of the license, see the file named LICENSE in the repository.
*/


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;

import static java.lang.Character.isLetter;

public class Main {

    private static String output = "";
    private static String[] types;
    private static ArrayList<String> names = new ArrayList<>(5);
    private static Map<String, String> namesAndTypes = new HashMap<>();
    private static String structDec = "struct dynarc_TYPE {\n\tTYPE* array;\n\tint len;\n\tint size;\n};\n";
    private static String arrayDec = "NAME.array = (TYPE *)malloc(10 * sizeof(TYPE));\nNAME.len = 0;\nNAME.size = 10;\n";
    private static String initStatement = "NAME[iterator] = VALUE;\n";
    private static String _1ArgFunction = "FUNCTION_CALL_TYPE(&ARG1);\n";
    private static String _2ArgFunction = "FUNCTION_CALL_TYPE(&ARG1, ARG2);\n";
    private static String _3ArgFunction = "FUNCTION_CALL_TYPE(&ARG1, ARG2, ARG3);\n";
    private static int firstDeclaration = 0;

    //------------------------------------------------------------------------------------------------------------------
    private static String importantFunctions =
            "void manage_memory_TYPE(struct dynarc_TYPE *dynarc_NAME) {\n" +
            "    if(dynarc_NAME->len == dynarc_NAME->size) {\n" +
            "        dynarc_NAME->size += 15;\n" +
            "        TYPE* tempArray = (TYPE *)malloc(dynarc_NAME->size * sizeof(TYPE));\n" +
            "        for(int i = 0; i < dynarc_NAME->len; i++) {\n" +
            "            tempArray[i] = dynarc_NAME->array[i];\n" +
            "        }\n" +
            "        free(dynarc_NAME->array);\n" +
            "        dynarc_NAME->array = tempArray;\n" +
            "        tempArray = NULL;\n" +
            "        free(tempArray);\n" +
            "    }\n" +
            "\n" +
            "    else if(dynarc_NAME->len == dynarc_NAME->size / 2) {\n" +
            "        dynarc_NAME->size = (dynarc_NAME->size / 2) + 5;\n" +
            "        TYPE* tempArray = (TYPE *)malloc(dynarc_NAME->size * sizeof(TYPE));\n" +
            "        for(int i = 0; i < dynarc_NAME->len; i++) {\n" +
            "            tempArray[i] = dynarc_NAME->array[i];\n" +
            "        }\n" +
            "        free(dynarc_NAME->array);\n" +
            "        dynarc_NAME->array = tempArray;\n" +
            "        tempArray = NULL;\n" +
            "        free(tempArray);\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "void pop_TYPE(struct dynarc_TYPE *dynarc_NAME) {\n" +
            "    manage_memory_TYPE(dynarc_NAME);\n" +
            "    dynarc_NAME->array[dynarc_NAME->len - 1] = 0;\n" +
            "    dynarc_NAME->len--;\n" +
            "}\n" +
            "\n" +
            "void del_TYPE(struct dynarc_TYPE *dynarc_NAME, int index) {\n" +
            "    if(index >= dynarc_NAME->len)\n" +
            "        printf(\"Tyring to delete a non-existent element !\");\n" +
            "    else {\n" +
            "        if(index == dynarc_NAME->len - 1) {\n" +
            "            manage_memory_TYPE(dynarc_NAME);\n" +
            "            dynarc_NAME->len--;\n" +
            "        }\n" +
            "        dynarc_NAME->array[index] = 0;\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "void push_TYPE(struct dynarc_TYPE *dynarc_NAME, TYPE value) {\n" +
            "    manage_memory_TYPE(dynarc_NAME);\n" +
            "    dynarc_NAME->array[dynarc_NAME->len] = value;\n" +
            "    dynarc_NAME->len++;\n" +
            "}\n" +
            "\n" +
            "void insert_TYPE(struct dynarc_TYPE *dynarc_NAME, TYPE value, int index) {\n" +
            "    if(index > dynarc_NAME->len)\n" +
            "        printf(\"Trying to insert an element outside the array !\");\n" +
            "    else {\n" +
            "        manage_memory_TYPE(dynarc_NAME);\n" +
            "        dynarc_NAME->array[index] = value;\n" +
            "        dynarc_NAME->len++;\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "void delN_TYPE(struct dynarc_TYPE *dynarc_NAME, TYPE value, int number) {\n" +
            "    if(number >= dynarc_NAME->len)\n" +
            "        printf(\"Trying to delete more elements than there are in the array !\");\n" +
            "    else {\n" +
            "        int i = 0;\n" +
            "        int j = 0;\n" +
            "        for(; i < dynarc_NAME->len && j < number; i++) {\n" +
            "            if(dynarc_NAME->array[i] == value) {\n" +
            "                dynarc_NAME->array[i] = 0;\n" +
            "                j++;\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n";
    //------------------------------------------------------------------------------------------------------------------

    private static boolean isDeclaredType(String type) {
        for(int i = 0; i < types.length; i++) {
            if(type.equals(types[i])) return true;
        }
        return false;
    }

    private static boolean isDeclaredDynarc(String name) {
        for(int i = 0; i < names.size(); i++) {
            if(name.equals(names.get(i))) return true;
        }
        return false;
    }

    private static void writeOutput(String stringToWrite, String end) {
        output = output + stringToWrite + end;
    }

    private static String structureDeclaration(String line) {
        String out = "";
        int indexUse = line.indexOf("use");
        int indexBopen = line.indexOf('[', indexUse) + 1;
        int indexBclose = line.indexOf(']', indexBopen);
        types = line.substring(indexBopen, indexBclose).split(",");
        for(int i = 0; i < types.length; i++) {
            types[i] = types[i].trim();
            out = out + structDec.replaceAll("TYPE", types[i]);
            out = out + importantFunctions.replaceAll("TYPE", types[i]);
        }
        return out;
    }

    private static String arrayDeclaration(String line) {
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
        namesAndTypes.put(arrayName, arrayType);
        out = out + arrayDec.replaceAll("TYPE", arrayType).replaceAll("NAME", arrayName);
        return out;
    }

    private static String arrayInitialization(String line) {
        String out = "";
        String name = line.substring(0, line.indexOf("=")).trim();
        if(!isDeclaredDynarc(name)) {
            System.err.println("Oops. Looks like [" + name + "] was not declared LL!");
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

    private static String setSingleValue(String line) {
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
        out = out + "manage_memory_TYPE(&dynarc_NAME);\n".replace("TYPE", namesAndTypes.get(name)).replace("dynarc_NAME", name);
        return out;
    }

    private static String _1ArgFunCall(String line, String action) {
        String name = line.substring(0, line.indexOf(".")).trim();
        return _1ArgFunction.replace("ARG1", name).replace("FUNCTION_CALL", "pop").replace("TYPE", namesAndTypes.get(name));
    }

    private static String _2ArgFunCall(String line, String action) {
        String name = line.substring(0, line.indexOf(".")).trim();
        String index = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
        return _2ArgFunction.replace("ARG1", name).replace("ARG2", index).replace("FUNCTION_CALL", action).replace("TYPE", namesAndTypes.get(name));
    }

    private static String _3ArgFunCall(String line, String action) {
        String name = line.substring(0, line.indexOf(".")).trim();
        String element = line.substring(line.indexOf("(") + 1, line.indexOf(",")).trim();
        String indexOrN = line.substring(line.indexOf(",") + 1, line.indexOf(")")).trim();
        return _3ArgFunction.replace("ARG1", name).replace("ARG2", element).replace("ARG3", indexOrN).replace("FUNCTION_CALL", action).replace("TYPE", namesAndTypes.get(name));
    }

    public static void main(String[] args) {
        String line;
        String filename = args[0];
        if(args.length == 0) {
            System.out.println("Trying to compile which file ? Read the Documentation !");
        }
        //String filename = "D:\\Data\\CODE\\JavaCode\\Dynarc\\src\\code.c";
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
                    writeOutput(structureDeclaration(line), "");
                else if (line.contains("dynarc_"))
                    writeOutput(arrayDeclaration(line), "");
                else if (line.contains(".pop"))
                    writeOutput(_1ArgFunCall(line, "pop"), "");
                else if (line.contains(".push"))
                    writeOutput(_2ArgFunCall(line, "push"), "");
                else if (line.contains(".removeN"))
                    writeOutput(_3ArgFunCall(line, "delN"), "");
                else if (line.contains(".insert"))
                    writeOutput(_3ArgFunCall(line, "insert"), "");
                else if (line.contains(".remove"))
                    writeOutput(_2ArgFunCall(line, "del"), "");
                else if (line.contains("=") && !line.contains("for") && !line.contains("if") && !line.contains("switch")) {
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
