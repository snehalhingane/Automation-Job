#!/bin/bash
cd checkout 
# #Set user to git config
 git config --global user.email "releng@calypso.com"
 git config --global user.name "releng"
# git config credential.helper "$GIT_USERNAME" "$GIT_PASSWORD"
# #withCredentials([usernamePassword(credentialsId: 'GitUser', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')])
# echo username=$GIT_USERNAME
# echo password=$GIT_PASSWORD
ACTION="$1"
VERSION="$2"
CREDENTIAL="$3"
if [[ "$ACTION" == "Post_Codefreez" ]]
then
	echo "Checking the Version"
	if [[ "$VERSION" == "16" || "$VERSION" == "17" ]]
	then 
		echo "Checking out to mr$VERSION-release"      
    	git checkout mr$VERSION-release
		echo "Pulling latest changes from mr$VERSION-release"
		git pull http://"$CREDENTIAL"@git.calypso.com/dev/release mr$VERSION-release
		#git branch -a | grep PROMOTE_16-Test		
		echo "Deleting PROMOTE_$VERSION"
		git push http://"$CREDENTIAL"@git.calypso.com/dev/release -d PROMOTE_$VERSION
        git push http://"$CREDENTIAL"@git.calypso.com/dev/release -d SHADOW_PROMOTE_$VERSION
		echo "Recreating PROMOTE_$VERSION and SHADOW_PROMOTE_$VERSION Branch"
        git checkout -b PROMOTE_$VERSION
		echo "Pusing PROMOTE_$VERSION to origin"
        git push http://"$CREDENTIAL"@git.calypso.com/dev/release -u PROMOTE_$VERSION
        git checkout -b SHADOW_PROMOTE_$VERSION
        git push http://"$CREDENTIAL"@git.calypso.com/dev/release -u SHADOW_PROMOTE_$VERSION
	fi
fi
