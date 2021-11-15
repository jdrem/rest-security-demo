package net.remgant.democlient;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties={"keystore.password=abcdefgh","cert.name=test.jks"})
class DemoClientApplicationTests {

    @Test
    void contextLoads() {
    }

}
