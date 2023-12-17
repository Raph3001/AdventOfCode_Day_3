import org.w3c.dom.ls.LSOutput;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public List<String> getData() {
        String filePath = "C:\\Users\\Rapha\\GearRatios\\src\\resources\\input.txt";
        List<String> dataLines = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            // Read lines from the file until reaching the end
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line); // Print each line to the console
                dataLines.add(line);
            }

            // Close the resources after reading
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return dataLines;
    }

    public int getSumOfAdjacentNumbersToSymbols(List<String> lines) {
        int sum = 0;
        List<Map<Integer, Integer>> intList = new ArrayList<>();
        List<Map<Integer, String>> characterMapping = new ArrayList<>();

        int lineCount = 0;
        for (String line : lines) {
            lineCount++;
            int count = 0;
            int lastIndexAdded = -10;
            for (char c : line.toCharArray()) {
                count++;
                if (isDigit(c)) {
                    if (intList.size() <= lineCount-1) {
                        intList.add(new HashMap<>());
                    }
                    if (count==lastIndexAdded+1) {
                        int toAdd = intList.get(intList.size()-1).get(lastIndexAdded);
                        intList.get(intList.size()-1).remove(lastIndexAdded);
                        intList.get(intList.size()-1).put(count, Integer.parseInt(toAdd + "" + c));
                        //System.out.println("f" + c);
                        lastIndexAdded = count;
                    }
                    else {
                        intList.get(intList.size()-1).put(count, Integer.parseInt((c+"")));
                        lastIndexAdded = count;
                    }
                }

                if (characterMapping.size() <= lineCount-1) {
                    characterMapping.add(new HashMap<>());
                }
                characterMapping.get(lineCount-1).put(count, c+"");

            }
            //System.out.println(characterMapping);



        }

        System.out.println(intList.size());
        System.out.println(characterMapping.size());
        intList.forEach(System.out::println);

        for (int i = 0; i<intList.size(); i++) {
            Map<Integer, Integer> intMapping = intList.get(i);
            Map<Integer, String> charMapping = characterMapping.get(i);
            Map<Integer, Integer> previousIntMapping = new HashMap<>();
            Map<Integer, String> previousCharMapping = new HashMap<>();
            Map<Integer, Integer> nextIntMapping = new HashMap<>();
            Map<Integer, String> nextChatMapping = new HashMap<>();
            if (i!=0) {
                previousIntMapping=intList.get(i-1);
                previousCharMapping=characterMapping.get(i-1);
            }
            if (i!=intList.size()-1) {
                nextIntMapping=intList.get(i+1);
                nextChatMapping=characterMapping.get(i+1);
            }

            for (int num : intMapping.keySet()) {
                if (intIsValid(intMapping.get(num), num, charMapping, nextChatMapping, previousCharMapping)) sum += intMapping.get(num);
            }
            
        }

        return sum;
    }

    private boolean intIsValid(int number, int index, Map<Integer, String> charMapping, Map<Integer, String> charMappingNL, Map<Integer, String> charMappingPL) {

        List<Integer> indicesToCheck = new ArrayList<>();
        if (index - ((number+"").length() + 1) > 0) {
            indicesToCheck.add(index - ((number+"").length()));
        }

        for (int i = 0; i<(number+"").length(); i++) {
            indicesToCheck.add(index-((number+"").length()-1-i));
        }

        if (index + 1 < charMapping.size()) {
            indicesToCheck.add(index+1);
        }
        System.out.println("These indices are checked: " + indicesToCheck);
        System.out.println("For this number mapping: " + number + " " + index);

        for(int i : indicesToCheck) {

            if (!(charMapping.get(i).equals(".")) && !isDigit(charMapping.get(i))) {
                return true;
            }

            if (!charMappingNL.isEmpty()) {
                if (!charMappingNL.get(i).equals(".") && !isDigit(charMappingNL.get(i))) {
                    return true;
                }
            }

            if (!charMappingPL.isEmpty()) {
                if (!charMappingPL.get(i).equals(".") && !isDigit(charMappingPL.get(i))) {
                    return true;
                }
            }

        }


        System.out.println("The code returns false for the number " + number + " at index " + index);
        return false;
    }

    private boolean isDigit(char c) {
        try {
            int i = Integer.parseInt((c + ""));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isDigit(String c) {
        try {
            int i = Integer.parseInt((c));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {

        Main main = new Main();
        System.out.println(main.getSumOfAdjacentNumbersToSymbols(main.getData()));

    }
}