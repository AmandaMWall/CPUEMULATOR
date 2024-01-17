package com.example;

public class HandleCode {     
    int[] registers = {0, 0, 0, 0};
    int pointer = 0;
    String[] splitCode;
    Result result = new Result();

    Checker checker = new Checker();

    public Result runCode(String code) {
        String[] currBlock;

        splitCode = code.split(" ");

        int startingCode;

        boolean ans;

        result.reply = "";

        // Go through each block of code
        while (pointer < splitCode.length) {
            currBlock = splitCode[pointer].split("");

            // Add fetch animation
            addAnim("F");
            addAnim(splitCode[pointer]);
            
            if (currBlock.length != 4) {
                result.reply += "Each block of code needs to be 4 characters long";
                return result;
            }

            // Try number codes first, then hexadecimal
            try {
                startingCode = Integer.parseInt(currBlock[0]);
                ans  = tryNumCodes(startingCode, currBlock);
            } catch (Exception e) {
                ans = tryAlphaCodes(currBlock[0], currBlock);
            }

            // Check if there was an error
            if (!ans) {
                return result;
            }

            pointer += 1;

            if (!checkAllRegisters()) {
                return result;
            }
       }

        return result;
    }

    private boolean stopExecution() {
        /*
         * 0 Stop execution and print out registers
         */

        // Add decode
        addAnim("D");
        addAnim("0 - Stop Execution");
        addAnim("0");
        addAnim("0");
        addAnim("0");
        
        // Go through each register and add values to reply
        for (int i = 0; i < 4; i++) {
            result.reply += registers[i];

            if (i < 3) {
                result.reply += " ";
            }
        }

        return true;
    }

    public boolean loadVal(String[] currBlock) {
        /*
          1knn load value nn into register k
         */

        // Add decode
        addAnim("D");
        addAnim("1 - Load Val");

        boolean cRegister = checker.checkRegisterInt(currBlock[1]);

        if (!cRegister) {
            result.reply += "Invalid register '" + currBlock[1] + "'. A register must be an integer between 0 and 3";
            return false;
        }

        // Decode
        addAnim(currBlock[1] + " - Register num.");

        int register = Integer.parseInt(currBlock[1]);
        boolean cValue = checker.checkInt(currBlock[2] + currBlock[3]);

        if (!cValue) {
            result.reply += "Invalid integer '" + currBlock[2] + currBlock[3] + "'.";
            return false;
        }

        // Decode
        addAnim(currBlock[2] + " - Num 1");
        addAnim(currBlock[3] + " - Num 2");

        registers[register] = Integer.parseInt(currBlock[2] + currBlock[3]);

        // Execute
        addAnim("E");
        addAnim("REG");
        addAnim(currBlock[1]);
        addAnim(Integer.toString(registers[register]));

        return true;
    }

    public boolean doMath(String[] currBlock) {
        /*
          Handles codes 2, 3, 4, 5

          - 2abc Add values from registers a and b, load into register c
          - 3abc Subtract values from registers a and b, load into register c
          - 4abc Divide values from registers a and b, load into register c
          - 5abc Multiply values from registers a and b, load into register c
         */
        
        // Decode
        addAnim("D");

        // Verify if registers are valid
        for (int i = 1; i < 4; i++) {
            boolean cRegister = checker.checkRegisterInt(currBlock[i]);

            if (!cRegister) {
                result.reply += "Invalid register '" + currBlock[i] + "'. A register must be an integer between 0 and 3";
                return false;
            }
        }

        // Get values from registers
        int val1 = registers[Integer.parseInt(currBlock[1])];
        int val2 = registers[Integer.parseInt(currBlock[2])];

        // Determine what type of math: Add, minus, divide, multiply
        switch (currBlock[0]) {
            case "2":
                addAnim("2 - Add");
                addAnim(currBlock[3] + " - Register num.");
                addAnim(val1 + " - Num 1");
                addAnim(val2 + " - Num 2");

                registers[Integer.parseInt(currBlock[3])] = val1 + val2;

                // Execute
                addAnim("E");
                addAnim("ALU");
                addAnim(val1 + " + " + val2 + " = " + registers[Integer.parseInt(currBlock[3])]);
                break;
            case "3":
                addAnim("3 - Minus");
                addAnim(currBlock[3] + " - Register num.");
                addAnim(val1 + " - Num 1");
                addAnim(val2 + " - Num 2");

                registers[Integer.parseInt(currBlock[3])] = val1 - val2;

                // Execute
                addAnim("E");
                addAnim("ALU");
                addAnim(val1 + " - " + val2 + " = " + registers[Integer.parseInt(currBlock[3])]);
                break;
            case "4":
                addAnim("4 - Divide");
                addAnim(currBlock[3] + " - Register num.");
                addAnim(val1 + " - Num 1");
                addAnim(val2 + " - Num 2");

                if (val2 == 0) {
                    result.reply += "Cannot divide by 0";
                    return false;
                }

                registers[Integer.parseInt(currBlock[3])] = val1 / val2;

                // Execute
                addAnim("E");
                addAnim("ALU");
                addAnim(val1 + " / " + val2 + " = " + registers[Integer.parseInt(currBlock[3])]);
                break;
            case "5":
                addAnim("5 - Multiply");
                addAnim(currBlock[3] + " - Register num.");
                addAnim(val1 + " - Num 1");
                addAnim(val2 + " - Num 2");

                registers[Integer.parseInt(currBlock[3])] = val1 * val2;

                // Execute
                addAnim("E");
                addAnim("ALU");
                addAnim(val1 + " * " + val2 + " = " + registers[Integer.parseInt(currBlock[3])]);
                break;
        }  

        addAnim("E");
        addAnim("REG");
        addAnim(currBlock[3]);
        addAnim(Integer.toString(registers[Integer.parseInt(currBlock[3])]));

        return true;
    }

