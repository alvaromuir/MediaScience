package com.vzmediascience


import org.apache.commons.cli._

/**
 * Created by Alvaro Muir, alvaro.muir@verizon.com on 9/10/15.
 */
object Run {
  def main(args: Array[String]): Unit = {

    val name = new java.io.File(this.getClass.getProtectionDomain
      .getCodeSource
      .getLocation
      .getPath)
      .getName


    val opt: Options = new Options()
      .addOption("h", false, "Print help for this application")
      .addOption("u", "userId", true, "DCM profile id")
      .addOption("A", "all-reports", false, "list all reports for account")
      .addOption("L", "file-list", false, "list all files for specific report")
      .addOption("D", "download-file", false, "download specific file from report")
      .addOption("R", "report-id", true, "specific report ID number, required for downloading a file")
      .addOption("F", "file-id", true, "specific file ID number, required for downloading a file")

    val parser: CommandLineParser = new DefaultParser()
    val cmd: CommandLine = parser.parse(opt, args)
    val f = new HelpFormatter()

    if (args.length == 0 || cmd.hasOption('h')) {
      f.printHelp(name, opt)
      System.exit(0)
    } else {
      if (cmd.hasOption("u")) {
        val userID = cmd.getOptionValue("u")
        val reports = new Reports(userID.toLong)

        if (cmd.hasOption("A")) {
          reports.listAllReports
          System.exit(0)
        }

        if (cmd.hasOption("L")) {
          if (!cmd.hasOption("R")) {
            println(s"$name -L requires a specific report number.")
            println
            f.printHelp(name, opt)
            System.exit(0)
          } else {
            val reportId = cmd.getOptionValue("R").toLong

            reports.listReportFiles(reportId)
            System.exit(0)
          }
        }

        if (cmd.hasOption("D")) {
          if (!cmd.hasOption("R") || !cmd.hasOption("F")) {
            println(s"$name -D requires a report number and file number.")
            println
            f.printHelp(name, opt)
            System.exit(0)
          } else {
            val reportId = cmd.getOptionValue("R").toLong
            val fileId = cmd.getOptionValue("F").toLong

            println("Downloading your report . . .")
            reports.downloadFile(reportId, fileId)
            System.exit(0)
          }
        }
      }
      else {
        println(s"Error: $name requires a DCM user profile ID. See help:")
        println
        f.printHelp(name, opt)
        System.exit(0)
      }
    }
  }
}
