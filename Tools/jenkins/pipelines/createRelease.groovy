/**
 * Create release branch.
 */

pipeline {
    agent { label 'base-lp-agent' }
    parameters {
        string(name: 'BRANCH', defaultValue: 'pr/jenkinsPipeline',
                description: 'Branch to build')
        string(name: 'TYPE', defaultValue: 'PATCH',
                description: 'Parameters to pass to deploy script')
    }
    stages {
        stage('Create Release Branch') {
            steps {
                timestamps {
                    ansiColor('xterm') {
                        createRelease()
                    }
                }
            }
        }
    }
}

def createRelease() {
    def pythonImage = docker.build(
            'leanplum/android-tools-python',
            '-f ./Tools/jenkins/python.dockerfile .')
    pythonImage.inside {
        sh 'make release TYPE=${TYPE}'
        sh 'git branch'
    }
}
