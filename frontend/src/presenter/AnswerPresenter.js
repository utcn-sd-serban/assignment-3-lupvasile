import model from "../model/model";

class AnswerPresenter {
    onViewDetails(answerId) {
        window.location.assign('#/answer-details/' + answerId)
    }

    onCreate(questionId) {
        var newId = Math.max(...model.state.answers.map(q => q.id)) + 1;
        var answer = model.state.newAnswer;
        model.addAnswer(newId, model.state.currentUser, answer.text, questionId);
        model.changeNewAnswerProperty("text", "");
    }

    onChange(property, value) {
        model.changeNewAnswerProperty(property, value);
    }

    onChangeForUpdate(property, value) {
        model.changeUpdateAnswerProperty(property, value);
    }

    onUpdate(answerId) {
        model.updateAnswerText(answerId, model.state.updateAnswer.text);
        model.changeUpdateAnswerProperty("text", "");
        window.location.assign('#/question-details/' + model.getAnswer(answerId).questionId);
    }

    onDelete(answerId) {
        model.deleteAnswer(answerId);
    }

    onEdit(answerId) {
        model.prepareAnswerForUpdate(answerId);
        window.location.assign("#/edit-answer/" + answerId);
    }

    onVote(answerId, vote) {
        if (vote > 0) {
            model.sendVoteAnswer(model.state.currentUser.id, answerId, true);
        } else {
            model.sendVoteAnswer(model.state.currentUser.id, answerId, false);
        }
    }
}

const answerPresenter = new AnswerPresenter();

export default answerPresenter;