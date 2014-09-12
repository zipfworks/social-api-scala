package com.github.kfang

import com.github.kfang.facebook.FacebookAPI
import com.github.kfang.google.{PeopleCollectionList, GoogleAuthScope, GoogleAPI}
import com.github.kfang.instagram.{IGAuthScope, InstagramAPI}
import com.typesafe.config.ConfigFactory
import spray.json._
import scala.util.Random

object ExampleFlows {

  def runInstagram(args: Array[String]): Unit = {

    //create the client
    val client = new InstagramAPI(ConfigFactory.load)
    println(client.requestExplicitUrl(IGAuthScope.Basic, IGAuthScope.Relationships))

    //read down the code
    val code = Console.readLine("code: ")

    //get the access token
    val accessToken = client.requestAccessToken(code).access_token
    println("access_token: " + accessToken)

    //get some user info
    val u = client.usersService(accessToken)
    println("UserInfo:\n" + u.getInfo.toJson.prettyPrint)

    //get some follows
    val r = client.relationshipsService(accessToken)
    println("Relationships:\n" + r.getFollows.toJson.prettyPrint)

  }

  def runGoogle(args: Array[String]): Unit = {

    //create the client
    val client = new GoogleAPI(ConfigFactory.load)

    //get code
    val state = Random.alphanumeric.take(10).toList.mkString
    println(client.getAccountsOauth2URL(state, GoogleAuthScope.PlusLogin))
    println("make sure the state sent back is: " + state)

    //get access token
    val code = Console.readLine("code: ")
    val accessToken = client.requestAccessToken(code).access_token


    //get user info
    val u = client.usersService(accessToken)
    println("UserInfo:\n" + u.getUserInfo.toJson.prettyPrint)

    //get user friends
    println("Friends:\n" + u.getUserFriends(PeopleCollectionList.Visible).toJson.prettyPrint)

  }

  def runFacebook(args: Array[String]): Unit = {
    //create the client
    val client = new FacebookAPI(ConfigFactory.load)

    //get access token
    val accessToken = Console.readLine("access_token: ")

    //get user info
    val u = client.usersService(accessToken)
    println("UserInfo:\n" + u.getUserInfo.toJson.prettyPrint)

    println("Friends:\n" + u.getUserFriends.toJson.prettyPrint)

  }

}

