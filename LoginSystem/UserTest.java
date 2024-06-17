import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UserTest {

    @Test
    public void testGetUsername() {
        // Create a User object
        User user = new User();
        
        // Set the username
        user.username = "testUser";
        
        // Test getUsername() method
        assertEquals("testUser", user.getUsername());
    }
}
