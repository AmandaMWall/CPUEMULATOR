package com.example;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.nio.file.Files;
import java.util.Scanner;

public class Main {
    static JFrame frame;

    static JFrame isaFrame;
    static JLabel isaLabel;
    static JTextArea isaDesc;
    static JLabel aboutLabel;
    static JTextArea about;

    static JLabel title;
    static JLabel csText;
    static JSpinner clockSpeed;
    static JButton pause;
    static JButton skip;
    static JButton isa;

    static JButton runCode;
    static JTextField code;
    static JButton runFile;
    static JFileChooser selectFile;
    static JButton output;
    static JLabel outputArea;

    static JLabel fetch;
    static Color fetchColor = Color.orange;
    static JLabel decode;
    static Color decodeColor = Color.green;
    static JLabel execute;
    static Color executeColor = Color.cyan;
    static Color inactiveColor = Color.gray;

    static JLabel r1Label;
    static JLabel r2Label;
    static JLabel r3Label;
    static JLabel r4Label;
    static JLabel r1;
    static JLabel r2;
    static JLabel r3;
    static JLabel r4;

    static JLabel aluLabel;
    static JLabel alu;
    static JLabel irLabel;
    static JLabel ir;
    static JLabel decodeLabel;
    static JLabel decode1; // operation, e.g. load
    static JLabel decode2; 
    static JLabel decode3;
    static JLabel decode4;

    static Thread t1; // thread for animate, so that UI updates dont affect pausing
    static int speed = 2;
    static Boolean paused = false;
    
    private static Boolean viewOutput = false; // user is viewing output

