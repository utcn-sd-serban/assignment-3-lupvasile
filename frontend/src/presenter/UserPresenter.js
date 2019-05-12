import model from "../model/model";
import userModel from "../model/userModel";

class UserPresenter {
    onBan(bannedUserId) {
        userModel.banUser(bannedUserId);
    }

    onInit() {
        userModel.loadAllUsers();
    }
}

const userPresenter = new UserPresenter();

export default userPresenter;