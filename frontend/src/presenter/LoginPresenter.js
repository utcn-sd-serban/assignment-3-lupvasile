import model from "../model/model";

class LoginPresenter {
    onLogin() {

        if (!model.makeLogin(model.state.loginUser.username, model.state.loginUser.password)) {
            window.location.assign("#/")
        } else {
            if (model.state.currentUser.isBlocked) {
                window.location.assign("#/banned-user")
            } else {
                window.location.assign("#/all-questions")
            }
        }
    }

    onChange(property, value) {
        model.changeLoginProperty(property, value);
    }
}

const loginPresenter = new LoginPresenter();

export default loginPresenter;