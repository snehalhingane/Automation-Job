#!groovy
@Library('calypso-pipeline-library')
import com.calypso.pipeline.Utils
import com.calypso.pipeline.UserInfo
calypsoJobProperties { props ->
    props['gerritTrigger'] = false
    props['description'] = 	"""
    							This automation can be used for doing post release steps to disable the jobs and recreate th PROMOTE and SHADOW_PROMOTE branch
    						"""
}
pipelineGradleAutomation(['limitCpu': '2', 'limitMemory': '15Gi']) {
    properties([parameters([
            choice( name: 'ACTION' , choices: ['Post_Release','Post_Codefreez'],description: '* Select action'), 
            choice( name: 'VERSION' , choices: ['16','17'],description: '* Select calypso version'),
            
        ])
    ])
    stage('Checkout') {
	    checkout([$class: 'GitSCM', branches: [[name: 'mr16-release']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CloneOption', depth: 0, noTags: false, reference: '/opt/git-reference/release.git', shallow: false, timeout: 10], [$class: 'WipeWorkspace'], [$class: 'CleanBeforeCheckout'], [$class: 'RelativeTargetDirectory', relativeTargetDir: 'checkout/']], gitTool: 'WANdisco-git-2.7.4', submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'GitUser', url: 'http://git.calypso.com/dev/release']]])
    }
	
	stage('Build') {
    withCredentials([usernamePassword(credentialsId: 'GitUser', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]){
            sh '''#!/bin/bash \n
                ./git_operations.sh  "${ACTION}" "${VERSION}" "${GIT_USERNAME}:${GIT_PASSWORD}"
            '''	    
        }
    }    
    stage("Disable jobs") {
    def jobs = load "disablejobs.groovy"
    jobs.disableJob()
    }
    stage('Post-Build Actions') {
            currentBuild.result = 'SUCCESS'
            if ((currentBuild.result == 'SUCCESS' && "${ACTION}" == "Post_Codefreez") && ("${VERSION}" == "16" || "${VERSION}" == "17")) {
                emailext(
                body: '${BUILD_URL}',
                mimeType: 'text/html',
                subject: 'Please Unlock PROMOTE_$VERSION and SHADOW_PROMOTE_$VERSION Branches',
                from : 'relengteam@adenza.com',
                to: 'snehal.hingane@adenza.com, relengteam@calypso.com'
            )
            }
            else if ((currentBuild.result == 'SUCCESS' && "${ACTION}" == "Post_Release") && ("${VERSION}" == "16" || "${VERSION}" == "17")) {
                emailext(
                body: '${BUILD_URL}',
                mimeType: 'text/html',
                subject: 'Please Lock PROMOTE_$VERSION and SHADOW_PROMOTE_$VERSION Branches',
                from : 'relengteam@adenza.com',
                to: 'snehal.hingane@adenza.com, relengteam@calypso.com'
            )
            }
    }    
}
