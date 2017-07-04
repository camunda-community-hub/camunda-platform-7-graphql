Camunda GraphQL
===============

Camunda GraphQL is a Community Extension for Camunda BPM that allows you to use [GraphQL](http://graphql.org/) to query and mutate [Process Engine data](https://docs.camunda.org/manual/latest/user-guide/process-engine/process-engine-api/) in a simple way. <br>


![Overview](/src/main/resources/png/overview_01.png?raw=true "Overview")

Installation
------------

1. Checkout this repository using Git<br>
2. Build the project
- for apache tomcat
     `mvn clean package`
- for jboss wildfly use the profile wildfly
    `mvn clean package -Pwildfly`
3. Deploy the `.war` file located in the `target/` folder to the server<br>
   Use Application Context `/graphql`.<br>
4. Open a GraphQL client: 
 - set the URL to `http://<server-name>:<PORT>/graphql` <br>
 - submit GraphQL statements :-)

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

   * https://github.com/graphql/graphiql

   <br>
   Future releases of Camunda GraphQL will include a GraphiQL client out-of-the-box (additionally to whatever other client you like to use).<br>
   <br>
   

Example GraphQL Queries
-----------------------


**Fetch all Tasks:**<br>

![query tasks](/src/main/resources/png/query_tasks.png?raw=true "simple GraphQL query")

(on the left side is the query we just typed in. After pressing the Play-Button on top the result appears on the right side. )

**Fetch Tasks with Filter:**<br>

![query tasks with args](/src/main/resources/png/query_tasks_w_filter_nameLike.png?raw=true "simple GraphQL query with arguments")

(Queries are based on the Camunda Process Engine API)

Example GraphQL Mutation
------------------------

**Assign a task:** <br>

![setAssigne mutation](/src/main/resources/png/mutation_01.png?raw=true "simple GraphQL mutation")

(Here it's specified that the fields `id`, `name` and `assignee` should be returned after the mutation.)

GraphQL Types - automatically generated documentation
-------------------------------------------------------------

**Fields of a TaskEntity (right hand side in screenshot)** <br>

![type Task](/src/main/resources/png/type_TaskEntity.png?raw=true "type TaskEntity")


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
GraphQL clients let you define these request headers, e.g. the [graphiql-app](https://github.com/skevy/graphiql-app) has a link _Edit HTTP headers_, see screenshots below.<br>


Basic Authentication
--------------------

To switch to Basic Authentication use: <br>
`auth.Filter=BASIC`

For example if you have a Camunda user `demo` with the password `demo` your Authorization-Header must be: <br>
Key=`Authorization` <br>
Value=`Basic ZGVtbzpkZW1v` ([why](https://en.wikipedia.org/wiki/Basic_access_authentication))<br>

<br>

JWT ([JSON Web Token](https://jwt.io)) Authentication
------------------

To switch to JWT - the preferred authentication method - you must set three properties: <br>
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
but cannot provide or issue them e.g. there is no login functionality!<br>
There is no JWT provider build into Camunda GraphQL server, because that does not belong to GraphQL at all.<br>
<br>
**Solution**<br>
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


Camunda Forum Thread (initial)
------------------------------

https://forum.camunda.org/t/developing-the-camunda-graphql-extension

