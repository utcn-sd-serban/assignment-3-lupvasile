import {EventEmitter} from "events";
import WebSocketListener from "../ws/WebSocketListener";
import RestClientFactory from "../rest/RestClientFactory";

const makeUser = (id, username, password, score, isModerator, isBlocked) => ({
    id, username, password, score, isModerator, isBlocked
})

const makeQuestion = (id, author, title, text, creationDateTime, tags, voteCount) => ({
    id, author, title, text, creationDateTime, tags, voteCount
})

const makeAnswer = (id, author, text, creationDateTime, questionId, voteCount) => ({
    id, author, text, creationDateTime, questionId, voteCount
})

class Model extends EventEmitter {
    constructor() {
        super();
        var localUsers = [makeUser(1, "u1", "pass", 0, false, false),
            makeUser(2, "u2", "pass", 0, true, false),
            makeUser(3, "u3", "pass", 0, false, true)];

        var currUserInd = 1;
        this.client = new RestClientFactory(localUsers[currUserInd].username, localUsers[currUserInd].password);
        this.listener = new WebSocketListener(localUsers[currUserInd].username, localUsers[currUserInd].password);
        this.listener.on("event",e => webSocketListener(e));

        this.state = {
            users: localUsers,

            currentUser: localUsers[1],

            loginUser: {
                username: "",
                password: ""
            },

            questions: [],

            newQuestion: {
                title: "",
                text: "",
                tagsAsString: ""
            },

            updateQuestion: {
                title: "",
                text: "",
            },

            tags: [],

            questionSearchText: "",

            answers: [],

            newAnswer: {text: ""},

            updateAnswer: {text: ""}
        };
    }

    banUser(requesterUserId, bannedUserId) {
        this.state.users.find(u => u.id === bannedUserId).isBlocked = true;
        this.emit("change", this.state);
    }

    listAnswersForQuestion(questionId) {
        return this.state.answers.filter(a => a.questionId === questionId);
    }

    getAnswer(answerId) {
        return this.state.answers.find(a => a.id === answerId);
    }

    sendVoteAnswerToLocalState(userId, answerId, voteType) {
        var answer = this.getAnswer(answerId);
        if (voteType) {
            answer.voteCount++;
            answer.author.score++;
            this.state.users.find(u => u.id === userId).score++;
        } else {
            answer.voteCount--;
            answer.author.score--;
            this.state.users.find(u => u.id === userId).score--;
        }

        this.emit("change", this.state);
    }

    addAnswerToLocalState(id, author, text, questionId) {
        this.state = {
            ...this.state,
            answers: [makeAnswer(id, author, text, "04/05/29", questionId, 0)].concat(this.state.answers)
        };

        this.emit("change", this.state);
    }

    updateAnswerTextToLocalState(answerId, newText) {
        var answer = this.getAnswer(answerId);
        if (answer === undefined) {
            return;
        }

        answer.text = newText;

        this.emit("change", this.state);
    }

    deleteAnswerToLocalState(answerId) {
        this.state = {
            ...this.state,
            answers: this.state.answers.filter(a => a.id !== answerId),
        };

        this.emit("change", this.state);
    }

    changeNewAnswerProperty(property, value) {
        this.state = {
            ...this.state,
            newAnswer: {
                ...this.state.newAnswer,
                [property]: value
            }
        };
        this.emit("change", this.state);
    }

    prepareAnswerForUpdate(answerId) {
        var answer = this.getAnswer(answerId);
        this.state.updateAnswer.text = answer.text;

        this.emit("change", this.state);
    }

    changeUpdateAnswerProperty(property, value) {
        this.state = {
            ...this.state,
            updateAnswer: {
                ...this.state.updateAnswer,
                [property]: value
            }
        };
        this.emit("change", this.state);
    }

    filterQuestionByTagCommaSeparatedInLocalState(tagText) {
        var tags = tagText.trim().split(',');
        return this.filterQuestionsByTag(tags);
    }

    filterQuestionsByTagInLocalState(tags) {
        return this.state.questions.filter(q => tags.every(t => q.tags.includes(t)));
    }

    filterQuestionsByTitleInLocalState(title) {
        return this.state.questions.filter(q => q.title.includes(title));
    }

    getQuestion(id) {
        var res = this.state.questions.find(q => q.id === id);
        return res;
    }

    addQuestionToLocalState(id, author, title, text, tags) {
        this.state = {
            ...this.state,
            questions: [makeQuestion(id, author, title, text, "04/05/29", tags, 0)].concat(this.state.questions)
        };

        /*this.state = {
            ...this.state,
            tags: this.makeTagsList()
        };*/
        this.emit("change", this.state);
    }

    updateQuestionToLocalState(questionId, newTitle, newText) {
        var question = this.getQuestion(questionId);
        if (question === undefined) {
            return;
        }

        question.title = newTitle;
        question.text = newText;

        this.emit("change", this.state);
        ///this does not work???
    }

    prepareQuestionForUpdate(questionId) {
        var question = this.getQuestion(questionId);
        this.state.updateQuestion.title = question.title;
        this.state.updateQuestion.text = question.text;

        this.emit("change", this.state);
    }

    deleteQuestionToLocalState(questionId) {
        this.state = {
            ...this.state,
            questions: this.state.questions.filter(q => q.id !== questionId),
        };
        this.emit("change", this.state);
    }

    sendVoteQuestionToLocalState(userId, questionId, voteType) {
        var question = this.getQuestion(questionId);
        if (voteType) {
            question.voteCount++;
            question.author.score++;
            this.state.users.find(u => u.id === userId).score++;
        } else {
            question.voteCount--;
            question.author.score--;
            this.state.users.find(u => u.id === userId).score--;
        }

        this.emit("change", this.state);
    }

    changeNewQuestionProperty(property, value) {
        this.state = {
            ...this.state,
            newQuestion: {
                ...this.state.newQuestion,
                [property]: value
            }
        };
        this.emit("change", this.state);
    }

    changeUpdateQuestionProperty(property, value) {
        this.state = {
            ...this.state,
            updateQuestion: {
                ...this.state.updateQuestion,
                [property]: value
            }
        };
        this.emit("change", this.state);
    }

    changeModelProperty(property, value) {
        this.state = {
            ...this.state,
            [property]: value
        };
        this.emit("change", this.state);
    }

    makeTagsList() {
        var allTags = this.state.tags.concat.apply([], this.state.questions.map(q => q.tags));
        return Array.from(new Set(allTags)).sort();
    }

    makeLogin(username, password) {
        this.client = new RestClientFactory(username, password);
        return this.client.createLoginClient().loadCurrentLoggedUser().then(user => {
            if (!user) return false;

            this.listener = new WebSocketListener(username,password);
            this.listener.on("event", event => webSocketListener(event));
            this.state = {
                ...this.state,
                currentUser: user
            };

            this.emit("change", this.state);

            return true;
        });
    }

    changeLoginProperty(property, value) {
        this.state = {
            ...this.state,
            loginUser: {
                ...this.state.loginUser,
                [property]: value
            }
        };
        this.emit("change", this.state);
    }
}

const model = new Model();

function webSocketListener(event) {
    {
        console.log(event.type.toString());
        if (event.type === "STUDENT_CREATED") {
            model.appendStudent(event.student);
        }
    }
}

export default model;
export {makeQuestion};