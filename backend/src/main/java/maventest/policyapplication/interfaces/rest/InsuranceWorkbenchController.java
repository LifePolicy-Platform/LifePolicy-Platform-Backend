package maventest.policyapplication.interfaces.rest;

import lombok.RequiredArgsConstructor;
import maventest.policyapplication.infrastructure.repository.InsuranceApplicationRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/insurance")
public class InsuranceWorkbenchController {

    private final InsuranceApplicationRepository insuranceApplicationRepository;

    @GetMapping("/workbench")
    public String workbench(Model model) {
        model.addAttribute("products", insuranceApplicationRepository.findAllProducts());
        model.addAttribute("quasarAssessment", "Quasar can be embedded by CDN in JSP, but this screen uses JSP + native JavaScript as the primary implementation to avoid framework mismatch and compose/runtime complexity.");
        return "insurance/workbench";
    }
}