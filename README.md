Camunda GraphQL
===============

Camunda GraphQL is a Community Extension for Camunda BPM that allows you to use [GraphQL](http://graphql.org/) to query and mutate [Process Engine data](https://docs.camunda.org/manual/latest/user-guide/process-engine/process-engine-api/) in a simple way. <br>


![Overview](/src/main/resources/png/overview_01.png?raw=true "Overview") 

Installation
------------
 
1. Build and deploy the WAR file of this project to your Camunda BPM Server. <br>
   Use Application Context `/graphql`.<br> 
   That's it. You added GraphQL to Camunda BPM at `http://<server-name>:<PORT>/graphql`

2. Optional: To play around with GraphQL we highly encourage you to install GraphiQL.<br>

   You can google for GraphiQL or install GraphiQL from these places: <br>
   * https://github.com/redound/graphql-ide <br>
   (Windows, Mac OS X)
   * https://github.com/skevy/graphiql-app <br>
   Mac User just type: `brew cask install graphiql`<br>
   Windows/Linux User:
     - `npm install -g electron` <br>
     - download graphiql-app zip/tar.gz from https://github.com/skevy/graphiql-app/releases <br>
     - unzip
     - `electron <app-folder>` with app-folder = `GraphiQL.app/Contents/Resources/app`
   
   * https://github.com/graphql/graphiql
   
   Configure GraphiQL to point to the GraphQL endpoint as defined in step 1. <br><br>
   **Basic Authentication**: <br>
   You must add an Authorization-Header to your GraphQL-request! <br>
   Example for user 'demo' and 'password 'demo' is: <br>
   Key='Authorization' <br>
   Value='Basic ZGVtbzpkZW1v' <br>
   
   In GraphiQL (graphiql-app) there is a link 'Edit HTTP headers' where you can adjust your request headers.
    
   <br>
   
  <br>
   
Example Queries
---------------


###Fetch all Tasks:<br>

![query tasks](/src/main/resources/png/query_tasks.png?raw=true "simple GraphQL query") 

(on the left side is the query we just typed in. After pressing the Play-Button on top the result appears on the right side. )

###Fetch Tasks with Filter: <br>

![query tasks with args](/src/main/resources/png/query_tasks_w_filter_nameLike.png?raw=true "simple GraphQL query with arguments")

(Queries are based on the Camunda Process Engine API) 

Example Mutation
----------------

###Set the assignee of a specific Task: <br>

![setAssigne mutation](/src/main/resources/png/mutation_01.png?raw=true "simple GraphQL mutation") 

(Here it's specified that the fields `id`, `name` and `assignee` should be returned after the mutation.)     

Camunda GraphQL Types (automatically generated documentation)
-------------------------------------------------------------

###Fields of a Task (TaskEntity)  <br>
![type Task](/src/main/resources/png/type_TaskEntity.png?raw=true "type TaskEntity") 


Camunda Forum Thread (initial)
------------------------------

https://forum.camunda.org/t/developing-the-camunda-graphql-extension 
