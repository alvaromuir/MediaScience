package com.vzmediascience

import java.io._

import com.google.api.client.http.{HttpResponse}
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.{Charsets, Strings}
import com.google.api.services.dfareporting.model
import com.google.api.services.dfareporting.model.{FileList, ReportList}

import scala.collection.JavaConversions._


/**
 * Created by Alvaro Muir, alvaro.muir@verizon.com on 9/10/15.
 */
case class Reports(USER_PROFILE_ID: Long) {
  val reportingFactory = DcmReportingFactory
  val reporting = reportingFactory.getInstance()

  def listAllReports {
    val fields = "nextPageToken,items(id,name,type)"
    var reports = new ReportList
    var nextPageToken: String = null
    do {
      reports = reporting.reports.list(USER_PROFILE_ID).setFields(fields).setPageToken(nextPageToken).execute

      for(report <- reports.getItems) {
        println(s"ID: ${report.getId} -- '${report.getName}', a ${report.getType} report")
      }

      nextPageToken = reports.getNextPageToken
    } while (!reports.getItems.isEmpty && !Strings.isNullOrEmpty(nextPageToken))
  }

  def listAllFiles {
    val fields = "nextPageToken,items(fileName,id,status)"
    var files = new FileList
    var nextPageToken: String = null

    do {
      files = reporting.files.list(USER_PROFILE_ID).setFields(fields).setPageToken(nextPageToken).execute

      for (file <- files.getItems) {
        println(s"ID: ${file.getId} -- '${file.getFileName}', status: ${file.getStatus}")
      }

      nextPageToken = files.getNextPageToken
    } while (!files.getItems.isEmpty && !Strings.isNullOrEmpty(nextPageToken))

  }


  def listReportFiles(reportId: Long, limit: Int = 7) {
    val fields = "nextPageToken,items(fileName,id,status)"
    var files = new FileList
    var nextPageToken: String = null
    var total = 0
    do {
      files = getReportFiles(reportId, fields, nextPageToken)
        for (file <- files.getItems.take(limit - total))  {
          val report = getFileInfo(reportId, file.getId)
          println(s"${report.getDateRange.getEndDate} | ID: ${report.getId} | status: ${report.getStatus} ")
          total += 1
        }
      nextPageToken = files.getNextPageToken
    } while (total < limit && !files.getItems.isEmpty && !Strings.isNullOrEmpty(nextPageToken))
  }


  def getReportFiles(reportId: Long, fields: String, nextPageToken: String): FileList = {
    reporting.reports.files.list(USER_PROFILE_ID, reportId)
      .setFields(fields)
      .setPageToken(nextPageToken)
      .setMaxResults(7)
      .execute
  }

  def getFileInfo(reportId: Long, fileId: Long):model.File = {
    reporting.reports().files().get(USER_PROFILE_ID, reportId, fileId).execute
  }

  def downloadFile(reportId: Long, fileId: Long) {
    val fileContents: HttpResponse = reporting.files.get(reportId, fileId).executeMedia()
    val outputStream: FileOutputStream = new FileOutputStream("report.txt")
    val writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"))
    try {
      val reader = new BufferedReader(new InputStreamReader(fileContents.getContent, Charsets.UTF_8))

      while (reader.readLine() != null) {
        val line = reader.readLine()
        writer.write(line)
        writer.write("\n")
      }
    } finally {
      writer.close()
      fileContents.disconnect()
    }
  }

}
