/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(){
  

pipeline {
    agent any
    environment {
        NEXUS_USER         = credentials('user-nexus')
        NEXUS_PASSWORD     = credentials('password-nexus')
    }
    parameters {
        choice(
            name:'compileTool',
            choices: ['Maven', 'Gradle'],
            description: 'Seleccione herramienta de compilacion'
        )
    }
    stages {
        stage("Pipeline"){
            steps {
                script{
                env.STAGE = ""
                  switch(params.compileTool)
                    {
                        case 'Maven':
                            //def ejecucion = load 'maven.groovy'
                            //ejecucion.call()
                            maven.call()
                        break;
                        case 'Gradle':
                            //def ejecucion = load 'gradle.groovy'
                            //ejecucion.call()
                            gradle.call()
                        break;
                    }
                }
            }
        }
    }
    post{
        success {
            slackSend color: 'good', message: "[duribef] [${JOB_NAME}] [$compileTool] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-duribef'
        }
        failure {
            slackSend color: 'danger', message: "[duribef] [${JOB_NAME}] [$compileTool] Ejecucion fallida en stage [${env.STAGE}]", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-duribef'
        }
        }
}

}

return this;