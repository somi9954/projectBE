package org.project.api.controllers.admin;


import ch.qos.logback.core.joran.conditional.IfAction;
import lombok.RequiredArgsConstructor;
import org.project.repositories.ConfigsRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 사이트 설정 유효성 검사
 */

@Component
@RequiredArgsConstructor
public class ConfigValidator implements Validator {

    private ConfigsRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AdminConfig.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AdminConfig config = (AdminConfig) target;

        String siteTitle = config.getSiteTitle();
        String siteDescription = config.getSiteDescription();
        String joinTerms = config.getJoinTerms();


        if (siteTitle != null && siteTitle.isBlank()) {
            errors.rejectValue("siteTitle", "NotBlank");
        }
        if (siteDescription != null && siteDescription.isBlank()) {
            errors.rejectValue("siteDescription", "NotBlank");
        }
        if (joinTerms != null && joinTerms.isBlank()) {
            errors.rejectValue("joinTerms", "NotBlank");
        }
    }
}