    public boolean moveVal(String[] currBlock) {
        /*
          6ab0 Move value from register a into register b
         */

        addAnim("D");
        addAnim("6 - Move");

        // Check if registers are valid
        for (int i = 1; i < 3; i++) {
            boolean cRegister = checker.checkRegisterInt(currBlock[i]);

            if (!cRegister) {
                result.reply += "Invalid register '" + currBlock[i] + "'. A register must be an integer between 0 and 3";
                return false;
            }

            addAnim(currBlock[i] + " - Reg " + i);
        }

        addAnim("0");

        // Get value from register
        int val1 = registers[Integer.parseInt(currBlock[1])];

        // Place in second register and clear first register
        registers[Integer.parseInt(currBlock[2])] = val1;
        registers[Integer.parseInt(currBlock[1])] = 0;

        // Execute
        addAnim("E");
        addAnim("REG");
        addAnim(currBlock[2]);
        addAnim(Integer.toString(val1));

        addAnim("E");
        addAnim("REG");
        addAnim(currBlock[1]);
        addAnim("0");

        return true;
    }

    public boolean bitwise(String[] currBlock) {
        /*
          - 7abc bitwise OR on values from register a and b, load result into register c
          - 8abc bitwise AND on values from register a and b, load result into register c
          - 9abc bitwise XOR on values from register a and b, load result into register c
         */

        addAnim("D");

        // Verify if registers are valid
        for (int i = 1; i < 4; i++) {
            boolean cRegister = checker.checkRegisterInt(currBlock[i]);

            if (!cRegister) {
                result.reply += "Invalid register '" + currBlock[i] + "'. A register must be an integer between 0 and 3";
                return false;
            }
        }

        // Get values in register now that they are valid
        int val1 = registers[Integer.parseInt(currBlock[1])];
        int val2 = registers[Integer.parseInt(currBlock[2])];

        registers[Integer.parseInt(currBlock[3])] = val1 | val2;

        switch (currBlock[0]) {
            case "7":
                addAnim("7 - OR");
                addAnim(currBlock[1] + "- Reg 1");
                addAnim(currBlock[2] + "- Reg 2");
                addAnim(currBlock[1] + "- Load into reg 3");

                registers[Integer.parseInt(currBlock[3])] = val1 | val2;

                // Execute
                addAnim("E");
                addAnim("ALU");
                addAnim(val1 + " ^ " + val2 + " = " + registers[Integer.parseInt(currBlock[3])]);
                break;
            case "8":
                addAnim("8 - AND");
                addAnim(currBlock[1] + "- Reg 1");
                addAnim(currBlock[2] + "- Reg 2");
                addAnim(currBlock[1] + "- Load into reg 3");

                registers[Integer.parseInt(currBlock[3])] = val1 & val2;

                // Execute
                addAnim("E");
                addAnim("ALU");
                addAnim(val1 + " ^ " + val2 + " = " + registers[Integer.parseInt(currBlock[3])]);
                break;
            case "9":
                addAnim("7 - XOR");
                addAnim(currBlock[1] + "- Reg 1");
                addAnim(currBlock[2] + "- Reg 2");
                addAnim(currBlock[1] + "- Load into reg 3");

                registers[Integer.parseInt(currBlock[3])] = val1 ^ val2;

                // Execute
                addAnim("E");
                addAnim("ALU");
                addAnim(val1 + " ^ " + val2 + " = " + registers[Integer.parseInt(currBlock[3])]);
                break;
        }  

        // Execute
        addAnim("E");
        addAnim("REG");
        addAnim(currBlock[3]);
        addAnim(Integer.toString(registers[Integer.parseInt(currBlock[3])]));

        return true;
    }

