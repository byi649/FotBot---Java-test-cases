package swen90006.fotbot;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.*;
import static org.junit.Assert.*;

public class PartitioningTests
{
    protected FotBot fotbot;

    //Any method annotated with "@Before" will be executed before each test,
    //allowing the tester to set up some shared resources.
    @Before public void setUp()
	throws DuplicateUserException, InvalidUsernameException, InvalidPasswordException, NoSuchUserException, IncorrectPasswordException
    {
	fotbot = new FotBot();
	fotbot.register("userName1", "password1!");
    fotbot.register("friend", "password1!");
    fotbot.register("notFriend", "password1!");
    fotbot.addFriend("friend", "password1!", "userName1");

    fotbot.incrementCurrentDay(3);
    fotbot.update("friend", "password1!", list(new Integer [] {2, 3, 4}));
    fotbot.update("notFriend", "password1!", list(new Integer [] {2, 3, 4}));
    }

    //Any method annotated with "@After" will be executed after each test,
    //allowing the tester to release any shared resources used in the setup.
    @After public void tearDown()
    {
    }

    @Test(expected = DuplicateUserException.class)
    public void fotBotTestEC1() throws DuplicateUserException, InvalidUsernameException, InvalidPasswordException {
	// User already exists
	fotbot.register("userName1", "password1!");
    }

    @Test(expected = InvalidUsernameException.class)
    public void fotBotTestEC2() throws DuplicateUserException, InvalidUsernameException, InvalidPasswordException {
        // Username less than 4 characters
        fotbot.register("use", "password1!");
    }

    @Test(expected = InvalidPasswordException.class)
    public void fotBotTestEC3() throws DuplicateUserException, InvalidUsernameException, InvalidPasswordException {
        // Password less than 8 characters
        fotbot.register("userName2", "pa1!");
    }

    @Test
    public void fotBotTestEC4() throws DuplicateUserException, InvalidUsernameException, InvalidPasswordException {
        // All conditions successful
        fotbot.register("userName2", "password1!");
        assertTrue(fotbot.isUser("userName2"));
    }

    @Test(expected = InvalidPasswordException.class)
    public void fotBotTestEC5() throws DuplicateUserException, InvalidUsernameException, InvalidPasswordException {
        // No special characters in password
        fotbot.register("userName2", "password1");
    }

    @Test(expected = NoSuchUserException.class)
    public void fotBotTestEC6() throws NoSuchUserException, IncorrectPasswordException {
        // No such user
	    List<Integer> newSteps_EC6 = list(new Integer [] {1000});
        fotbot.update("userName2", "password1!", newSteps_EC6);
    }

    @Test(expected = IncorrectPasswordException.class)
    public void fotBotTestEC7() throws NoSuchUserException, IncorrectPasswordException {
        // Incorrect password
        List<Integer> newSteps_EC7 = list(new Integer [] {1000});
        fotbot.update("userName1", "password2!", newSteps_EC7);
    }

    @Test
    public void fotBotTestEC8() throws NoSuchUserException, IncorrectPasswordException {
        // Number of steps added = 0
        List<Integer> newSteps_EC8 = list(new Integer [] {});
        fotbot.update("userName1", "password1!", newSteps_EC8);

        List<Integer> steps_EC8 = fotbot.getStepData("userName1", "password1!", "userName1");
	    List<Integer> expected_EC8 = list(new Integer [] {0, 0, 0});
	    assertEquals(expected_EC8, steps_EC8);
    }

    @Test
    public void fotBotTestEC9() throws NoSuchUserException, IncorrectPasswordException {
        // Number of steps added = 1
        List<Integer> newSteps_EC9 = list(new Integer [] {1});
        fotbot.update("userName1", "password1!", newSteps_EC9);

        List<Integer> steps_EC9 = fotbot.getStepData("userName1", "password1!", "userName1");
        List<Integer> expected_EC9 = list(new Integer [] {0, 0, 1});
        assertEquals(expected_EC9, steps_EC9);
    }

    @Test
    public void fotBotTestEC10() throws NoSuchUserException, IncorrectPasswordException {
        // Number of steps added = 2
        List<Integer> newSteps_EC10 = list(new Integer [] {1, 2});
        fotbot.update("userName1", "password1!", newSteps_EC10);

        List<Integer> steps_EC10 = fotbot.getStepData("userName1", "password1!", "userName1");
        List<Integer> expected_EC10 = list(new Integer [] {0, 1, 2});
        assertEquals(expected_EC10, steps_EC10);
    }

    @Test
    public void fotBotTestEC11() throws NoSuchUserException, IncorrectPasswordException {
        // Number of steps added = number of days
        List<Integer> newSteps_EC11 = list(new Integer [] {1, 2, 3});
        fotbot.update("userName1", "password1!", newSteps_EC11);

        List<Integer> steps_EC11 = fotbot.getStepData("userName1", "password1!", "userName1");
        List<Integer> expected_EC11 = list(new Integer [] {1, 2, 3});
        assertEquals(expected_EC11, steps_EC11);
    }

