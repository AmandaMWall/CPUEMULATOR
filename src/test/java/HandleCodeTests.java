import com.example.HandleCode;
import com.example.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HandleCodeTests {
    HandleCode handleCode = new HandleCode();

    @Test
    public void testCode1() {
        // Load 1knn
        // Load 20 into R0, stop execution.
        Result result = handleCode.runCode("1020 0000");

        Assertions.assertEquals("20 0 0 0", result.reply);
    }

    @Test
    public void testCode2() {
        // 2abc Add
        // Load 20 into R1, Load 30 into R1, Add R0 + R1 and load into R2, stop execution
        Result result = handleCode.runCode("1020 1130 2012 0000");

        Assertions.assertEquals("20 30 50 0", result.reply);
    }

    @Test
    public void testCode3() {
        // 3abc Subtract
        // Load 20 into R1, Load 30 into R0, Add R1 - R0 and load into R2, stop execution
        Result result = handleCode.runCode("1020 1130 3102 0000");

        Assertions.assertEquals("20 30 10 0", result.reply);
    }

    @Test
    public void testCode4() {
        // 4abc Divide
        // Load 20 into R1, Load 2 into R0, Add R0 / R1 and load into R2, stop execution
        Result result = handleCode.runCode("1020 1102 4012 0000");

        Assertions.assertEquals("20 2 10 0", result.reply);
    }

    @Test
    public void testCode5() {
        // 5abc Multiply
        // Load 20 into R1, Load 2 into R0, Add R0 * R1 and load into R2, stop execution
        Result result = handleCode.runCode("1020 1102 5012 0000");

        Assertions.assertEquals("20 2 40 0", result.reply);
    }

    @Test
    public void testCode6() {
        // 6ab0 Move
        // Load 20 into R0, Move R0 to R1, stop execution.
        Result result = handleCode.runCode("1020 6010 0000");

        Assertions.assertEquals("0 20 0 0", result.reply);
    }

    @Test
    public void testCode7() {
        // 7abc bitwise OR
        // Load 2 into R0, Load 4 into R1, R0 | R1 and load into R2, stop execution
        Result result = handleCode.runCode("1002 1104 7012 0000");

        Assertions.assertEquals("2 4 6 0", result.reply);
    }

    @Test
    public void testCode8() {
        // 8abc bitwise AND
        // Load 2 into R0, Load 4 into R1, R0 & R1 and load into R2, stop execution
        Result result = handleCode.runCode("1002 1104 8012 0000");

        Assertions.assertEquals("2 4 0 0", result.reply);
    }

    @Test
    public void testCode9() {
        // 9abc bitwise XOR
        // Load 2 into R0, Load 4 into R1, R0 ^ R1 and load into R2, stop execution
        Result result = handleCode.runCode("1002 1104 9012 0000");

        Assertions.assertEquals("2 4 6 0", result.reply);
    }

    @Test
    public void testCodeA() {
        // Aab0 move pointer if value equal
        // Load 2 into R0, Load 2 into R1, check if R0 == R1, if so skip next instruction,
        // Load 4 into R2, stop execution.
        Result result = handleCode.runCode("1002 1102 A010 1204 0000");

        Assertions.assertEquals("2 2 0 0", result.reply);
    }

    @Test
    public void testCodeB() {
        // Bab0 move pointer if value is not equal
        // Load 2 into R0, Load 3 into R1, check if R0 == R1, if so skip next instruction,
        // Load 4 into R2, stop execution.
        Result result = handleCode.runCode("1002 1103 B010 1204 0000");

        Assertions.assertEquals("2 3 0 0", result.reply);
    }

    @Test
    public void testCodeC() {
        // C000 clear registers
        // Load 2 into R0, Load 3 into R1, Load 4 into R2. Load 5 into R3, clear registers,
        // stop execution.
        Result result = handleCode.runCode("1002 1103 1204 1305 C000 0000");

        Assertions.assertEquals("0 0 0 0", result.reply);
    }

    @Test
    public void testCodeD() {
        // Dkaa goto instruction if
        // Goto instruction 4 if register 0 is 0,
        // Load 2 into R0, Load 3 into R1, Load 4 into R2. Load 5 into R3, clear registers,
        // stop execution.
        Result result = handleCode.runCode("D004 1002 1103 1204 1305 0000");

        Assertions.assertEquals("0 0 0 5", result.reply);
    }
}