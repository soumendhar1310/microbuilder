podTemplate(label: 'mypod', containers: [
    containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'kubectl', image: 'smesch/kubectl', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'docker', image: 'docker', ttyEnabled: true, command: 'cat',
        envVars: [containerEnvVar(key: 'DOCKER_CONFIG', value: '/tmp/'),])],
        volumes: [secretVolume(secretName: 'docker-config', mountPath: '/tmp'),
                  hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')
  ]) {
  node ('mypod') {
      
    def DOCKER_HUB_ACCOUNT = 'soumendhar1310'
    def DOCKER_IMAGE_NAME = 'microbuilder'
    def K8S_DEPLOYMENT_NAME = 'microbuilder'
      
    stage 'Get a Maven project'
    git url: 'https://github.com/soumendhar1310/microbuilder.git', credentialsId: '7fbd83c5-014d-45fb-93c0-3b160e9e1bf3', branch: 'master'
    container('maven') {
    
      stage('Test') {
            //def pom = readMavenPom file: 'pom.xml'
            //print "Build: " + pom.version
            //env.POM_VERSION = pom.version
            sh 'mvn clean test -Dmaven.test.failure.ignore=true'
            junit '**/target/surefire-reports/TEST-*.xml'
            //currentBuild.description = "v${pom.version} (${env.branch})"
     }
     
     stage('QA') {
    
        withSonarQubeEnv('sonar') {
                sh 'mvn sonar:sonar -Dsonar.host.url=http://172.30.180.239:9000'
        }
    }
     
      stage 'Build a Maven project'
      sh 'mvn clean install -Dmaven.test.skip=true'
    }
    container('docker') {
                stage('Docker Build & Push Current & Latest Versions') {
                    sh ("docker build -t ${DOCKER_HUB_ACCOUNT}/${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER} .")
                    sh ("docker push ${DOCKER_HUB_ACCOUNT}/${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER}")
                    sh ("docker tag ${DOCKER_HUB_ACCOUNT}/${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER} ${DOCKER_HUB_ACCOUNT}/${DOCKER_IMAGE_NAME}:latest")
                    sh ("docker push ${DOCKER_HUB_ACCOUNT}/${DOCKER_IMAGE_NAME}:latest")
                }
            }
            
    container('kubectl') {
                stage('Deploy New Build To Kubernetes') {
                    sh ("kubectl set image deployment/${K8S_DEPLOYMENT_NAME} ${K8S_DEPLOYMENT_NAME}=${DOCKER_HUB_ACCOUNT}/${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER}")
                }
            }
 
  }
}
