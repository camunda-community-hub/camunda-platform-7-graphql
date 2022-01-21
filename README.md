[![Community Extension Badge](https://img.shields.io/badge/Community%20Extension-An%20open%20source%20community%20maintained%20project-FF4700)](https://github.com/camunda-community-hub/community)
[![Lifecycle: Incubating](https://img.shields.io/badge/Lifecycle-Incubating-blue)](https://github.com/Camunda-Community-Hub/community/blob/main/extension-lifecycle.md#incubating-)

Camunda Platform GraphQL
===============

Camunda Platform GraphQL is a Community Extension for Camunda Platform that allows you to use [GraphQL](http://graphql.org/) to query and mutate [Process Engine data](https://docs.camunda.org/manual/latest/user-guide/process-engine/process-engine-api/) in a simple way. <br>


![Overview](extension/src/main/resources/png/overview_01.png?raw=true "Overview")

Release 0.4.0
-------------
- updated the resolvers to support the GraphQL Kickstart
- GraphiQL endpoint default set to /graphiql
- GraphQL endpoint default set to /graphql
- Added historic queries
- Added relationship between historic types
- refactored the test setup and move test from webapp to extension
- changed the app to execute on Spring Boot
- Updated example
- updated documentation


Spring Boot Camunda GraphQL Server Embbedded
--------------------------

### Spring Boot (only supported on version 0.4.0 or higher)
In your Camunda Spring Boot project add the following dependencies

**Gradle**

Camunda Extension GraphQL
```groovy
    implementation group: 'org.camunda.bpm.extension.graphql', name: 'camunda-bpm-graphql', version: '0.4.0'
```

Spring Boot GraphQL Kickstart (the version 11.1.0 or higher)
```groovy
    implementation group: 'com.graphql-java-kickstart', name: 'graphql-spring-boot-starter', version: '11.1.0'
    implementation group: 'com.graphql-java-kickstart', name: 'graphiql-spring-boot-starter', version: '11.1.0'
    implementation group: 'com.graphql-java-kickstart', name: 'graphql-java-tools', version: '11.0.1'
```

**Maven**

Camunda Extension GraphQL
```xml
    <dependency>
        <groupId>org.camunda.bpm.extension.graphql</groupId>
        <artifactId>camunda-bpm-graphql</artifactId>
        <version>0.4.0</version>
    </dependency>
```
Spring Boot GraphQL Kickstart (the version 11.1.0 or higher)
```xml
    <dependency>
        <groupId>com.graphql-java-kickstart</groupId>
        <artifactId>graphql-spring-boot-starter</artifactId>
        <version>11.1.0</version>
    </dependency>

    <dependency>
        <groupId>com.graphql-java-kickstart</groupId>
        <artifactId>graphiql-spring-boot-starter</artifactId>
        <version>11.1.0</version>
    </dependency>

    <dependency>
        <groupId>com.graphql-java-kickstart</groupId>
        <artifactId>graphql-java-tools</artifactId>
        <version>11.0.1</version>
    </dependency>
```

Use the [example](https://github.com/camunda-community-hub/camunda-platform-graphql/tree/main/example) if necessary. 

By default the GraphQL and GraphiQL are available at the uri /graphql and /graphiql respectively, this configuration
and others can be change by properties. The available settings can be consulted directly in the project of
[Spring Boot GraphQL Kick Start](https://github.com/graphql-java-kickstart/graphql-spring-boot)

Install the Camunda GraphQL Server on Tomcat or Wildfly
--------------------------

Build the GraphQL server
------------------------
1. Checkout or Clone this repository using Git<br>
2. Adapt `extension/src/main/resources/application.properties`: <br>
3. Build the project<br>
 for Apache Tomcat:     `mvn clean package`<br>
 for JBoss WildFly use the profile wildfly:  `mvn clean package -Pwildfly`<br>

### Tomcat installation
- Get the latest Release (`.war` file) from the 
[Camunda Repo](https://app.camunda.com/nexus/content/repositories/camunda-bpm-community-extensions/org/camunda/bpm/extension/graphql/camunda-bpm-graphql/) <br>
- deploy it to your Tomcat server e.g. copy it to the Tomcat /webapps` folder

### Wildfly installation
For WildFly you have to clone the project and build the `.war` file. <bR>
See chapter [Build the GraphQL server](#build-the-graphql-server).
   
   
Test the Installation
---------------------
### Access the GraphQL endpoint with a browser
- URL:  `http://<server-name>:<PORT>/camunda-graphql/graphql/?query=`{_here is your query_} <br>
  e.g. [http://localhost:8080/camunda-graphql/graphql/?query={tasks{name}}](http://localhost:8080/camunda-graphql/graphql/?query={tasks{name}})<br>
  This will return a JSON object with the names of the current tasks (rendered by your browser).<br>

### Access the GraphQL endpoint with GraphiQL _an in-browser IDE for exploring GraphQL_
- GraphiQL is available at `http://<server-name>:<PORT>/camunda-graphql/graphiql/` <br>
  e.g. [http://localhost:8080/camunda-graphql/graphiql/](http://localhost:8080/camunda-graphql/graphiql/)<br>
- checkout chapter [GraphQL Queries and Mutations](#graphql-queries-and-mutations)
   

Other GraphQL clients
--------------------- 
Beside the build-in GraphiQL you can use other GraphQL clients. <br>
Basically...
- ...point your GraphQL client to the GraphQL server endpoint: <br>
`http://<server-name>:<PORT>/camunda-graphql/graphql` <br>
- depending on the GraphQL server authentication settings you need to add Authentication Header to your requests

Examples of other GraphQL clients:<br>

   * GraphQL IDE - An extensive IDE for exploring GraphQL API's<br>
   Link: https://github.com/redound/graphql-ide<br>
   (Windows, Mac OS X - needs npm/yarn)<br>
   
   * Light, Electron-based Wrapper around GraphiQL<br>
   Link: https://github.com/skevy/graphiql-app <br>
   Mac User just type: `brew cask install graphiql`<br>
   Windows/Linux User:
     - `npm install -g electron` <br>
     - download graphiql-app zip/tar.gz from https://github.com/skevy/graphiql-app/releases <br>
     - unzip
     - type in: `electron` _app-folder_ <br>
     with _app-folder_ = `GraphiQL.app/Contents/Resources/app` (unzipped from above)
   <br>
   

GraphQL Queries and Mutations
-----------------------------


**Query Tasks:**<br>

![query tasks](extension/src/main/resources/png/query_tasks.png?raw=true "GraphQL query for Tasks")


**Query Tasks using a Filter:**<br>

![query tasks with filter](extension/src/main/resources/png/query_tasks_w_filter_nameLike.png?raw=true "GraphQL query for Tasks with filter")


**Query Process Instances:**<br>

![query proceses](extension/src/main/resources/png/query_process_instances.png?raw=true "GraphQL query for Process Instances")

**Query Process Instance Variables:**<br>

![query proceses with vars](extension/src/main/resources/png/query_process_instances_w_vars.png?raw=true "GraphQL query for Process Instances with Variables")

**Query Task Variables:**<br>

![query tasks with vars](extension/src/main/resources/png/query_tasks_w_vars.png?raw=true "GraphQL query for Process Instances with Variables")


**Mutation**<br>
**Assign a user to a task** <br>

![setAssignee mutation](extension/src/main/resources/png/mutation_01.png?raw=true "simple GraphQL mutation")


**Mutation**<br>
**Start a Process Instance with start variables** <br>
![startProcessInstance](extension/src/main/resources/png/mutation_startProcessInstance.png?raw=true "startProcessInstance")

GraphQL Schemas and Types
-------------------------

A GraphQL Schema defines the capabilities of a GraphQL server. It exposes all available types and directives on the server, as well as the entry points for query, mutation, and subscription operations.


![Docs Schema](extension/src/main/resources/png/docs_open.png?raw=true "docs open") <br><br>

![Docs Root Types](extension/src/main/resources/png/docs_root_types.png?raw=true "docs root types")<br><br>

### Currently defined queries: 

![Docs Queries](extension/src/main/resources/png/docs_queries.png?raw=true "docs queries")<br><br>

### Currently defined mutations:

![Docs Mutations](extension/src/main/resources/png/docs_mutations.png?raw=true "docs mutations")<br><br>


Defining / Extending the Camunda GraphQL Schema
-----------------------------------------------

We decided to use the Schema Definition Language (a GraphQL DSL) to define the Camunda GraphQL schema instead of coding it in Java. <br>
The Schema Definition Language is very easy to understand.<bR>
For example this is the Type Definition of the Camunda Task: <br><br>    
![graphqls TaskEntity](extension/src/main/resources/png/schema_TaskEntity_graphqls.png?raw=true "graphqls TaskEntity")<br><br>
  
To get an understanding of Schemas please visit: <bR>
 - http://graphql.org/learn/schema/ <br>
 - http://graphql-java.readthedocs.io/en/stable/schema.html <br>

The Camunda GraphQL Schema is comprised of several schema files located at `src/main/resource/*.graphqls`. <br>
This is an attempt to group GraphQL Type Definitions by topics<br>

![schema files overview](extension/src/main/resources/png/schema_files.png?raw=true "schema files")<br><br>


The so called _Root Types_ serve as entry points for Queries and Mutations (in the future: Subscriptions etc.) <br>

The Root Types schema file is `src/main/resources/camunda.graphqls`<br>



Introspection
-------------


For interactive GraphQL code completion, build-time validation, GraphQL Schema stiching or other fancy things  
any GraphQL client can use GraphQLs Introspection to retrieve the whole or parts of the Servers GraphQL Schema.<br>

This is a possible and probably quite complete _Introspection Query_ (from [GraphQL Voyager](https://apis.guru/graphql-voyager/)): 
    

```
query IntrospectionQuery {
    __schema {
      queryType { name }
      mutationType { name }
      subscriptionType { name }
      types {
        ...FullType
      }
      directives {
        name
        description
        locations
        args {
          ...InputValue
        }
      }
    }
  }

  fragment FullType on __Type {
    kind
    name
    description
    fields(includeDeprecated: true) {
      name
      description
      args {
        ...InputValue
      }
      type {
        ...TypeRef
      }
      isDeprecated
      deprecationReason
    }
    inputFields {
      ...InputValue
    }
    interfaces {
      ...TypeRef
    }
    enumValues(includeDeprecated: true) {
      name
      description
      isDeprecated
      deprecationReason
    }
    possibleTypes {
      ...TypeRef
    }
  }

  fragment InputValue on __InputValue {
    name
    description
    type { ...TypeRef }
    defaultValue
  }

  fragment TypeRef on __Type {
    kind
    name
    ofType {
      kind
      name
      ofType {
        kind
        name
        ofType {
          kind
          name
          ofType {
            kind
            name
            ofType {
              kind
              name
              ofType {
                kind
                name
                ofType {
                  kind
                  name
                }
              }
            }
          }
        }
      }
    }
  }
```
<br><br>
The **response** of the above Introspection Query can be pasted into tools like [GraphQL Voyager](https://apis.guru/graphql-voyager/) as a _Custom Schema_ 
and within seconds you get a graphical representation of your Schema, like so: <br><br>

![graphical GraphQL Schema](extension/src/main/resources/png/graphql_voyager.png?raw=true "graphical GraphQL Schema")<br>

Awesome!<br><br>



Authentication
--------------
The Camunda GraphQL server supports three Authentication methods: <br>
 * Basic <br>
 * JWT ([JSON Web Token](https://jwt.io))<br>
 * No authentication <br>

Properties which manage authentication are:<br> 
 * `auth.Filter`<br>
 * `JWT.secret`<br>
 * `JWT.issuer`<br> 

These properties can be set as<br>
 * JNDI attributes from `java:comp/env`
 * Java System properties
 * OS environment variables
 * properties in [application.properties](https://github.com/camunda/camunda-bpm-graphql/blob/master/src/main/resources/application.properties) <br>

E.g. if you are using Tomcat you can add them to catalina.properties.<br>

If authentication is switched on (Basic or JWT) the Camunda GraphQL Server expects an Authorization-Header in the client request.<br>
GraphQL clients let you define these request headers, e.g. the [graphiql-app](https://github.com/skevy/graphiql-app) has a link _Edit HTTP headers<br>

![http headers 01](extension/src/main/resources/png/http_headers_01.png?raw=true "http headers")<br><br>

![http headers 02](extension/src/main/resources/png/http_headers_02.png?raw=true "http headers details")<br><br>


Basic Authentication
--------------------

To switch to Basic Authentication use: <br>
`auth.Filter=BASIC`

For example if you have a Camunda user `demo` with the password `demo` your Authorization-Header must be: <br>
Key=`Authorization` <br>
Value=`Basic ZGVtbzpkZW1v` ([why?](https://en.wikipedia.org/wiki/Basic_access_authentication))<br>

<br>

JWT ([JSON Web Token](https://jwt.io)) Authentication
------------------

To switch to JWT you must set three properties: <br>
`auth.Filter=JWT`<br>
`JWT.secret=Whatever_Random_Secret_String_You_Prefer`<br>
`JWT.issuer=Usualy_The_Name_Of_Your_Company`<br> 

A JWT Authorization-Header could look like this: <br>
Key=`Authorization` <br>
Value=`Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjYW11bmRhIiwiZXhwIjoxNDk3NzA4NjY3LCJpYXQiOjE0OTc2MjIyNjcsInVzZXJuYW1lIjoiZGVtbyJ9.Z-7NTGsHYqsEoc98yOgeT5LD9oGnei6jDPs-FQQhqDw`<br>

**Important!**<br>
The GraphQL Server can validate **existing** JSON Web Token presented in the Authorization-Header based on
<br>
`JWT.secret`<br>
`JWT.issuer`<br>
(future releases will support private/public key)
<br>
but cannot provide or issue them. There is no login functionality "out of the box"<br>
There is no JWT provider build into Camunda GraphQL server, because that does not belong to GraphQL at all.<br>
<br>
**Workaround (in case you do not have a JWT provider)**<br>
To create valid JWTs you need a JWT provider. <br>
A JWT provider for Camunda can be found here: https://github.com/Loydl/camunda-jwt-provider <br>  
The `JWT.secret` and `JWT.issuer` settings of the <br>
- JWT provider<br> 
- and the Camunda GraphQL server
<br>
must be the same, otherwise the JWT cannot be validated and user do not get authenticated.<br>
(BTW, this is also a good option to basically implement Single Sign On).<br> 
<br>
Further information about JWT: https://jwt.io  <br>

No Authentication
-----------------

To switch off authentication you simply set:<br> 
`auth.Filter=NO` <br> 
or delete this property, e.g. in catalina.properties put it in a comment:<br> 
`#auth.Filter=xyz` <br>
(If the value of `auth.Filter` is equal `JWT` or `BASIC` than authentication is switched on, otherwise it is switched off.)    
<br>

Goals
---------
- expose the complete Camunda Java API in GraphQL
- build modern web and mobile clients for Camunda Platform (freedom to choose your GUI library)
- build GraphQL-based, customizable versions of **Tasklist, Cockpit, Admin**
- Camunda BPM as part of a micro-services architecture. Service accessible through a GraphQL endpoint <br>
(using GraphQL Schema Stiching to combine many GraphQL endpoints to one GraphQL API gateway)
- Realtime GUIs (add GraphQL subscriptions to Camunda GraphQL)


Camunda Forum Thread (initial)
------------------------------

https://forum.camunda.org/t/developing-the-camunda-graphql-extension

