Work in progress, scala API for DCM reporting
Requires a "native" client_secrets.json (grab one from your google console)


usage:

To list all reports use the A flag:
```
$java -jar target/scala-2.11/MediaScience-assembly-1.0.jar -u DCM_PROFILE_ID -A
```

To list all files for a given report -R
```
$java -jar target/scala-2.11/MediaScience-assembly-1.0.jar -u DCM_PROFILE_ID -L -R 123455678
```

To download a report use the -D flag with the report id -R and file number -F
```
java -jar target/scala-2.11/MediaScience-assembly-1.0.jar -u 1511370 -D -R 27154768 -F 142808810
```

Requires scala and all the stuff in the build file
A faster broadband connection helps too.


@alvaromuir