/**
 * Create release branch.
 */

pipeline {
    agent { label 'base-lp-agent' }
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    parameters {
        string(name: 'BRANCH', defaultValue: 'master',
                description: 'Branch to build')
        choice(name: 'TYPE', choices: ['major', 'minor', 'patch'],
                description: 'Which kind of release to start.')
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
    }
}
