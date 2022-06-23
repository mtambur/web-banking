#!/bin/bash

export CDD_APPLICATION_NAME=$CI_PROJECT_NAME
export CDD_APPLICATION_VERSION_NAME=$CI_BUILD_REF_NAME
export CDD_GIT_COMMIT_ID=$CI_COMMIT_SHA
export CDD_APPLICATION_VERSION_BUILD_NUMBER=$CI_BUILD_ID


curl -s --header "Content-Type: application/json" --header "Accept: application/json" -d "{ \"applicationName\": \"$CDD_APPLICATION_NAME\", \"applicationSourceName\": \"$CDD_APPLICATION_SOURCE\", \"applicationVersionBuildNumber\": \"$CDD_APPLICATION_VERSION_BUILD_NUMBER\", \"applicationVersionName\": \"$CDD_APPLICATION_VERSION_NAME\", \"commits\": [ { \"commitId\": \"$CDD_GIT_COMMIT_ID\" } ]}" "$CDD_SERVER_URL/cdd/design/$CDD_TENANT_ID/v1/applications/application-versions/application-version-builds" -H "Authorization: Bearer $CDD_API_KEY"
