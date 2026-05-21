pipeline {
    agent any

    environment {
        COMMIT_HASH = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
        REGISTRY    = "local"
        APP_NAME    = "bank-app"
        IMAGE_FULL  = "${REGISTRY}/${APP_NAME}:${COMMIT_HASH}"
    }

    stages {
        stage('Project Build') {
            steps {
                sh './mvnw clean package'
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${IMAGE_FULL} ."
            }
        }

        stage('Deploy Staging') {
            steps {
                sh """
                    helm upgrade ${APP_NAME}-staging ./helm \
                      --install \
                      --namespace staging \
                      --create-namespace \
                      --set image=${IMAGE_FULL}
                """
            }
        }

        stage('Human Approve Prod') {
            steps {
                input message: "Развернуть сборку ${COMMIT_HASH} в Prod?", ok: "Да"
            }
        }

        stage('Deploy Prod') {
            steps {
                sh """
                    helm upgrade ${APP_NAME}-prod ./helm \
                      --install \
                      --namespace production \
                      --create-namespace \
                      --set image=${IMAGE_FULL}
                """
            }
        }
    }
}