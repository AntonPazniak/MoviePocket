package com.moviePocket.component;

import com.moviePocket.db.repository.user.RoleRepository;
import com.moviePocket.util.RoleConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        RoleConstants.ROLES.forEach(e -> {
            if (!roleRepository.existsById(e.getId())) {
                roleRepository.save(e);
            }
        });
    }

}
