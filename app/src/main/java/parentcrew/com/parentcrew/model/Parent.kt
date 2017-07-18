package parentcrew.com.parentcrew.model


class Parent {

    var uid: String? = null
    var name: String? = null
    var location: String? = null
    var lookingFor: String? = null
    var interest: String? = null
    var profilePic: String? = null
    var kid: Kid? = null

    constructor(uid: String, name: String, location: String, lookingFor: String, interest: String, profilePic: String, kid: Kid) {
        this.uid = uid
        this.name = name
        this.location = location
        this.lookingFor = lookingFor
        this.interest = interest
        this.profilePic = profilePic
        this.kid = kid
    }

    constructor() {}
}
