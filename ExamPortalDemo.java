import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class ExamException extends Exception {
    ExamException(String message) {
        super(message);
    }

    ExamException(String message, Throwable cause) {
        super(message, cause);
    }
}

class InvalidSubmissionException extends ExamException {
    InvalidSubmissionException(String message) {
        super(message);
    }
}

class SubmissionStorageException extends ExamException {
    SubmissionStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

class SubmissionStorage {
    void save(String fileName, String answer)
            throws SubmissionStorageException {
        try {
            Files.writeString(Path.of(fileName), answer);
        } catch (IOException e) {
            // TODO 1 completed: wrap IOException and preserve its cause.
            throw new SubmissionStorageException(
                    "Unable to store examination submission.", e);
        }
    }
}

class ExamService {
    private final SubmissionStorage storage = new SubmissionStorage();

    void submit(String studentId, String answer) throws ExamException {
        if (studentId == null || studentId.isBlank()
                || answer == null || answer.isBlank()) {
            throw new InvalidSubmissionException(
                    "Student ID and answer are required.");
        }
        storage.save("submission-" + studentId + ".txt", answer);
    }
}

public class ExamPortalDemo {
    public static void main(String[] args) {
        ExamService service = new ExamService();

        try {
            service.submit("192521054C",
                    "Java exception handling answer");
            System.out.println("Submission saved successfully.");
        } catch (ExamException e) {
            System.out.println("Submission failed: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Root cause: "
                        + e.getCause().getClass().getSimpleName());
            }
        }
    }
}
