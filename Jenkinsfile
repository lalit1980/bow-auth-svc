
node{
        def DOCKER_HUB_ACCOUNT = "lalit1980"
        def DOCKER_IMAGE_NAME  = "bow-auth-svc"
        def TAG_NUMBER = "${env.BUILD_NUMBER}"
        def BRANCH = "${env.BRANCH_NAME}"
        stage("SCM Checkout"){
            git branch: 'develop', credentialsId: 'GITHUB_CREDENTIAL1', url: 'https://github.com/lalit1980/bow-auth-svc.git'
        }
        stage("Unit Test & App Scan"){
            echo "Unit Test Case Execution started...."
            echo "Unit Test Case Execution ended...."
            def mavenHome =  tool name: "Maven3.6.3", type: "maven"
            def mavenCMD = "${mavenHome}/bin/mvn"
            sh "${mavenCMD} clean package"
            echo "AppScan for vulnerabilty scan execution started...."
            echo "AppScan for vulnerabilty scan execution ended...."
        }
        stage("SonarQube"){
            echo "SonarQube scan started...."
            echo "SonarQube coverage started...."
        }
        stage("Docker Build & Tag"){
            withCredentials([string(credentialsId: 'DOCKER_HUB_CREDENTIALS', variable: 'DOCKER_HUB_CREDENTIALS')]) {
                sh "docker login -u lalit1980 -p ${DOCKER_HUB_CREDENTIALS}"
            }
            echo "docker build -t ${DOCKER_HUB_ACCOUNT}/${DOCKER_IMAGE_NAME}:${TAG_NUMBER} ."
            sh ("docker build -t ${DOCKER_HUB_ACCOUNT}/${DOCKER_IMAGE_NAME}:${TAG_NUMBER} .")
        }

        stage("Docker Push"){
            echo "docker push ${DOCKER_HUB_ACCOUNT}/${DOCKER_IMAGE_NAME}:${TAG_NUMBER}"
            sh ("docker push ${DOCKER_HUB_ACCOUNT}/${DOCKER_IMAGE_NAME}:${TAG_NUMBER}")
        }
      stage("K8S Deployment"){
            echo "Kubernetes deployment started....."
            sh "chmod +x changeTag.sh"
                sh "./changeTag.sh ${TAG_NUMBER}"
                sshagent(['KOPS-WORKSTATION']) {
                    sh "ssh -i nishu_bow.pem ubuntu@ec2-52-74-34-239.ap-southeast-1.compute.amazonaws.com rm -rf /home/ubuntu/bow/api/"
                    sh "ssh -i nishu_bow.pem ubuntu@ec2-52-74-34-239.ap-southeast-1.compute.amazonaws.com mkdir -p /home/ubuntu/bow/api/"
                    sh "scp -o StrictHostKeyChecking=no api_bow.yml ubuntu@ec2-52-74-34-239.ap-southeast-1.compute.amazonaws.com:/home/ubuntu/bow/api/"
                    script{
                        try{
                            sh "ssh -i nishu_bow.pem ubuntu@ec2-52-74-34-239.ap-southeast-1.compute.amazonaws.com kubectl apply -f /home/ubuntu/bow/api/api_bow.yml"
                        }catch(error){
                            echo "Kubernetes deployment ended with error....."
                            //sh "ssh ubuntu@ec2-52-66-125-64.ap-south-1.compute.amazonaws.com kubectl create -f /home/ubuntu/bow/"
                        }
                    }
                }
            echo "Kubernetes deployment ended....."
        }
        stage("Deployment Report and Notification"){
            echo "Deploment status report published..."

        }

}

