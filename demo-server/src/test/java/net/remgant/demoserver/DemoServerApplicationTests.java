package net.remgant.demoserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"user.names=alice","admin.names=bob"})
class DemoServerApplicationTests {

    @Test
    void contextLoads() {
    }

}
