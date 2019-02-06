#!/usr/bin/env bash
curl --user 7fc758b99c7d6f1f9ddf2260927443bd61d9aa77: \
    --request POST \
    --form revision=2b426454a648bcf6f8d8d4c4b14528e1eb9c392d\
    --form config=@config.yml \
    --form notify=false \
        https://circleci.com/api/v1.1/project/github/Vladuken/KMP-HomeWork1/tree/feature