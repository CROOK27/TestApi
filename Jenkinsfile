pipeline {
    agent any

    tools {
        jdk 'JDK-17'
        maven 'Maven-3.9'
    }

    parameters {
        string(name: 'BRANCH', defaultValue: 'main', description: 'Git branch to build')
        choice(name: 'TEST_SUITE', choices: ['all', 'product', 'auth'], description: 'Test suite to run')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: params.BRANCH,
                    url: 'https://github.com/ваш-username/ваш-репозиторий.git',
                    credentialsId: 'github-credentials'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile -DskipTests'
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    def testCommand = 'mvn test'
                    if (params.TEST_SUITE != 'all') {
                        testCommand += " -Dtest=${params.TEST_SUITE.capitalize()}*Test"
                    }
                    sh testCommand
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
                ])
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/**/*.xml'
            allure([
                includeProperties: false,
                jdk: '',
                properties: [],
                reportBuildPolicy: 'ALWAYS',
                results: [[path: 'target/allure-results']]
            ])
        }
        success {
            emailext(
                subject: "✅ Build Successful: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """<h2>✅ Build Successful!</h2>
                       <p><b>Job:</b> ${env.JOB_NAME}</p>
                       <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                       <p><b>Branch:</b> ${params.BRANCH}</p>
                       <p><b>Allure Report:</b> <a href="${env.BUILD_URL}allure">View Report</a></p>""",
                to: 'ваш-email@example.com'
            )
        }
        failure {
            emailext(
                subject: "❌ Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """<h2>❌ Build Failed!</h2>
                       <p><b>Job:</b> ${env.JOB_NAME}</p>
                       <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                       <p><b>Branch:</b> ${params.BRANCH}</p>
                       <p><b>Console Output:</b> <a href="${env.BUILD_URL}console">View Logs</a></p>""",
                to: 'ваш-email@example.com'
            )
        }
    }
}