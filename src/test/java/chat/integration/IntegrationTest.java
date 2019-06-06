package chat.integration;

import chat.Main;
import chat.component.ChatDialog;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testApplicationContext.xml"})
public class IntegrationTest {

    @Autowired
    private ChatDialog chatDialog;

    @Ignore
    @Before
    public void setup() {
        Main.setAppContextLocation("testApplicationContext.xml");
        Main.main(new String[0]);
    }

    @Ignore
    @Test
    public void nothing() {
        System.out.println("1");
        assertEquals("", "");
    }

    @Ignore
    @After
    public void cleanup() {
        chatDialog.getStage().close();
    }
}
