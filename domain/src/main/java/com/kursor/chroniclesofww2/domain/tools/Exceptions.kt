package com.kursor.chroniclesofww2.domain.tools

import java.lang.Exception

class UnauthorizedException : Exception("Unauthorized")

class ConnectionException : Exception("Couldn't connect to server")