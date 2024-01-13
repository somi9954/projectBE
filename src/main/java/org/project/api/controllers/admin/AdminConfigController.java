package org.project.api.controllers.admin;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.commons.Utils;
import org.project.commons.exceptions.BadRequestException;
import org.project.commons.rests.JSONData;
import org.project.models.admin.ConfigInfoService;
import org.project.models.admin.ConfigSaveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/config")
@RequiredArgsConstructor
public class AdminConfigController {
    private final ConfigSaveService saveService;
    private final ConfigInfoService infoService;

    String code = "config";

    @GetMapping
    public ResponseEntity<JSONData> config(Errors errors) {
        try { commonProcess(errors);

        AdminConfig form = infoService.get(code, AdminConfig.class);
        form = form == null ? new AdminConfig() : form;

        JSONData jsonData = new JSONData(form);

        // ResponseEntity로 감싸서 반환
            return ResponseEntity.status(jsonData.getStatus()).body(jsonData);
    } catch (BadRequestException e) {
        // BadRequestException이 발생한 경우
        JSONData errorData = new JSONData();
        errorData.setSuccess(false);
        errorData.setMessage(e.getMessage());
        errorData.setStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity.badRequest().body(errorData);
    }
}


    @PostMapping
    public ResponseEntity<JSONData> config(@RequestBody @Valid AdminConfig form, Errors errors) {

        saveService.save(code, form);

        commonProcess(errors);

        JSONData data = new JSONData();
        data.setStatus(HttpStatus.CREATED);

        return ResponseEntity.status(data.getStatus()).body(data);
    }

    private void commonProcess(Errors errors) {
      if (errors.hasErrors()) {
          throw new BadRequestException(Utils.getMessages(errors));
      }
    }
}
