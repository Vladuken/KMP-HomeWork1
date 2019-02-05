#!/usr/bin/env bash
curl --user 7fc758b99c7d6f1f9ddf2260927443bd61d9aa77: \
    --request POST \
    --form revision=3f4481dd1a24e514546a01707550d672c3fcdf48\
    --form config=@config.yml \
    --form notify=false \
        https://circleci.com/api/v1.1/project/github/Vladuken/KMP-HomeWork1/tree/feature