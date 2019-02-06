#!/usr/bin/env bash
curl --user 7fc758b99c7d6f1f9ddf2260927443bd61d9aa77: \
    --request POST \
    --form revision=bd9883ba8c958f5a3e52808ca7daa83513e16512\
    --form config=@config.yml \
    --form notify=false \
        https://circleci.com/api/v1.1/project/github/Vladuken/KMP-HomeWork1/tree/feature