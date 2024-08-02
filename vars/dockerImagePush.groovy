//def call(String project,String ImageTag ,String hubUser){
//    withCredentials([usernamePassword(
//            credentialsId: "docker",
//            passwordVariable: "PASS",
//            usernameVariable: "USER")]) {
//        sh "docker login -u '$USER' -p '$PASS'"
//    }
//    sh "docker image push ${hubUser}/${project}:${ImageTag}"
//    sh "docker image push ${hubUser}/${project}:latest"
//}


def call(String aws_account_id, String region, String ecr_repoName) {
    withEnv(["AWS_ACCESS_KEY_ID=${env.AWS_ACCESS_KEY_ID}", "AWS_SECRET_ACCESS_KEY=${env.AWS_SECRET_ACCESS_KEY}"]) {
        sh """
            aws configure set aws_access_key_id ${AWS_ACCESS_KEY_ID}
            aws configure set aws_secret_access_key ${AWS_SECRET_ACCESS_KEY}
            aws configure set region ${region}

            aws ecr get-login-password --region ${region} | docker login --username AWS --password-stdin ${aws_account_id}.dkr.ecr.${region}.amazonaws.com
            docker push ${aws_account_id}.dkr.ecr.${region}.amazonaws.com/${ecr_repoName}:latest

            aws configure set aws_access_key_id none
            aws configure set aws_secret_access_key none
            aws configure set region none
        """
    }
}