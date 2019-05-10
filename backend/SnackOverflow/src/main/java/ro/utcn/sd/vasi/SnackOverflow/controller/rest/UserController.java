package ro.utcn.sd.vasi.SnackOverflow.controller.rest;

import jdk.internal.instrumentation.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.utcn.sd.vasi.SnackOverflow.dto.UserLoggedInDTO;
import ro.utcn.sd.vasi.SnackOverflow.event.BaseEvent;
import ro.utcn.sd.vasi.SnackOverflow.services.UserManagementService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserManagementService userManagementService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/me")
    public UserLoggedInDTO readCurrentUser() {
        return UserLoggedInDTO.ofEntity(userManagementService.loadCurrentUser());
    }

    @EventListener(BaseEvent.class)
    public void handleEvent(BaseEvent event) {
        log.info("Got an event: {}.", event);
        messagingTemplate.convertAndSend("/topic/events", event);
    }
}
