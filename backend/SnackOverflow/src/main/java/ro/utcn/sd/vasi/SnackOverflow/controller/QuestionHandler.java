package ro.utcn.sd.vasi.SnackOverflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ro.utcn.sd.vasi.SnackOverflow.model.*;
import ro.utcn.sd.vasi.SnackOverflow.services.AnswerManagementService;
import ro.utcn.sd.vasi.SnackOverflow.services.QuestionManagementService;
import ro.utcn.sd.vasi.SnackOverflow.services.UserManagementService;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Profile("!test")
public class QuestionHandler extends CommandHandler{
    public QuestionHandler(ConsoleController consoleController, AnswerManagementService answerManagementService, QuestionManagementService questionManagementService, UserManagementService userManagementService) {
        super(consoleController, answerManagementService, questionManagementService, userManagementService);
    }

    @Override
    public boolean handleCommand(String command) {
        switch (command) {
            case "list questions":
                handleListQuestions();
                return true;
            case "add question":
                handleAddQuestion();
                return true;
            case "filter by tag":
                handleFilterByTag();
                return true;
            case "filter by title":
                handleFilterByTitle();
                return true;
            case "see question":
                handleSeeQuestionAnswers();
                return true;
            case "update question":
                handleUpdateQuestion();
                return true;
            case "delete question":
                handleDeleteQuestion();
                return true;
            default:
                return false;
        }
    }

    private void handleListQuestions() {
        List<Question> questions = questionManagementService.listQuestions();
        questionManagementService.listQuestions().forEach(x->{
            UserData user = userManagementService.getUserData(x.getAuthorId()).orElse(null);
            print(x.toStringNoText() + " author: " + user.getUsername() + " score: " + user.getScore());});
    }

    private void handleAddQuestion() {
        print("Enter question title");
        String title = scanner.nextLine().trim();

        print("Enter question text");
        String text = scanner.nextLine().trim();

        print("Existing tags: " + questionManagementService.listAllTags());
        print("New tags will be automatically added");
        print("Please input the tags, comma separated");
        String tagStr = scanner.nextLine().trim();
        Set<Tag> tags = new TreeSet<>(Arrays.asList(tagStr.toLowerCase().split(",")).stream().map(x->new Tag(x)).collect(Collectors.toList()));
        tags.removeIf(x->x.getName().equals(""));
        questionManagementService.addQuestion(currentUser.getId(),title,text,tags);
        print("Question successfully added");
    }

    private void handleFilterByTag() {
        print("Existing tags: " + questionManagementService.listAllTags());
        print("Please input the tags, comma separated");
        String tagStr = scanner.nextLine().trim();
        Set<Tag> tags = new TreeSet<>(Arrays.asList(tagStr.toLowerCase().split(",")).stream().map(x->new Tag(x)).collect(Collectors.toList()));

        List<Question> questions = questionManagementService.filterQuestionsByTag(tags);

        questions.forEach(x->{
            UserData user = userManagementService.getUserData(x.getAuthorId()).orElse(null);
            print(x.toStringNoText() + " author: " + user.getUsername() + " score: " + user.getScore());});

        if(questions.isEmpty()) print("No questions");
    }

    private void handleFilterByTitle() {
        print("Please input the title");
        String title = scanner.nextLine().trim();

        List<Question> questions = questionManagementService.filterQuestionsByTitle(title);
        questions.forEach(x->{
            UserData user = userManagementService.getUserData(x.getAuthorId()).orElse(null);
            print(x.toStringNoText() + " author: " + user.getUsername() + " score: " + user.getScore());});

        if(questions.isEmpty()) print("No questions");
    }

    private void handleSeeQuestionAnswers() {
        print("Enter question id");
        int id = scanner.nextInt();
        scanner.nextLine();

        Question question = questionManagementService.getQuestion(id);
        UserData userQ = userManagementService.getUserData(question.getAuthorId()).orElse(null);
        print(question.toString() + " author: " + userQ.getUsername() + " score: " + userQ.getScore());

        List<Answer> ans = answerManagementService.listAnswersForQuestion(id);
        if(ans.isEmpty()) print("No answers");
        else ans.forEach(x->{
            UserData user = userManagementService.getUserData(x.getAuthorId()).orElse(null);
            print(x.toString() + " author: " + user.getUsername() + " score: " + user.getScore());});
    }

    private void handleUpdateQuestion() {
        print("Enter question id");
        int questionId = scanner.nextInt();
        scanner.nextLine();

        print("Enter new question title");
        String newTitle = scanner.nextLine().trim();

        print("Enter new question text");
        String newText = scanner.nextLine().trim();

        questionManagementService.updateQuestion(currentUser.getId(),questionId,newTitle,newText);
    }

    private void handleDeleteQuestion() {
        print("Enter question id");
        int questionId = scanner.nextInt();
        scanner.nextLine();

        questionManagementService.deleteQuestion(currentUser.getId(),questionId);
    }
}
