package com.abrenchev;


public class SampleTest {
    private String testString;

    @Before
    public void setUp() {
        testString = "Hello, Bob!";
    }

    @Test(
            description = "Dummy green test"
    )
    public void alwaysGreenTest() {
    }

    @Test(
            description = "Test strings should match"
    )
    public void testCase() {
        if (!testString.equals("Hello, Bob!")) {
            throw new RuntimeException("testString is incorrect");
        };
    }

    @Test(
            description = "This test is always broken"
    )
    public void brokenTestCase() {
        throw new RuntimeException("No chance it would be fixed");
    }

    @After
    public void tearDown() {
        testString = null;
    }
}
