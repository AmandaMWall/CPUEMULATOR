package com.example;

public class Checker {
    public boolean checkRegisterInt(String value) {
        /*
          A register must be an integer between 0 and 3
         */
        int register;
        try {
            register = Integer.parseInt(value);
        } catch (Exception e) {
            return false;
        }

        return register >= 0 && register <= 3;
    }

    // Check if integer is valid
    public boolean checkInt(String value) {
        try {
            Integer.parseInt(value);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
