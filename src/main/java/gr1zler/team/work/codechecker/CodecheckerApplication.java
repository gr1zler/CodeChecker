package gr1zler.team.work.codechecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class CodecheckerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodecheckerApplication.class, args);
    }

}
