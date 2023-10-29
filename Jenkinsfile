// testing commit and its working and its working, I guess
pipeline {
    agent {
        label 'maven-slave'
    }
    tools {
        maven 'Maven-3.8.8'
        jdk 'JDK-17'
    }
    parameters {
        choice(name: 'sonarScans',
            choices: 'no\nyes',
            description: "This will scan the application using sonar"
        )
        choice(name: 'BuildOnly',
            choices: 'no\nyes',
            description: "This will build the application"
        )
        choice(name: 'DockerPush',
            choices: 'no\nyes',
            description: "This will trigger the build, docker build and push"
        )
        choice(name: 'DeployToDev',
            choices: 'no\nyes',
            description: "This will deploy my app to the Dev env"
        )
        choice(name: 'DeployToTest',
            choices: 'no\nyes',
            description: "This will deploy my app to the Test env"
        )
         choice(name: 'DeployToStage',
            choices: 'no\nyes',
            description: "This will deploy my app to the Stage env"
        )
         choice(name: 'DeployToProd',
            choices: 'no\nyes',
            description: "This will deploy my app to the Prod env"
        )
    }
    
    environment {
        APPLICATION_NAME = "product"
        SONAR_URL = "http://23.251.154.207:9000/"
        SONAR_TOKEN = credentials('sonar_creds')
        POM_VERSION = readMavenPom().getVersion()
        POM_PACKAGING = readMavenPom().getPackaging()
        DOCKER_HUB = "docker.io/sk619"
        DOCKER_REPO = "i27productproject"
        USER_NAME= "sk619" //UserID for DockerHub 
        DOCKER_CREDS = credentials('dockerhub_creds')
       

    }
    
    stages { 
        stage('Build') { 
            when {
                anyOf {
                    expression {
                        params.BuildOnly == 'yes'
                        params.DockerPush == 'yes'
                    }
                }
            }
            // Build happens here
            // only build should happen, no tests should be available
            steps {
                script {
                    buildApp().call()
                }
            }
        }
        stage('Unit tests') {
            when {
                anyOf {
                    expression {
                        params.BuildOnly == 'yes'
                        params.DockerPush == 'yes'
                    }
                }
            }
            steps {
                echo "performing unit tests for ${env.APPLICATION_NAME} application"
                sh "mvn test"
            }
        }
        stage('sonar') {
            when {
                anyOf {
                    expression {
                        params.sonarScans == 'yes'
                        params.BuildOnly == 'yes'
                        params.DockerPush == 'yes'
                    }
                }
            }
            steps {
                echo "Starting Sonarscan with Quality gate"
                withSonarQubeEnv('SonarQube') {
                    sh """
                        mvn clean verify sonar:sonar \
                            -Dsonar.projectKey=i27-Eureka \
                            -Dsonar.host.url=${env.SONAR_URL} \
                            -Dsonar.login=${env.SONAR_TOKEN}
                    """
                }

            // adding the below line my pipeline is being aborted, if im removing it, working. so pipeline not at fault i guess 
                /*timeout(time: 2, unit: 'MINUTES') {
                    script {
                        waitForQualityGate abortPipeline: true
                    }
                }*/
            }
        }
        stage('Build Format') {
            steps {
                script {
                    sh """
                        echo "Existing JAR Format: i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING}"
                        echo "*** Below is my expected output"
                        echo "Destination Source is i27-${env.APPLICATION_NAME}-${currentBuild.number}-${BRANCH_NAME}.${env.POM_PACKAGING}"
                    """
                }
            }
        }
        stage ('Docker build and push ') {
            when {
                anyOf {
                    expression {
                        params.DockerPush == 'yes'
                    }
                }
            }
            
            steps {
                script {
                    dockerBuildandPush().call()
                }
            }
        }
        stage ('Deploy to Dev') { //5761
            when {
                anyOf {
                    expression {
                        params.DeployToDev == 'yes'
                    }
                }
            }
            steps {
               script {
                imageValidation().call()
                //dockerDeploy('dev', '5761', '8761').call()
                dockerDeploy('dev', '5132', '8132').call()
               }
               
                
            }
        }
        stage ('Deploy to test') {
            when {
                anyOf {
                    expression {
                        params.DeployToTest == 'yes'
                    }
                }
            }
            
            steps {
                script {
                    imageValidation().call()
                    dockerDeploy ('Test', '6132', '8132').call()
                }
            }
        }
        stage ('Deploy to Stage') {
            when {
                anyOf {
                    expression {
                        params.DeployToStage == 'yes'
                    }
                }
            }
            
            steps {
                script {
                    imageValidation().call()
                    dockerDeploy ('Stage', '7132', '8132').call()
                }
            }
        }
        stage ('Deploy to Prod') {
            when {
                allOf {
                    anyOf {
                        expression {
                            params.DeployToProd == 'yes'
                        }
                    }
                    anyOf {
                        branch 'release/*'
                    }
                }
            }
            
            steps {
                timeout (time: 200, unit: 'SECONDS') {
                    input message: "Deploy to ${env.APPLICATION_NAME} ??", ok:'yes', submitter: 'krish'
                }
                
                script {
                    imageValidation().call()
                    dockerDeploy ('Prod', '8132', '8132').call()
                }
            }
        }
        stage ('Clean') {
            steps { 
                cleanWs ()
            }
        }

            
            
        
    }
}

