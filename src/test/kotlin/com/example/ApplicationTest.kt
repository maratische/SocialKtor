package com.example

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import maratische.socialktor.plugins.configureRouting
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }

    @Test
    fun testUid() = testApplication {
        application {
            configureRouting()
        }
        client.get("/uidcreator/key?privateKey=t6R6iUH2o1HVI2ofb0uLexTIcmdmAnyk6rOnDnK5").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
        client.get("/uidcreator?data=N5XPg1N7N6MnMIJ4kreMwNLB55Dynat3br7j888NKKn8cgfopfL1rd6m9qKvJ-Y6Vf0Q2qveHjD7WS9oc5CJ2Vjs7eq21_fq2tHTUbfmLZ9nzNnyHpEuc6A5O-inw-DntEyPqcHy-QccaGnzLxERw-9fXaYlBqFlrPll1glJ0GE~~~MfLDGOQ-3GsCQvgeVPNRzYSr9KIZqOF0ekvbWohIQ30").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
//        http://localtest.me/uidcreator?data=N5XPg1N7N6MnMIJ4kreMwNLB55Dynat3br7j888NKKmxv-O4PW3JDeAhddz8pu-HvCqIzcjYRgGrVrehiBN7_lm_IH7oFE7eivnjbm64Sw3H5zXifFoWzSWalqdcrGaywSI-E9oPAHFnKxaCc2RUXazDmGbW1Bv6lu026BbgY4M~~~tFIcN0RBYYhkVuISOIDg4bO0lETl0Ynf-Q3MYPzfD8E
//        http://localtest.me/uidcreator?data=N5XPg1N7N6MnMIJ4kreMwNLB55Dynat3br7j888NKKn8cgfopfL1rd6m9qKvJ-Y6Vf0Q2qveHjD7WS9oc5CJ2Vjs7eq21_fq2tHTUbfmLZ9nzNnyHpEuc6A5O-inw-DntEyPqcHy-QccaGnzLxERw-9fXaYlBqFlrPll1glJ0GE~~~MfLDGOQ-3GsCQvgeVPNRzYSr9KIZqOF0ekvbWohIQ30
    }
}
