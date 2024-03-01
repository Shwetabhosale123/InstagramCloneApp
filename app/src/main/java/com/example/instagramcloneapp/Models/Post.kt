package com.example.instagramcloneapp.Models

class Post {
    var postUrl:String=""
    var caption:String=""
    var Uid:String=""
    var time:String=""
    constructor()
    constructor(postUrl: String, caption: String) {
        this.postUrl = postUrl
        this.caption = caption
    }

    constructor(postUrl: String, caption: String, Uid: String, time: String) {
        this.postUrl = postUrl
        this.caption = caption
        this.Uid = Uid
        this.time = time
    }

}