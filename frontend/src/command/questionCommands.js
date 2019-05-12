import questionModel from "../model/questionModel";

class AddQuestionCommand {
    constructor(title, text, tags) {
        this.title = title;
        this.text = text;
        this.tags = tags;
    }

    execute() {
        return questionModel.addQuestion(this.title, this.text, this.tags);
    }
}

class UpdateQuestionCommand {
    constructor(questionId, newTitle, newText) {
        this.questionId = questionId;
        this.newTitle = newTitle;
        this.newText = newText;
    }

    execute() {
        return questionModel.updateQuestion(this.questionId, this.newTitle, this.newText);
    }
}


class DeleteQuestionCommand {
    constructor(questionId) {
        this.questionId = questionId;
    }

    execute() {
        return questionModel.deleteQuestion(this.questionId);
    }
}

class VoteQuestionCommand {
    constructor(questionId, voteValue) {
        this.questionId = questionId;
        this.voteValue = voteValue;
    }

    execute() {
        return questionModel.voteQuestion(this.questionId, this.voteValue);
    }
}

export {AddQuestionCommand, DeleteQuestionCommand, UpdateQuestionCommand, VoteQuestionCommand};