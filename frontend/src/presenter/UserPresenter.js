import model from "../model/model";

class UserPresenter {
    onBan(bannedUserId) {
        model.banUser(model.state.currentUser.id, bannedUserId);
    }
}

const userPresenter = new UserPresenter();

export default userPresenter;