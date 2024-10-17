package com.Digi.FirstSpring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class JiraController {

    @Autowired
    private JiraService jiraService;

    @GetMapping("/jira/historico/{issueKey}")
    public Set<String> obterHistorico(@PathVariable String issueKey) {
        return jiraService.obterHistoricoChamado(issueKey);
    }
}

