# AEM Importer for Markdown

[![CircleCI](https://circleci.com/gh/adobe/aem-importer-markdown.svg?style=svg)](https://circleci.com/gh/adobe/aem-importer-markdown)

Imports Markdown documents into AEM, creating an AEM content package on the go.

## Why would you want this?

If you have a bunch of Markdown files, for instance [API documentation generated from JSON Schema](https://github.com/adobe/xdm/tree/master/docs/reference), or technical documentation that you want to publish on an AEM-powered web site, for instance [Adobe I/O](https://www.adobe.io). Your Markdown files are not hosted on Github.com, so you cannot use the built-in Markdown importer in Adobe I/O, but with this program you can include the Markdown to AEM conversion into your Continuous Integration process.

## Prerequisites

This is a Java application, so you need Java 8 or higher installed on your system.

```bash
$ java -version
java version "1.8.0_121"
Java(TM) SE Runtime Environment (build 1.8.0_121-b13)
Java HotSpot(TM) 64-Bit Server VM (build 25.121-b13, mixed mode)
```

If you want to build this project from source (right now, that's required), you will also need Maven 3.

```bash
$ mvn -v
Apache Maven 3.3.9 (bb52d8502b132ec0a5a3f4c09453c07478323dc5; 2015-11-10T17:41:47+01:00)
Maven home: /usr/local/Cellar/maven/3.3.9/libexec
Java version: 1.8.0_121, vendor: Oracle Corporation
Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "mac os x", version: "10.12.6", arch: "x86_64", family: "mac"
```

## Getting Started

### Building from Source

To use the application, check out this source code, then build with Maven:

```bash
$ mvn clean package
```

If you are using Adobe's default `settings.xml` and you are outside of Adobe's VPN, it is a good idea to disable the default profile:

```bash
$ mvn clean package -P \!artifactory-corp
```

You will end up with a file called `target/importer-with-dependencies.jar`.

### Creating an AEM Content Package

Run the application using `java -jar` and pass in a configuration file:

```bash
$ java -jar target/importer-jar-with-dependencies.jar markdown2AEM.yml
```

This will create a file `importerDemo.zip`, which you can install in your AEM instance using the package manager or curl command.

```curl -u userid:password -F file=@"importerDemo.zip" -F name="importerDemo.zip" -F force=true -F install=true http://localhost:port/crx/packmgr/service.jsp```

## Configuration

All configuration happens in a `.yaml` file. This configuration file specifies where to find the Markdown files, where the content should be put in AEM and what kind of content should be created.

There are two configuration modes:
1. Integrated GitHub Client: the importer will pull all files from GitHub (Enterprise)
2. Local Checkout: the importer assumes you have all files in a local checkout

### Integrated GitHub Client

following settings exist for the integrated GitHub client:

* `githubUrl`: hostname of your GitHub instance
* `githubContentUrl`: API endpoint for retrieving binaries from GitHub
* `githubApiUrl`: hostname of the GitHub API server
* `apiToken`: the API token to access GitHub. Get it from **GitHub** -> **Settings** -> **Personal access tokens** -> **Generate new token**
* `commitTime`: TODO
* `repositoryUrl`: URL of the repository you want to access
* `privateRepository`: set `true` if this is a private repository
* `branches`: a list of branches or tags that will be imported. This is useful when you have multiple API versions that need to be documented in parallel.

#### Example Configuration
```yaml
githubUrl: github.com
githubContentUrl: https://raw.githubusercontent.com
githubApiUrl: api.github.com
# don't put your API token on GitHub. Use read-only tokens.
apiToken: cafea1b0c6faee11d6dcbabef838f2abcdec6feac
commitTime: 1
repositoryUrl: https://github.com/iotester/importerTest
privateRepository: false

branches:
 - master
 - develop

```

### Local Checkout

The local checkout is ideal when you are working with a CI system that already has your Git credentials. You don't need to generate an API token, you just call `git export` or `git clone` in your build script before calling `importer.jar`.

Following settings are available for local checkout:

* `githubUrl`: hostname of your GitHub instance
* `repositoryUrl`: URL of the repsoitory, in case you want to enable the "edit on GitHub link"
* `branches`: a list of branches or tags that will be imported. This is useful when you have multiple API versions that need to be documented in parallel.
* `workingDirs`: a map of branch names to local checkout directories. Both absolute and relative path names are acceptable


#### Example Configuration
```yaml
repositoryUrl : https://github.com/iotester/importerTest
privateRepository : false

branches:
 - master
 - develop

 #if working dirs are set then api import will be skipped
workingDirs :
 - master:./exports/master
 - develop:./exports/develop
```

## Contributing

This application is work in progress and we are happy about any contribution. You can 
- make a pull request on GitHub
- file an issue against the APM project in jira.adobe.com
- just say hi in the `#www_adobe_io` channel on Slack (Enterprise Grid)

### Setting up to Deploy

This project is configured to deploy to [Adobe Artifactory](https://artifactory.corp.adobe.com/artifactory/webapp/#/artifacts/browse/tree/General/maven-markdown-tools-snapshot/io/adobe/udp/markdown-importer). In order to deploy yourself, you need to update your `settings.xml` with following snippets:

```xml
  <servers>
    <server>
      <username><!-- replace with your LDAP user name --></username>
      <password><!-- replace with your API key from Artifactory --></password>
      <id>central</id>
    </server>
    <server>
      <username><!-- replace with your LDAP user name --></username>
      <password><!-- replace with your API key from Artifactory --></password>
      <id>snapshots</id>
    </server>
  </servers>
```

and

```xml
    <profile>
      <repositories>
        <repository>
          <snapshots>
            <enabled>false</enabled>
          </snapshots>
          <id>central</id>
          <name>maven-markdown-tools-release</name>
          <url>https://artifactory.corp.adobe.com:443/artifactory/maven-markdown-tools-release</url>
        </repository>
        <repository>
          <snapshots />
          <id>snapshots</id>
          <name>maven-markdown-tools-snapshot</name>
          <url>https://artifactory.corp.adobe.com:443/artifactory/maven-markdown-tools-snapshot</url>
        </repository>
      </repositories>
      <pluginRepositories>
        <pluginRepository>
          <snapshots>
            <enabled>false</enabled>
          </snapshots>
          <id>central</id>
          <name>maven-markdown-tools-release</name>
          <url>https://artifactory.corp.adobe.com:443/artifactory/maven-markdown-tools-release</url>
        </pluginRepository>
        <pluginRepository>
          <snapshots />
          <id>snapshots</id>
          <name>maven-markdown-tools-snapshot</name>
          <url>https://artifactory.corp.adobe.com:443/artifactory/maven-markdown-tools-snapshot</url>
        </pluginRepository>
      </pluginRepositories>
      <id>artifactory-markdown</id>
    </profile>
```

and

```xml
    <activeProfiles>
        <activeProfile>artifactory-markdown</activeProfile>
    </activeProfiles>
```

### Deploying a Build

```bash
$ mvn deploy
```

## License/Copyright

Copyright 2017 Adobe Systems Incorporated. All rights reserved.
This file is licensed to you under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License. You may obtain a copy
of the License at http://www.apache.org/licenses/LICENSE-2.0
