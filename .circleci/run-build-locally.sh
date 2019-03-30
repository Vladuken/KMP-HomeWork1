#!/usr/bin/env bash
curl --user 7fc758b99c7d6f1f9ddf2260927443bd61d9aa77: \
    --request POST \
    --form revision=bcdf9b7b5219f642cc08898700615094253d8429\
    --form config=@config.yml \
    --form notify=false \
        https://circleci.com/api/v1.1/project/github/Vladuken/KMP-HomeWork1/tree/feature