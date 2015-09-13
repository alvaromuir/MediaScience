Work in progress, scala API for DCM reporting
Requires a "native" client_secrets.json (grab one from your google console), to be placed in src/main/resources


usage:

To list all reports use the A flag:
```
$java -jar target/scala-2.11/MediaScience-assembly-1.0.jar -u DCM_PROFILE_ID -A
```

To list all files for a given report -R
```
$java -jar MediaScience-assembly-1.0.jar -u DCM_PROFILE_ID -L -R REPORT_ID
```

Pretty print report info with  -I flag with the report id -R and file number -F
```
java -jar MediaScience-assembly-1.0.jar -u DCM_PROFILE_ID -I -R REPORT_ID -F FILE_ID
```

To download a report use the -D flag with the report id -R and file number -F
```
java -jar MediaScience-assembly-1.0.jar -u DCM_PROFILE_ID -D -R REPORT_ID -F FILE_ID
```

Requires scala and all the stuff in the build file
A faster broadband connection helps too.


@alvaromuir
