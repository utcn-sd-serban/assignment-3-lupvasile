import React from 'react';
import { HashRouter, Route, Switch } from "react-router-dom";
import './App.css';
import BannedUser from './view/BannedUser';
import SmartAddQuestion from './view/SmartAddQuestion';
import SmartLogin from './view/SmartLogin';
import SmartQuestionDetails from './view/SmartQuestionDetails';
import SmartQuestionList from './view/SmartQuestionList';
import SmartUpdateAnswer from './view/SmartUpdateAnswer';
import SmartUpdateQuestion from './view/SmartUpdateQuestion';
import SmartUserList from './view/SmartUserList';



const App = () => (
  <div className="App">
    <HashRouter>
      <Switch>
        <Route exact={true} component={SmartLogin} path="/" />
        <Route exact={true} component={BannedUser} path="/banned-user" />
        <Route exact={true} component={SmartQuestionList} path="/all-questions/:filterType/:filterText" />
        <Route exact={true} component={SmartAddQuestion} path="/add-question" />
        <Route exact={true} component={SmartQuestionDetails} path="/question-details/:questionId" />'
        <Route exact={true} component={SmartUpdateQuestion} path="/edit-question/:questionId" />'
        <Route exact={true} component={SmartUpdateAnswer} path="/edit-answer/:answerId" />'
        <Route exact={true} component={SmartQuestionList} path="/all-questions" />
        <Route exact={true} component={SmartUserList} path="/ban-users" />
      </Switch>
    </HashRouter>
  </div>
);
//pot sa nu clonez questionu manual inainte de update? cand accesez linku prima data la update sa isi ia starea intrabarii? gen update-quesion/questionId
//e mai bine sau mai rau fara displayedQuestionList?
//ar trebui si userul stocal in url?
export default App;
