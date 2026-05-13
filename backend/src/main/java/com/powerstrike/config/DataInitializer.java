package com.powerstrike.config;

import com.powerstrike.model.Activity;
import com.powerstrike.model.User;
import com.powerstrike.repository.ActivityRepository;
import com.powerstrike.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@powerstrike.com").isEmpty()) {
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail("admin@powerstrike.com");
            admin.setDni("00000000");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            admin.setActive(true);
            userRepository.save(admin);

            User cliente = new User();
            cliente.setName("Juan Pérez");
            cliente.setEmail("juan@gmail.com");
            cliente.setDni("12345678");
            cliente.setPassword(passwordEncoder.encode("cliente123"));
            cliente.setRole("CLIENT");
            cliente.setActive(true);
            userRepository.save(cliente);
        }

        if (activityRepository.count() == 0) {
            Activity boxeo = new Activity();
            boxeo.setName("Boxeo");
            boxeo.setDay("Lunes");
            boxeo.setSchedule("08:00 - 09:00");
            boxeo.setMonthlyCost(5000.0);
            boxeo.setActive(true);
            activityRepository.save(boxeo);

            Activity spinning = new Activity();
            spinning.setName("Spinning");
            spinning.setDay("Miércoles");
            spinning.setSchedule("18:00 - 19:00");
            spinning.setMonthlyCost(4500.0);
            spinning.setActive(true);
            activityRepository.save(spinning);

            Activity funcional = new Activity();
            funcional.setName("Entrenamiento Funcional");
            funcional.setDay("Viernes");
            funcional.setSchedule("07:00 - 08:00");
            funcional.setMonthlyCost(4000.0);
            funcional.setActive(true);
            activityRepository.save(funcional);
        }
    }
}
