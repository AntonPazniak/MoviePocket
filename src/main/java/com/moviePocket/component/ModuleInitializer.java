package com.moviePocket.component;


import com.moviePocket.db.repository.ModelRepository;
import com.moviePocket.util.ModelsConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModuleInitializer implements CommandLineRunner {

    private final ModelRepository modelRepository;

    @Override
    public void run(String... args) {
        var modules = modelRepository.findAll();
        var constModules = ModelsConstant.modules;

        if (modules.size() != constModules.size()) {
            constModules.forEach(e -> {
                if (!modelRepository.existsById((long) e.getId()))
                    modelRepository.save(e);
            });
        }
    }
}
