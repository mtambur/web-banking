library 'cdd-libs'
properties([
        [
                $class: 'JiraProjectProperty'
        ],
        [$class: 'LeastLoadDisabledProperty', leastLoadDisabled: false],
        disableConcurrentBuilds(),
        parameters(
                [string(defaultValue: 'dummy', description: '', name: 'GIRO_NAME_KEY', trim: true),
                 string(defaultValue: env.BRANCH_NAME, description: 'Giro brach', name: 'GIRO_BRANCH', trim: true)]),
        [$class: 'ThrottleJobProperty', categories: [], limitOneJobWithMatchingParams: false, maxConcurrentPerNode: 0, maxConcurrentTotal: 0, paramsToUseForLimit: '', throttleEnabled: false, throttleOption: 'project']
])
params.each { k, v -> env[k] = v }
pipeline {
    agent { label 'isl' }
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    tools {
        jdk 'JDK 1.8'
    }
    environment {
        ARTIFACT_PATH = get_artifact_path()
        GIRO_GIT_PASSWORD = credentials('bldcddbuild.co.AccessKey')
        APP_VERSION = "${env.BRANCH_NAME}"
        GIRO_GIT_BRANCH = "${GIRO_BRANCH}"
        GIRO_NAME = "${GIRO_NAME_KEY}"
        SPECIAL_BRANCH = load_propertie_from_giro("${GIRO_NAME_KEY}", "special_branch")
    }
    stages {
        stage('Gradle build') {
            steps {
                withCredentials([string(credentialsId: 'cdd_artifactory_password', variable: 'ARTIFACTORY_PASSWORD')]) {
                    sh """git clean -f"""
                    sh """bash gradlew clean build war \
            -xtest \
            -Partifactory_username=bldcddbuild.co \
            -Partifactory_password=$ARTIFACTORY_PASSWORD \
            -Pbuild_with_installer=true"""
                }
            }
        }
        stage('UploadArchives war') {
            steps {
                sh " echo ${ARTIFACT_PATH}"
                upload_archives_only()
            }
        }

        stage('Upload Image to Artifactory') {
            steps {
                sh"docker login -u bldcddbuild.co -p C@passw0rd2 esd-cdd-docker-release-candidate-local.artifactory-lvn.broadcom.net"
                sh "docker image build -t esd-cdd-docker-release-candidate-local.artifactory-lvn.broadcom.net/com/ca/cdd/dummy-app:$env.BUILD_NUMBER ."
                sh "docker push esd-cdd-docker-release-candidate-local.artifactory-lvn.broadcom.net/com/ca/cdd/dummy-app:$env.BUILD_NUMBER"
                sh "docker image build -t esd-cdd-docker-release-candidate-local.artifactory-lvn.broadcom.net/com/ca/cdd/dummy-app:latest ."
                sh "docker push esd-cdd-docker-release-candidate-local.artifactory-lvn.broadcom.net/com/ca/cdd/dummy-app:latest"
            }
        }


    }
    post {
        always {
            deleteDir()
        }
        failure {
            send_emails()
        }
    }
}
