package ro.utcn.sd.vasi.SnackOverflow.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.utcn.sd.vasi.SnackOverflow.dto.UserLoggedInDTO;
import ro.utcn.sd.vasi.SnackOverflow.services.UserManagementService;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserManagementService userManagementService;

    @GetMapping("/me")
    public UserLoggedInDTO readCurrentUser() {
        return UserLoggedInDTO.ofEntity(userManagementService.loadCurrentUser());
    }
}
