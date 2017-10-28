# plum

Plum is a command line tool for sorting csv's containing rows of people.

Master Branch Tests: [![CircleCI](https://circleci.com/gh/drewverlee/plum.svg?style=svg&circle-token=e36a553b9697f3352402f167b2a06a33f5304d75)](https://circleci.com/gh/drewverlee/plum)

## Usage

    $ java -jar plum-<VERSION>-standalone.jar <TODO> args

## Development

### Perquisites

* [leinagain](https://leiningen.org/#install)
* [clojure](https://clojure.org/guides/getting_started)

### Testing

    $ lein test
 
### Check Test Coverage

    $ lein cloverage
    
## To Build

    $ lein uberjar
