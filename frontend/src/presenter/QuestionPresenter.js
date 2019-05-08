import model from "../model/model";

class QuestionPresenter {
    onViewDetails(questionId) {
        window.location.assign('#/question-details/' + questionId)
    }

    onCreate() {
        var newId = Math.max(...model.state.questions.map(q => q.id)) + 1;
        var question = model.state.newQuestion;
        var tags = question.tagsAsString.trim().split(',');
        model.addQuestion(newId, model.state.currentUser, question.title, question.text, tags);
        model.changeNewQuestionProperty("title", "");
        model.changeNewQuestionProperty("text", "");
        model.changeNewQuestionProperty("tagsAsString", "");
        window.location.assign("#/all-questions");
    }

    onChange(property, value) {
        model.changeNewQuestionProperty(property, value);
    }

    onChangeForUpdate(property, value) {
        model.changeUpdateQuestionProperty(property, value);
    }

    onChangeSearchText(value) {
        model.changeModelProperty("questionSearchText", value);
    }

    onSearchByTitle() {
        if (model.state.questionSearchText === "") {
            window.location.assign('#/all-questions/');
        } else {
            window.location.assign('#all-questions/filterByTitle/' + model.state.questionSearchText);
        }
    }

    onSearchByTag() {
        if (model.state.questionSearchText === "") {
            window.location.assign('#/all-questions/');
        } else {
            window.location.assign('#all-questions/filterByTag/' + model.state.questionSearchText);
        }
    }

    onUpdate(questionId) {
        model.updateQuestion(questionId, model.state.updateQuestion.title, model.state.updateQuestion.text);
        model.changeUpdateQuestionProperty("title", "");
        model.changeUpdateQuestionProperty("text", "");
        window.location.assign('#/question-details/' + questionId);
    }

    onDelete(questionId) {
        window.location.assign('#/all-questions/')
        model.deleteQuestion(questionId);
    }

    onEdit(questionId) {
        model.prepareQuestionForUpdate(questionId);
        window.location.assign("#/edit-question/" + questionId);
    }

    onVote(questionId, vote) {
        if (vote > 0) {
            model.sendVote(model.state.currentUser.id, questionId, true);
        } else {
            model.sendVote(model.state.currentUser.id, questionId, false);
        }
    }
}

const questionPresenter = new QuestionPresenter();

export default questionPresenter;