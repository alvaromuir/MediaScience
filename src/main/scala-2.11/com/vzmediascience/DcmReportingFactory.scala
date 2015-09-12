package com.vzmediascience

import java.io.InputStreamReader
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.{GoogleAuthorizationCodeFlow, GoogleClientSecrets}
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.{HttpRequest, HttpRequestInitializer}
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.dfareporting.{Dfareporting, DfareportingScopes}
import com.google.common.collect.ImmutableList

/**
 * Created by Alvaro Muir, alvaro.muir@verizon.com on 9/9/15.
 */



object DcmReportingFactory {

  private val DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".vzmediascience/dcm_reporting")
  private val JSON_FACTORY = JacksonFactory.getDefaultInstance
  private val dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR)
  val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

  private val SCOPES = ImmutableList.of(DfareportingScopes.DFAREPORTING, DfareportingScopes.DFATRAFFICKING)

  def authorize: Credential = {
    val clientSecrets: GoogleClientSecrets = GoogleClientSecrets.load(
      JSON_FACTORY, new InputStreamReader(DcmReportingFactory.getClass.getResourceAsStream("/client_secret.json")))

    val flow:GoogleAuthorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(dataStoreFactory).build
    new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user")
  }

  def getInstance(): Dfareporting = {
    val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
    val dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR)
    val credential = authorize

    new Dfareporting.Builder(httpTransport, JSON_FACTORY,
      new HttpRequestInitializer() {

//        this is overridden to ensure gip'd downloads
        def initialize(request: HttpRequest) {
          credential.initialize(request)
          request.getHeaders()
            .setAcceptEncoding("gzip")
            .setUserAgent("YOUR_PROGRAM (gzip)")
        }
      })
      .setApplicationName("vzmediascience-platform-1.0")
      .build()
  }

}
