import React from "react";
import NavBar from './user/NavBar'
import QuestionSummary from "./question/QuestionSummary";
import QuestionList from "./question/QuestionList";
import AddQuestion from "./question/AddQuestion";
import AnswerDetails from "./answer/AnswerDetails";
import AnswerList from "./answer/AnswerList";

const makeQuestion = (id, author, title, text, creationDateTime, tags, voteCount) => ({
    id, author, title, text, creationDateTime, tags, voteCount
})

const makeAnswer = (id, author, text, creationDateTime, questionId, voteCount) => ({
    id, author, text, creationDateTime, questionId, voteCount
})

const question = makeQuestion(1, { id: 1, username: "u1", password: "asdf", score: 11 }, "question 1", "ana are mere multe1",
    "02/02/02", ["tag1", "tag2", "tag3"], 5)

const answer = makeAnswer(1, { id: 1, username: "u1", password: "asdf", score: 11 }, "answer 1", "02/02/02", 1, 0)
const questions = [question, question, question]
const answers = [answer, answer, answer]
const Test = () => (

    <AnswerList answers={answers}
        loggedUser={{ id: 1, username: "u1", password: "asdf", score: 11, isModerator: false }} />
);

export default Test;