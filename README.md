# plum

Plum is a command line tool for sorting csv's containing rows of people.

Master Branch Tests: [![CircleCI](https://circleci.com/gh/drewverlee/plum.svg?style=svg&circle-token=e36a553b9697f3352402f167b2a06a33f5304d75)](https://circleci.com/gh/drewverlee/plum)

## Usage

    $ java -jar plum-<VERSION>-standalone.jar sort sort-fn input.csv output.csv

    run `sort --help` for more information

    Examples:
    Asking for help

    ```
    ➜  plum git:(add-command-line) ✗ java -jar target/uberjar/plum-0.1.0-SNAPSHOT-standalone.jar sort --help
    Sorts csvs
    options
      -h, --help
    Usage: sort sort-fn input-csv output-csv
    arg        | pos | name
    sort       |  0  | The base function
    sort-fn    |  1  | The sort function to apply to the input csv examples: birth-date,last-name,gender-and-lastname
    input-csv  |  2  | The existing csv location
    output-csv |  3  | The existing csv output location
    ```

    Giving a bad input
    ```
    ➜  plum git:(add-command-line) ✗ java -jar target/uberjar/plum-0.1.0-SNAPSHOT-standalone.jar bad-input
    The following errors occurred while parsing your command:

    The base function {bad-input} in position 0 isn't in the list of accepted functions: sort
    ```

## Development

### Project managment

checkout the [github project](Build Statu://github.com/drewverlee/plum/projects/1)

### Perquisites

* [leinagain](https://leiningen.org/#install)
* [clojure](https://clojure.org/guides/getting_started)

### Testing

    $ lein test

### Check Test Coverage

    $ lein cloverage

## To Build

    $ lein uberjar