    public boolean movePointer(String[] currBlock) {
        /*
            - Aab0 if value in reg a is equal to value in reg b, skip next instruction
            - Bab0 if value in reg a is not equal to value in reg b, skip next instruction
            - Dkaa goto instruction a if value is 0 in register k
         */

        addAnim("D");

        // Check if register is valid
        for (int i = 1; i < 3; i++) {
            boolean cRegister = checker.checkRegisterInt(currBlock[i]);

            if (!cRegister) {
                result.reply += "Invalid register '" + currBlock[i] + "'. A register must be an integer between 0 and 3";
                return false;
            }
        }

        // Get values now that we have verified the registers
        int val1 = registers[Integer.parseInt(currBlock[1])];
        int val2 = registers[Integer.parseInt(currBlock[2])];

        if (currBlock[0].equals("A")) {
            addAnim("A - Skip next if");
            addAnim(currBlock[1] + " - Reg 1");
            addAnim(currBlock[2] + " - Reg 2");
            addAnim("0");


            if (val1 == val2) {
                pointer += 1;
            }

            // Execute
            addAnim("E");
            addAnim("ALU");
            addAnim(val1 + " == " + val2 + " = " + (val1 != val2) );

        } else if (currBlock[0].equals("B")) {
            addAnim("B - Skip next if");
            addAnim(currBlock[1] + " - Reg 1");
            addAnim(currBlock[2] + " - Reg 2");
            addAnim("0");

            if (val1 != val2) {
                pointer += 1;
            }

            // Execute
            addAnim("E");
            addAnim("ALU");
            addAnim(val1 + " != " + val2 + " = " + (val1 == val2) );

        } else {
            addAnim("D - Goto if");
            addAnim(currBlock[1] + " - Register");
            addAnim(currBlock[2] + " - Num 1");
            addAnim(currBlock[3] + " - Num 2");

            if (val1 == 0) {
                System.out.println(pointer);
                // -1 because pointer is incremented at the end of loop
                pointer = Integer.parseInt(currBlock[2] + currBlock[3]) - 1;

            }

            // Execute
            addAnim("E");
            addAnim("ALU");
            addAnim(val1 + " == " + "0" + " = " + (val1 == 0));
        }


        return true;
    }

    public boolean clearRegisters() {
        // C000 - Clear registers
        addAnim("D");
        addAnim("C - clear registers");
        addAnim("0");
        addAnim("0");
        addAnim("0");
        
        // Go through each register and set to 0
        for (int i = 0; i < 4; i++) {
            registers[i] = 0;

            // Execute
            addAnim("E");
            addAnim("REG");
            addAnim(Integer.toString(i));
            addAnim("0");
        }

        return true;
    }

    public boolean tryNumCodes(int startingCode, String[] currBlock) {
        boolean ans;

        if (startingCode == 0) {
            ans = stopExecution();
        }
        else if (startingCode == 1) {
            ans = loadVal(currBlock);
        }
        // Codes 2 - 5
        else if (startingCode >= 2 && startingCode <= 5) {
            ans = doMath(currBlock);
        }
        else if (startingCode == 6) {
            ans = moveVal(currBlock);
        }
        // Codes 7 - 9
        else if (startingCode >= 7 && startingCode <= 9) {
            ans = bitwise(currBlock);
        } else {
            result.reply += "Invalid code " + currBlock[0];
            ans = false;
        }

        return ans;
    }

    public boolean tryAlphaCodes(String startingCode, String[] currBlock) {
        boolean ans;

        if (startingCode.equals("A") || startingCode.equals("B") || startingCode.equals("D")) {
            ans = movePointer(currBlock);
        } else if (startingCode.equals("C")) {
            ans = clearRegisters();
        } else {
            result.reply += "Invalid code " + currBlock[0];
            ans = false;
        }

        return ans;
    }

    public boolean checkAllRegisters() {
        for (int i = 0; i < 4; i++) {
            if (registers[i] < -256 || registers[i] > 256) {
                result.reply += "Registers " + i + " is " + registers[i] + " which is less than -256 or greater than 256.";
                return false;
            }
        }

        return true;
    }

    public void addAnim(String val) {
        result.animationSteps.add(val);
    }
}
