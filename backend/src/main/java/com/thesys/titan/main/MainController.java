package com.thesys.titan.main;

import org.springframework.web.bind.annotation.RestController;

import com.thesys.titan.config.VaultConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MainController {

    private final VaultConfig vaultConfig;

    @GetMapping("/api/hello")
    public String hello() {
        log.debug("Hello endpoint was called");
        log.debug("Vault Config: {}", vaultConfig.getUserPwd());
        return "Hello, World!";
    }

}
