Camunda GraphQL
===============

This repository contains Camunda GraphQL.

Camunda GraphQL allows you to query existing Camunda BPM Process Engine data in a simple way.<br> 
See http://graphql.org/ for more info about GraphQL. It's amazing. 


Installation
------------

1. Build and deploy the WAR file of this project to your Camunda BPM Server.<br>
   That's it. You added GraphQL to Camunda BPM. 

   Important: <br>
   Use Application Context `/graphql` for deplyoment.<br>
   This will define the GraphQL endpoint typically at  `http://localhost:8080/graphql`.
   
2. To play around with GraphQL we highly encourage you to install GraphiQL.<br>
   Configure GraphiQL to point to the GraphQL endpoint as defined in step 1. 

   You can google for GraphiQL or install GraphiQL from these places: <br>
   * https://github.com/redound/graphql-ide <br>
   (Windows, Mac OS X)
   * https://github.com/skevy/graphiql-app <br>
   (Mac User just type: `brew cask install graphiql`)<br>
   
   * https://github.com/graphql/graphiql
     
### Basic Auth

You need to send with every request an Authorization header (username: user, password: password).
```
Authorization: Basic dXNlcjpwYXNzd29yZA==
```
####Camunda Forum

Thread: <br> 
https://forum.camunda.org/t/developing-the-camunda-graphql-extension 
