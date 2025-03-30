package com.example.testee.controller.pageapi;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
public class PageApiController {

    private static final Logger logger = LoggerFactory.getLogger(PageApiController.class);
    private final RestTemplate restTemplate;

    @Autowired
    public PageApiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/pageapi")
    public ResponseEntity<String> fetchGithubRepos() {
        String githubApiUrl = "https://api.github.com/users/andersoncsgo/repos";
        logger.info("Requisição recebida em /pageapi. Chamando API do GitHub: {}", githubApiUrl);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(githubApiUrl, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                logger.info("Sucesso ao buscar dados do GitHub via /pageapi.");
                return ResponseEntity.ok(response.getBody());
            } else {
                logger.warn("API do GitHub retornou status não OK para /pageapi: {}", response.getStatusCode());
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }

        } catch (RestClientException e) {
            logger.error("Erro ao chamar a API do GitHub via /pageapi: {}", githubApiUrl, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao contatar a API do GitHub: " + e.getMessage());
        }
    }
}