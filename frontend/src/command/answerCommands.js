import answerModel from "../model/answerModel";

class AddAnswerCommand {
    constructor(text,questionId) {
        this.text = text;
        this.questionId = questionId;
    }

    execute() {
        return answerModel.addAnswer(this.text, this.questionId);
    }
}

class UpdateAnswerCommand {
    constructor(answerId, newText) {
        this.answerId = answerId;
        this.newText = newText;
    }

    execute() {
        return answerModel.updateAnswer(this.answerId, this.newText);
    }
}

class DeleteAnswerCommand {
    constructor(answerId) {
        this.answerId = answerId;
    }

    execute() {
        return answerModel.deleteAnswer(this.answerId);
    }
}

class VoteAnswerCommand {
    constructor(answerId, voteValue) {
        this.answerId = answerId;
        this.voteValue = voteValue;
    }

    execute() {
        return answerModel.voteAnswer(this.answerId, this.voteValue);
    }
}

export {AddAnswerCommand, DeleteAnswerCommand, UpdateAnswerCommand, VoteAnswerCommand};