    @Test(expected = NoSuchUserException.class)
    public void fotBotTestEC12() throws NoSuchUserException, IncorrectPasswordException {
        // User doesn't exist
        fotbot.getStepData("userName2", "password1!", "userName1");
    }

    @Test(expected = IncorrectPasswordException.class)
    public void fotBotTestEC13() throws NoSuchUserException, IncorrectPasswordException {
        // Password incorrect
        fotbot.getStepData("userName1", "password2!", "userName1");
    }

    @Test(expected = NoSuchUserException.class)
    public void fotBotTestEC14() throws NoSuchUserException, IncorrectPasswordException {
        // Target user doesn't exist
        fotbot.getStepData("userName1", "password1!", "userName2");
    }

    @Test
    public void fotBotTestEC15() throws NoSuchUserException, IncorrectPasswordException {
        // Success: User is admin
        List<Integer> newSteps_EC15 = list(new Integer [] {1, 2, 3});
        fotbot.update("userName1", "password1!", newSteps_EC15);

        List<Integer> steps_EC15 = fotbot.getStepData(fotbot.ADMIN_USERNAME, fotbot.ADMIN_PASSWORD, "userName1");
        List<Integer> expected_EC15 = list(new Integer [] {1, 2, 3});
        assertEquals(expected_EC15, steps_EC15);
    }

    @Test
    public void fotBotTestEC16() throws NoSuchUserException, IncorrectPasswordException {
        // Success: User targets self
        List<Integer> newSteps_EC16 = list(new Integer [] {1, 2, 3});
        fotbot.update("userName1", "password1!", newSteps_EC16);

        List<Integer> steps_EC16 = fotbot.getStepData("userName1", "password1!", "userName1");
        List<Integer> expected_EC16 = list(new Integer [] {1, 2, 3});
        assertEquals(expected_EC16, steps_EC16);
    }

    @Test
    public void fotBotTestEC17() throws NoSuchUserException, IncorrectPasswordException {
        // Success: User targets friend
        List<Integer> steps_EC17 = fotbot.getStepData("userName1", "password1!", "friend");
        List<Integer> expected_EC17 = list(new Integer [] {2, 3, 4});
        assertEquals(expected_EC17, steps_EC17);
    }

    @Test
    public void fotBotTestEC18() throws NoSuchUserException, IncorrectPasswordException {
        // User targets non-friend
        List<Integer> steps_EC18 = fotbot.getStepData("userName1", "password1!", "notFriend");
        List<Integer> expected_EC18 = list(new Integer [] {});
        assertEquals(expected_EC18, steps_EC18);
    }

//    //Any method annotation with "@Test" is executed as a test.
//    @Test public void aTest()
//    {
//	//the assertEquals method used to check whether two values are
//	//equal, using the equals method
//	final int expected = 2;
//	final int actual = 1 + 1;
//	assertEquals(expected, actual);
//    }

//    @Test public void anotherTest()
//	throws DuplicateUserException, InvalidUsernameException, InvalidPasswordException
//    {
//	fotbot.register("userName2", "password2!");
//
//	//the assertTrue method is used to check whether something holds.
//	assertTrue(fotbot.isUser("userName2"));
//	assertFalse(fotbot.isUser("nonUser"));
//    }

//    @Test public void sampleFotBotTest()
//	throws NoSuchUserException, IncorrectPasswordException
//    {
//	fotbot.incrementCurrentDay(2);
//
//	List<Integer> newSteps = list(new Integer [] {1000, 2000});
//
//	//username1 is created in the setUp() method, which is run before every test
//	fotbot.update("userName1", "password1!", newSteps);
//
//	List<Integer> steps = fotbot.getStepData("userName1", "password1!", "userName1");
//	List<Integer> expected = list(new Integer [] {1000, 2000});
//	assertEquals(expected, steps);
//    }
  
//    //To test that an exception is correctly throw, specify the expected exception after the @Test
//    @Test(expected = java.io.IOException.class)
//    public void anExceptionTest()
//	throws Throwable
//    {
//	throw new java.io.IOException();
//    }

//    //This test should fail.
//    //To provide additional feedback when a test fails, an error message
//    //can be included
//    @Test public void aFailedTest()
//    {
//	//include a message for better feedback
//	final int expected = 2;
//	final int actual = 1 + 2;
//	//Uncomment the following line to make the test fail
//	//assertEquals("Some failure message", expected, actual);
//    }

    private List<Integer> list(Integer [] elements)
    {
	return new ArrayList<Integer>(Arrays.asList(elements));
    }
}
