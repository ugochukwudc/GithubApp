package com.example.myapplication.models


import com.google.gson.annotations.SerializedName

/**
 * {
 *  "login": "octocat",
 *  "id": 583231,
 *  "node_id": "MDQ6VXNlcjU4MzIzMQ==",
 *  "avatar_url": "https://avatars3.githubusercontent.com/u/583231?v=4",
 *  "gravatar_id": "",
 *  "url": "https://api.github.com/users/octocat",
 *  "html_url": "https://github.com/octocat",
 *  "followers_url": "https://api.github.com/users/octocat/followers",
 *  "following_url": "https://api.github.com/users/octocat/following{/other_user}",
 *  "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
 *  "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
 *  "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
 *  "organizations_url": "https://api.github.com/users/octocat/orgs",
 *  "repos_url": "https://api.github.com/users/octocat/repos",
 *  "events_url": "https://api.github.com/users/octocat/events{/privacy}",
 *  "received_events_url": "https://api.github.com/users/octocat/received_events",
 *  "type": "User",
 *  "site_admin": false,
 *  "name": "The Octocat",
 *  "company": "GitHub",
 *  "blog": "http://www.github.com/blog",
 *  "location": "San Francisco",
 *  "email": null,
 *  "hireable": null,
 *  "bio": null,
 *  "public_repos": 8,
 *  "public_gists": 8,
 *  "followers": 2794,
 *  "following": 9,
 *  "created_at": "2011-01-25T18:44:36Z",
 *  "updated_at": "2019-10-10T13:13:34Z"
    }
 */
data class UserGson(
    @SerializedName("login")
    var login: String? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("node_id")
    var nodeId: String? = null,
    @SerializedName("avatar_url")
    var avatarUrl: String? = null,
    @SerializedName("gravatar_id")
    var gravatarId: String? = null,
    @SerializedName("url")
    var url: String? = null,
    @SerializedName("html_url")
    var htmlUrl: String? = null,
    @SerializedName("followers_url")
    var followersUrl: String? = null,
    @SerializedName("following_url")
    var followingUrl: String? = null,
    @SerializedName("gists_url")
    var gistsUrl: String? = null,
    @SerializedName("starred_url")
    var starredUrl: String? = null,
    @SerializedName("subscriptions_url")
    var subscriptionsUrl: String? = null,
    @SerializedName("organizations_url")
    var organizationsUrl: String? = null,
    @SerializedName("repos_url")
    var reposUrl: String? = null,
    @SerializedName("events_url")
    var eventsUrl: String? = null,
    @SerializedName("received_events_url")
    var receivedEventsUrl: String? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("site_admin")
    var siteAdmin: Boolean? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("company")
    var company: Any? = null,
    @SerializedName("blog")
    var blog: String? = null,
    @SerializedName("location")
    var location: String? = null,
    @SerializedName("email")
    var email: Any? = null,
    @SerializedName("hireable")
    var hireable: Boolean? = null,
    @SerializedName("bio")
    var bio: String? = null,
    @SerializedName("public_repos")
    var publicRepos: Int? = null,
    @SerializedName("public_gists")
    var publicGists: Int? = null,
    @SerializedName("followers")
    var followers: Int? = null,
    @SerializedName("following")
    var following: Int? = null,
    @SerializedName("created_at")
    var createdAt: String? = null,
    @SerializedName("updated_at")
    var updatedAt: String? = null
)