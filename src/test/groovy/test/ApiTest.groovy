package test

import com.github.tomakehurst.wiremock.WireMockServer
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.hamcrest.Matcher
import org.spockframework.util.Assert
import spock.lang.Shared
import spock.lang.Specification

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

import static com.github.tomakehurst.wiremock.client.WireMock.*

class ApiTest extends Specification {

    private static final String BASE_URL = "http://localhost:8080"
    @Shared
    WireMockServer wireMockServer = new WireMockServer(8080)
//    @Shared
//    HttpClient client = HttpClient.newHttpClient()

    def setupSpec() {
        wireMockServer.start()
    }

    def cleanupSpec() {
        wireMockServer.stop()
    }

    def "create data"() {
        given: "create stub for POST endpoint"
        stubFor(post(urlEqualTo("/api/data"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(containing("{\"data\": \"data value\"}"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withBody("new data was created")))

        when: "POST end point is performed to the server for creating new test data"
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .basePath("/api/data")
                .contentType(ContentType.JSON)
                .body("{\"data\": \"data value\"}")
                .post()
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:8080/api/new-data"))
//                .POST("{\"data\": \"data value\"}" as HttpRequest.BodyPublisher)
//                .build()
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString())

        then: "response code is 201"
        response.then().statusCode(201)
//        Assert.that(response.statusCode() == 201)

        and: "response contains string in body: new data was created"
        response.getBody().asString() == 'new data was created'
//        Assert.that(true, "new data was created", response.body())
    }

    def "get data"() {
        given: "create stub for GET endpoint"
        stubFor(get(urlEqualTo("/api/data"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("some data")))

        when: "GET end point is performed to the server"
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .basePath("/api/data")
                .get()

        then: "response code is 200"
        response.then().statusCode(200)

        and: "response contains string in body: some data"
        response.getBody().asString() == "some data"
    }

    def "update data"() {
        given: "create stub for PUT endpoint"
        stubFor(put(urlEqualTo("/api/data"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(containing("{\"data\": \"data value\"}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("data was updated")))

        when: "PUT end point is performed to the server for updating new test data"
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .basePath("/api/data")
                .contentType(ContentType.JSON)
                .body("{\"data\": \"data value\"}")
                .put()

        then: "response code is 200"
        response.then().statusCode(200)

        and: "response contains string in body: data was updated"
        response.getBody().asString() == 'data was updated'
    }

    def "delete data"() {
        given: "create stub for DELETE endpoint"
        stubFor(delete(urlEqualTo("/api/data"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(containing("{\"data\": \"data value\"}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("data was deleted")))

        when: "DELETE end point is performed to the server for deleting test data"
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .basePath("/api/data")
                .contentType(ContentType.JSON)
                .body("{\"data\": \"data value\"}")
                .delete()

        then: "response code is 200"
        response.then().statusCode(200)

        and: "response contains string in body: data was deleted"
        response.getBody().asString() == 'data was deleted'
    }
}