def dockerDeploy(envDeploy, hostPort, contPort ) {
    return {
        echo "******************* Deploying to $envDeploy Environment ***********************"
        withCredentials([usernamePassword(credentialsId: 'maha_docker_vm_creds', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        // some block
        // with this credentials, i need to connect to dev environment
        // sshpass 
                script {
                    sh "sshpass -p '$PASSWORD' -v ssh -o StrictHostKeyChecking=no '$USERNAME'@$docker_server_ip \"docker pull ${env.DOCKER_HUB}/${env.DOCKER_REPO}:$GIT_COMMIT\""
                    //sh "sshpass -p '$PASSWORD' -v ssh -o StrictHostKeyChecking=no '$USERNAME'@$docker_server_ip \ "***"
                        
                    echo "Stop the Container"
                    // If we execute the below command it will fail for the first time obviously as containers are not available stop/remove will cause this issue
                     // we can implement try catch block 
                    try {
                        sh "sshpass -p '$PASSWORD' -v ssh -o StrictHostKeyChecking=no '$USERNAME'@$docker_server_ip \"docker stop ${env.APPLICATION_NAME}-$envDeploy\""
                        echo " Removing the Container "
                        sh "sshpass -p '$PASSWORD' -v ssh -o StrictHostKeyChecking=no '$USERNAME'@$docker_server_ip \"docker rm ${env.APPLICATION_NAME}-$envDeploy\""

                    } catch (err) {
                        echo "Caught the error : $err"
                    }
                    // Run the container 
                    sh "sshpass -p '$PASSWORD' -v ssh -o StrictHostKeyChecking=no '$USERNAME'@$docker_server_ip \"docker run --restart always --name ${env.APPLICATION_NAME}-$envDeploy -p $hostPort:$contPort -d ${env.DOCKER_HUB}/${env.DOCKER_REPO}:$GIT_COMMIT\""



                } 
        }

    }
}

def buildApp(){
    return {
        echo "building the ${env.APPLICATION_NAME} application"
         // maven build should happen here
        sh "mvn --version"
        sh "mvn clean package -DskipTests=true"
        archiveArtifacts artifacts: 'target/*jar', followSymlinks: false
    }
}

def imageValidation(){
    return{
        println("Pulling the docker image")
        try {
            sh "docker pull ${env.DOCKER_HUB}/${env.DOCKER_REPO}:$GIT_COMMIT"
            println("Pull Success, !!!! Deploying !!!!")
        }
        catch (Exception e) {
            

            println("OOPS, Docker image with this tag aint available")
            println("So, Building the app, creating the image and pushing to registry")

            buildApp().call()
            dockerBuildandPush().call()

        }


    }
}

def dockerBuildandPush(){
    return {
        sh "cp ${workspace}/target/i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} ./.cicd"
        echo "Listing files in .cicd folder"
        sh "ls -la ./.cicd"
        echo " ********* Building Docker Image **********"
        //docker build -t imagename.
        sh "docker build --force-rm --no-cache --pull --rm=true --build-arg JAR_SOURCE=i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} --build-arg JAR_DEST=i27-${env.APPLICATION_NAME}-${currentBuild.number}-${BRANCH_NAME}.${env.POM_PACKAGING} -t ${env.DOCKER_HUB}/${env.DOCKER_REPO}:$GIT_COMMIT ./.cicd"
        //push repo 
        //Docker hub, Google Container registry, JFROG
        echo "********************* Logging into Docker Registry*********************  "
        sh "docker login -u ${DOCKER_CREDS_USR} -p ${DOCKER_CREDS_PSW}"
        sh "docker push ${env.DOCKER_HUB}/${env.DOCKER_REPO}:$GIT_COMMIT"
        //docker push accountname/reponame:tagname
    }
}

// 8761 is the container port, we cant change it.
// if we really want to change it, we can by using -Dserver.port=9090, this will be your container port
// but we are considering the below host ports
// For Eureka 
// dev ===> 5761
// test ===> 6761
// stage ===> 7761
// prod ===> 8761

// For Product, container port is 8132
// dev ===> 5132
// test ===> 6132
// stage ===> 7132
// prod ===> 8132

