package lk.ac.pdn.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;

@SpringBootApplication
@EnableAsync
public class SmsUopApplication {

    public static void main(String[] args) {
        try {
            // 1. Check if .env file exists in the root directory
            File envFile = new File(".env");

            if (envFile.exists()) {
                System.out.println("✅ Found .env file. Loading variables...");
                Dotenv dotenv = Dotenv.configure()
                        .ignoreIfMissing()
                        .load();

                // 2. Load variables into System Properties (Spring Boot will pick these up)
                // We only set them if they are not null to avoid overwriting defaults with nulls
                setSystemPropertyIfPresent("spring.datasource.url", dotenv.get("DB_URL"));
                setSystemPropertyIfPresent("spring.datasource.username", dotenv.get("DB_USERNAME"));
                setSystemPropertyIfPresent("spring.datasource.password", dotenv.get("DB_PASSWORD"));

                setSystemPropertyIfPresent("spring.mail.username", dotenv.get("EMAIL_USERNAME"));
                setSystemPropertyIfPresent("spring.mail.password", dotenv.get("EMAIL_PASSWORD"));

                setSystemPropertyIfPresent("spring.security.oauth2.client.registration.google.client-id", dotenv.get("GOOGLE_CLIENT_ID"));
                setSystemPropertyIfPresent("spring.security.oauth2.client.registration.google.client-secret", dotenv.get("GOOGLE_CLIENT_SECRET"));

                System.out.println("Environment variables loaded from .env");
            } else {
                System.out.println("⚠️ No .env file found. Proceeding with default settings from application.yml");
            }

            // 3. Start the Application
            SpringApplication.run(SmsUopApplication.class, args);

        } catch (Exception e) {
            // If the .env loader fails, we print it but TRY TO START anyway using defaults
            System.err.println("⚠️ Warning: Failed to load .env file: " + e.getMessage());
            System.err.println("Attempting to start application with default configuration...");
            SpringApplication.run(SmsUopApplication.class, args);
        }
    }

    private static void setSystemPropertyIfPresent(String key, String value) {
        if (value != null && !value.trim().isEmpty()) {
            System.setProperty(key, value);
        }
    }
}