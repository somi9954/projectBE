package org.project.models.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.project.api.controllers.admin.AdminConfig;
import org.project.api.controllers.admin.ConfigValidator;
import org.project.entities.Configs;
import org.project.repositories.ConfigsRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
@RequiredArgsConstructor
public class ConfigSaveService {
    private final ConfigsRepository repository;
    private final ConfigValidator validator;

    public void save(AdminConfig config, Errors errors) {
        validator.validate(config, errors);

        if (errors.hasErrors()) {
            return;
        }
    }

    public <T> void save(String code, T value) {
        Configs config = repository.findById(code).orElseGet(Configs::new);
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());

        String json = null;
        try {
            json = om.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        config.setCode(code);
        config.setValue(json);

        repository.saveAndFlush(config);
    }
}
