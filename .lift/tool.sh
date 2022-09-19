#!/usr/bin/env bash

# dir=$1 but it is not needed
commit=$2
cmd=$3 

# ... to be filled in ... 

if [[ "$cmd" = "run" ]] ; then 
    run 
fi 
if [[ "$cmd" = "applicable" ]] ; then 
    applicable 
fi 
if [[ "$cmd" = "version" ]] ; then 
    version 
fi

function version() {
    echo 1
}

function applicable() {
    echo "true"
}

function run() {
    echo "[{ \"type\": \"Hello World\", \
            \"message\": \"We are analyzing commit $commit\", \
            \"file\": \"N/A\", \
            \"line\": 0, \
            \"details_url\": \"https://help.sonatype.com/lift/extending-lift\" \
          }]"
}

