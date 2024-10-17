package com.Digi.FirstSpring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashSet;
import java.util.Set;

@Service
public class JiraService {

    @Value("${jira.url}")
    private String jiraUrl;

    @Value("${jira.username}")
    private String username;

    @Value("${jira.api.token}")
    private String apiToken;

    public Set<String> obterHistoricoChamado(String issueKey) {
        String url = UriComponentsBuilder.fromHttpUrl(jiraUrl)
                .path("/rest/api/3/issue/")
                .path(issueKey)
                .queryParam("expand", "changelog")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.setBasicAuth(username, apiToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JiraIssue> response = restTemplate.exchange(url, HttpMethod.GET, entity, JiraIssue.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Set<String> emails = new HashSet<>();
            for (History history : response.getBody().getChangelog().getHistories()) {
                String authorEmail = history.getAuthor().getEmailAddress();
                if (authorEmail != null) {
                    emails.add(authorEmail);
                }
            }
            return emails;
        } else {
            System.err.println("Erro ao obter o histórico do chamado " + issueKey + ": " + response.getStatusCode());
            return null;
        }
    }
}
