package swen90006.fotbot;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileSystems;

import org.junit.*;
import static org.junit.Assert.*;

//By extending PartitioningTests, we inherit tests from the script
public class BoundaryTests
    extends PartitioningTests
{
    @Test
    public void fotBotTestEC2B() throws DuplicateUserException, InvalidUsernameException, InvalidPasswordException {
        // On: Username has 4 characters
        fotbot.register("user", "password1!");
        assertTrue(fotbot.isUser("user"));
    }

    @Override
    @Test(expected = InvalidPasswordException.class)
    public void fotBotTestEC3() throws DuplicateUserException, InvalidUsernameException, InvalidPasswordException {
        // Off: Password length of 7 characters
        fotbot.register("userName2", "pa1!567");
    }

    @Test
    public void fotBotTestEC3B() throws DuplicateUserException, InvalidUsernameException, InvalidPasswordException {
        // On: Password length of 8 characters
        fotbot.register("userName2", "passwo1!");
        assertTrue(fotbot.isUser("userName2"));
    }

    @Test
    public void fotBotTestEC11B()
    throws DuplicateUserException, InvalidUsernameException, InvalidPasswordException, NoSuchUserException, IncorrectPasswordException {
        // Off: Number of steps added = number of days - 1
        fotbot = new FotBot();
        fotbot.register("userName1", "password1!");
        fotbot.incrementCurrentDay(4);

        List<Integer> newSteps_EC11B = list(new Integer [] {1, 2, 3});
        fotbot.update("userName1", "password1!", newSteps_EC11B);

        List<Integer> steps_EC11B = fotbot.getStepData("userName1", "password1!", "userName1");
        List<Integer> expected_EC11B = list(new Integer [] {0, 1, 2, 3});
        assertEquals(expected_EC11B, steps_EC11B);
    }

    private List<Integer> list(Integer [] elements) {
        return new ArrayList<Integer>(Arrays.asList(elements));
    }
}
