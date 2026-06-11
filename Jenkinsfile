pipeline {
    agent any

    stages {

        stage('Build') {
            steps {
                sh './mvnw clean compile -q'
            }
        }

        stage('Testes Unitarios') {
            steps {
                sh './mvnw test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

    }

    post {
        success {
            echo "Pipe ok."
        }
        failure {
            echo "Pipe falhou."
        }
        always {
            cleanWs()
        }
    }
}
