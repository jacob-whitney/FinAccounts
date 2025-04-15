/**
 * Advanced Database Development
 * April 23, 2025
 * AccountsTest.java
 * @author Jacob Whitney
 */

// Imports
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Written unit tests for each function
 * in the program
 */
public class AccountsTest {
    private Account acc1 = new Account(
            1001,
            "Eating Out",
            "Spending",
            100.25
    );
    private int id2 = 1002;
    private String name2 = "Spain Trip 2025";
    private String type2 = "Saving";
    private double amount2 = 1536.94;

    /**
     * Setup any necessary values before each assertion
     */
    @org.junit.jupiter.api.BeforeEach
    void setUp() {

    }

    /**
     * Test if Account can be created successfully
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Create Account Test")
    void testCreateAccount() {
        Account acc2 = new Account(id2, name2, type2, amount2);

        // Assertions
        assertEquals(acc2.getId(), id2, "IDs do not match");
        assertEquals(acc2.getName(), name2, "Name does not match");
        assertEquals(acc2.getType(), type2, "Type does not match");
        assertEquals(acc2.getBalance(), amount2, "Balance does not match");
    }

    /**
     * Test if Account setters work correctly
     */
    @org.junit.jupiter.api.Test@DisplayName("Set Account Values Test")
    void testSetAccountValues() {
        int id3 = 1003;
        String name3 = "Bank of America";
        String type3 = "Checking";
        double amount3 = 501.22;
        acc1.setId(id3);
        acc1.setName(name3);
        acc1.setType(type3);
        acc1.setBalance(amount3);

        assertEquals(acc1.getId(), id3, "IDs do not match");
        assertEquals(acc1.getName(), name3, "Name does not match");
        assertEquals(acc1.getType(), type3, "Type does not match");
        assertEquals(acc1.getBalance(), amount3, "Balance does not match");
    }
}
