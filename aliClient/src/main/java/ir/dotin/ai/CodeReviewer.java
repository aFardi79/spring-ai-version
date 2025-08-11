package ir.dotin.ai;

import ir.dotin.deepseek.DeepSeekClient;
import org.springframework.stereotype.Component;

@Component
public class CodeReviewer {

    private final DeepSeekClient deepSeekClient;

    public CodeReviewer(DeepSeekClient deepSeekClient) {
        this.deepSeekClient = deepSeekClient;
    }

    public String review(String title, String description, String diff) {
        String prompt = String.format("""
            As a senior software engineer, your task is to review a pull request. Based on the provided information, decide if the pull request should be approved or rejected.

            **Pull Request Information:**
            *   **Title:** %s
            *   **Description:** %s

            **Code Diff:**
            ```diff
            %s
            ```

            **Review Instructions:**
            1.  Analyze the code changes for quality, best practices, potential bugs, and adherence to project standards.
            2.  Consider the context provided by the pull request title and description.
            3.  Provide a final decision: `APPROVE` or `REJECT`.
            4.  Provide a concise summary of your reasoning.
            5.  If you suggest rejection or have specific feedback, provide clear, actionable comments.

            **Output Format:**
            DECISION: [APPROVE/REJECT]
            REASON: [Your summary here]
            COMMENTS:
            [Your detailed comments here, if any]
            """, title, description, diff);

        return deepSeekClient.call(prompt);
    }
}
