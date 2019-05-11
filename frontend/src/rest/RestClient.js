const BASE_URL = "http://localhost:8080";

export default class RestClient {
    constructor(username, password) {
        this.authorization = "Basic " + btoa(username + ":" + password);
    }

    asdf(user) {
        return fetch(BASE_URL + "/me2", {
            method: "POST",
            body: JSON.stringify(user),
            headers: {
                "Authorization": this.authorization,
                "Content-Type": "application/json"
            }
        }).then(response => response.json());
    }

    loadCurrentLoggedUser() {
        return fetch(BASE_URL + "/me", {
            method: "GET",
            headers: {
                "Authorization": this.authorization
            }
        }).then(response => {
            if (!response.ok) return false;
            else return response.json();
        })
    }

    loadAllStudents() {
        return fetch(BASE_URL + "/students", {
            method: "GET",
            headers: {
                "Authorization": this.authorization
            }
        }).then(response => response.json());
    }

    createStudent(firstName, lastName) {
        return fetch(BASE_URL + "/students", {
            method: "POST",
            body: JSON.stringify({
                firstName: firstName,
                lastName: lastName
            }),
            headers: {
                "Authorization": this.authorization,
                "Content-Type": "application/json"
            }
        }).then(response => response.json());
    }
}