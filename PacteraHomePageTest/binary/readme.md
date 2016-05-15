1. How to build:
   Run "maven install" after the project has been imported into Eclipse
   During this process the two test steps will be automatically executed
2. How to run from command line:
   0) Modify parameters in testng.xml based on the location of binaries/drivers on your system
   1) After the build process, if valid testng.xml and log4j.xml are under the same folder as 
      PacteraHomePageTest-0.0.1-SNAPSHOT-tests.jar, then just execute:
      java -jar PacteraHomePageTest-0.0.1-SNAPSHOT-tests.jar
   2) If valid testng.xml and log4j.xml are at other locations, the execute:
      java -jar PacteraHomePageTest-0.0.1-SNAPSHOT-tests.jar full_path_to_testng_xml full_path_to_log4j_xml
3. Support Chrome/Firefox/Explorer
4. PacteraHomePageTest-0.0.1-SNAPSHOT-tests.jar and dependency-jars folder should be in the same folder
   