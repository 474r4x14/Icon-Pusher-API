package com.iconpusher.api

import java.security.MessageDigest
import kotlin.math.floor
//import javax.xml.bind.DatatypeConverter

object Misc {


    fun tokenFromId(id: Int): String
    {
        val str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        var ar = "";
        var tmpId = id
        var tries = 0
        while (tmpId > 0) {
            val quotient = floor(tmpId.toDouble() / str.length);
            val remainder = tmpId % str.length;
            ar += str.substring(remainder,remainder+1);
            tmpId = quotient.toInt();
            tries++
        }
        return ar.reversed()
    }


    fun idFromToken(token: String): Int
    {
        val str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        var ret = 0;
        var iteration = 0;

        for (i in token.length-1 downTo  0) {
            ret += str.indexOf(token[i]) * Math.pow(str.length.toDouble(),iteration.toDouble()).toInt();
            iteration++;
        }
        return ret;
    }

    fun extFromMime(mime: String): String {
        if (mime == "image/png") {
            return "png";
        }else if (mime == "image/jpeg") {
            return "jpg";
        }else if (mime == "image/gif") {
            return "gif";
        } else {
//            die("unknown mime [$mime]");
        }
        return ""
    }
/*
    fun sha1(input: String) = hashString("SHA-1", input)
    fun md5(input: String) = hashString("MD5", input)

    private fun hashString(type: String, input: String): String {
        val bytes = MessageDigest
            .getInstance(type)
            .digest(input.toByteArray())
        return DatatypeConverter.printHexBinary(bytes).toUpperCase()
    }
    */
}