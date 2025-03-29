package lan.scrooge.api.infrastructure.web._debug;

import lan.scrooge.api.domain.entities.ScroogeUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug-tools")
public class DebugRestController {

  @GetMapping("/me")
  public ResponseEntity<ScroogeUser> getPrincipal(
      @AuthenticationPrincipal ScroogeUser currentUser) {

    return ResponseEntity.ok(currentUser);
  }
}
