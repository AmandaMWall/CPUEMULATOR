package com.example;

import java.util.ArrayList;
import java.util.Iterator;

class Animate {
    static ArrayList<String> animationSteps;
    static Iterator<String> it;
    static int speed = 2000;

    public Animate(ArrayList<String> animSteps) {
        animationSteps = animSteps;
    }

    public void animUi() {
        it = animationSteps.iterator();
        String currBlock = "";

        while (it.hasNext()) {
            currBlock = it.next();

            switch (currBlock) {
                case "F":
                    startFetch();
                    break;
                case "D":
                    startDecode();
                    break;
                case "E":
                    startExecute();
                    break;
            }

            // Call pause in main
            Main.pause();

            Main.clearALU();
            Main.clearDecode();
        }

        Main.toOutput();
    }

    private static void startFetch() {
        Main.setActionFetch();

        if (it.hasNext()) {
            Main.setIR(it.next());
        }
    }

    private static void startDecode() {
        Main.setActionDecode();

        int num = 0;

        while (num < 4 && it.hasNext()) {
            switch (num) {
                case 0:
                    Main.setDecode1(it.next());
                    break;
                case 1:
                    Main.setDecode2(it.next());
                    break;
                case 2:
                    Main.setDecode3(it.next());
                    break;
                case 3:
                    Main.setDecode4(it.next());
                    break;
            }
            num += 1;
        }
    }

    private static void startExecute() {
        Main.setActionExecute();

        // Get what execute it is
        if (it.hasNext()) {
            switch (it.next()) {
                // Set a register REG <reg number> <value>
                case "REG":
                    setRegister();
                    break;
                // Set alu ALU <equation>
                case "ALU":
                    setALU();
                    break;
            }
        }
    }

    private static void setALU() {
        if (it.hasNext()) {
            Main.setALU(it.next());
        }
    }

    private static void setRegister() {
        if (it.hasNext()) {
            switch (it.next()) {
                case "0":
                    Main.setR1(it.next());
                    break;
                case "1":
                    Main.setR2(it.next());
                    break;
                case "2":
                    Main.setR3(it.next());
                    break;
                case "3":
                    Main.setR4(it.next());
                    break;
            }
        }
    }
}
