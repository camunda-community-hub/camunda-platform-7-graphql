Camunda GraphQL
===============

Camunda GraphQL is a Community Extension for Camunda BPM that allows you to use [GraphQL](http://graphql.org/) to query and mutate [Process Engine data](https://docs.camunda.org/manual/latest/user-guide/process-engine/process-engine-api/) in a simple way. <br>


![Overview](/src/main/resources/png/overview_01.png?raw=true "Overview")

Installation
------------

1. Checkout this repository using Git<br>
2. Adapt `src/main/resources/application.properties`: <br>
For a quick test set `auth.Filter` to `NO`. (`JWT` is the default. Check the chapter Authorization)
3. Build the project<br>
 for apache tomcat:     `mvn clean package`<br>
 for jboss wildfly use the profile wildfly:  `mvn clean package -Pwildfly`<br>
4. Deploy the `.war` file located in the `target/` folder to your server<br>
   Use Application Context `/graphql`.<br>
5. Run a quick test:    
   Open this URL in a Browser: `http://<server-name>:<PORT>/graphql/?query={tasks{name}}` <br>
   e.g. [http://localhost:8080/graphql/?query={tasks{name}}]()<br>
   You should see a JSON response with a list of tasks names<br> 
6. Open a GraphQL client: <br>
  set the URL to `http://<server-name>:<PORT>/graphql` <br>
  e.g. [http://localhost:8080/graphql/]()<br>
  submit GraphQL statements :-) <br>

GraphQL client
--------------- 
You can easily explore GraphQL with one of these clients:<br>

   * https://github.com/redound/graphql-ide <br>
   (Windows, Mac OS X - needs npm/yarn)
   * https://github.com/skevy/graphiql-app <br>
   Mac User just type: `brew cask install graphiql`<br>
   Windows/Linux User:
     - `npm install -g electron` <br>
     - download graphiql-app zip/tar.gz from https://github.com/skevy/graphiql-app/releases <br>
     - unzip
     - type in: `electron` _app-folder_ <br>
     with _app-folder_ = `GraphiQL.app/Contents/Resources/app` (unzipped from above)

   * GraphiQL client: https://github.com/graphql/graphiql

   <br>
   Future releases of Camunda GraphQL will include a GraphiQL client.<br>
   <br>
   

GraphQL Queries and Mutations
-----------------------------


**Tasks:**<br>

![query tasks](/src/main/resources/png/query_tasks.png?raw=true "GraphQL query for Tasks")


**Tasks with a Filter:**<br>

![query tasks with filter](/src/main/resources/png/query_tasks_w_filter_nameLike.png?raw=true "GraphQL query for Tasks with filter")


**Process Instances:**<br>

![query proceses](/src/main/resources/png/query_process_instances.png?raw=true "GraphQL query for Process Instances")

**Process Instance Variables:**<br>

![query proceses with vars](/src/main/resources/png/query_process_instances_w_vars.png?raw=true "GraphQL query for Process Instances with Variables")

**Task Variables:**<br>

![query tasks with vars](/src/main/resources/png/query_tasks_w_vars.png?raw=true "GraphQL query for Process Instances with Variables")


**Assign a task (it's called a "mutation")** <br>

![setAssigne mutation](/src/main/resources/png/mutation_01.png?raw=true "simple GraphQL mutation")


GraphQL Schemas and Types
-------------------------

A GraphQL Schema defines the capabilities of a GraphQL server. It exposes all available types and directives on the server, as well as the entry points for query, mutation, and subscription operations.


![Docs Schema](/src/main/resources/png/docs_open.png?raw=true "docs open") <br><br>

![Docs Root Types](/src/main/resources/png/docs_root_types.png?raw=true "docs root types")<br><br>

![Docs Queries](/src/main/resources/png/docs_queries.png?raw=true "docs queries")<br><br>

![Docs Mutations](/src/main/resources/png/docs_mutations.png?raw=true "docs mutations")<br><br>

![type TaskEntity](/src/main/resources/png/docs_TaskEntity.png?raw=true "type TaskEntity")<br><br>

Defining / Extending the Camunda GraphQL Schema
-----------------------------------------------

We decided to use the Schema Definition Language (a GraphQL DSL) to define the Camunda GraphQL schema instead of coding it in Java. <br>
The Schema Definition Language is very easy to understand.<bR>
For example this is the Type Definition of the Camunda Task Entity (as defined in summer 2017): <br><br>    
![graphqls TaskEntity](/src/main/resources/png/schema_TaskEntity_graphqls.png?raw=true "graphqls TaskEntity")<br><br>
  
To get an understanding of Schemas please visit: <bR>
 - http://graphql.org/learn/schema/ <br>
 - http://graphql-java.readthedocs.io/en/stable/schema.html <br>

The whole Camunda GraphQL Schema is divided into several schema files located at `src/main/resource/*.graphqls` - an attempt to group GraphQL Type Definitions by topics<br>

![schema files overview](/src/main/resources/png/schema_files.png?raw=true "schema files")<br><br>


Every GraphQL Schema defines so called _Root Types_ which gives you the entry points for Queries, Mutations, Subscriptions etc. <br>

We have defined a single Root Types schema file `src/main/resources/camunda.graphqls` - you will recognize some of the queries:<br>

![schema file root types](/src/main/resources/png/schema_root_types_file.png?raw=true "schema file root types")<br><br>



Introspection
-------------


For interactive GraphQL code completion, build-time validation or other fancy things to represent and interact with a Schema a client (i.e. a GraphQL tools builder) can use a GraphQL Introspection Query to retrieve the Servers GraphQL Schema.<br>

This is a possible and probably quite complete Introspection Query (from [GraphQL Voyager](https://apis.guru/graphql-voyager/)): 
    

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
The **response** to the above Introspection Query can be pasted into tools like [GraphQL Voyager](https://apis.guru/graphql-voyager/) and within seconds you get a graphical representation of your Schema: <br><br>

![graphical GraphQL Schema](/src/main/resources/png/graphql_voyager.png?raw=true "graphical GraphQL Schema")<br>

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
 * properties in the file `application.properties` <br>

E.g. if you are using Tomcat you can add them to catalina.properties.<br>

If authentication is switched on (Basic or JWT) the Camunda GraphQL Server expects an Authorization-Header in the client request.<br>
GraphQL clients let you define these request headers, e.g. the [graphiql-app](https://github.com/skevy/graphiql-app) has a link _Edit HTTP headers<br>

![http headers 01](/src/main/resources/png/http_headers_01.png?raw=true "http headers")<br><br>

![http headers 02](/src/main/resources/png/http_headers_02.png?raw=true "http headers details")<br><br>


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
- Camunda Java API exposed to GraphQL, completely
- GraphQL subscriptions for realtime GUIs
- build Web and mobile GUIs for Camunda BPM based on GraphQL using modern GUI-libs and JavaScript/TypeScript-frameworks 


Camunda Forum Thread (initial)
------------------------------

https://forum.camunda.org/t/developing-the-camunda-graphql-extension