    public static void main(String[] args) {
        frame = new JFrame();
        frame.setSize(800, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        createISAFrame();
        createTopbar();
        createCPUAnimationArea();
        createCodeArea();

        frame.setLayout(null);

        // making the frame visible
        frame.setVisible(true);
    }

    private static void runInput(String input) {
        clearUI();
        HandleCode handleCode = new HandleCode();
        Result result = handleCode.runCode(input);

        try {
            String filename = "<YOUR PATH>" + "results.txt";
            FileWriter fw = new FileWriter(filename, true);
            fw.append(result.reply + "\n");
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
        
    

        outputArea.setText(result.reply);
        toOutput();
        Animate animate = new Animate(result.animationSteps);

        t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                animate.animUi();
            }
        });  
        t1.start();
    }

    public static void pause() {
        try {
            // If it isnt paused, do set speed
            if (!paused) {
                Thread.sleep(speed * 1000); 
            }

            // If paused, sleep indefinitely
            while (paused) {
                Thread.sleep(speed * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static void createISAFrame() {
        // ISA Frame
        isaFrame = new JFrame();
        isaFrame.setResizable(false);
        isaFrame.setSize(800, 600);

        // About area
        aboutLabel = new JLabel("About");
        aboutLabel.setFont(aboutLabel.getFont ().deriveFont (28.0f));
        aboutLabel.setBounds(60, 10, 400, 50);
        isaFrame.add(aboutLabel);

        // Text area to allow for long text
        about = new JTextArea("");
        about.setBounds(60, 60, 400, 50);
        about.append("A fantasy emulated CPU designed for educational purposes.");
        about.setLineWrap(true);
        about.setWrapStyleWord(true);
        about.setOpaque(false);
        about.setEditable(false);
        isaFrame.add(about);

        // ISA
        isaLabel = new JLabel("Instruction Set");
        isaLabel.setFont(isaLabel.getFont ().deriveFont (28.0f));
        isaLabel.setBounds(60, 100, 200, 50);
        isaFrame.add(isaLabel);

        isaDesc = new JTextArea("");
        isaDesc.setBounds(60, 150, 400, 400);

        String isaDescText[] = {
            "0's are ignored",
            "0000 Stop execution",
            "1knn load value nn into register k",
            "2abc Add values from registers a and b, load into register c",
            "3abc Subtract values from registers a and b, load into register c",
            "4abc Divide values from registers a and b, load into register c",
            "5abc Multiply values from registers a and b, load into register c",
            "6ab0 Move value from register a into register b",
            "7abc bitwise OR on values from register a and b, load result into register c",
            "8abc bitwise AND on values from register a and b, load result into register c",
            "9abc bitwise XOR on values from register a and b, load result into register c",
            "Aab0 if value in reg a is equal to value in reg b, skip next instruction",
            "Bab0 if value in reg a is not equal to value in reg b, skip next instruction",
            "Dkaa goto instruction a if value is 0 in register k"
        };

        for (int k = 0; k < isaDescText.length; k++) {
            isaDesc.append(isaDescText[k] + "\n");
        } 
        isaDesc.setLineWrap(true);
        isaDesc.setWrapStyleWord(true);
        isaDesc.setOpaque(false);
        isaDesc.setEditable(false);
        isaFrame.add(isaDesc);


        // using no layout managers
        isaFrame.setLayout(null);
    }

    private static void createTopbar() {
        // Top Bar //
        // Title
        title = new JLabel("CPU Emulator");
        title.setBounds(60, 10, 400, 50);
        title.setFont(title.getFont ().deriveFont (32.0f));
        frame.add(title);

        // ISA button
        isa = new JButton("ISA");
        isa.setBackground(Color.decode("#add8e6"));
        isa.setBounds(600, 10, 100, 30);

        // When ISA button is clicked
        isa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isaFrame.setVisible(true);
            }
        });

        frame.add(isa);

        // Time control //
        String nums[] = {"0", "1", "2", "3", "4", "5"}; // Clock speed can be set from 0 - 5
        SpinnerModel model1 = new SpinnerListModel(nums);
        clockSpeed = new JSpinner(model1);
        clockSpeed.setBounds(60, 60, 50, 50);

        ChangeListener listener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                speed = Integer.parseInt(clockSpeed.getValue().toString());
            }
        };

        clockSpeed.addChangeListener(listener);
        
        frame.add(clockSpeed);

        csText = new JLabel("Seconds/Instruction");
        csText.setBounds(115, 60, 150, 50);
        frame.add(csText);

        pause = new JButton("Pause");
        pause.setBounds(250, 60, 100, 50);

        // When pause button is clicked
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (paused) {
                    paused = false;
                } else {
                    paused = true;
                }
            }
        });
        
        frame.add(pause);

        skip = new JButton("End");
        skip.setBounds(360, 60, 100, 50);
        frame.add(skip);

        // When skip button is clicked
        skip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                speed = 0;
            }
        });

    }

    private static void createCPUAnimationArea() {
        UIHelper uiHelper = new UIHelper();

        fetch = uiHelper.createActionLbl("Fetch", 0);
        frame.add(fetch);

        decode = uiHelper.createActionLbl("Decode", 1);
        frame.add(decode);

        execute = uiHelper.createActionLbl("Execute", 2);
        frame.add(execute);

        // Registers
        r1Label = uiHelper.createRegisterLabel(0);
        frame.add(r1Label);
        r1 = uiHelper.createRegister(0);
        frame.add(r1);

        r2Label = uiHelper.createRegisterLabel(1);
        frame.add(r2Label);
        r2 = uiHelper.createRegister(1);
        frame.add(r2);

        r3Label = uiHelper.createRegisterLabel(2);
        frame.add(r3Label);
        r3 = uiHelper.createRegister(2);
        frame.add(r3);

        r4Label = uiHelper.createRegisterLabel(3);
        frame.add(r4Label);
        r4 = uiHelper.createRegister(3);
        frame.add(r4);

        frame.setBackground(Color.red);

        // ALU
        aluLabel = new JLabel("ALU (Arithmetic Logic Unit)");
        aluLabel.setBounds(60, 350, 200, 50);
        frame.add(aluLabel);

        alu = new JLabel("Any arithmetic operations will appear here", SwingConstants.CENTER);
        alu.setBounds(60, 370, 300, 50);
        alu.setOpaque(true);
        alu.setBackground(Color.lightGray);
        frame.add(alu);

        // IR
        irLabel = new JLabel("IR (Instruction Register)");
        irLabel.setBounds(60, 420, 200, 50);
        frame.add(irLabel);

        ir = new JLabel("0000", SwingConstants.CENTER);
        ir.setBounds(60, 470, 50, 50);
        ir.setOpaque(true);
        ir.setBackground(Color.lightGray);
        frame.add(ir);

        // Decode stages
        decodeLabel = new JLabel("Decode Stages", SwingConstants.CENTER);
        decodeLabel.setBounds(200, 420, 150, 50);
        frame.add(decodeLabel);

        decode1 = uiHelper.createDecodeStage(0);
        frame.add(decode1);

        decode2 = uiHelper.createDecodeStage(1);
        frame.add(decode2);

        decode3 = uiHelper.createDecodeStage(2);
        frame.add(decode3);

        decode4 = uiHelper.createDecodeStage(3);
        frame.add(decode4);
    }

    private static void createCodeArea() {
        // Code Area //
        // Code box
        code = new JTextField("1020 0000");
        code.setBounds(400, 200, 300, 300);
        frame.add(code);

        // Output box
        outputArea = new JLabel("Output will display here");
        outputArea.setBounds(400, 200, 300, 300);
        outputArea.setVisible(false);
        frame.add(outputArea);

        // Run code button
        runCode = new JButton("Run Code");
        runCode.setBounds(400, 150, 100, 50);
        runCode.setBackground(Color.decode("#add8e6"));

        // When runcode button is clicked
        runCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get input and run code
                String input = code.getText();
                runInput(input);  
            }
        });

        frame.add(runCode);

        // Select file button
        selectFile = new JFileChooser();

        // Run file button
        runFile = new JButton("Run File");
        runFile.setBackground(Color.decode("#add8e6"));
        runFile.setBounds(500, 150, 100, 50);

        frame.add(runFile);

        // If run file is clicked
        runFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFile.setMultiSelectionEnabled(false);

                // Open select file and if file not selected do nothing
                if (selectFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = selectFile.getSelectedFile();
                    try {
                        // Get mimeType
                        String mimeType = Files.probeContentType(file.toPath());
                        
                        // Ensure file is plain text file
                        if (!mimeType.equals("text/plain")) {
                            System.out.println("File must be text/plain");
                            return;
                        }

                        String input = "";

                        // Get text file contents
                        try {
                            Scanner myReader = new Scanner(file);
                            // Go through file and get every line
                            while (myReader.hasNextLine()) {
                                input += myReader.nextLine();
                            }

                            myReader.close();

                            runInput(input);
                        } catch (Exception e2) {
                            System.out.println("An unexpected error occured. Please try again later");
                            return;
                        }
      
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                 }
            }
        });

        // Output button
        output = new JButton("Output");
        output.setBackground(Color.decode("#add8e6"));
        output.setBounds(600, 150, 100, 50);

        // If output button is clicked toggle output/code box view
        output.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (viewOutput) {
                    code.setVisible(true);
                    outputArea.setVisible(false);
                    viewOutput = false;
                } else {
                    code.setVisible(false);
                    outputArea.setVisible(true);
                    viewOutput = true;
                }
            }
        });

        frame.add(output);
    }

    // Helper functions that ui interfaces need //
    private static void clearUI() {
        clearAction();
        clearDecode();
        clearALU();
        clearRegisters();
        clearIR();
    }

    private static void clearAction() {
        fetch.setBackground(inactiveColor);
        decode.setBackground(inactiveColor);
        execute.setBackground(inactiveColor);
    }

    public static void clearDecode() {
        decode1.setText("");
        decode2.setText("");
        decode3.setText("");
        decode4.setText("");
    }

    public static void clearALU() {
        alu.setText("Any arithmetic operations will appear here");
    }

    private static void clearRegisters() {
        r1.setText("0");
        r2.setText("0");
        r3.setText("0");
        r4.setText("0");
    }

    private static void clearIR() {
        ir.setText("0000");
    }

    // Interfaces for animate to use to edit ui state //

    // Actions
    public static void setActionFetch() {
        clearAction();
        fetch.setBackground(fetchColor);
    }

    public static void setActionDecode() {
        clearAction();
        decode.setBackground(decodeColor);
    }

    public static void setActionExecute() {
        clearAction();
        execute.setBackground(executeColor);
    }

    // IR
    public static void setIR(String val) {
        ir.setText(val);
    }

    // Decode
    public static void setDecode1(String text) {
        decode1.setText(text);
    }

    public static void setDecode2(String text) {
        decode2.setText(text);
    }

    public static void setDecode3(String text) {
        decode3.setText(text);
    }

    public static void setDecode4(String text) {
        decode4.setText(text);
    }

    // ALU
    public static void setALU(String text) {
        clearALU();
        alu.setText(text);
    }

    // Registers
    public static void setR1(String val) {
        r1.setText(val);
    }

    public static void setR2(String val) {
        r2.setText(val);
    }

    public static void setR3(String val) {
        r3.setText(val);
    }

    public static void setR4(String val) {
        r4.setText(val);
    }

    // Output click
    public static void toOutput() {
        output.doClick(); // go to output area
    }
}
