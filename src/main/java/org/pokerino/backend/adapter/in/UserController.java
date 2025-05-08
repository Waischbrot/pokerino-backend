package org.pokerino.backend.adapter.in;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/level")
    public ResponseEntity<?> level(@RequestParam String username) {
        return ResponseEntity.ofNullable(0);
    }

}
