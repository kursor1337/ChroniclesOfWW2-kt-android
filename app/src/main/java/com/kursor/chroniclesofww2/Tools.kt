package com.kursor.chroniclesofww2

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.model.DivisionResources
import com.kursor.chroniclesofww2.model.Nation
import com.kursor.chroniclesofww2.model.Scenario
import com.kursor.chroniclesofww2.model.board.Division
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

object Tools {

    private const val PREF = "pref"
    private const val COMPLETED_MISSIONS = "completed missions"
    private const val USERNAME = "username"
    private const val SCORE = "score"

    val GSON = Gson()
    var currentConnection: Connection? = null

    private lateinit var sharedPreferences: SharedPreferences
    lateinit var completedScenarios: MutableList<String>
        private set
    lateinit var username: String
        private set
    var score: Int = 0
        private set

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val completedMissionsJson = sharedPreferences.getString(COMPLETED_MISSIONS, "")
        completedScenarios = if (completedMissionsJson.isNullOrBlank()) mutableListOf()
        else GSON.fromJson(completedMissionsJson, object : TypeToken<MutableList<String>>() {}.type)
        username = sharedPreferences.getString(USERNAME, "player") ?: "player"
        score = sharedPreferences.getInt(SCORE, 0)
    }

    fun completedScenario(scenario: Scenario) {
        completedScenarios += scenario.name
    }

    fun getScreenWidth() = Resources.getSystem().displayMetrics.widthPixels

    fun getScreenHeight() = Resources.getSystem().displayMetrics.heightPixels


    fun getSelfIpAddress(): String {
        var selfIp = ""
        try {
            val enumNetworkInterfaces: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (enumNetworkInterfaces.hasMoreElements()) {
                val networkInterface: NetworkInterface = enumNetworkInterfaces.nextElement()
                val enumInetAddress: Enumeration<InetAddress> = networkInterface.inetAddresses
                while (enumInetAddress.hasMoreElements()) {
                    val inetAddress: InetAddress = enumInetAddress
                        .nextElement()
                    if (inetAddress.isSiteLocalAddress) {
                        selfIp = inetAddress.hostAddress
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
            Log.e("GET_IP", "IP NOT FOUND")
        }
        return selfIp
    }

    fun validIP(ip: String?): Boolean {
        return if (ip == null || ip.length < 7 || ip.length > 15) false
        else try {
            var x = 0
            var y = ip.indexOf('.')
            if (y == -1 || ip[x] == '-' || ip.substring(x, y).toInt() > 255) return false
            x = ip.indexOf('.', ++y)
            if (x == -1 || ip[y] == '-' || ip.substring(y, x).toInt() > 255) return false
            y = ip.indexOf('.', ++x)
            !(y == -1 || ip[x] == '-' || ip.substring(x, y).toInt() > 255 || ip[++y] == '-' || ip.substring(y, ip.length).toInt() > 255 || ip[ip.length - 1] == '.')
        } catch (e: Exception) {
            false
        }
    }


//    class StandardScenarios {
//
//        private val missionList: MutableList<Scenario> = mutableListOf()
//        private var counter = 0
//        private val DEFAULT_MISSION_NAME = "Default"
//        private lateinit var missionNames: Array<String>
//        private lateinit var mission_intros: Array<String>
//        private var missionsCreated = false
//
//        private val DEFAULT_MISSION: Scenario = Scenario(
//            DEFAULT_MISSION_NAME, "",
//            Nation.DEFAULT, DivisionResources(mapOf(
//                Division.Type.INFANTRY to 1,
//                Division.Type.ARMORED to 1,
//                Division.Type.ARTILLERY to 1
//            ), )
//        )
//
//        fun initMissions(context: Context) {
//            if (missionsCreated) return
//            missionNames = context.resources.getStringArray(R.array.missions)
//            createNewMission(
//                Nation.BRITAIN, 6, 3, 2,
//                Nation.GERMANY, 6, 3, 2
//            )
//            createNewMission(
//                Nation.GERMANY, 6, 4, 2,
//                Nation.FRANCE, 8, 2, 4
//            )
//            createNewMission(
//                Nation.GERMANY, 6, 4, 4,
//                Nation.USSR, 8, 2, 4
//            )
//            createNewMission(
//                Nation.JAPAN, 10, 0, 0,
//                Nation.USA, 6, 0, 4
//            )
//            createNewMission(
//                Nation.ITALY, 6, 4, 4,
//                Nation.BRITAIN, 8, 2, 4
//            )
//            createNewMission(
//                Nation.GERMANY, 6, 4, 4,
//                Nation.USA, 8, 2, 4
//            )
//            missionsCreated = true
//        }
//    }


}
