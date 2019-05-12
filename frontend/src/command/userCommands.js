import userModel from "../model/userModel";

class BanUserCommand {
    constructor(bannedUserId) {
        this.bannedUserId = bannedUserId;
    }

    execute() {
        return userModel.banUser(this.bannedUserId);
    }
}

export {BanUserCommand};
