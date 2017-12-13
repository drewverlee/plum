# plum

Plum is a restful api that accepts and returns sorted people records.

Master Branch Tests: [![CircleCI](https://circleci.com/gh/drewverlee/plum.svg?style=svg&circle-token=e36a553b9697f3352402f167b2a06a33f5304d75)](https://circleci.com/gh/drewverlee/plum)

See the *Demo* section for more on how to run the project.
See the swagger docs for more on the API.

## Swagger docs

visit the root url `/` you should expect to be greeted by an interactive API.

## Development

Checklist:

* Follow the Demo instructions to understand how the app works.
* Make sure you have the preprequistes
* Run the tests

It should be easy to add more commandline actions and sorting functions. 
Flexibility in other area's will need consideration.

### Demo

see `/test/demo.sh` its a executable script that starts the server
and makes api calls. It assumes you have all the preprequistes.

### To start server

    $ lein ring server

### Testing

    $ lein test

### Check Test Coverage

    $ lein cloverage

### To Build server

    $ lein do clean, ring uberjar

### To run server

    $  java -jar ../target/uberjar/plum-server.jar

### Project managements

checkout the [github project](https://github.com/drewverlee/plum/projects/1)

### Perquisites

* [leinagain](https://leiningen.org/#install)
* [clojure](https://clojure.org/guides/getting_started)
