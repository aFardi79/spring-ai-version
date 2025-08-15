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
            2.  Pay special attention to Hibernate session management:
                   - Detect any occurrence where a Hibernate session is opened using:
                   - HibernateSession <var> = HibernateManager.createSession();
                   - Check if that session is not properly closed in all control-flow paths.
                   - If the session is passed to another method in the same class, verify that the callee (and any further same-class callees) close the session parameter.
                   - try-with-resources counts as closed.
                   - Closing any alias variable of the session counts as closing it.
                   - If a session escapes the class (returned or stored in a field) without being closed, treat it as a leak.
                   - Ignore strings/comments and framework-managed scopes (@Transactional) unless HibernateManager.createSession() is explicitly called.
            3. If such a session leak is found:
                - Extract and output ONLY the raw smallest enclosing method or code block where the session may leak.
                - Do not explain the leak in the code block output.
            4.  Provide your final decision: APPROVE or REJECT.
            5.  Provide a concise summary of your reasoning.
            6.  If you reject or have feedback, provide clear, actionable comments.

            **Output Format:**
            DECISION: [APPROVE/REJECT]
            REASON: [Your summary here]
            COMMENTS:
            [Your detailed comments here, if any]
            """, title, description, diff);

        return deepSeekClient.call(prompt);
    }
}
