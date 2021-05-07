package com.example.tvmazesample

open class ShowMain {

    var id: Int? = null
    var url: String? = null
    var name: String? = null
    var season: Int? = null
    var number: Int? = null
    var type: String? = null
    var airdate: String? = null
    var airtime: String? = null
    var airstamp: String? = null
    var runtime: Int? = null
    //var image: Int? = null
    var summary: String? = null
    var show: Show? = null
    var _links: Links? = null

    data class Show(
        var id: Int?,
        var url: String?,
        var name: String?,
        var type: String?,
        var language: String?,
        var genres: ArrayList<String>?,
        var status: String?,
        var runtime: Int?,
        var premiered: String?,
        var officialSite: String?,
        var schedule: Schedule?,
        var rating: Rating?,
        var weight: Int?,
        var network:Network?,
        var webChannel: WebChannel?,
        var dvdCountry:String?,
        var externals:Externals?,
        var image: Image?,
        var summary: String?,
        var updated: Int?,
        var _links: Links?
        )
    data class Schedule(var time:String?, var days: ArrayList<String>?)
    data class Rating(var average: Float?)
    data class Network(var id:Int?, var name:String?,var country: Country)
    data class Country(var name:String?, var code:String?, var timezone:String?)
    data class WebChannel(var id: Int?, var name: String?, var country: Country?)
    data class Externals(var tvrage:Int?, var thetvdb:Int?, var imdb: String?)
    data class Image(var original: String?, var medium: String?)
    data class Links(var self:Self?, var previousepisode:PreviousEpisode?, var nextepisode: NextEpisode?)
    data class Self(var href:String?)
    data class PreviousEpisode(var href:String?)
    data class NextEpisode(var href: String?)
}